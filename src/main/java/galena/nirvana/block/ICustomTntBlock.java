package galena.nirvana.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public interface ICustomTntBlock {
    void onCaughtFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter);

    /** Called instead of vanilla's own {@code TntBlock#onDestroyedByExplosion} - a nearby creeper
     * or real TNT destroying this block would otherwise still spawn a genuine vanilla TntEntity,
     * turning it into a real destructive explosion regardless of what this block disguises as. */
    void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion);
}
