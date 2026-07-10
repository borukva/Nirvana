package galena.nirvana.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import galena.nirvana.world.entity.ICustomCreeper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Creeper.class)
public abstract class CreeperMixin {

    @WrapWithCondition(
            method = "explodeCreeper",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
            )
    )
    public boolean createCustomExplosion(ServerLevel instance, Entity creeper, double x, double y, double z, float radius, Level.ExplosionInteraction interaction) {
        if(!(creeper instanceof ICustomCreeper customCreeper)) return true;
        return !customCreeper.customExplode(x, y, z, radius);
    }

}
