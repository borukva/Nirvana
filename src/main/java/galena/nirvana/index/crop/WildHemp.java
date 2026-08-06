package galena.nirvana.index.crop;

import galena.nirvana.utils.FlowerModels;
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
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;

public class WildHemp extends FlowerBlock implements PolymerTexturedBlock, FactoryBlock, BonemealableBlock {
    private final BlockState model;
    private String modelId;

    public WildHemp(Holder<MobEffect> suspiciousStewEffect, int effectDuration, Properties settings, String modelId) {
        super(suspiciousStewEffect, effectDuration, settings);
        this.model = PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT);
        this.modelId = modelId;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new FlowerModel(initialBlockState, modelId);
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.defaultBlockState();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        popResource(world, pos, new ItemStack(this));
    }

    static class FlowerModel extends BlockModel {
        public eu.pb4.factorytools.api.util.LazyItemStack modelFlower;
        public ItemDisplayElement main;

        public FlowerModel(BlockState blockState, String modelId) {
            this.modelFlower = FlowerModels.FLOWER_MODELS.get(modelId);
            this.main = ItemDisplayElementUtil.createSimple(modelFlower);
            addElement(main);
        }
    }
}