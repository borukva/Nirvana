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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class WildHemp extends FlowerBlock implements PolymerTexturedBlock, FactoryBlock, Fertilizable {
    private final BlockState model;
    private String modelId;

    public WildHemp(RegistryEntry<StatusEffect> suspiciousStewEffect, int effectDuration, Settings settings, String modelId) {
        super(suspiciousStewEffect, effectDuration, settings);
        this.model = PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT_BLOCK);
        this.modelId = modelId;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new FlowerModel(initialBlockState, modelId);
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.getDefaultState();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        dropStack(world, pos, new ItemStack(this));
    }

    static class FlowerModel extends BlockModel {
        public ItemStack modelFlower;
        public ItemDisplayElement main;

        public FlowerModel(BlockState blockState, String modelId) {
            this.modelFlower = FlowerModels.FLOWER_MODELS.get(modelId);
            this.main = ItemDisplayElementUtil.createSimple(modelFlower);
            addElement(main);
        }
    }
}