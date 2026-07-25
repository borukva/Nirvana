package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.effects.PeaceClimax;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class SmokingItem extends BowItem implements PolymerItem {
    private final List<StatusEffectInstance> effects;
    private final Identifier modelData;
    private final String namePath;
    private final ItemStack remainder;
    private final SoundEvent sound;
    private static final int COOLDOWN_TICKS = 20;
    private static final int PARTICLE_DELAY_TICKS = 50;
    private static final int USE_DURATION = 40;
    private static final Map<UUID, Long> particleSchedules = new HashMap<>();
    private static final Identifier TICK_EVENT_ID = Identifier.of("nirvana", "joint_tick");
    private static final Map<UUID, List<StatusEffectInstance>> scheduledEffects = new HashMap<>();

    /**
     * Used by {@link PotionBongItem}, which doesn't extend this class but wants the same
     * delayed smoke-particle puff after use.
     */
    static void scheduleSmoke(ServerWorld world, UUID playerUuid) {
        particleSchedules.put(playerUuid, world.getTime() + PARTICLE_DELAY_TICKS);
    }

    static {
        ServerTickEvents.END_SERVER_TICK.register(TICK_EVENT_ID, (server) -> {
            particleSchedules.entrySet().removeIf(entry -> {
                UUID playerUuid = entry.getKey();
                long tick = entry.getValue();
                if (server.getOverworld().getTime() >= tick) {
                    PlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);
                    if (player != null && !player.isRemoved()) {
                        server.getOverworld().spawnParticles(
                                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                player.getX(), player.getY() + 1.6, player.getZ(),
                                10,
                                0.2, 0.2, 0.2,
                                0.05
                        );
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
        super(settings);
        this.effects = effects;
        this.modelData = Identifier.of(Nirvana.MOD_ID, path);
        this.namePath = path;
        this.remainder = remainder;
        this.sound = sound;
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

        for (StatusEffectInstance effect : getEffects(stack)) {
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

            long scheduledTick = serverWorld.getTime() + PARTICLE_DELAY_TICKS;
            particleSchedules.put(user.getUuid(), scheduledTick);
            scheduledEffects.put(user.getUuid(), getEffects(stack));

            if (user instanceof PlayerEntity player) {
                if (stack.isDamageable()) {
                    player.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
                    player.incrementStat(Stats.USED.getOrCreateStat(this));
                    stack.setDamage(stack.getDamage() + 1);
                    if (stack.getDamage() >= stack.getMaxDamage()) {
                        return this.remainder.copy();
                    }
                } else {
                    return this.remainder.copy();
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

    private static final int MAX_PEACE_AMPLIFIER = 7;

    /**
     * Peace stacks (amplifier climbs with repeated hits, triggering {@link PeaceClimax} at
     * thresholds) instead of just refreshing duration like a normal effect re-application.
     */
    private static void applyEffect(LivingEntity target, StatusEffectInstance template) {
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