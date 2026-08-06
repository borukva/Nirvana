package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import galena.nirvana.entity.ICustomTnt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PrimedTnt.class)
public abstract class TntEntityMixin {

    // createExplosion(...) is just called explode(...) now, same full parameter list.
    @WrapOperation(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
            )
    )
    private void createCustomExplosion(Level instance, Entity source, DamageSource damageSource, ExplosionDamageCalculator behavior, double x, double y, double z, float power, boolean createFire, Level.ExplosionInteraction type, Operation<Void> original) {
        if (!(source instanceof ICustomTnt customTnt) || !customTnt.customExplode(x, y, z, power)) {
            original.call(instance, source, damageSource, behavior, x, y, z, power, createFire, type);
        }
    }
}
