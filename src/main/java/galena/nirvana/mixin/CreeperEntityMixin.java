package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import galena.nirvana.entity.ICustomCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {

    @WrapOperation(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)V"
            )
    )
    private void createCustomExplosion(ServerWorld instance, Entity source, double x, double y, double z, float power, World.ExplosionSourceType type, Operation<Void> original) {
        if (!(source instanceof ICustomCreeper customCreeper) || !customCreeper.customExplode(x, y, z, power)) {
            original.call(instance, source, x, y, z, power, type);
        }
    }
}
