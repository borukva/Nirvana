package io.github.fabricators_of_create.porting_lib.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public abstract class SoundDefinitionsProvider implements DataProvider {
    protected SoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper) {}

    public void add(SoundEvent sound, SoundDefinition definition) {}

    public void add(ResourceLocation location, SoundDefinition definition) {}

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "SoundDefinitions";
    }
}
