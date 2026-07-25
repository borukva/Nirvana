package galena.nirvana.data;

import galena.nirvana.Nirvana;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class NirvanaRecipeTypes {

    public static final RecipeSerializer<? extends SpecialCraftingRecipe> SUSPICIOUS_PIPE_RECIPE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Nirvana.id("suspicious_crafting"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(SuspiciousPipeCraftingRecipe::new)
            );

    public static final RecipeSerializer<? extends SpecialCraftingRecipe> SUSPICIOUS_SALVE_RECIPE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Nirvana.id("suspicious_salve_crafting"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(SuspiciousSalveCraftingRecipe::new)
            );

    public static void register() {
        // loads this class, triggering the field initializers above (the actual registration,
        // via RecipeSerializer.register)
    }
}
