package io.github.fabricators_of_create.porting_lib.models.generators;

import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;

public final class MultiPartBlockStateBuilder implements IGeneratedBlockState {
    public MultiPartBlockStateBuilder(Block block) {}

    @Override
    public JsonObject toJson() {
        return new JsonObject();
    }
}
