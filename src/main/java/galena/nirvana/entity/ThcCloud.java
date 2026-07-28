package galena.nirvana.entity;

import galena.nirvana.effects.NirvanaEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Server-authoritative visual/gameplay stand-in for a TNT blast: a harmless explosion sound +
 * particle burst, followed by a lingering area-effect cloud that applies our own real "Peace"
 * status effect. Uses a vanilla smoke particle as the cloud's visual — a genuinely custom
 * particle type would need the same kind of client-recognition work as blocks/items and wasn't
 * worth it for a cosmetic detail.
 */
public class ThcCloud {
    private static final int DEFAULT_AMPLIFIER = 4;
    private static final int SMOKE_PUFF_COUNT = 90;
    /** The puff should stand about two blocks tall, so spread half that either side of centre. */
    private static final double SMOKE_PUFF_HEIGHT = 1.0;

    public static AreaEffectCloudEntity spawnCloud(ServerWorld world, Vec3d at, float size, int peaceSeconds) {
        world.createExplosion(null, at.x, at.y, at.z, size / 10, World.ExplosionSourceType.NONE);

        world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                at.x, at.y + SMOKE_PUFF_HEIGHT, at.z,
                SMOKE_PUFF_COUNT,
                size * 0.8, SMOKE_PUFF_HEIGHT, size * 0.8,
                0.02
        );

        var cloud = new AreaEffectCloudEntity(world, at.x, at.y, at.z);
        cloud.addEffect(new StatusEffectInstance(NirvanaEffects.PEACE, 20 * peaceSeconds, DEFAULT_AMPLIFIER));
        cloud.setParticleType(ParticleTypes.CAMPFIRE_COSY_SMOKE);
        cloud.setRadius(1.5F * size);
        cloud.setRadiusGrowth(-0.01F);
        cloud.setDuration(200);

        world.spawnEntity(cloud);
        return cloud;
    }
}
