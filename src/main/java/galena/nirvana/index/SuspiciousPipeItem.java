package galena.nirvana.index;

import galena.nirvana.effects.SuspiciousItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Unlike other {@link SmokingItem}s, this one has no fixed effect list - it's crafted
 * dynamically (see {@link galena.nirvana.data.SuspiciousPipeCraftingRecipe}) from a specific flower,
 * same as vanilla Suspicious Stew, and reads whatever effects that crafting baked into the stack.
 */
public class SuspiciousPipeItem extends SmokingItem {
    public SuspiciousPipeItem(Settings settings) {
        super(settings, List.of(), "suspicious_pipe", new ItemStack(NirvanaItems.OLD_PIPE), NirvanaSounds.SMOKING, true);
    }

    @Override
    protected List<StatusEffectInstance> getEffects(ItemStack stack) {
        return SuspiciousItem.getEffects(stack);
    }
}
