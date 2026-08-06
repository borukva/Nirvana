package galena.nirvana.index;

import eu.pb4.polymer.core.api.other.PolymerMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.server.level.ServerLevel;

public class PeacefulAuraStatusEffect extends MobEffect implements PolymerMobEffect {

    public PeacefulAuraStatusEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return false;

        double radius = 10 + amplifier * 2;

        for (Mob mob : world.getEntities(EntityTypeTest.forClass(Mob.class), player.getBoundingBox().inflate(radius), e -> true)) {
            if (mob.getTarget() == player) {
                mob.setTarget(null);
            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
