package galena.nirvana.world.block;

import galena.nirvana.world.entity.PrimedThc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ThcBlock extends TntBlock implements ICustomTntBlock {

    public ThcBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (level.isClientSide()) return;

        var primed = new PrimedThc(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
        level.addFreshEntity(primed);
        level.playSound(null, primed.getX(), primed.getY(), primed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        // TntBlockMixin cancels vanilla TntBlock#prime for ICustomTntBlock blocks, which also
        // skips vanilla's block-to-air removal that normally follows a successful prime() call.
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        var primed = new PrimedThc(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getIndirectSourceEntity());
        var fuse = primed.getFuse();
        primed.setFuse(level.random.nextInt(fuse / 4) + fuse / 8);
        level.addFreshEntity(primed);
    }
}
