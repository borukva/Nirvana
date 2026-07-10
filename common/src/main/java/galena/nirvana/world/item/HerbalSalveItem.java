package galena.nirvana.world.item;

import galena.nirvana.index.NirvanaParticles;
import galena.nirvana.index.NirvanaSounds;
import galena.nirvana.platform.Services;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;

public class HerbalSalveItem extends Item {

    public HerbalSalveItem(Properties properties) {
        super(properties);
    }

    private static List<MobEffectInstance> getEffects(ItemStack stack) {
        return SuspiciousItem.getEffects(stack, 160 * Services.CONFIG.common().herbalSalveFactor());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;

        var effects = getEffects(stack);
        if (effects.isEmpty()) return InteractionResult.PASS;

        effects.forEach(target::addEffect);

        player.playSound(NirvanaSounds.HERBAL_SALVE.get());

        player.getCooldowns().addCooldown(stack, 40);

        if (player.level() instanceof ServerLevel level) {
            var xOffset = target.getBbWidth() / 2;
            var yOffset = target.getBbHeight() / 2;
            level.sendParticles(NirvanaParticles.HERBAL_SALVE.get(),
                    target.getX() + xOffset, target.getY() + yOffset, target.getZ() + xOffset,
                    20, xOffset, yOffset, xOffset, 0.05
            );
        }

        if (!(player.getAbilities().instabuild)) {
            if (stack.getCount() > 1) {
                stack.shrink(1);
            } else {
                var remainder = getCraftingRemainder();
                player.setItemInHand(hand, ItemStack.EMPTY);
                if (remainder != null && !remainder.isEmpty()) player.addItem(remainder.copy());
            }
        }

        return player.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        if (!getEffects(stack).isEmpty()) return ItemUseAnimation.BLOCK;
        return super.getUseAnimation(stack);
    }
}
