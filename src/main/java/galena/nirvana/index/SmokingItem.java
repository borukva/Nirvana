package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.effects.PeaceClimax;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import galena.nirvana.entity.SmokeRing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class SmokingItem extends BowItem implements PolymerItem {
    private final List<MobEffectInstance> effects;
    private final Identifier modelData;
    private final String namePath;
    /** {@link Items#AIR} means "no remainder" (the item just disappears). Kept as a bare {@link
     * Item} rather than a pre-built {@link ItemStack}: constructing an ItemStack this early (at
     * class-init time, from the constructor argument callers pass in) crashes with "Components
     * not bound yet" on 26.2 - it has to wait until an actual stack is needed at runtime. */
    private final Item remainder;
    private final SoundEvent sound;
    private final boolean smokeRing;
    private static final int COOLDOWN_TICKS = 20;
    private static final int PARTICLE_DELAY_TICKS = 50;
    private static final int USE_DURATION = 40;
    private static final Map<UUID, Long> particleSchedules = new HashMap<>();
    private static final Identifier TICK_EVENT_ID = Identifier.fromNamespaceAndPath("nirvana", "joint_tick");
    private static final Map<UUID, List<MobEffectInstance>> scheduledEffects = new HashMap<>();
    private static final Set<UUID> scheduledRings = new HashSet<>();

    /** The same "Sprigatito"/"Skeker" reskin {@link galena.nirvana.mixin.CatEntityMixin} applies. */
    private static final ResourceKey<CatVariant> SPRIGATITO = ResourceKey.create(Registries.CAT_VARIANT, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "sprigatito"));
    /** Covers the whole pause+zoomies routine below. */
    private static final int CAT_SPEED_DURATION_TICKS = 180;
    private static final int CAT_SPEED_AMPLIFIER = 4;
    /** Cats mid-smoke ignore further joint clicks instead of restarting/stacking the animation. */
    private static final Set<UUID> smokingCats = new HashSet<>();
    private static final Map<UUID, CatSmokeSchedule> catSmokeSchedules = new HashMap<>();
    private static final double ZOOMIES_SPEED_MODIFIER = 1.8;
    /** Beat of stillness before it bolts - a dead stop reads more like "it just got spooked" than
     * launching straight into a run. */
    private static final int ZOOMIES_PAUSE_TICKS = 5;
    private static final int ZOOMIES_MIN_POINTS = 4;
    private static final int ZOOMIES_MAX_POINTS = 10;
    private static final int ZOOMIES_HORIZONTAL_RANGE = 7;
    private static final int ZOOMIES_VERTICAL_RANGE = 4;
    /** Hard cap on the whole pause+run-the-circuit routine, regardless of how many points it
     * actually gets through. */
    private static final int ZOOMIES_TIMEOUT_TICKS = 200;
    private static final Map<UUID, ZoomiesState> zoomiesCats = new HashMap<>();

    private record CatSmokeSchedule(long tick, UUID playerUuid) {
    }

    /** {@code nextIndex} tracks progress through {@code points}; mutable since it advances in
     * place tick over tick rather than being replaced wholesale like the map's other schedules. */
    private static final class ZoomiesState {
        final List<Vec3> points;
        final long startAt;
        final long deadline;
        int nextIndex = 0;

        ZoomiesState(List<Vec3> points, long startAt, long deadline) {
            this.points = points;
            this.startAt = startAt;
            this.deadline = deadline;
        }
    }

    /**
     * Used by {@link BongItem} and {@link PotionBongItem}, which don't extend this class but want
     * the same delayed smoke-particle puff (and exhale sound) after use.
     */
    static void scheduleSmoke(ServerLevel world, UUID playerUuid) {
        particleSchedules.put(playerUuid, world.getGameTime() + PARTICLE_DELAY_TICKS);
    }

    /**
     * Used by {@link BongItem} and {@link PotionBongItem}, which don't extend this class but want
     * their effects applied at exhale time (alongside the puff/sound) instead of the instant they
     * finish using the item, same as pipes and joints already do.
     */
    static void scheduleEffects(UUID playerUuid, List<MobEffectInstance> effects) {
        scheduledEffects.put(playerUuid, effects);
    }

    /**
     * Stand-in for the original mod's custom smoke-ring particle, which a server-only mod can't
     * register - see {@link SmokeRing}, which imitates one with a display entity. Only the pipes
     * do this, same as the original.
     */
    private static void spawnSmokeRing(ServerLevel world, Player player) {
        var look = player.getViewVector(1F);
        SmokeRing.spawn(world, player.getEyePosition().add(look.scale(0.4)), look);
    }

    static {
        ServerTickEvents.END_SERVER_TICK.register(TICK_EVENT_ID, (server) -> {
            particleSchedules.entrySet().removeIf(entry -> {
                UUID playerUuid = entry.getKey();
                long tick = entry.getValue();
                if (server.overworld().getGameTime() >= tick) {
                    Player player = server.getPlayerList().getPlayer(playerUuid);
                    if (player != null && !player.isRemoved()) {
                        // A pipe exhales its smoke ring and nothing else - the generic puff is for
                        // everything that doesn't blow one.
                        if (scheduledRings.remove(playerUuid)) {
                            spawnSmokeRing(server.overworld(), player);
                        } else {
                            server.overworld().sendParticles(
                                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                    player.getX(), player.getY() + 1.6, player.getZ(),
                                    10,
                                    0.2, 0.2, 0.2,
                                    0.05
                            );
                        }

                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                NirvanaSounds.BLOW, player.getSoundSource(), 0.6F, 1.0F);

                        List<MobEffectInstance> effects = scheduledEffects.remove(playerUuid);
                        if (effects != null) {
                            for (MobEffectInstance effect : effects) {
                                applyEffect(player, effect);
                            }
                        }
                    }
                    return true;
                }
                return false;
            });

            catSmokeSchedules.entrySet().removeIf(entry -> {
                UUID catUuid = entry.getKey();
                CatSmokeSchedule schedule = entry.getValue();
                if (server.overworld().getGameTime() >= schedule.tick()) {
                    exhaleOnCat(server.overworld(), catUuid, schedule.playerUuid());
                    smokingCats.remove(catUuid);
                    return true;
                }
                return false;
            });

            zoomiesCats.entrySet().removeIf(entry -> {
                UUID catUuid = entry.getKey();
                ZoomiesState zoomies = entry.getValue();
                long now = server.overworld().getGameTime();
                if (!(server.overworld().getEntity(catUuid) instanceof Cat cat) || cat.isRemoved()
                        || now >= zoomies.deadline || zoomies.nextIndex >= zoomies.points.size()) {
                    return true;
                }
                // Nothing to do yet during the pre-bolt pause, and only re-aim once the current
                // path has actually ended (arrived, blocked, or some other goal cancelled it) -
                // re-issuing every single tick would make it flinch in place instead of committing
                // to each leg of the run.
                if (now >= zoomies.startAt && cat.getNavigation().isDone()) {
                    Vec3 target = zoomies.points.get(zoomies.nextIndex);
                    zoomies.nextIndex++;
                    cat.getNavigation().moveTo(target.x, target.y, target.z, ZOOMIES_SPEED_MODIFIER);
                }
                return false;
            });
        });
    }

    /**
     * The "stoned cat" reaction requested alongside the Sprigatito/Skeker reskin: same puff/sound
     * a player's own exhale gets, then a strong Speed buff and a beat of stillness before it darts
     * around to a handful of random nearby points in sequence - the "zoomies" a startled cat gets,
     * rather than a single flee-and-stop. Driven from the tick loop (see {@link #zoomiesCats})
     * instead of one navigation.moveTo() call per point, since a lone call is easily overridden the
     * very next tick by whatever the cat's own goal selector currently favors.
     */
    private static void exhaleOnCat(ServerLevel world, UUID catUuid, UUID playerUuid) {
        if (!(world.getEntity(catUuid) instanceof Cat cat) || cat.isRemoved()) return;

        world.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                cat.getX(), cat.getY() + cat.getBbHeight() / 2, cat.getZ(),
                10, 0.2, 0.2, 0.2, 0.05
        );
        world.playSound(null, cat.getX(), cat.getY(), cat.getZ(), NirvanaSounds.BLOW, cat.getSoundSource(), 0.6F, 1.0F);

        // ambient=false, visible=false: no swirling particle ring around the cat while it's sped up.
        cat.addEffect(new MobEffectInstance(MobEffects.SPEED, CAT_SPEED_DURATION_TICKS, CAT_SPEED_AMPLIFIER, false, false));
        cat.hiss();

        int pointCount = ZOOMIES_MIN_POINTS + world.getRandom().nextInt(ZOOMIES_MAX_POINTS - ZOOMIES_MIN_POINTS + 1);
        List<Vec3> points = new java.util.ArrayList<>();
        for (int i = 0; i < pointCount; i++) {
            Vec3 point = DefaultRandomPos.getPos(cat, ZOOMIES_HORIZONTAL_RANGE, ZOOMIES_VERTICAL_RANGE);
            if (point != null) points.add(point);
        }
        if (!points.isEmpty()) {
            long now = world.getGameTime();
            zoomiesCats.put(catUuid, new ZoomiesState(points, now + ZOOMIES_PAUSE_TICKS, now + ZOOMIES_TIMEOUT_TICKS));
        }
    }

    /**
     * @param remainder stack left behind once durability runs out (e.g. an old pipe, or empty
     *                  for consumable items like a joint that just disappears)
     * @param sound     played when the item is used; most variants share {@link NirvanaSounds#SMOKING},
     *                  bongs use {@link NirvanaSounds#BONG} instead
     */
    public SmokingItem(Properties settings, List<MobEffectInstance> effects, String path, Item remainder, SoundEvent sound) {
        this(settings, effects, path, remainder, sound, false);
    }

    /**
     * @param smokeRing whether exhaling puffs a ring of smoke along the player's line of sight -
     *                  in the original mod only the pipes do
     */
    public SmokingItem(Properties settings, List<MobEffectInstance> effects, String path, Item remainder, SoundEvent sound, boolean smokeRing) {
        super(settings);
        this.effects = effects;
        this.modelData = Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, path);
        this.namePath = path;
        this.remainder = remainder;
        this.sound = sound;
        this.smokeRing = smokeRing;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item." + Nirvana.MOD_ID + "." + this.namePath);
    }

    /**
     * Overridden by {@link SuspiciousPipeItem} to read the stack's own (crafting-time) effects
     * instead of the fixed list every other {@code SmokingItem} is constructed with.
     */
    protected List<MobEffectInstance> getEffects(ItemStack stack) {
        return this.effects;
    }

    @Override
    public void appendHoverText(ItemStack stack,
                              TooltipContext context,
                              TooltipDisplay displayComponent,
                              Consumer<Component> tooltip,
                              TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, tooltip, type);
        appendEffectTooltip(getEffects(stack), tooltip);
    }

    /** Shared with {@link BongItem}, which doesn't extend this class. */
    static void appendEffectTooltip(List<MobEffectInstance> effects, Consumer<Component> tooltip) {
        for (MobEffectInstance effect : effects) {
            Component text = Component.translatable(effect.getDescriptionId());

            int seconds = effect.getDuration() / 20;
            String formattedSeconds = String.format("%02d", seconds);

            if (effect.getAmplifier() > 0) {
                text = text.copy().append(" " + (effect.getAmplifier() + 1));
            }
            text = text.copy().append(" (00:" + formattedSeconds + ")");

            if (effect.getEffect().value().isBeneficial()) {
                tooltip.accept(text.copy().withStyle(ChatFormatting.BLUE));
            } else {
                tooltip.accept(text.copy().withStyle(ChatFormatting.RED));
            }
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        return false;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.BOW;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider registries) {
        return this.modelData;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    /**
     * Right-clicking a reskinned ("stoned") cat with a joint specifically (not the pipes) shares
     * it with them - see {@link #exhaleOnCat}. Only the joint, per request; everything else here
     * (durability hit, cooldown-free since it's a one-off gag) mirrors a normal self-use.
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!this.namePath.equals("joint")) return InteractionResult.PASS;
        if (!(target instanceof Cat cat) || !cat.getVariant().is(SPRIGATITO)) return InteractionResult.PASS;
        if (!(player.level() instanceof ServerLevel world)) return InteractionResult.SUCCESS;
        if (!smokingCats.add(cat.getUUID())) return InteractionResult.SUCCESS;

        world.playSound(null, cat.getX(), cat.getY(), cat.getZ(), this.sound, cat.getSoundSource(), 0.5F, 1.0F);
        catSmokeSchedules.put(cat.getUUID(), new CatSmokeSchedule(world.getGameTime() + PARTICLE_DELAY_TICKS, player.getUUID()));

        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, takeHit(stack));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (user.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        user.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (world instanceof ServerLevel serverWorld) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    this.sound, user.getSoundSource(), 0.5F, 1.0F);

            scheduleSmoke(serverWorld, user.getUUID());
            scheduledEffects.put(user.getUUID(), getEffects(stack));
            if (this.smokeRing) {
                scheduledRings.add(user.getUUID());
            }

            if (user instanceof Player player) {
                player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
                player.awardStat(Stats.ITEM_USED.get(this));

                // Creative mode never spends the item, same as vanilla consumables.
                if (!player.getAbilities().instabuild) {
                    return takeHit(stack);
                }
            }
        }

        // Deliberately not calling super.finishUsingItem(...) here: BowItem's own implementation
        // looks for an arrow to fire, which has nothing to do with this item and could mutate
        // the same stack instance the delayed effect-application callback above still holds a
        // reference to.
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    /** Shared by the player-use path above and the dispenser behaviour below. */
    private ItemStack takeHit(ItemStack stack) {
        if (stack.isDamageableItem()) {
            stack.setDamageValue(stack.getDamageValue() + 1);
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                return new ItemStack(this.remainder);
            }
            return stack;
        }
        return new ItemStack(this.remainder);
    }

    /** Radius the original mod defaults both its joint and bong secondhand-smoke config to. */
    private static final double DISPENSER_EFFECT_RADIUS = 15.0;

    /**
     * Lets a dispenser "smoke" a joint or pipe loaded into it - no fake player is involved, so
     * unlike a normal use there's no single entity to hand the effect to: instead it's applied
     * (when {@code appliesEffect} is set) to every living entity within a radius of the dispenser
     * itself, exactly like the original mod's own dispenser behaviour. The item is still consumed
     * the same way a normal use would consume it. Only {@link NirvanaItems#JOINT} actually applies
     * an effect this way in the original - the pipes register this too, but purely for the smoke
     * particles, matching their own {@code appliesEffect = false}.
     */
    static void registerDispenserBehavior(SmokingItem item, boolean appliesEffect) {
        DispenserBlock.registerBehavior(item, (BlockSource pointer, ItemStack stack) -> {
            ServerLevel world = pointer.level();
            Vec3 center = pointer.center();
            Direction facing = pointer.state().getValue(DispenserBlock.FACING);
            Vec3 look = Vec3.atLowerCornerOf(facing.getUnitVec3i());
            Vec3 mouth = center.add(look.scale(0.5));

            if (appliesEffect) {
                double range = DISPENSER_EFFECT_RADIUS * 2;
                var targets = world.getEntities(EntityTypeTest.forClass(LivingEntity.class),
                        AABB.ofSize(center, range, range, range), target -> true);
                for (LivingEntity target : targets) {
                    for (MobEffectInstance effect : item.getEffects(stack)) {
                        applyEffect(target, effect);
                    }
                }
            }

            if (item.smokeRing) {
                SmokeRing.spawn(world, mouth, look);
            } else {
                world.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        mouth.x, mouth.y, mouth.z, 5, 0.0, 0.2 + world.getRandom().nextDouble() * 0.1, 0.0, 0.02);
            }

            world.playSound(null, center.x, center.y, center.z, item.sound, SoundSource.BLOCKS, 0.5F, 1.0F);

            return item.takeHit(stack);
        });
    }

    private static final int MAX_PEACE_AMPLIFIER = 7;

    /**
     * Peace stacks (amplifier climbs with repeated hits, triggering {@link PeaceClimax} at
     * thresholds) instead of just refreshing duration like a normal effect re-application. Used
     * directly by {@link BongItem}, which doesn't extend this class, and by
     * {@link galena.nirvana.entity.ThcCloud} so a THC/Reefer blast stacks the exact same way a
     * fresh puff would instead of just re-flattening whoever's already mid-effect back to the
     * blast's own base amplifier.
     */
    public static void applyEffect(LivingEntity target, MobEffectInstance template) {
        if (template.getEffect() == NirvanaEffects.PEACE) {
            var existing = target.getEffect(NirvanaEffects.PEACE);
            int amplifier = Math.min(
                    existing != null ? existing.getAmplifier() + 1 : template.getAmplifier(),
                    MAX_PEACE_AMPLIFIER
            );
            target.addEffect(new MobEffectInstance(NirvanaEffects.PEACE, template.getDuration(), amplifier));
            PeaceClimax.onIncreasedTo(target, amplifier);
        } else {
            target.addEffect(new MobEffectInstance(template));
        }
    }
}