package galena.nirvana.data;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;

/** 1 old_pipe + 1 weed + 6 of a single flower type -> suspicious_pipe. */
public class SuspiciousPipeCraftingRecipe extends SuspiciousCraftingRecipe {
    private static final Ingredient BASE_ITEM = Ingredient.ofItems(NirvanaItems.OLD_PIPE);

    public SuspiciousPipeCraftingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    protected Ingredient getBase() {
        return BASE_ITEM;
    }

    @Override
    protected int getRequiredFlowers() {
        return 6;
    }

    @Override
    protected int getRequiredWeed() {
        return 1;
    }

    @Override
    protected int getDurationFactor() {
        return 4;
    }

    @Override
    protected Item getResult() {
        return NirvanaItems.SUSPICIOUS_PIPE;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return NirvanaRecipeTypes.SUSPICIOUS_PIPE_RECIPE_SERIALIZER;
    }
}
