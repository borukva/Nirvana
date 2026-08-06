package galena.nirvana.entity;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.mixin.CreeperEntityAccessor;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * A "peaceful" creeper variant: explodes into a {@link ThcCloud} instead of a real explosion,
 * and drops a reefer head when killed by a charged creeper's blast (capped at one per blast,
 * same as vanilla mob-head drops). Disguised as an (empty, invisible) item display, with the
 * actual look supplied by a {@link ReeferModel} puppet - Polymer has no way to give a real mob
 * entity type a custom limbed model without a client mod.
 */
public class Reefer extends Creeper implements PolymerEntity, ICustomCreeper {
    @Nullable
    private ReeferModel model;

    public Reefer(EntityType<? extends Creeper> type, Level world) {
        super(type, world);
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityTypes.ITEM_DISPLAY;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.model == null && level() instanceof ServerLevel) {
            this.model = new ReeferModel(this);
            EntityAttachment.ofTicking(this.model, this);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.model != null) {
            this.model.destroy();
            this.model = null;
        }
    }

    @Override
    public boolean customExplode(double x, double y, double z, float radius) {
        if (level() instanceof ServerLevel serverWorld) {
            ThcCloud.spawnCloud(serverWorld, new Vec3(x, y, z), 1F, 30);
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        var cause = source.getDirectEntity();
        if (cause != this && cause instanceof Creeper creeper && creeper.isPowered()) {
            var accessor = (CreeperEntityAccessor) creeper;
            if (!accessor.getHeadsDropped()) {
                accessor.setHeadsDropped(true);
                spawnAtLocation(world, NirvanaBlocks.REEFER_HEAD_ITEM);
            }
        }

        // Same easter egg as a vanilla creeper dropping a music disc when a skeleton lands the
        // killing blow.
        var attacker = source.getEntity();
        if (attacker != null && attacker.getType().builtInRegistryHolder().is(EntityTypeTags.SKELETONS)) {
            spawnAtLocation(world, NirvanaItems.MUSIC_DISC_JAM);
        }
    }
}
