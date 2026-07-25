package galena.nirvana.world.gen;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaBlocks;
import net.minecraft.block.Block;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> WILD_HEMP_PATCH_KEY = registerKey("wild_hemp_patch");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        registerFlowerPatch(context, WILD_HEMP_PATCH_KEY, NirvanaBlocks.WILD_HEMP);
    }

    private static void registerFlowerPatch(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, Block block) {
        context.register(key, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchFeatureConfig(
                        32,
                        6,
                        4,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(BlockStateProvider.of(block))
                        )
                )
        ));
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Nirvana.MOD_ID, name));
    }
}