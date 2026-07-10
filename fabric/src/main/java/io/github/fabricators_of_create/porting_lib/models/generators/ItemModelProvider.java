package io.github.fabricators_of_create.porting_lib.models.generators;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public abstract class ItemModelProvider extends ModelProvider<ItemModelBuilder> {
    public ItemModelProvider(PackOutput output, String modid, ExistingFileHelper helper) {
        super(output, modid, ITEM_FOLDER, helper);
    }

    public ItemModelBuilder basicItem(Item item) {
        return null;
    }

    public ItemModelBuilder basicItem(ResourceLocation item) {
        return null;
    }

    @Override
    public String getName() {
        return "ItemModels";
    }
}
