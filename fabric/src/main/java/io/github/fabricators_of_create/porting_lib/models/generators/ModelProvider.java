package io.github.fabricators_of_create.porting_lib.models.generators;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class ModelProvider<T extends ModelBuilder<T>> implements DataProvider {
    public static final String BLOCK_FOLDER = "block";
    public static final String ITEM_FOLDER = "item";

    protected final String modid;
    protected final String folder;
    public final Map<ResourceLocation, T> generatedModels = new HashMap<>();

    protected ModelProvider(PackOutput output, String modid, String folder, ExistingFileHelper helper) {
        this.modid = modid;
        this.folder = folder;
    }

    public ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }

    @SuppressWarnings("unchecked")
    public T withExistingParent(String name, String parent) {
        return withExistingParent(name, ResourceLocation.parse(parent));
    }

    @SuppressWarnings("unchecked")
    public T withExistingParent(String name, ResourceLocation parent) {
        return getBuilder(name);
    }

    @SuppressWarnings("unchecked")
    public T getBuilder(String path) {
        ResourceLocation loc = path.contains(":") ? ResourceLocation.parse(path) : modLoc(path);
        return (T) new ModelBuilder<>(loc, null);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "ModelProvider";
    }
}
