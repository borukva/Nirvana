package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import galena.nirvana.effects.NirvanaEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(cancellable = true, at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z")
    public void canAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity) (Object) (this);

        if (NirvanaEffects.arePeaceful(self, target)) {
            cir.setReturnValue(false);
        }
    }

//    @WrapOperation(
//            method = "damageEquipment",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/lang/Math;max(FF)F"
//            )
//    )
//    private float hurtDeerStalker(float a, float b, Operation<Float> original) {
//        var amount = Math.max(a, b);
//        var self = (LivingEntity) (Object) (this);
//
//        for (EquipmentSlot slot : EquipmentSlot.values()) {
//            var stack = self.getEquippedStack(slot);
//            if (stack.getItem() instanceof ArmorLike) {
//                stack.hurtAndBreak((int) amount, self, slot);
//            }
//        }
//
//        return amount;
//    }
}
