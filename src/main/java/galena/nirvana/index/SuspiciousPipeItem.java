package galena.nirvana.index;

import galena.nirvana.effects.SuspiciousItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Unlike other {@link SmokingItem}s, this one has no fixed effect list - it's crafted
 * dynamically (see {@link galena.nirvana.data.SuspiciousPipeCraftingRecipe}) from a specific flower,
 * same as vanilla Suspicious Stew, and reads whatever effects that crafting baked into the stack.
 */
public class SuspiciousPipeItem extends SmokingItem {
    private static final int MIN_EFFECT_DURATION_TICKS = 20;

    public SuspiciousPipeItem(Properties settings) {
        super(settings, List.of(), "suspicious_pipe", NirvanaItems.OLD_PIPE, NirvanaSounds.SMOKING, true);
    }

    @Override
    protected @NonNull List<MobEffectInstance> getEffects(@NonNull ItemStack stack) {
        return SuspiciousItem.getEffects(stack).stream()
                .map(effect -> effect.isInfiniteDuration() || effect.getDuration() >= MIN_EFFECT_DURATION_TICKS
                        ? effect
                        : new MobEffectInstance(effect.getEffect(), MIN_EFFECT_DURATION_TICKS,
                                effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()))
                .toList();
    }
}
