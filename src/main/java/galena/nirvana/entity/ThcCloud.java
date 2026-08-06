package galena.nirvana.entity;

import galena.nirvana.effects.NirvanaEffects;
import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.SmokingItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

/**
 * Server-authoritative visual/gameplay stand-in for a TNT blast: a harmless explosion sound +
 * particle burst, followed by a lingering area-effect cloud that applies our own real "Peace"
 * status effect. Uses a vanilla smoke particle as the cloud's visual — a genuinely custom
 * particle type would need the same kind of client-recognition work as blocks/items and wasn't
 * worth it for a cosmetic detail.
 */
public class ThcCloud {
    /** Peace V for anyone but a cat. */
    private static final int DEFAULT_AMPLIFIER = 4;
    /** Cats get off lightly - Peace I instead of the full dose everyone else catches. */
    private static final int CAT_AMPLIFIER = 0;
    private static final int SMOKE_PUFF_COUNT = 90;
    /** The puff should stand about two blocks tall, so spread half that either side of centre. */
    private static final double SMOKE_PUFF_HEIGHT = 1.0;

    /** Vanilla's own area-effect-cloud hitbox is a thin 0.5-block-tall slab - too easy to just
     * stand over or under and miss the effect entirely, so it's padded out further each way. */
    private static final double VERTICAL_PADDING_BELOW = 0.5;
    private static final double VERTICAL_PADDING_ABOVE = 0.75;
    private static final float CLOUD_HEIGHT = (float) (VERTICAL_PADDING_BELOW + 0.5 + VERTICAL_PADDING_ABOVE);
    private static final int CLOUD_DURATION_TICKS = 200;
    /** How far a blast reaches to chain-ignite nearby THC blocks - deliberately tighter than real
     * TNT's own ~4-block reach, so a row of THC "pops" one neighbour at a time instead of most of
     * a whole line catching in the same wave. */
    private static final double CHAIN_REACTION_RADIUS_PER_SIZE = 2.0;
    /** How hard a newly-triggered THC gets shoved away from the blast centre, same idea as the
     * knockback a real explosion gives nearby entities - tapered to nothing at the edge of range. */
    private static final double CHAIN_REACTION_KNOCKBACK = 0.4;

    public static AreaEffectCloud spawnCloud(ServerLevel world, Vec3 at, float size, int peaceSeconds) {
        world.explode(null, at.x, at.y, at.z, size / 10, Level.ExplosionInteraction.NONE);

        world.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                at.x, at.y + SMOKE_PUFF_HEIGHT, at.z,
                SMOKE_PUFF_COUNT,
                size * 0.8, SMOKE_PUFF_HEIGHT, size * 0.8,
                0.02
        );

