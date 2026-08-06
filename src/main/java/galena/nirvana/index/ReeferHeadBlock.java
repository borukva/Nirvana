package galena.nirvana.index;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import galena.nirvana.Nirvana;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

/**
 * Decorative stand-in for the current mod's custom {@code SkullBlock.Type} — real skull types
 * are a static, non-syncable client enum, so this can't reuse vanilla's own skull rendering.
 * <p>
 * Disguised as a real vanilla skull, which is what gives it every behaviour a head is expected to
 * have for free and consistently on both sides: the small skull-sized hitbox, and mounting flat
 * against a wall (hitbox included) when placed on one. A barrier or plant-block disguise each lost
 * one of those - the plant block wasn't solid on the client, so players walked in and got yanked
 * back or stuck, and neither sticks to walls. Vanilla's skull block-entity renderer still draws
 * the stock skull underneath, so this block uses its own slightly inflated copy of the head model
 * ({@code reefer_head_block}) to fully cover it - the mob keeps the uninflated {@code
 * reefer_mob_head}.
 * <p>
 * The actual look comes from a {@link FactoryBlock} virtual-entity puppet, reusing the same head +
 * leafy-crown model as the mob's own head display element (see {@link
 * galena.nirvana.entity.ReeferModel}) - centered on the block and rotated to face the direction it
 * was placed in, same as a vanilla mob head.
 */
public class ReeferHeadBlock extends Block implements PolymerBlock, FactoryBlock {
    public ReeferHeadBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.ROTATION_16, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ROTATION_16);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        int rotation = Mth.floor((ctx.getRotation() * 16.0F / 360.0F) + 0.5D) & 15;
        return defaultBlockState().setValue(BlockStateProperties.ROTATION_16, rotation);
    }

    // Deliberately no custom collision/outline shape: this block's own settings are copied from
    // CREEPER_HEAD, so its shapes already match the skull the client is shown.

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.SKELETON_SKULL.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, state.getValue(BlockStateProperties.ROTATION_16));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState.getValue(BlockStateProperties.ROTATION_16));
    }

    static class Model extends ElementHolder {
        Model(int rotation) {
            var stack = new ItemStack(Items.PAPER);
            stack.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "reefer_head_block"));
            var display = ItemDisplayElementUtil.createSimple(stack);
            // The model is authored with the neck on model point (8,8,8), which lands on this
            // element's origin - a block attachment's own origin being the block's center. Drop
            // it half a block so the head rests on the block floor like a vanilla skull; X/Z need
            // no fudging, which is also what keeps it centered at every rotation rather than
            // orbiting an off-center pivot.
            display.setTranslation(new Vector3f(0F, -0.5F, 0F));
            // +180 degrees: rotation 0 otherwise faces the same way the placing player faced,
            // i.e. away from them, instead of towards them like a mounted trophy head should.
            display.setLeftRotation(new Quaternionf().rotateY((180F - rotation * 22.5F) * Mth.DEG_TO_RAD));
            addElement(display);
        }
    }
}
