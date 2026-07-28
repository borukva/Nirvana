package galena.nirvana.index;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import galena.nirvana.Nirvana;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * The wall-mounted counterpart to {@link ReeferHeadBlock}. Vanilla splits skulls into two separate
 * blocks - one that stands on the floor with a 16-step rotation, one that mounts flat on a wall
 * with a facing - and the item picks between them; without this half, the head simply couldn't be
 * placed on a wall at all. Disguised as the matching vanilla wall skull so the sideways hitbox and
 * wall-hugging placement come for free and identically on both sides.
 */
public class ReeferWallHeadBlock extends Block implements PolymerBlock, FactoryBlock {
    public ReeferWallHeadBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    /**
     * The item asks the wall variant for a placement state no matter which face was clicked, so
     * this has to reject non-horizontal ones rather than shove e.g. {@code UP} into a horizontal
     * property - which throws, and takes the whole placement (floor variant included) down with
     * it.
     */
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                return getDefaultState().with(Properties.HORIZONTAL_FACING, direction.getOpposite());
            }
        }
        return null;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.SKELETON_WALL_SKULL.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState.get(Properties.HORIZONTAL_FACING));
    }

    static class Model extends ElementHolder {
        Model(Direction facing) {
            var stack = new ItemStack(Items.PAPER);
            stack.set(DataComponentTypes.ITEM_MODEL, Identifier.of(Nirvana.MOD_ID, "reefer_head_block"));
            var display = ItemDisplayElementUtil.createSimple(stack);
            // Vanilla wall skulls sit at mid-height and pushed back against the wall they're on,
            // rather than resting on the floor like the standing variant. Pushed the full 4px even
            // though reefer_head_block's cube is inflated half a pixel per side: that buries the
            // extra half pixel in the wall, which is the lesser evil - stopping half a pixel short
            // instead lines our back face up exactly with the vanilla skull's, and the stock skull
            // shows through.
            var back = facing.getOpposite();
            float pushBack = 0.25F;
            display.setTranslation(new Vector3f(back.getOffsetX() * pushBack, -0.25F, back.getOffsetZ() * pushBack));
            display.setLeftRotation(new Quaternionf().rotateY(-facing.getPositiveHorizontalDegrees() * MathHelper.RADIANS_PER_DEGREE));
            addElement(display);
        }
    }
}