        int effectDuration = 20 * peaceSeconds;
        // Every entity this cloud ever touches gets stacked exactly once by it, no matter how many
        // ticks they spend standing inside - otherwise re-applying every tick of a continuous stay
        // would stack the amplifier dozens of times a second instead of once per blast.
        Set<LivingEntity> alreadyStacked = new HashSet<>();
        // Spawned lower by the same amount the hitbox is padded downward, so the padded box still
        // spans from (at.y - below) to (at.y + 0.5 + above) instead of drifting the whole range down.
        var cloud = new AreaEffectCloud(world, at.x, at.y - VERTICAL_PADDING_BELOW, at.z) {
            @Override
            public EntityDimensions getDimensions(Pose pose) {
                return EntityDimensions.scalable(getRadius() * 2, CLOUD_HEIGHT);
            }

            @Override
            public void tick() {
                super.tick();
                // Deliberately not vanilla's own cloud.addEffect(...): that applies one fixed
                // amplifier to everyone it touches, and cats need a lighter dose than the rest,
                // which vanilla's own AreaEffectCloud has no per-entity-type hook for.
                if (level() instanceof ServerLevel serverWorld) {
                    applyPeaceByType(serverWorld, this, effectDuration, alreadyStacked);
                }
            }
        };
        cloud.setCustomParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE);
        float radius = 1.5F * size;
        cloud.setRadius(radius);
        cloud.setRadiusPerTick(-0.01F);
        cloud.setDuration(CLOUD_DURATION_TICKS);

        world.addFreshEntity(cloud);
        triggerNearbyThc(world, at, CHAIN_REACTION_RADIUS_PER_SIZE * size);
        return cloud;
    }

    /**
     * Same living entities vanilla's own cloud would catch, each getting whichever base amplifier
     * fits their kind - but through {@link SmokingItem#applyEffect}, so a blast stacks on top of
     * whatever Peace someone's already carrying (from an earlier blast, a joint, a bong...) the
     * exact same way repeated puffs already do, instead of flattening them back to the blast's own
     * fixed level. Only ever runs once per entity per cloud - see {@code alreadyStacked}.
     */
    private static void applyPeaceByType(ServerLevel world, AreaEffectCloud cloud, int effectDuration, Set<LivingEntity> alreadyStacked) {
        var targets = world.getEntities(EntityTypeTest.forClass(LivingEntity.class), cloud.getBoundingBox(),
                target -> target.isAffectedByPotions() && !alreadyStacked.contains(target));

        for (LivingEntity target : targets) {
            alreadyStacked.add(target);
            int amplifier = target instanceof Cat ? CAT_AMPLIFIER : DEFAULT_AMPLIFIER;
            SmokingItem.applyEffect(target, new MobEffectInstance(NirvanaEffects.PEACE, effectDuration, amplifier));
        }
    }

    /** Vanilla's own explosion step size, in blocks, when tracing a ray outward for resistance. */
    private static final float RAY_STEP = 0.3F;
    /** Vanilla's own per-step decay even through open air (before any block resistance is added). */
    private static final float RAY_DECAY_PER_STEP = RAY_STEP * 0.75F;
    /** Converts our "reach in open air" radius into vanilla's own "power" input for that formula:
     * in pure air each step only loses {@link #RAY_DECAY_PER_STEP}, so max reach = power / that * step. */
    private static final float RAY_POWER_PER_RADIUS = RAY_DECAY_PER_STEP / RAY_STEP;

    /**
     * Same chain-reaction feel as vanilla TNT: any THC block caught in the blast primes itself
     * (own random-ish fuse via {@link PrimedThc}'s own countdown) instead of exploding instantly,
     * so a row of THC "pops" one after another rather than all at once. Needed because this whole
     * blast is a cosmetic {@code ExplosionSourceType.NONE} explosion that never actually destroys
     * blocks, so nothing here would ever prime a neighbour on its own otherwise.
     * <p>
     * Reach is checked the same way vanilla's own explosion decides what it can destroy: a ray
     * from the blast centre to the candidate block loses "intensity" to every block's blast
     * resistance along the way, not just straight-line distance - so a THC block hidden behind a
     * few blocks of stone survives even well within the nominal radius, same as real TNT would.
     */
    private static void triggerNearbyThc(ServerLevel world, Vec3 at, double radius) {
        int r = (int) Math.ceil(radius);
        double radiusSqr = radius * radius;
        float power = (float) (radius * RAY_POWER_PER_RADIUS);
        BlockPos center = BlockPos.containing(at);

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, -r, -r), center.offset(r, r, r))) {
            if (world.getBlockState(pos).getBlock() != NirvanaBlocks.THC) continue;
            Vec3 blockCenter = Vec3.atCenterOf(pos);
            if (blockCenter.distanceToSqr(at) > radiusSqr) continue;
            if (!reachesThroughBlocks(world, at, blockCenter, power)) continue;

            var primed = new PrimedThc(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, null);
            // Matches TntBlock#onDestroyedByExplosion exactly: a chain-triggered fuse is much
            // shorter (and randomized) than the full one a freshly-lit TNT/THC gets, which is why
            // real chain reactions visibly race along faster than a single lit block would.
            int defaultFuse = primed.getFuse();
            primed.setFuse(world.getRandom().nextInt(defaultFuse / 4) + defaultFuse / 8);
            knockBack(primed, at, blockCenter, radius);
            world.addFreshEntity(primed);
            world.playSound(null, primed.getX(), primed.getY(), primed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        }
    }

    /**
     * Shoves a freshly-triggered THC away from the blast centre, on top of {@link PrimedThc}'s own
     * small "pop" velocity. Same shape vanilla's own explosion knockback uses for entities caught
     * in the blast: {@code velocity += normalize(direction) * impact}, where impact fades linearly
     * from 1 right at the centre to 0 at the edge of {@code radius} - no artificial extra upward
     * push tacked on, so it reads as a real shove rather than a scripted little hop.
     */
    private static void knockBack(PrimedThc primed, Vec3 blastCenter, Vec3 targetPos, double radius) {
        Vec3 away = targetPos.subtract(blastCenter);
        double distance = away.length();
        if (distance < 1.0E-4) return;

        double impact = Math.max(0, 1.0 - distance / radius) * CHAIN_REACTION_KNOCKBACK;
        Vec3 kick = away.scale(impact / distance);
        primed.setDeltaMovement(primed.getDeltaMovement().add(kick));
    }

    /** Traces a single ray from {@code from} to {@code to}, vanilla-explosion-style, returning
     * whether any blast intensity is left by the time it arrives. */
    private static boolean reachesThroughBlocks(ServerLevel world, Vec3 from, Vec3 to, float power) {
        Vec3 offset = to.subtract(from);
        double length = offset.length();
        if (length < 1.0E-4) return true;
        Vec3 step = offset.scale(RAY_STEP / length);

        float intensity = power;
        Vec3 pos = from;
        for (double travelled = 0; travelled < length; travelled += RAY_STEP) {
            BlockState state = world.getBlockState(BlockPos.containing(pos));
            if (!state.isAir()) {
                intensity -= (state.getBlock().getExplosionResistance() + 0.3F) * RAY_STEP;
            }
            intensity -= RAY_DECAY_PER_STEP;
            if (intensity <= 0) return false;
            pos = pos.add(step);
        }
        return true;
    }
}
