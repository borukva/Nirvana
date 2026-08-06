package galena.nirvana.world.gen;

import galena.nirvana.Nirvana;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> WILD_HEMP_PLACED_KEY = registerKey("wild_hemp_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        context.register(WILD_HEMP_PLACED_KEY, new PlacedFeature(
                context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(ModConfiguredFeatures.WILD_HEMP_PATCH_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(4),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                )
        ));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, name));
    }
}