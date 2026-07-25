package galena.nirvana.entity;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.index.ThcBlock;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Optional;

public class MinecartThc extends TntMinecartEntity implements PolymerEntity {

    public MinecartThc(EntityType<? extends TntMinecartEntity> type, World world) {
        super(type, world);
        // The client is disguised as a genuine vanilla TntMinecartEntity, so it renders whatever
        // CUSTOM_BLOCK_STATE carries and otherwise falls back to *its own* getDefaultContainedBlock()
        // (hardcoded to Blocks.TNT) - our override below never runs on the client. Pushing the real,
        // disguised THC state through this tracked field is what actually makes it show up in-world.
        setCustomBlockState(Optional.of(((ThcBlock) NirvanaBlocks.THC).getDisguisedState()));
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.TNT_MINECART;
    }

    @Override
    public BlockState getDefaultContainedBlock() {
        return NirvanaBlocks.THC.getDefaultState();
    }

    @Override
    protected Item asItem() {
        return NirvanaItems.THC_MINECART;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(NirvanaItems.THC_MINECART);
    }

    @Override
    protected void explode(DamageSource source, double power) {
        if (getEntityWorld() instanceof ServerWorld serverWorld) {
            ThcCloud.spawnCloud(serverWorld, getEntityPos(), 1.5F, 60);
        }
        discard();
    }
}
