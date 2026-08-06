package galena.nirvana.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import galena.nirvana.entity.ICustomCreeper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Creeper.class)
public abstract class CreeperEntityMixin {

    // explodeCreeper() (Yarn: explode()) calls ServerLevel.explode(...) directly now, not the
    // old createExplosion(...) - same idea (make a real explosion), renamed and trimmed down to
    // just the parameters a mob's own self-detonation actually needs.
    @WrapOperation(
            method = "explodeCreeper",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
            )
    )
    private void createCustomExplosion(ServerLevel instance, Entity source, double x, double y, double z, float power, Level.ExplosionInteraction type, Operation<Void> original) {
        if (!(source instanceof ICustomCreeper customCreeper) || !customCreeper.customExplode(x, y, z, power)) {
            original.call(instance, source, x, y, z, power, type);
        }
    }
}
