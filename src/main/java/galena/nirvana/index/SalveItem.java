package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.function.Consumer;

public class SalveItem extends SimplePolymerItem {
    private final List<MobEffectInstance> effects;
    private final String namePath;
    /** {@link Items#AIR} means "no remainder" (the item just disappears). Kept as a bare {@link
     * Item} rather than a pre-built {@link ItemStack}: constructing an ItemStack this early (at
     * class-init time, from the constructor argument callers pass in) crashes with "Components
     * not bound yet" on 26.2 - it has to wait until an actual stack is needed at runtime. */
    private final Item remainder;

    public SalveItem(Properties settings, List<MobEffectInstance> effects, String path, Item remainder) {
        super(settings);
        this.effects = effects;
        this.namePath = path;
        this.remainder = remainder;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item." + Nirvana.MOD_ID + "." + this.namePath);
    }

    /**
     * Overridden by {@link HerbalSalveItem} to read the stack's own (crafting-time) effects
     * instead of the fixed list every other {@code SalveItem} is constructed with.
     */
    protected List<MobEffectInstance> getEffects(ItemStack stack) {
        return this.effects;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;

        var effects = getEffects(stack);
        if (effects.isEmpty()) return InteractionResult.PASS;

        effects.forEach(target::addEffect);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                NirvanaSounds.HERBAL_SALVE, player.getSoundSource(), 0.5F, 1.0F);

        player.getCooldowns().addCooldown(stack, 40);

        if (player.level() instanceof ServerLevel world) {
            var xOffset = target.getBbWidth() / 2;
            var yOffset = target.getBbHeight() / 2;
            world.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    target.getX() + xOffset, target.getY() + yOffset, target.getZ() + xOffset,
                    20, xOffset, yOffset, xOffset, 0.05
            );
        }

        if (!player.getAbilities().instabuild) {
            if (stack.getCount() > 1) {
                stack.shrink(1);
            } else {
                player.setItemInHand(hand, ItemStack.EMPTY);
                if (this.remainder != Items.AIR) player.addItem(new ItemStack(this.remainder));
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        if (!getEffects(stack).isEmpty()) return ItemUseAnimation.BLOCK;
        return super.getUseAnimation(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack,
                              TooltipContext context,
                              TooltipDisplay displayComponent,
                              Consumer<Component> tooltip,
                              TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, tooltip, type);

        for (MobEffectInstance effect : getEffects(stack)) {
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
}
