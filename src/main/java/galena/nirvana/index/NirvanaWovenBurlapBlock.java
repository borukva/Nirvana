package galena.nirvana.index;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import static galena.nirvana.Nirvana.id;

public class NirvanaWovenBurlapBlock extends RotatedPillarBlock implements PolymerTexturedBlock {
    private final BlockState[] model = new BlockState[3];

    public NirvanaWovenBurlapBlock(Properties settings, String path) {
        super(settings.noOcclusion());
        model[0] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(id("block/"+path), 90, 90));
        model[1] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(id("block/"+path), 0, 0));
        model[2] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(id("block/"+path), 90, 0));
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return switch (state.getValue(RotatedPillarBlock.AXIS)) {
            case X -> model[0];
            case Y -> model[1];
            case Z -> model[2];
        };
    }
}
