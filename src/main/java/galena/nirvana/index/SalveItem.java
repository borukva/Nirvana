package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.component.type.TooltipDisplayComponent;

import java.util.List;
import java.util.function.Consumer;

public class SalveItem extends SimplePolymerItem {
    private final List<StatusEffectInstance> effects;
    private final String namePath;
    private final ItemStack remainder;

    public SalveItem(Settings settings, List<StatusEffectInstance> effects, String path, ItemStack remainder) {
        super(settings);
        this.effects = effects;
        this.namePath = path;
        this.remainder = remainder;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item." + Nirvana.MOD_ID + "." + this.namePath);
    }

    /**
     * Overridden by {@link HerbalSalveItem} to read the stack's own (crafting-time) effects
     * instead of the fixed list every other {@code SalveItem} is constructed with.
     */
    protected List<StatusEffectInstance> getEffects(ItemStack stack) {
        return this.effects;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (player.getItemCooldownManager().isCoolingDown(stack)) return ActionResult.PASS;

        var effects = getEffects(stack);
        if (effects.isEmpty()) return ActionResult.PASS;

        effects.forEach(target::addStatusEffect);

        player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                NirvanaSounds.HERBAL_SALVE, player.getSoundCategory(), 0.5F, 1.0F);

        player.getItemCooldownManager().set(stack, 40);

        if (player.getEntityWorld() instanceof ServerWorld world) {
            var xOffset = target.getWidth() / 2;
            var yOffset = target.getHeight() / 2;
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    target.getX() + xOffset, target.getY() + yOffset, target.getZ() + xOffset,
                    20, xOffset, yOffset, xOffset, 0.05
            );
        }

        if (!player.getAbilities().creativeMode) {
            if (stack.getCount() > 1) {
                stack.decrement(1);
            } else {
                player.setStackInHand(hand, ItemStack.EMPTY);
                if (!this.remainder.isEmpty()) player.giveItemStack(this.remainder.copy());
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (!getEffects(stack).isEmpty()) return UseAction.BLOCK;
        return super.getUseAction(stack);
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
}
