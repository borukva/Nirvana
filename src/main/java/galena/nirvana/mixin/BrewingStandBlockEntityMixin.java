package galena.nirvana.mixin;

import galena.nirvana.data.NirvanaBrewingRecipes;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets hoppers move bong/potion_bong between brewing stands. This is purely server-side
 * automation (no client cooperation needed) - vanilla's own {@code isValid()} hardcodes its 3
 * potion slots to 4 vanilla items, which also gates hopper insertion, not just the player-facing
 * GUI (that part stays out of reach, since it's enforced identically on an unmodified client).
 */
@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void nirvana$isValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot >= 0 && slot <= 2 && NirvanaBrewingRecipes.isBongRelated(stack)) {
            cir.setReturnValue(true);
        }
    }
}
