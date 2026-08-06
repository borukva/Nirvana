package galena.nirvana.datagen;

import galena.nirvana.index.NirvanaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;

import static galena.nirvana.index.NirvanaBlocks.*;

/**
 * {@code FabricBlockLootTableProvider} was renamed to {@link FabricBlockLootSubProvider} - still a
 * standalone, directly-registrable {@code DataProvider} (see {@link NirvanaDataGenerator}), and
 * still the class to use instead of vanilla's own {@code BlockLootSubProvider} directly: unlike
 * vanilla's version, its strict-validation completeness check (every block needs a loot table)
 * is scoped to this mod's own namespace instead of every block in the game.
 */
public class ModLootTableProvider extends FabricBlockLootSubProvider {
    public ModLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        // Kept as a hand-written data/nirvana/loot_table/blocks/potted_wild_hemp.json (drops both
        // the flower pot and the wild hemp, matching every vanilla potted-plant loot table)
        // instead of being regenerated here.
        excludeFromStrictValidation(POTTED_WILD_HEMP);

        add(HEMP, createCropDrops(HEMP, NirvanaItems.HEMP, NirvanaItems.HEMP_SEEDS,
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(HEMP)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.AGE_7, 7))));
        addWildHempDrop();
        add(BLISS_BLOOM, blissBloomDrop());
        // Both head variants drop the one head item, standing or wall-mounted alike.
        dropOther(REEFER_HEAD, REEFER_HEAD_ITEM);
        dropOther(REEFER_WALL_HEAD, REEFER_HEAD_ITEM);
        dropSelf(HEMP_CRATE);
        dropSelf(WEED_CRATE);
        dropSelf(THC);
        dropSelf(HEMP_BURLAP);
        dropSelf(WHITE_HEMP_BURLAP);
        dropSelf(LIGHT_GRAY_HEMP_BURLAP);
        dropSelf(GRAY_HEMP_BURLAP);
        dropSelf(BLACK_HEMP_BURLAP);
        dropSelf(BROWN_HEMP_BURLAP);
        dropSelf(RED_HEMP_BURLAP);
        dropSelf(ORANGE_HEMP_BURLAP);
        dropSelf(YELLOW_HEMP_BURLAP);
        dropSelf(LIME_HEMP_BURLAP);
        dropSelf(GREEN_HEMP_BURLAP);
        dropSelf(CYAN_HEMP_BURLAP);
        dropSelf(LIGHT_BLUE_HEMP_BURLAP);
        dropSelf(BLUE_HEMP_BURLAP);
        dropSelf(PURPLE_HEMP_BURLAP);
        dropSelf(MAGENTA_HEMP_BURLAP);
        dropSelf(PINK_HEMP_BURLAP);
        dropSelf(WOVEN_BURLAP);
        dropSelf(WHITE_WOVEN_BURLAP);
        dropSelf(LIGHT_GRAY_WOVEN_BURLAP);
        dropSelf(GRAY_WOVEN_BURLAP);
        dropSelf(BLACK_WOVEN_BURLAP);
        dropSelf(BROWN_WOVEN_BURLAP);
        dropSelf(RED_WOVEN_BURLAP);
        dropSelf(ORANGE_WOVEN_BURLAP);
        dropSelf(YELLOW_WOVEN_BURLAP);
        dropSelf(LIME_WOVEN_BURLAP);
        dropSelf(GREEN_WOVEN_BURLAP);
        dropSelf(CYAN_WOVEN_BURLAP);
        dropSelf(LIGHT_BLUE_WOVEN_BURLAP);
        dropSelf(BLUE_WOVEN_BURLAP);
        dropSelf(PURPLE_WOVEN_BURLAP);
        dropSelf(MAGENTA_WOVEN_BURLAP);
        dropSelf(PINK_WOVEN_BURLAP);
    }
    private void addWildHempDrop() {
        var itemLookup = this.registries.lookupOrThrow(Registries.ITEM);
        add(WILD_HEMP, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(NirvanaItems.WILD_HEMP)
                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(itemLookup, Items.SHEARS)))
                        .otherwise(LootItem.lootTableItem(NirvanaItems.HEMP)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))))));
    }

    private LootTable.Builder blissBloomDrop() {
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(BLISS_BLOOM)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)))
                .add(LootItem.lootTableItem(BLISS_BLOOM_ITEM)));
    }
}
