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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import eu.pb4.factorytools.api.util.LazyItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

public class BlissBloom extends DoublePlantBlock implements PolymerTexturedBlock, FactoryBlock {
    private final BlockState model = PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT);
    // Deliberately lazy: resolving a real ItemStack this early (static init, at mod-init time)
    // runs before components are bound and crashes with "Components not bound yet".
    private static final LazyItemStack DISPLAY_MODEL = ItemDisplayElementUtil.getModel(Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, "block/bliss_bloom"));

    public BlissBloom(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.ROSE_BUSH.defaultBlockState();
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        if (initialBlockState.getValue(HALF) != DoubleBlockHalf.LOWER) return null;
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
