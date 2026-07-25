package galena.nirvana.entity;

import galena.nirvana.index.NirvanaBlocks;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Disguised as a plain primed-TNT entity ({@code EntityType.TNT}) — its visible block state
 * (driven by {@code TntEntity#getBlockState}) already matches whatever {@link galena.nirvana.index.ThcBlock}
 * disguises as, so this needs no separate texture work.
 */
public class PrimedThc extends TntEntity implements PolymerEntity, ICustomTnt {

    public PrimedThc(EntityType<? extends TntEntity> type, World world) {
        super(type, world);
        setBlockState(NirvanaBlocks.THC.getDefaultState());
    }

    public PrimedThc(World world, double x, double y, double z, LivingEntity igniter) {
        super(NirvanaEntities.THC, world);
        setPosition(x, y, z);
        double angle = world.getRandom().nextDouble() * Math.PI * 2.0;
        setVelocity(-Math.sin(angle) * 0.02, 0.2, -Math.cos(angle) * 0.02);
        setFuse(80);
        setBlockState(NirvanaBlocks.THC.getDefaultState());
        // TntEntity has no public "owner" setter — copyFrom-style ownership isn't essential here
        // since ThcCloud's effect application doesn't depend on entity ownership.
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.TNT;
    }

    @Override
    public boolean customExplode(double x, double y, double z, float power) {
        if (getEntityWorld() instanceof ServerWorld serverWorld) {
            ThcCloud.spawnCloud(serverWorld, new Vec3d(x, y, z), 1.5F, 60);
        }
        return true;
    }
}
