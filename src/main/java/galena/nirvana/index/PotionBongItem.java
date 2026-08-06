package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.effect.MobEffectInstance;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import java.util.ArrayList;
import java.util.List;

/**
 * A bong variant pre-filled with a vanilla potion's effects (dynamic per-stack, via the stack's
 * own {@code POTION_CONTENTS} component), rather than a fixed effect list baked into the item
 * like {@link SmokingItem}. One stack per registered potion shows up in the creative tab.
 * <p>
 * Disguised as a genuine {@code Items.POTION} (not {@code Items.BOW}) so it can actually be
 * dragged into a brewing stand's potion slot by hand - see {@link BongItem} for why. The disguise
 * item and the use-animation are independent as far as Polymer is concerned, so the bow-draw
 * "smoking" animation is kept; this also no longer needs the BowItem-arrow-firing workaround
 * since it doesn't extend {@code BowItem} at all.
 */
public class PotionBongItem extends Item implements PolymerItem {
    private static final int COOLDOWN_TICKS = 20;
    private static final int USE_DURATION = 40;

    public PotionBongItem(Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(ItemStack stack) {
        var contents = stack.get(DataComponents.POTION_CONTENTS);
        var base = Component.translatable("item." + Nirvana.MOD_ID + ".potion_bong");
        return contents != null ? contents.getName("item." + Nirvana.MOD_ID + ".potion_bong.effect.") : base;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.POTION;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider registries) {
        return Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "potion_bong");
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
        if (stack.get(DataComponents.POTION_CONTENTS) == null) {
            return InteractionResult.PASS;
        }
        if (user.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }
        user.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        var contents = stack.get(DataComponents.POTION_CONTENTS);
        if (world instanceof ServerLevel && contents != null) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    NirvanaSounds.BONG, user.getSoundSource(), 0.5F, 1.0F);

            SmokingItem.scheduleSmoke((ServerLevel) world, user.getUUID());
            List<MobEffectInstance> effects = new ArrayList<>();
            contents.forEachEffect(effects::add, 1.0F);
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
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                } else {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
            }
        }

        return stack;
    }
}
