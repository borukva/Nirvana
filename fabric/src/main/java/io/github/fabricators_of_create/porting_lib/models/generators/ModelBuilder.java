package io.github.fabricators_of_create.porting_lib.models.generators;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.resources.ResourceLocation;

public class ModelBuilder<T extends ModelBuilder<T>> extends ModelFile {
    public static final String MODDED_DATA_KEY = "forge:hints";

    protected ModelFile parent;

    protected ModelBuilder(ResourceLocation location, ExistingFileHelper helper) {
        super(location);
    }

    @Override
    protected boolean exists() {
        return true;
    }
}
