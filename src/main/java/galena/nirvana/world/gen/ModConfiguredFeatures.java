package galena.nirvana.world.gen;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * Vanilla dropped its old {@code FLOWER}/{@code RANDOM_PATCH} feature entirely in favour of the
 * much heavier {@code VEGETATION_PATCH} (cave-surface-aware, 10-parameter config) - overkill for
 * a simple flat flower scatter. This registers {@code SIMPLE_BLOCK} directly as the top-level
 * feature instead of wrapping it in a patch/cluster feature; {@link ModPlacedFeatures}'s own
 * {@code SquarePlacementModifier} still spreads attempts across each chunk, just one wild hemp
 * per attempt rather than vanilla's old 6-32-plant clusters.
 */
public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_HEMP_PATCH_KEY = registerKey("wild_hemp_patch");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        registerFlowerPatch(context, WILD_HEMP_PATCH_KEY, NirvanaBlocks.WILD_HEMP);
    }

    private static void registerFlowerPatch(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Block block) {
        context.register(key, new ConfiguredFeature<>(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(block))
        ));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, name));
    }
}
