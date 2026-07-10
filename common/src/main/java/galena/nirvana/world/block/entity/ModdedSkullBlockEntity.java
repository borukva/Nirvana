package galena.nirvana.world.block.entity;

import galena.nirvana.index.NirvanaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModdedSkullBlockEntity extends SkullBlockEntity {

    public ModdedSkullBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return NirvanaBlocks.MODDED_SKULL.get();
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        // SkullBlockEntity hardcodes BlockEntityType.SKULL in its constructor, but our block
        // is registered under NirvanaBlocks.MODDED_SKULL. Redirect validation to the correct type.
        return NirvanaBlocks.MODDED_SKULL.get().isValid(state);
    }

}
