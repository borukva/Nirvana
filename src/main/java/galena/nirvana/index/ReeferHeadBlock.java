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
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

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
    public ReeferHeadBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.ROTATION, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.ROTATION);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        int rotation = MathHelper.floor((ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15;
        return getDefaultState().with(Properties.ROTATION, rotation);
    }

    // Deliberately no custom collision/outline shape: this block's own settings are copied from
    // CREEPER_HEAD, so its shapes already match the skull the client is shown.

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.SKELETON_SKULL.getDefaultState().with(Properties.ROTATION, state.get(Properties.ROTATION));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState.get(Properties.ROTATION));
    }

    static class Model extends ElementHolder {
        Model(int rotation) {
            var stack = new ItemStack(Items.PAPER);
            stack.set(DataComponentTypes.ITEM_MODEL, Identifier.of(Nirvana.MOD_ID, "reefer_head_block"));
            var display = ItemDisplayElementUtil.createSimple(stack);
            // The model is authored with the neck on model point (8,8,8), which lands on this
            // element's origin - a block attachment's own origin being the block's center. Drop
            // it half a block so the head rests on the block floor like a vanilla skull; X/Z need
            // no fudging, which is also what keeps it centered at every rotation rather than
            // orbiting an off-center pivot.
            display.setTranslation(new Vector3f(0F, -0.5F, 0F));
            // +180 degrees: rotation 0 otherwise faces the same way the placing player faced,
            // i.e. away from them, instead of towards them like a mounted trophy head should.
            display.setLeftRotation(new Quaternionf().rotateY((180F - rotation * 22.5F) * MathHelper.RADIANS_PER_DEGREE));
            addElement(display);
        }
    }
}
