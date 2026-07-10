package io.github.fabricators_of_create.porting_lib.models.generators;

import net.minecraft.resources.ResourceLocation;

public abstract class ModelFile {
    protected ResourceLocation location;

    protected ModelFile(ResourceLocation location) {
        this.location = location;
    }

    protected abstract boolean exists();

    public ResourceLocation getLocation() {
        return location;
    }
}
