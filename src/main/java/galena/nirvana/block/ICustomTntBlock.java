package galena.nirvana.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

public interface ICustomTntBlock {
    void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter);

    /** Called instead of vanilla's own {@code TntBlock#wasExploded} - a nearby creeper
     * or real TNT destroying this block would otherwise still spawn a genuine vanilla PrimedTnt,
     * turning it into a real destructive explosion regardless of what this block disguises as. */
    void wasExploded(ServerLevel world, BlockPos pos, Explosion explosion);
}
