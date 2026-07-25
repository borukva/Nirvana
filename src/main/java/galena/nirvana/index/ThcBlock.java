package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.block.ICustomTntBlock;
import galena.nirvana.entity.NirvanaEntities;
import galena.nirvana.entity.PrimedThc;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * TNT's own shape/behavior class-wise, but with its own top/side/bottom texture set instead of
 * TNT's look; spawns {@link PrimedThc} instead of vanilla TNT on ignite.
 */
public class ThcBlock extends TntBlock implements PolymerTexturedBlock, ICustomTntBlock {
    private final BlockState model = PolymerBlockResourceUtils.requestBlock(
            BlockModelType.FULL_BLOCK,
            PolymerBlockModel.of(Identifier.of(Nirvana.MOD_ID, "block/thc"))
    );

    public ThcBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    /**
     * The real, note-block-hack vanilla state THC disguises as - needed by {@link
     * galena.nirvana.entity.MinecartThc} to make the client (which never runs our server-only
     * block/entity classes) actually render the custom texture instead of falling back to
     * whatever a vanilla TNT minecart hardcodes.
     */
    public BlockState getDisguisedState() {
        return model;
    }

    @Override
    public void onCaughtFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!(world instanceof ServerWorld serverWorld)) return;

        var primed = new PrimedThc(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
        serverWorld.spawnEntity(primed);
        world.playSound(null, primed.getX(), primed.getY(), primed.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        // TntBlockMixin cancels vanilla TntBlock#primeTnt for ICustomTntBlock blocks, which also
        // skips vanilla's block-to-air removal that normally follows a successful primeTnt() call.
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
    }
}
