package galena.nirvana.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ICustomTntBlock {
    void onCaughtFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter);
}
