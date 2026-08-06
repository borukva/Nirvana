package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import java.util.List;
import java.util.function.Consumer;

/**
 * Unlike the rest of the {@link SmokingItem} family, this is disguised as a genuine
 * {@code Items.POTION} rather than {@code Items.BOW} - vanilla's brewing stand slot-insertion
 * check is hardcoded to a handful of real vanilla items, and only a stack whose real network
 * identity matches one of them is accepted by an unmodified client, so this can actually be
 * dragged into a stand's potion slot by hand. The disguise item and the use-animation are
 * independent choices as far as Polymer is concerned, so the bow-draw "smoking" animation
 * ({@link ItemUseAnimation#BOW}) is kept; this also no longer needs the BowItem-arrow-firing workaround
 * the rest of the family has to guard against, since it doesn't extend {@code BowItem} at all.
 */
public class BongItem extends Item implements PolymerItem {
    private static final int COOLDOWN_TICKS = 20;
    private static final int USE_DURATION = 40;

    private final List<MobEffectInstance> effects;
    /** {@link Items#AIR} means "no remainder" (the item just disappears). Kept as a bare {@link
     * Item} rather than a pre-built {@link ItemStack}: constructing an ItemStack this early (at
     * class-init time, from the constructor argument callers pass in) crashes with "Components
     * not bound yet" on 26.2 - it has to wait until an actual stack is needed at runtime. */
    private final Item remainder;

    public BongItem(Properties settings, List<MobEffectInstance> effects, Item remainder) {
        super(settings);
        this.effects = effects;
        this.remainder = remainder;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item." + Nirvana.MOD_ID + ".bong");
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.POTION;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "bong");
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
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
                    NirvanaSounds.BONG, user.getSoundSource(), 0.5F, 1.0F);

            SmokingItem.scheduleSmoke(serverWorld, user.getUUID());
            SmokingItem.scheduleEffects(user.getUUID(), effects);

            boolean creative = false;
            if (user instanceof Player player) {
                player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
                player.awardStat(Stats.ITEM_USED.get(this));
                creative = player.getAbilities().instabuild;
            }

            // Creative mode never spends the item, same as vanilla consumables.
            if (!creative) {
                if (stack.isDamageableItem()) {
                    stack.setDamageValue(stack.getDamageValue() + 1);
                    if (stack.getDamageValue() >= stack.getMaxDamage()) {
                        return new ItemStack(this.remainder);
                    }
                } else {
                    return new ItemStack(this.remainder);
                }
            }
        }

        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack,
                              TooltipContext context,
                              TooltipDisplay displayComponent,
                              Consumer<Component> tooltip,
                              TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, tooltip, type);
        SmokingItem.appendEffectTooltip(effects, tooltip);
    }
}
