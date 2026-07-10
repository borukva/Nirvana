package galena.nirvana.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import galena.nirvana.index.NirvanaEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    public boolean canAttack(boolean original, ServerLevel level, DamageSource damageSource) {
        if (original) return true;
        var instance = (LivingEntity) (Object) this;
        return damageSource.getEntity() instanceof LivingEntity attacker
                && NirvanaEffects.arePeaceful(instance, attacker);
    }

}
