package galena.nirvana.mixin;

import galena.nirvana.data.NirvanaBrewingRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects the custom bong-brewing steps (see {@link NirvanaBrewingRecipes#getCustomResult}) ahead
 * of vanilla's own matching, since {@code bong}/{@code potion_bong} can't be registered as real
 * potion containers (vanilla's {@code assertPotion} check requires a genuine {@code PotionItem}).
 */
@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {
    @Inject(method = "hasRecipe", at = @At("HEAD"), cancellable = true)
    private void nirvana$hasCustomRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (NirvanaBrewingRecipes.getCustomResult(input, ingredient, (BrewingRecipeRegistry) (Object) this) != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private void nirvana$craftCustomRecipe(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = NirvanaBrewingRecipes.getCustomResult(input, ingredient, (BrewingRecipeRegistry) (Object) this);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
