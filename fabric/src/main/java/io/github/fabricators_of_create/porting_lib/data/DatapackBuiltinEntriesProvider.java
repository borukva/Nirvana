package io.github.fabricators_of_create.porting_lib.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;

// Stub replacing porting-lib's DatapackBuiltinEntriesProvider which requires MC <=1.21.2.
// Only needed to satisfy registrate-fabric's ProviderType static initializer at startup;
// actual datagen is not used at gameplay runtime.
public class DatapackBuiltinEntriesProvider extends RegistriesDatapackGenerator {

    private final CompletableFuture<HolderLookup.Provider> fullRegistries;

    public DatapackBuiltinEntriesProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds) {
        super(output, registries);
        this.fullRegistries = registries;
    }

    public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
        return fullRegistries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf();
    }

}
