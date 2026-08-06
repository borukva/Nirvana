package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

import java.util.EnumMap;
import java.util.Map;

public class NirvanaBurlapBlock extends Block implements PolymerTexturedBlock {
    public static EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final Map<Direction, BlockState> models = new EnumMap<>(Direction.class);

    public NirvanaBurlapBlock(Properties settings, String path) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));

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
                                Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "block/" + path),
                                0, yRot
                        )
                )
        ));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return models.getOrDefault(state.getValue(FACING), models.get(Direction.NORTH));
    }
}
