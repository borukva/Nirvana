package galena.nirvana.data;

import galena.nirvana.Nirvana;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;

public class NirvanaRecipeTypes {

    public static final RecipeSerializer<? extends CustomRecipe> SUSPICIOUS_PIPE_RECIPE_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Nirvana.id("suspicious_crafting"),
            SuspiciousPipeCraftingRecipe.SERIALIZER
            );

    public static final RecipeSerializer<? extends CustomRecipe> SUSPICIOUS_SALVE_RECIPE_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Nirvana.id("suspicious_salve_crafting"),
            SuspiciousSalveCraftingRecipe.SERIALIZER
            );

    public static void register() {
        // loads this class, triggering the field initializers above (the actual registration,
        // via RecipeSerializer.register)
    }
}
