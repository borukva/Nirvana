package io.github.fabricators_of_create.porting_lib.models.generators;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.data.PackOutput;

public abstract class BlockModelProvider extends ModelProvider<BlockModelBuilder> {
    public BlockModelProvider(PackOutput output, String modid, ExistingFileHelper helper) {
        super(output, modid, BLOCK_FOLDER, helper);
    }

    @Override
    public String getName() {
        return "BlockModels";
    }
}
