package galena.nirvana.index;

import galena.nirvana.Nirvana;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

/**
 * Decorative stand-in for the current mod's custom {@code SkullBlock.Type} — real skull types
 * are a static, non-syncable client enum, so this can't keep the 3D skull-on-a-stick shape.
 * Single non-rotated custom texture (rotation-per-facing was dropped for scope; the block
 * always looks the same regardless of which way it's placed).
 */
public class ReeferHeadBlock extends Block implements PolymerTexturedBlock {
    private final BlockState model;

    public ReeferHeadBlock(Settings settings) {
        super(settings);
        this.model = PolymerBlockResourceUtils.requestBlock(
                BlockModelType.FULL_BLOCK,
                PolymerBlockModel.of(Identifier.of(Nirvana.MOD_ID, "block/reefer_head"))
        );
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }
}
