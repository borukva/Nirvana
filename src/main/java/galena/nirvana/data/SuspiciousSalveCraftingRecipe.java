package galena.nirvana.data;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;

/** 1 bowl + 3 weed + 3 of a single flower type -> herbal_salve. */
public class SuspiciousSalveCraftingRecipe extends SuspiciousCraftingRecipe {
    private static final Ingredient BASE_ITEM = Ingredient.ofItems(Items.BOWL);

    public SuspiciousSalveCraftingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    protected Ingredient getBase() {
        return BASE_ITEM;
    }

    @Override
    protected int getRequiredFlowers() {
        return 3;
    }

    @Override
    protected int getRequiredWeed() {
        return 3;
    }

    @Override
    protected int getDurationFactor() {
        return 3;
    }

    @Override
    protected Item getResult() {
        return NirvanaItems.HERBAL_SALVE;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return NirvanaRecipeTypes.SUSPICIOUS_SALVE_RECIPE_SERIALIZER;
    }
}
