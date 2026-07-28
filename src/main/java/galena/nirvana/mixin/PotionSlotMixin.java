package galena.nirvana.mixin;

import galena.nirvana.data.NirvanaBrewingRecipes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@code BrewingStandScreenHandler.PotionSlot#matches} hardcodes acceptance to
 * Items.POTION/SPLASH_POTION/LINGERING_POTION/GLASS_BOTTLE. An unmodified vanilla client already
 * accepts bong/potion_bong locally (both are network-disguised as Items.POTION, so the client's
 * own copy of this same check passes) - without this, the SERVER's authoritative re-check would
 * see the real (undisguised) item and bounce it right back out of the slot.
 */
@Mixin(targets = "net.minecraft.screen.BrewingStandScreenHandler$PotionSlot")
public abstract class PotionSlotMixin {
    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private static void nirvana$matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (NirvanaBrewingRecipes.isBongRelated(stack)) {
            cir.setReturnValue(true);
        }
    }
}
