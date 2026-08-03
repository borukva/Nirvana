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
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
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

    /**
     * A real nearby explosion (a creeper, real TNT, anything) destroying this block would
     * otherwise fall through to vanilla's own {@code TntBlock#onDestroyedByExplosion}, which
     * spawns a genuine vanilla TntEntity - turning THC into a real destructive explosion the
     * instant something else blows up next to it. Spawns {@link PrimedThc} instead, with the same
     * short randomized fuse vanilla itself uses here (notably shorter than a freshly-lit one -
     * see {@code TntBlock#onDestroyedByExplosion}), so chain reactions started by outside sources
     * still race along the same way a THC-to-THC chain already does.
     */
    @Override
    public void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion) {
        if (!world.getGameRules().getBoolean(GameRules.TNT_EXPLODES)) return;

        var primed = new PrimedThc(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getCausingEntity());
        int defaultFuse = primed.getFuse();
        primed.setFuse(world.getRandom().nextInt(defaultFuse / 4) + defaultFuse / 8);
        world.spawnEntity(primed);
    }
}
