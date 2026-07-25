package galena.nirvana.entity;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.mixin.CreeperEntityAccessor;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
// import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
// import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * A "peaceful" creeper variant: explodes into a {@link ThcCloud} instead of a real explosion,
 * and drops a reefer head when killed by a charged creeper's blast (capped at one per blast,
 * same as vanilla mob-head drops). Disguised as a plain {@code EntityType.CREEPER} for now — no
 * client mod means no custom mob model, so it looks like an ordinary creeper.
 *
 * A work-in-progress custom look (multi-part virtual entity puppet + minecraft:interaction
 * hitbox, see {@link ReeferModel} and reefer_mob.json) is shelved below, commented out, to
 * revisit later.
 */
public class Reefer extends CreeperEntity implements PolymerEntity, ICustomCreeper {
    // @Nullable
    // private ReeferModel model;

    public Reefer(EntityType<? extends CreeperEntity> type, World world) {
        super(type, world);
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.CREEPER;
        // return EntityType.ITEM_DISPLAY;
    }

    // @Override
    // public void tick() {
    //     super.tick();
    //     if (this.model == null && getEntityWorld() instanceof ServerWorld) {
    //         this.model = new ReeferModel(this);
    //         EntityAttachment.ofTicking(this.model, this);
    //     }
    // }
    //
    // @Override
    // public void remove(RemovalReason reason) {
    //     super.remove(reason);
    //     if (this.model != null) {
    //         this.model.destroy();
    //         this.model = null;
    //     }
    // }

    @Override
    public boolean customExplode(double x, double y, double z, float radius) {
        if (getEntityWorld() instanceof ServerWorld serverWorld) {
            ThcCloud.spawnCloud(serverWorld, new Vec3d(x, y, z), 1F, 30);
        }
        return true;
    }

    @Override
    protected void dropLoot(ServerWorld world, DamageSource source, boolean causedByPlayer) {
        var cause = source.getSource();
        if (cause != this && cause instanceof CreeperEntity creeper && creeper.isCharged()) {
            var accessor = (CreeperEntityAccessor) creeper;
            if (!accessor.getHeadsDropped()) {
                accessor.setHeadsDropped(true);
                dropItem(world, NirvanaBlocks.REEFER_HEAD_ITEM);
            }
        }
    }
}
