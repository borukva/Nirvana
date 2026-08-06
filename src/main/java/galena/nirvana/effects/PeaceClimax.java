package galena.nirvana.effects;

import galena.nirvana.entity.NirvanaEntities;
import galena.nirvana.entity.Reefer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

/**
 * The "high enough" payoff for repeatedly smoking Peace-effect items: once the effect's
 * amplifier crosses a threshold, any nearby real creepers get swapped out for reefers outright.
 * Thresholds are hardcoded here (matching the main Fabric mod's config defaults) since this
 * project has no config system of its own yet.
 */
public class PeaceClimax {
    private static final int REEFER_AFTER_HITS = 3;
    private static final int HUNGER_AFTER_HITS = 5;

    private static final double REEFER_CONVERSION_RANGE = 20;
    private static final double REEFER_CONVERSION_RANGE_SQR = REEFER_CONVERSION_RANGE * REEFER_CONVERSION_RANGE;
    /** Only the nearest handful convert per hit, so a hit near a big farm doesn't wipe it out at once. */
    private static final int MAX_CONVERTED_PER_HIT = 3;

    public static void onIncreasedTo(LivingEntity target, int amplifier) {
        if (!(target.level() instanceof ServerLevel world)) return;
        int hitsTaken = amplifier + 1;

        if (hitsTaken >= REEFER_AFTER_HITS) {
            transformCreepers(target.position(), world);
        }

        if (hitsTaken >= HUNGER_AFTER_HITS) {
            target.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 20, 2));
        }
    }

    private static void transformCreepers(Vec3 around, ServerLevel world) {
        var box = AABB.ofSize(around, REEFER_CONVERSION_RANGE * 2, REEFER_CONVERSION_RANGE * 2, REEFER_CONVERSION_RANGE * 2);
        var targets = world.getEntities(EntityTypeTest.forClass(Creeper.class), box,
                creeper -> !(creeper instanceof Reefer) && creeper.distanceToSqr(around) <= REEFER_CONVERSION_RANGE_SQR);

        targets.sort(Comparator.comparingDouble(creeper -> creeper.distanceToSqr(around)));

        for (Creeper creeper : targets.subList(0, Math.min(targets.size(), MAX_CONVERTED_PER_HIT))) {
            var replacement = new Reefer(NirvanaEntities.REEFER, world);
            replacement.snapTo(creeper.getX(), creeper.getY(), creeper.getZ(), creeper.getYRot(), creeper.getXRot());
            creeper.discard();
            world.addFreshEntity(replacement);
        }
    }
}
