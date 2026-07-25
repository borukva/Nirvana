package galena.nirvana.world.gen;

import galena.nirvana.Nirvana;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> WILD_HEMP_PLACED_KEY = registerKey("wild_hemp_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        context.register(WILD_HEMP_PLACED_KEY, new PlacedFeature(
                context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getOrThrow(ModConfiguredFeatures.WILD_HEMP_PATCH_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(4),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                        BiomePlacementModifier.of()
                )
        ));
    }

    private static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Nirvana.MOD_ID, name));
    }
}