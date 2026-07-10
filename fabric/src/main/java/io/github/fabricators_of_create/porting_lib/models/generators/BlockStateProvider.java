package io.github.fabricators_of_create.porting_lib.models.generators;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

public abstract class BlockStateProvider implements DataProvider {
    public BlockStateProvider(PackOutput output, String modid, ExistingFileHelper helper) {}

    protected abstract void registerStatesAndModels();

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "BlockStates";
    }

    public VariantBlockStateBuilder getVariantBuilder(Block block) {
        return null;
    }

    public MultiPartBlockStateBuilder getMultipartBuilder(Block block) {
        return null;
    }

    public BlockModelProvider models() {
        return null;
    }

    public ItemModelProvider itemModels() {
        return null;
    }
}
