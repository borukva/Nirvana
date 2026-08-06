package galena.nirvana.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModFlowerGeneration {
    public static void generateFlowers() {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.FLOWER_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.WILD_HEMP_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.PLAINS),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.WILD_HEMP_PLACED_KEY
        );
    }
}