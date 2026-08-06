package galena.nirvana.entity;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.index.ThcBlock;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import java.util.Optional;

public class MinecartThc extends MinecartTNT implements PolymerEntity {

    public MinecartThc(EntityType<? extends MinecartTNT> type, Level world) {
        super(type, world);
        // The client is disguised as a genuine vanilla MinecartTNT, so it renders whatever
        // CUSTOM_DISPLAY_BLOCK carries and otherwise falls back to *its own* getDefaultDisplayBlockState()
        // (hardcoded to Blocks.TNT) - our override below never runs on the client. Pushing the real,
        // disguised THC state through this tracked field is what actually makes it show up in-world.
        setCustomDisplayBlockState(Optional.of(((ThcBlock) NirvanaBlocks.THC).getDisguisedState()));
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityTypes.TNT_MINECART;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return NirvanaBlocks.THC.defaultBlockState();
    }

    @Override
    protected Item getDropItem() {
        return NirvanaItems.THC_MINECART;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(NirvanaItems.THC_MINECART);
    }

    @Override
    protected void explode(DamageSource source, double power) {
        if (level() instanceof ServerLevel serverWorld) {
            ThcCloud.spawnCloud(serverWorld, position(), 1.5F, 60);
        }
        discard();
    }
}
