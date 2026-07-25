package galena.nirvana.index;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PeacefulAuraStatusEffect extends StatusEffect implements PolymerStatusEffect {

    public PeacefulAuraStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!(entity instanceof PlayerEntity player)) return false;

        double radius = 10 + amplifier * 2;

        for (MobEntity mob : world.getEntitiesByClass(MobEntity.class, player.getBoundingBox().expand(radius), e -> true)) {
            if (mob.getTarget() == player) {
                mob.setTarget(null);
            }
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
