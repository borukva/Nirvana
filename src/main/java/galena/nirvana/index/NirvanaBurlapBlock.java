package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.EnumMap;
import java.util.Map;

public class NirvanaBurlapBlock extends Block implements PolymerTexturedBlock {
    public static EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    private final Map<Direction, BlockState> models = new EnumMap<>(Direction.class);

    public NirvanaBurlapBlock(Settings settings, String path) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));

        Map<Direction, Integer> rotations = Map.of(
                Direction.NORTH, 0,
                Direction.SOUTH, 180,
                Direction.WEST, 270,
                Direction.EAST, 90
        );

        rotations.forEach((dir, yRot) -> models.put(dir,
                PolymerBlockResourceUtils.requestBlock(
                        BlockModelType.FULL_BLOCK,
                        PolymerBlockModel.of(
                                Identifier.of(Nirvana.MOD_ID, "block/" + path),
                                0, yRot
                        )
                )
        ));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return models.getOrDefault(state.get(FACING), models.get(Direction.NORTH));
    }
}
