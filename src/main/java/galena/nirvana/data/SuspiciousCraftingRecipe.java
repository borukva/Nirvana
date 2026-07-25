package galena.nirvana.data;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

/**
 * Same idea as vanilla Suspicious Stew: a base container + some weed + N of a single flower type
 * produces an item whose effects come from that flower's own vanilla suspicious-stew effect
 * table, with duration multiplied by a per-recipe factor. Used for both
 * {@link SuspiciousPipeCraftingRecipe} (base: old_pipe) and {@link SuspiciousSalveCraftingRecipe}
 * (base: bowl).
 */
public abstract class SuspiciousCraftingRecipe extends SpecialCraftingRecipe {
    private static final Ingredient WEED_ITEM = Ingredient.ofItems(NirvanaItems.WEED);

    protected SuspiciousCraftingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    protected abstract Ingredient getBase();
    protected abstract int getRequiredFlowers();
    protected abstract int getRequiredWeed();
    protected abstract int getDurationFactor();
    protected abstract Item getResult();

    // Not a static Ingredient like WEED_ITEM: tags aren't populated yet at mod-init time (when
    // static fields are evaluated), so this is checked lazily per-stack instead.
    private static boolean isFlower(ItemStack stack) {
        return stack.isIn(ItemTags.SMALL_FLOWERS);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasBase = false;
        int weedCount = 0;
        int flowerCount = 0;
        Item flowerType = null;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (getBase().test(stack)) {
                    if (hasBase) {
                        return false;
                    }
                    hasBase = true;
                } else if (WEED_ITEM.test(stack)) {
                    weedCount++;
                } else if (isFlower(stack)) {
                    if (flowerType == null) {
                        flowerType = stack.getItem();
                    } else if (flowerType != stack.getItem()) {
                        return false;
                    }
                    flowerCount++;
                } else {
                    return false;
                }
            }
        }

        return hasBase && weedCount == getRequiredWeed() && flowerCount == getRequiredFlowers();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput container, RegistryWrapper.WrapperLookup lookup) {
        ItemStack result = new ItemStack(getResult(), 1);
        SuspiciousStewIngredient effectIngredient = null;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getStackInSlot(i);
            if (isFlower(stack)) {
                effectIngredient = SuspiciousStewIngredient.of(stack.getItem());
                break;
            }
        }

        if (effectIngredient != null) {
            int durationFactor = getDurationFactor();
            SuspiciousStewEffectsComponent effects = new SuspiciousStewEffectsComponent(
                    effectIngredient.getStewEffects().effects()
                            .stream()
                            .map(it -> new SuspiciousStewEffectsComponent.StewEffect(it.effect(), it.duration() * durationFactor))
                            .toList()
            );
            result.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, effects);
        }

        return result;
    }
}
