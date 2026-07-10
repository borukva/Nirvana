package io.github.fabricators_of_create.porting_lib.models.generators;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.resources.ResourceLocation;

public class ItemModelBuilder extends ModelBuilder<ItemModelBuilder> {
    public ItemModelBuilder(ResourceLocation location, ExistingFileHelper helper) {
        super(location, helper);
    }
}
