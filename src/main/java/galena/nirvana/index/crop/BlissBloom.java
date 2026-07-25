package galena.nirvana.index.crop;

import galena.nirvana.Nirvana;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

public class BlissBloom extends TallPlantBlock implements PolymerTexturedBlock, FactoryBlock {
    private final BlockState model = PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT_BLOCK);
    private static final ItemStack DISPLAY_MODEL = ItemDisplayElementUtil.getModel(Identifier.of(Nirvana.MOD_ID, "block/bliss_bloom"));

    public BlissBloom(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.ROSE_BUSH.getDefaultState();
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        if (initialBlockState.get(HALF) != DoubleBlockHalf.LOWER) return null;
        return new Model();
    }

    static class Model extends BlockModel {
        public ItemDisplayElement main;

        public Model() {
            this.main = ItemDisplayElementUtil.createSimple(DISPLAY_MODEL);
            // Model geometry spans both the lower and upper block, so no vertical offset is
            // needed here beyond what the model itself encodes.
            this.main.setScale(new Vector3f(1));
            addElement(main);
        }
    }
}
