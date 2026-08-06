package galena.nirvana.entity;

import galena.nirvana.index.NirvanaBlocks;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * Disguised as a plain primed-TNT entity ({@code EntityType.TNT}) — its visible block state
 * (driven by {@code PrimedTnt#getBlockState}) already matches whatever {@link galena.nirvana.index.ThcBlock}
 * disguises as, so this needs no separate texture work.
 */
public class PrimedThc extends PrimedTnt implements PolymerEntity, ICustomTnt {

    public PrimedThc(EntityType<? extends PrimedTnt> type, Level world) {
        super(type, world);
        setBlockState(NirvanaBlocks.THC.defaultBlockState());
    }

    public PrimedThc(Level world, double x, double y, double z, LivingEntity igniter) {
        super(NirvanaEntities.THC, world);
        setPos(x, y, z);
        double angle = world.getRandom().nextDouble() * Math.PI * 2.0;
        setDeltaMovement(-Math.sin(angle) * 0.02, 0.2, -Math.cos(angle) * 0.02);
        setFuse(80);
        setBlockState(NirvanaBlocks.THC.defaultBlockState());
        // PrimedTnt has no public "owner" setter — copyFrom-style ownership isn't essential here
        // since ThcCloud's effect application doesn't depend on entity ownership.
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityTypes.TNT;
    }

    @Override
    public boolean customExplode(double x, double y, double z, float power) {
        if (level() instanceof ServerLevel serverWorld) {
            ThcCloud.spawnCloud(serverWorld, new Vec3(x, y, z), 1.5F, 60);
        }
        return true;
    }
}
