package galena.nirvana.effects;

import galena.nirvana.entity.NirvanaEntities;
import galena.nirvana.entity.Reefer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

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

    public static void onIncreasedTo(LivingEntity target, int amplifier) {
        if (!(target.getEntityWorld() instanceof ServerWorld world)) return;
        int hitsTaken = amplifier + 1;

        if (hitsTaken >= REEFER_AFTER_HITS) {
            transformCreepers(target.getEntityPos(), world);
        }

        if (hitsTaken >= HUNGER_AFTER_HITS) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * 20, 2));
        }
    }

    private static void transformCreepers(Vec3d around, ServerWorld world) {
        var box = Box.of(around, REEFER_CONVERSION_RANGE * 2, REEFER_CONVERSION_RANGE * 2, REEFER_CONVERSION_RANGE * 2);
        var targets = world.getEntitiesByType(TypeFilter.instanceOf(CreeperEntity.class), box,
                creeper -> !(creeper instanceof Reefer) && creeper.squaredDistanceTo(around) <= REEFER_CONVERSION_RANGE_SQR);

        for (CreeperEntity creeper : targets) {
            var replacement = new Reefer(NirvanaEntities.REEFER, world);
            replacement.refreshPositionAndAngles(creeper.getX(), creeper.getY(), creeper.getZ(), creeper.getYaw(), creeper.getPitch());
            creeper.discard();
            world.spawnEntity(replacement);
        }
    }
}
