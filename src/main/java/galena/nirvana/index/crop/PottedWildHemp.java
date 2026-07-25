package galena.nirvana.index.crop;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * A standalone flowerpot holding {@link galena.nirvana.index.NirvanaBlocks#WILD_HEMP}.
 * Right-click-to-empty still works (vanilla {@code FlowerPotBlock} logic), but this pot can't
 * be reached by right-clicking wild hemp into a vanilla empty pot — that lookup table is
 * private in vanilla and would need a mixin to extend. Disguised as a potted fern since there's
 * no vanilla "potted plant" custom-texture template.
 */
public class PottedWildHemp extends FlowerPotBlock implements PolymerBlock {
    public PottedWildHemp(Block content, Settings settings) {
        super(content, settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.POTTED_FERN.getDefaultState();
    }
}
