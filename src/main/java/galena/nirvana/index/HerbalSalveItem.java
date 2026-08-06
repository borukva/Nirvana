package galena.nirvana.index;

import galena.nirvana.effects.SuspiciousItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Unlike other {@link SalveItem}s, this one has no fixed effect list - it's crafted dynamically
 * (see {@link galena.nirvana.data.SuspiciousSalveCraftingRecipe}) from a specific flower, same as
 * vanilla Suspicious Stew, and reads whatever effects that crafting baked into the stack.
 */
public class HerbalSalveItem extends SalveItem {
    public HerbalSalveItem(Properties settings, Item remainder) {
        super(settings, List.of(), "herbal_salve", remainder);
    }

    @Override
    protected List<MobEffectInstance> getEffects(ItemStack stack) {
        return SuspiciousItem.getEffects(stack);
    }
}
