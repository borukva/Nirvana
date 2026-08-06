package galena.nirvana;

import galena.nirvana.datagen.ModItemTagProvider;
import galena.nirvana.datagen.ModLootTableProvider;
import galena.nirvana.datagen.ModModelProvider;
import galena.nirvana.datagen.ModRecipeProvider;
import galena.nirvana.datagen.ModWorldGenerator;
import galena.nirvana.world.gen.ModConfiguredFeatures;
import galena.nirvana.world.gen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class NirvanaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModWorldGenerator::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModItemTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder builder) {
        builder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        builder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}
