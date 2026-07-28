package galena.nirvana.datagen;

import galena.nirvana.index.NirvanaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;

import java.util.concurrent.CompletableFuture;

import static galena.nirvana.index.NirvanaBlocks.*;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    @Override
    public void generate() {
        addCropDrop(HEMP, NirvanaItems.HEMP, NirvanaItems.HEMP_SEEDS);
        addWildHempDrop();
        addDrop(BLISS_BLOOM, blissBloomDrop());
        // Both head variants drop the one head item, standing or wall-mounted alike.
        addDrop(REEFER_HEAD, REEFER_HEAD_ITEM);
        addDrop(REEFER_WALL_HEAD, REEFER_HEAD_ITEM);
        addDrop(HEMP_CRATE);
        addDrop(WEED_CRATE);
        addDrop(THC);
        addDrop(HEMP_BURLAP);
        addDrop(WHITE_HEMP_BURLAP);
        addDrop(LIGHT_GRAY_HEMP_BURLAP);
        addDrop(GRAY_HEMP_BURLAP);
        addDrop(BLACK_HEMP_BURLAP);
        addDrop(BROWN_HEMP_BURLAP);
        addDrop(RED_HEMP_BURLAP);
        addDrop(ORANGE_HEMP_BURLAP);
        addDrop(YELLOW_HEMP_BURLAP);
        addDrop(LIME_HEMP_BURLAP);
        addDrop(GREEN_HEMP_BURLAP);
        addDrop(CYAN_HEMP_BURLAP);
        addDrop(LIGHT_BLUE_HEMP_BURLAP);
        addDrop(BLUE_HEMP_BURLAP);
        addDrop(PURPLE_HEMP_BURLAP);
        addDrop(MAGENTA_HEMP_BURLAP);
        addDrop(PINK_HEMP_BURLAP);
        addDrop(WOVEN_BURLAP);
        addDrop(WHITE_WOVEN_BURLAP);
        addDrop(LIGHT_GRAY_WOVEN_BURLAP);
        addDrop(GRAY_WOVEN_BURLAP);
        addDrop(BLACK_WOVEN_BURLAP);
        addDrop(BROWN_WOVEN_BURLAP);
        addDrop(RED_WOVEN_BURLAP);
        addDrop(ORANGE_WOVEN_BURLAP);
        addDrop(YELLOW_WOVEN_BURLAP);
        addDrop(LIME_WOVEN_BURLAP);
        addDrop(GREEN_WOVEN_BURLAP);
        addDrop(CYAN_WOVEN_BURLAP);
        addDrop(LIGHT_BLUE_WOVEN_BURLAP);
        addDrop(BLUE_WOVEN_BURLAP);
        addDrop(PURPLE_WOVEN_BURLAP);
        addDrop(MAGENTA_WOVEN_BURLAP);
        addDrop(PINK_WOVEN_BURLAP);
    }
    private void addWildHempDrop() {
        var itemLookup = this.registries.getOrThrow(RegistryKeys.ITEM);
        addDrop(WILD_HEMP, LootTable.builder().pool(LootPool.builder()
                .with(ItemEntry.builder(NirvanaItems.WILD_HEMP)
                        .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(itemLookup, Items.SHEARS)))
                        .alternatively(ItemEntry.builder(NirvanaItems.HEMP)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0, 2)))))));
    }

    private LootTable.Builder blissBloomDrop() {
        return LootTable.builder().pool(LootPool.builder()
                .conditionally(BlockStatePropertyLootCondition.builder(BLISS_BLOOM)
                        .properties(StatePredicate.Builder.create().exactMatch(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)))
                .with(ItemEntry.builder(BLISS_BLOOM_ITEM)));
    }

    private void addCropDrop(Block cropBlock, Item cropItem, Item seedItem) {
        BlockStatePropertyLootCondition.Builder builder = BlockStatePropertyLootCondition.builder(cropBlock)
                .properties(StatePredicate.Builder.create().exactMatch(Properties.AGE_7, 7));
        addDrop(cropBlock, customCropDrop(cropBlock, cropItem, seedItem, builder));
    }
    public LootTable.Builder customCropDrop(Block crop, Item product, Item seeds, LootCondition.Builder condition) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.applyExplosionDecay(crop, LootTable.builder().pool(LootPool.builder().with(ItemEntry.builder(product).conditionally(condition).alternatively(ItemEntry.builder(seeds)))).pool(LootPool.builder().conditionally(condition).with(ItemEntry.builder(product).apply(ApplyBonusLootFunction.binomialWithBonusCount(impl.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 2)))));
    }
}
