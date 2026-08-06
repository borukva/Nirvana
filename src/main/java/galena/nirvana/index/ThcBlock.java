package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.block.ICustomTntBlock;
import galena.nirvana.entity.NirvanaEntities;
import galena.nirvana.entity.PrimedThc;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * TNT's own shape/behavior class-wise, but with its own top/side/bottom texture set instead of
 * TNT's look; spawns {@link PrimedThc} instead of vanilla TNT on ignite.
 */
public class ThcBlock extends TntBlock implements PolymerTexturedBlock, ICustomTntBlock {
    private final BlockState model = PolymerBlockResourceUtils.requestBlock(
            BlockModelType.FULL_BLOCK,
            PolymerBlockModel.of(Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "block/thc"))
    );

    public ThcBlock(Properties settings) {
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
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!(world instanceof ServerLevel serverWorld)) return;

        var primed = new PrimedThc(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
        serverWorld.addFreshEntity(primed);
        world.playSound(null, primed.getX(), primed.getY(), primed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        // TntBlockMixin cancels vanilla TntBlock#primeTnt for ICustomTntBlock blocks, which also
        // skips vanilla's block-to-air removal that normally follows a successful primeTnt() call.
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
    }

    /**
     * A real nearby explosion (a creeper, real TNT, anything) destroying this block would
     * otherwise fall through to vanilla's own {@code TntBlock#onDestroyedByExplosion}, which
     * spawns a genuine vanilla PrimedTnt - turning THC into a real destructive explosion the
     * instant something else blows up next to it. Spawns {@link PrimedThc} instead, with the same
     * short randomized fuse vanilla itself uses here (notably shorter than a freshly-lit one -
     * see {@code TntBlock#onDestroyedByExplosion}), so chain reactions started by outside sources
     * still race along the same way a THC-to-THC chain already does.
     */
    @Override
    public void wasExploded(ServerLevel world, BlockPos pos, Explosion explosion) {
        if (!world.getGameRules().get(GameRules.TNT_EXPLODES)) return;

        var igniter = explosion.getDirectSourceEntity() instanceof LivingEntity living ? living : null;
        var primed = new PrimedThc(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
        int defaultFuse = primed.getFuse();
        primed.setFuse(world.getRandom().nextInt(defaultFuse / 4) + defaultFuse / 8);
        world.addFreshEntity(primed);
    }
}
