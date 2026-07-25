package galena.nirvana.mixin;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Experiment: {@code BrewingStandScreenHandler.PotionSlot#matches} hardcodes acceptance to
 * Items.POTION/SPLASH_POTION/LINGERING_POTION/GLASS_BOTTLE. An unmodified vanilla client already
 * accepts {@link NirvanaItems#TEST_POTION_BONG} locally (it's network-disguised as Items.POTION,
 * so the client's own copy of this same check passes) - the bounce-back the player observed comes
 * from the SERVER's authoritative re-check seeing the real (undisguised) item and rejecting it.
 * Patching just this side should be enough to make it actually stick.
 */
@Mixin(targets = "net.minecraft.screen.BrewingStandScreenHandler$PotionSlot")
public abstract class PotionSlotMixin {
    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private static void nirvana$matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(NirvanaItems.TEST_POTION_BONG)) {
            cir.setReturnValue(true);
        }
    }
}
