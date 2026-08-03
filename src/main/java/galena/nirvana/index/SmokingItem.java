package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.effects.PeaceClimax;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import galena.nirvana.entity.SmokeRing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class SmokingItem extends BowItem implements PolymerItem {
    private final List<StatusEffectInstance> effects;
    private final Identifier modelData;
    private final String namePath;
    private final ItemStack remainder;
    private final SoundEvent sound;
    private final boolean smokeRing;
    private static final int COOLDOWN_TICKS = 20;
    private static final int PARTICLE_DELAY_TICKS = 50;
    private static final int USE_DURATION = 40;
    private static final Map<UUID, Long> particleSchedules = new HashMap<>();
    private static final Identifier TICK_EVENT_ID = Identifier.of("nirvana", "joint_tick");
    private static final Map<UUID, List<StatusEffectInstance>> scheduledEffects = new HashMap<>();
    private static final Set<UUID> scheduledRings = new HashSet<>();

    /**
     * Used by {@link BongItem} and {@link PotionBongItem}, which don't extend this class but want
     * the same delayed smoke-particle puff (and exhale sound) after use.
     */
    static void scheduleSmoke(ServerWorld world, UUID playerUuid) {
        particleSchedules.put(playerUuid, world.getTime() + PARTICLE_DELAY_TICKS);
    }

    /**
     * Stand-in for the original mod's custom smoke-ring particle, which a server-only mod can't
     * register - see {@link SmokeRing}, which imitates one with a display entity. Only the pipes
     * do this, same as the original.
     */
    private static void spawnSmokeRing(ServerWorld world, PlayerEntity player) {
        var look = player.getRotationVec(1F);
        SmokeRing.spawn(world, player.getEyePos().add(look.multiply(0.4)), look);
    }

    static {
        ServerTickEvents.END_SERVER_TICK.register(TICK_EVENT_ID, (server) -> {
            particleSchedules.entrySet().removeIf(entry -> {
                UUID playerUuid = entry.getKey();
                long tick = entry.getValue();
                if (server.getOverworld().getTime() >= tick) {
                    PlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);
                    if (player != null && !player.isRemoved()) {
                        // A pipe exhales its smoke ring and nothing else - the generic puff is for
                        // everything that doesn't blow one.
                        if (scheduledRings.remove(playerUuid)) {
                            spawnSmokeRing(server.getOverworld(), player);
                        } else {
                            server.getOverworld().spawnParticles(
                                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                    player.getX(), player.getY() + 1.6, player.getZ(),
                                    10,
                                    0.2, 0.2, 0.2,
                                    0.05
                            );
                        }

                        player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                                NirvanaSounds.BLOW, player.getSoundCategory(), 0.6F, 1.0F);

                        List<StatusEffectInstance> effects = scheduledEffects.remove(playerUuid);
                        if (effects != null) {
                            for (StatusEffectInstance effect : effects) {
                                applyEffect(player, effect);
                            }
                        }
                    }
                    return true;
                }
                return false;
            });
        });
    }

    /**
     * @param remainder stack left behind once durability runs out (e.g. an old pipe, or empty
     *                  for consumable items like a joint that just disappears)
     * @param sound     played when the item is used; most variants share {@link NirvanaSounds#SMOKING},
     *                  bongs use {@link NirvanaSounds#BONG} instead
     */
    public SmokingItem(Settings settings, List<StatusEffectInstance> effects, String path, ItemStack remainder, SoundEvent sound) {
        this(settings, effects, path, remainder, sound, false);
    }

    /**
     * @param smokeRing whether exhaling puffs a ring of smoke along the player's line of sight -
     *                  in the original mod only the pipes do
     */
    public SmokingItem(Settings settings, List<StatusEffectInstance> effects, String path, ItemStack remainder, SoundEvent sound, boolean smokeRing) {
        super(settings);
        this.effects = effects;
        this.modelData = Identifier.of(Nirvana.MOD_ID, path);
        this.namePath = path;
        this.remainder = remainder;
        this.sound = sound;
        this.smokeRing = smokeRing;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item." + Nirvana.MOD_ID + "." + this.namePath);
    }

    /**
     * Overridden by {@link SuspiciousPipeItem} to read the stack's own (crafting-time) effects
     * instead of the fixed list every other {@code SmokingItem} is constructed with.
     */
    protected List<StatusEffectInstance> getEffects(ItemStack stack) {
        return this.effects;
    }

    @Override
    public void appendTooltip(ItemStack stack,
                              TooltipContext context,
                              TooltipDisplayComponent displayComponent,
                              Consumer<Text> tooltip,
                              TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, tooltip, type);
        appendEffectTooltip(getEffects(stack), tooltip);
    }

    /** Shared with {@link BongItem}, which doesn't extend this class. */
    static void appendEffectTooltip(List<StatusEffectInstance> effects, Consumer<Text> tooltip) {
        for (StatusEffectInstance effect : effects) {
            Text text = Text.translatable(effect.getTranslationKey());

            int seconds = effect.getDuration() / 20;
            String formattedSeconds = String.format("%02d", seconds);

            if (effect.getAmplifier() > 0) {
                text = text.copy().append(" " + (effect.getAmplifier() + 1));
            }
            text = text.copy().append(" (00:" + formattedSeconds + ")");

            if (effect.getEffectType().value().isBeneficial()) {
                tooltip.accept(text.copy().withColor(Formatting.BLUE.getColorValue()));
            } else {
                tooltip.accept(text.copy().withColor(Formatting.RED.getColorValue()));
            }
        }
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        return false;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.BOW;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return this.modelData;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.PASS;
        }

        user.setCurrentHand(hand);
        return ActionResult.SUCCESS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    this.sound, user.getSoundCategory(), 0.5F, 1.0F);

            scheduleSmoke(serverWorld, user.getUuid());
            scheduledEffects.put(user.getUuid(), getEffects(stack));
            if (this.smokeRing) {
                scheduledRings.add(user.getUuid());
            }

            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
                player.incrementStat(Stats.USED.getOrCreateStat(this));

                // Creative mode never spends the item, same as vanilla consumables.
                if (!player.getAbilities().creativeMode) {
                    return takeHit(stack);
                }
            }
        }

        // Deliberately not calling super.finishUsing(...) here: BowItem's own implementation
        // looks for an arrow to fire, which has nothing to do with this item and could mutate
        // the same stack instance the delayed effect-application callback above still holds a
        // reference to.
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    /** Shared by the player-use path above and the dispenser behaviour below. */
    private ItemStack takeHit(ItemStack stack) {
        if (stack.isDamageable()) {
            stack.setDamage(stack.getDamage() + 1);
            if (stack.getDamage() >= stack.getMaxDamage()) {
                return this.remainder.copy();
            }
            return stack;
        }
        return this.remainder.copy();
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
        DispenserBlock.registerBehavior(item, (BlockPointer pointer, ItemStack stack) -> {
            ServerWorld world = pointer.world();
            Vec3d center = pointer.centerPos();
            Direction facing = pointer.state().get(DispenserBlock.FACING);
            Vec3d look = Vec3d.of(facing.getVector());
            Vec3d mouth = center.add(look.multiply(0.5));

            if (appliesEffect) {
                double range = DISPENSER_EFFECT_RADIUS * 2;
                var targets = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class),
                        Box.of(center, range, range, range), target -> true);
                for (LivingEntity target : targets) {
                    for (StatusEffectInstance effect : item.getEffects(stack)) {
                        applyEffect(target, effect);
                    }
                }
            }

            if (item.smokeRing) {
                SmokeRing.spawn(world, mouth, look);
            } else {
                world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        mouth.x, mouth.y, mouth.z, 5, 0.0, 0.2 + world.getRandom().nextDouble() * 0.1, 0.0, 0.02);
            }

            world.playSound(null, center.x, center.y, center.z, item.sound, SoundCategory.BLOCKS, 0.5F, 1.0F);

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
    public static void applyEffect(LivingEntity target, StatusEffectInstance template) {
        if (template.getEffectType() == NirvanaEffects.PEACE) {
            var existing = target.getStatusEffect(NirvanaEffects.PEACE);
            int amplifier = Math.min(
                    existing != null ? existing.getAmplifier() + 1 : template.getAmplifier(),
                    MAX_PEACE_AMPLIFIER
            );
            target.addStatusEffect(new StatusEffectInstance(NirvanaEffects.PEACE, template.getDuration(), amplifier));
            PeaceClimax.onIncreasedTo(target, amplifier);
        } else {
            target.addStatusEffect(new StatusEffectInstance(template));
        }
    }
}