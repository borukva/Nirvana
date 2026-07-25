package galena.nirvana.datagen;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;

public class NirvanaRecipeGenerator extends RecipeGenerator {
    private final RegistryEntryLookup<Item> itemLookup;

    public NirvanaRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
        itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
    }

    @Override
    public void generate() {
        compressBlockRecipe(NirvanaBlocks.HEMP_CRATE_ITEM, NirvanaItems.HEMP, exporter);
        compressBlockRecipe(NirvanaBlocks.WEED_CRATE_ITEM, NirvanaItems.WEED, exporter);

        burlapColored(NirvanaBlocks.WHITE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.WHITE_DYE, exporter);
        burlapColored(NirvanaBlocks.ORANGE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.ORANGE_DYE, exporter);
        burlapColored(NirvanaBlocks.MAGENTA_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.MAGENTA_DYE, exporter);
        burlapColored(NirvanaBlocks.LIGHT_BLUE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.LIGHT_BLUE_DYE, exporter);
        burlapColored(NirvanaBlocks.YELLOW_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.YELLOW_DYE, exporter);
        burlapColored(NirvanaBlocks.LIME_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.LIME_DYE, exporter);
        burlapColored(NirvanaBlocks.PINK_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.PINK_DYE, exporter);
        burlapColored(NirvanaBlocks.GRAY_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.GRAY_DYE, exporter);
        burlapColored(NirvanaBlocks.LIGHT_GRAY_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.LIGHT_GRAY_DYE, exporter);
        burlapColored(NirvanaBlocks.CYAN_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.CYAN_DYE, exporter);
        burlapColored(NirvanaBlocks.PURPLE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.PURPLE_DYE, exporter);
        burlapColored(NirvanaBlocks.BLUE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.BLUE_DYE, exporter);
        burlapColored(NirvanaBlocks.BROWN_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.BROWN_DYE, exporter);
        burlapColored(NirvanaBlocks.GREEN_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.GREEN_DYE, exporter);
        burlapColored(NirvanaBlocks.RED_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.RED_DYE, exporter);
        burlapColored(NirvanaBlocks.BLACK_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.BLACK_DYE, exporter);

        wovenColored(NirvanaBlocks.WHITE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.WHITE_DYE, exporter);
        wovenColored(NirvanaBlocks.ORANGE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.ORANGE_DYE, exporter);
        wovenColored(NirvanaBlocks.MAGENTA_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.MAGENTA_DYE, exporter);
        wovenColored(NirvanaBlocks.LIGHT_BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.LIGHT_BLUE_DYE, exporter);
        wovenColored(NirvanaBlocks.YELLOW_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.YELLOW_DYE, exporter);
        wovenColored(NirvanaBlocks.LIME_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.LIME_DYE, exporter);
        wovenColored(NirvanaBlocks.PINK_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.PINK_DYE, exporter);
        wovenColored(NirvanaBlocks.GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.GRAY_DYE, exporter);
        wovenColored(NirvanaBlocks.LIGHT_GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.LIGHT_GRAY_DYE, exporter);
        wovenColored(NirvanaBlocks.CYAN_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.CYAN_DYE, exporter);
        wovenColored(NirvanaBlocks.PURPLE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.PURPLE_DYE, exporter);
        wovenColored(NirvanaBlocks.BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.BLUE_DYE, exporter);
        wovenColored(NirvanaBlocks.BROWN_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.BROWN_DYE, exporter);
        wovenColored(NirvanaBlocks.GREEN_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.GREEN_DYE, exporter);
        wovenColored(NirvanaBlocks.RED_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.RED_DYE, exporter);
        wovenColored(NirvanaBlocks.BLACK_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.BLACK_DYE, exporter);

        wovenRecipe(NirvanaBlocks.WHITE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WHITE_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.ORANGE_WOVEN_BURLAP_ITEM, NirvanaBlocks.ORANGE_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.MAGENTA_WOVEN_BURLAP_ITEM, NirvanaBlocks.MAGENTA_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.LIGHT_BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.LIGHT_BLUE_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.YELLOW_WOVEN_BURLAP_ITEM, NirvanaBlocks.YELLOW_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.LIME_WOVEN_BURLAP_ITEM, NirvanaBlocks.LIME_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.PINK_WOVEN_BURLAP_ITEM, NirvanaBlocks.PINK_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.GRAY_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.LIGHT_GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.LIGHT_GRAY_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.CYAN_WOVEN_BURLAP_ITEM, NirvanaBlocks.CYAN_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.PURPLE_WOVEN_BURLAP_ITEM, NirvanaBlocks.PURPLE_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.BLUE_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.BROWN_WOVEN_BURLAP_ITEM, NirvanaBlocks.BROWN_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.GREEN_WOVEN_BURLAP_ITEM, NirvanaBlocks.GREEN_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.RED_WOVEN_BURLAP_ITEM, NirvanaBlocks.RED_HEMP_BURLAP_ITEM, exporter);
        wovenRecipe(NirvanaBlocks.BLACK_WOVEN_BURLAP_ITEM, NirvanaBlocks.BLACK_HEMP_BURLAP_ITEM, exporter);

        seedsRecipe(NirvanaItems.HEMP, NirvanaItems.HEMP_SEEDS, 2, exporter);

        campfireCookingRecipe(exporter, NirvanaItems.HEMP, NirvanaItems.WEED);
        offerSmelting(List.of(NirvanaItems.HEMP), RecipeCategory.FOOD, NirvanaItems.WEED, 0.35f, 200, "weed");

        createShapeless(RecipeCategory.FOOD, NirvanaItems.STUFFED_PIPE, 1)
                .input(NirvanaItems.OLD_PIPE)
                .input(NirvanaItems.WEED)
                .input(NirvanaItems.WEED)
                .input(NirvanaItems.WEED)
                .criterion(hasItem(NirvanaItems.OLD_PIPE), conditionsFromItem(NirvanaItems.OLD_PIPE))
                .offerTo(exporter);
        // suspicious_pipe is crafted dynamically instead (see SuspiciousPipeCraftingRecipe / the
        // suspicious_pipe_crafting.json trigger file) - its effects come from whichever flower
        // was used, same as vanilla Suspicious Stew. herbal_salve works the same way (see
        // SuspiciousSalveCraftingRecipe / suspicious_salve_crafting.json).

        // peace_salve is archived for now (see NirvanaItems.PEACE_SALVE).
        // createShapeless(RecipeCategory.MISC, NirvanaItems.PEACE_SALVE, 1)
        //         .input(Items.BOWL)
        //         .input(NirvanaItems.WEED)
        //         .input(NirvanaItems.WEED)
        //         .input(NirvanaItems.WEED)
        //         .criterion(hasItem(NirvanaItems.WEED), conditionsFromItem(NirvanaItems.WEED))
        //         .offerTo(exporter);

        createShaped(RecipeCategory.BUILDING_BLOCKS, NirvanaBlocks.HEMP_BURLAP_ITEM, 1)
                .pattern("GG")
                .pattern("GG")
                .input('G', NirvanaItems.HEMP_CLOTH)
                .criterion(hasItem(NirvanaItems.HEMP_CLOTH), conditionsFromItem(NirvanaItems.HEMP_CLOTH))
                .offerTo(exporter);

        createShaped(RecipeCategory.BUILDING_BLOCKS, NirvanaBlocks.WOVEN_BURLAP_ITEM, 4)
                .pattern("GG")
                .pattern("GG")
                .input('G', NirvanaBlocks.HEMP_BURLAP_ITEM)
                .criterion(hasItem(NirvanaBlocks.HEMP_BURLAP_ITEM), conditionsFromItem(NirvanaBlocks.HEMP_BURLAP_ITEM))
                .offerTo(exporter);

        createShaped(RecipeCategory.MISC, Items.LEATHER, 1)
                .pattern("GGG")
                .pattern("GGG")
                .input('G', NirvanaItems.HEMP_CLOTH)
                .criterion(hasItem(NirvanaItems.HEMP_CLOTH), conditionsFromItem(NirvanaItems.HEMP_CLOTH))
                .offerTo(exporter);

        createShaped(RecipeCategory.MISC, NirvanaItems.HEMP_CLOTH, 1)
                .pattern("GG")
                .pattern("GG")
                .input('G', NirvanaItems.HEMP)
                .criterion(hasItem(NirvanaItems.HEMP), conditionsFromItem(NirvanaItems.HEMP))
                .offerTo(exporter);

        createShaped(RecipeCategory.MISC, NirvanaItems.OLD_PIPE, 1)
                .pattern("  S")
                .pattern("GIG")
                .pattern("BG ")
                .input('G', Items.SPRUCE_PLANKS)
                .input('I', Items.RAW_GOLD)
                .input('S', Items.IRON_INGOT)
                .input('B', Items.RESIN_CLUMP)
                .criterion(hasItem(Items.RESIN_CLUMP), conditionsFromItem(Items.RESIN_CLUMP))
                .offerTo(exporter);

        createShapeless(RecipeCategory.BUILDING_BLOCKS, NirvanaItems.WEED_BROWNIE, 2)
                .input(NirvanaItems.HEMP_SEEDS)
                .input(Items.WHEAT)
                .input(Items.COCOA_BEANS)
                .criterion(hasItem(NirvanaItems.HEMP_SEEDS), conditionsFromItem(NirvanaItems.HEMP_SEEDS))
                .offerTo(exporter);

        createShapeless(RecipeCategory.FOOD, NirvanaItems.JOINT, 1)
                .input(NirvanaItems.WEED)
                .input(Items.PAPER)
                .criterion(hasItem(NirvanaItems.WEED), conditionsFromItem(NirvanaItems.WEED))
                .offerTo(exporter);
    }

    private void compressBlockRecipe(Item blockItem, Item item, RecipeExporter exporter){
        createShaped(RecipeCategory.DECORATIONS, blockItem, 1)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .input('S', item)
                .criterion(hasItem(item), conditionsFromItem(item))
                .offerTo(exporter);
        createShapeless(RecipeCategory.FOOD, item, 9)
                .input(blockItem, 1)
                .criterion(hasItem(blockItem), conditionsFromItem(blockItem))
                .offerTo(exporter, Identifier.of(Nirvana.MOD_ID, getRecipeName(blockItem) + "_to_" + getRecipeName(item)).toString());
    }

    private void campfireCookingRecipe(RecipeExporter exporter, Item input, Item output) {
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(input), RecipeCategory.FOOD, output, 0, 600, RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter);
    }

    private void burlapColored(Item item, Item ingredient, Item ingredient2, RecipeExporter exporter){
        createShapeless(RecipeCategory.BUILDING_BLOCKS, item, 1)
                .input(ingredient)
                .input(ingredient2)
                .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
                .offerTo(exporter, Identifier.of(Nirvana.MOD_ID, getRecipeName(ingredient) + "_to_" + getRecipeName(item)).toString());
    }

    private void wovenColored(Item item, Item ingredient, Item ingredient2, RecipeExporter exporter){
        createShapeless(RecipeCategory.BUILDING_BLOCKS, item, 1)
                .input(ingredient)
                .input(ingredient2)
                .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
                .offerTo(exporter, Identifier.of(Nirvana.MOD_ID, getRecipeName(ingredient) + "_to_" + getRecipeName(item)).toString());
    }

    private void wovenRecipe(Item item, Item item2, RecipeExporter exporter){
        createShaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .pattern("SS")
                .pattern("SS")
                .input('S', item2)
                .criterion(hasItem(item2), conditionsFromItem(item2))
                .offerTo(exporter);
    }
    private void seedsRecipe(Item item, Item seeds, int count, RecipeExporter exporter){
        createShapeless(RecipeCategory.FOOD, seeds, count)
                .input(item)
                .criterion(hasItem(item), conditionsFromItem(item))
                .offerTo(exporter, Identifier.of(Nirvana.MOD_ID, getRecipeName(item) + "_to_" + getRecipeName(seeds)).toString());
    }

}