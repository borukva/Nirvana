package galena.nirvana.mixin;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * {@code FabricBrewingRecipeRegistryBuilder} (used here in 1.21.x) is gone in 26.2's fabric-api -
 * vanilla's own {@code PotionBrewing.Builder} is the extension point now, built once via
 * {@code PotionBrewing.bootstrap(...)} which calls this exact method to seed every vanilla mix.
 * Piggybacking on the same builder here is the direct replacement.
 * <p>
 * Registers a harmless water -&gt; water no-op recipe purely to get {@code weed} into the
 * registry's valid-ingredient list (so the brewing stand GUI accepts it in the top slot at all);
 * {@link galena.nirvana.data.NirvanaBrewingRecipes#getCustomResult}, wired in via
 * {@link BrewingRecipeRegistryMixin}, always intercepts water + weed before this no-op recipe
 * would ever actually run.
 */
@Mixin(PotionBrewing.class)
public abstract class PotionBrewingMixin {
    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void nirvana$addWeedIngredient(PotionBrewing.Builder builder, CallbackInfo ci) {
        builder.addMix(Potions.WATER, NirvanaItems.WEED, Potions.WATER);
    }
}
