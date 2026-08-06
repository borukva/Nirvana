package galena.nirvana.mixin;

import galena.nirvana.data.NirvanaBrewingRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects the custom bong-brewing steps (see {@link NirvanaBrewingRecipes#getCustomResult}) ahead
 * of vanilla's own matching, since {@code bong}/{@code potion_bong} can't be registered as real
 * potion containers (vanilla's {@code assertPotion} check requires a genuine {@code PotionItem}).
 */
@Mixin(PotionBrewing.class)
public abstract class BrewingRecipeRegistryMixin {
    /**
     * {@code BrewingStandBlockEntity#isBrewable} - the gate that decides whether a brew even
     * *starts* - checks {@code hasMix}, not {@code hasPotionMix}: {@code hasMix} first requires
     * {@code isContainer(bottomSlotStack)} (a genuine registered vanilla container item - potion,
     * splash potion, lingering potion, glass bottle) before it ever consults {@code hasPotionMix}.
     * {@code bong}/{@code potion_bong} were never registered as containers, so without this,
     * {@code isBrewable} always fails the instant one of them is sitting in the bottom slot - the
     * water+weed -> bong step works (a water bottle IS a real container), but every step after
     * that (bong + nether wart -> potion_bong, potion_bong + catalyst -> ...) never even begins.
     * {@code doBrew} itself calls {@code mix} directly with no such gate, so patching just this
     * entry point is enough once a brew is actually allowed to start.
     */
    @Inject(method = "hasMix", at = @At("HEAD"), cancellable = true)
    private void nirvana$hasCustomMix(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (NirvanaBrewingRecipes.getCustomResult(input, ingredient, (PotionBrewing) (Object) this) != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasPotionMix", at = @At("HEAD"), cancellable = true)
    private void nirvana$hasCustomRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (NirvanaBrewingRecipes.getCustomResult(input, ingredient, (PotionBrewing) (Object) this) != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private void nirvana$craftCustomRecipe(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = NirvanaBrewingRecipes.getCustomResult(input, ingredient, (PotionBrewing) (Object) this);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
