package galena.nirvana.datagen;

import galena.nirvana.Nirvana;
import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;

public class NirvanaRecipeGenerator extends RecipeProvider {
    public NirvanaRecipeGenerator(HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
    }

    @Override
    public void buildRecipes() {
        compressBlockRecipe(NirvanaBlocks.HEMP_CRATE_ITEM, NirvanaItems.HEMP);
        compressBlockRecipe(NirvanaBlocks.WEED_CRATE_ITEM, NirvanaItems.WEED);

        burlapColored(NirvanaBlocks.WHITE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.white());
        burlapColored(NirvanaBlocks.ORANGE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.orange());
        burlapColored(NirvanaBlocks.MAGENTA_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.magenta());
        burlapColored(NirvanaBlocks.LIGHT_BLUE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.lightBlue());
        burlapColored(NirvanaBlocks.YELLOW_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.yellow());
        burlapColored(NirvanaBlocks.LIME_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.lime());
        burlapColored(NirvanaBlocks.PINK_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.pink());
        burlapColored(NirvanaBlocks.GRAY_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.gray());
        burlapColored(NirvanaBlocks.LIGHT_GRAY_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.lightGray());
        burlapColored(NirvanaBlocks.CYAN_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.cyan());
        burlapColored(NirvanaBlocks.PURPLE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.purple());
        burlapColored(NirvanaBlocks.BLUE_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.blue());
        burlapColored(NirvanaBlocks.BROWN_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.brown());
        burlapColored(NirvanaBlocks.GREEN_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.green());
        burlapColored(NirvanaBlocks.RED_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.red());
        burlapColored(NirvanaBlocks.BLACK_HEMP_BURLAP_ITEM, NirvanaBlocks.HEMP_BURLAP_ITEM, Items.DYE.black());

        wovenColored(NirvanaBlocks.WHITE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.white());
        wovenColored(NirvanaBlocks.ORANGE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.orange());
        wovenColored(NirvanaBlocks.MAGENTA_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.magenta());
        wovenColored(NirvanaBlocks.LIGHT_BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.lightBlue());
        wovenColored(NirvanaBlocks.YELLOW_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.yellow());
        wovenColored(NirvanaBlocks.LIME_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.lime());
        wovenColored(NirvanaBlocks.PINK_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.pink());
        wovenColored(NirvanaBlocks.GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.gray());
        wovenColored(NirvanaBlocks.LIGHT_GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.lightGray());
        wovenColored(NirvanaBlocks.CYAN_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.cyan());
        wovenColored(NirvanaBlocks.PURPLE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.purple());
        wovenColored(NirvanaBlocks.BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.blue());
        wovenColored(NirvanaBlocks.BROWN_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.brown());
        wovenColored(NirvanaBlocks.GREEN_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.green());
        wovenColored(NirvanaBlocks.RED_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.red());
        wovenColored(NirvanaBlocks.BLACK_WOVEN_BURLAP_ITEM, NirvanaBlocks.WOVEN_BURLAP_ITEM, Items.DYE.black());

        wovenRecipe(NirvanaBlocks.WHITE_WOVEN_BURLAP_ITEM, NirvanaBlocks.WHITE_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.ORANGE_WOVEN_BURLAP_ITEM, NirvanaBlocks.ORANGE_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.MAGENTA_WOVEN_BURLAP_ITEM, NirvanaBlocks.MAGENTA_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.LIGHT_BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.LIGHT_BLUE_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.YELLOW_WOVEN_BURLAP_ITEM, NirvanaBlocks.YELLOW_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.LIME_WOVEN_BURLAP_ITEM, NirvanaBlocks.LIME_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.PINK_WOVEN_BURLAP_ITEM, NirvanaBlocks.PINK_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.GRAY_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.LIGHT_GRAY_WOVEN_BURLAP_ITEM, NirvanaBlocks.LIGHT_GRAY_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.CYAN_WOVEN_BURLAP_ITEM, NirvanaBlocks.CYAN_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.PURPLE_WOVEN_BURLAP_ITEM, NirvanaBlocks.PURPLE_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.BLUE_WOVEN_BURLAP_ITEM, NirvanaBlocks.BLUE_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.BROWN_WOVEN_BURLAP_ITEM, NirvanaBlocks.BROWN_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.GREEN_WOVEN_BURLAP_ITEM, NirvanaBlocks.GREEN_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.RED_WOVEN_BURLAP_ITEM, NirvanaBlocks.RED_HEMP_BURLAP_ITEM);
        wovenRecipe(NirvanaBlocks.BLACK_WOVEN_BURLAP_ITEM, NirvanaBlocks.BLACK_HEMP_BURLAP_ITEM);

        seedsRecipe(NirvanaItems.HEMP, NirvanaItems.HEMP_SEEDS, 2);

        campfireCookingRecipe(NirvanaItems.HEMP, NirvanaItems.WEED);
        smeltingRecipe(NirvanaItems.HEMP, NirvanaItems.WEED, 0.35f, 200, "weed");

        shapeless(RecipeCategory.FOOD, NirvanaItems.STUFFED_PIPE, 1)
                .requires(NirvanaItems.OLD_PIPE)
                .requires(NirvanaItems.WEED)
                .requires(NirvanaItems.WEED)
                .requires(NirvanaItems.WEED)
                .unlockedBy(getHasName(NirvanaItems.OLD_PIPE), has(NirvanaItems.OLD_PIPE))
                .save(output);
        // suspicious_pipe is crafted dynamically instead (see SuspiciousPipeCraftingRecipe / the
        // suspicious_pipe_crafting.json trigger file) - its effects come from whichever flower
        // was used, same as vanilla Suspicious Stew. herbal_salve works the same way (see
        // SuspiciousSalveCraftingRecipe / suspicious_salve_crafting.json).

        // peace_salve is archived for now (see NirvanaItems.PEACE_SALVE).
        // shapeless(RecipeCategory.MISC, NirvanaItems.PEACE_SALVE, 1)
        //         .requires(Items.BOWL)
        //         .requires(NirvanaItems.WEED)
        //         .requires(NirvanaItems.WEED)
        //         .requires(NirvanaItems.WEED)
        //         .unlockedBy(getHasName(NirvanaItems.WEED), has(NirvanaItems.WEED))
        //         .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, NirvanaBlocks.HEMP_BURLAP_ITEM, 1)
                .pattern("GG")
                .pattern("GG")
                .define('G', NirvanaItems.HEMP_CLOTH)
                .unlockedBy(getHasName(NirvanaItems.HEMP_CLOTH), has(NirvanaItems.HEMP_CLOTH))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, NirvanaBlocks.WOVEN_BURLAP_ITEM, 4)
                .pattern("GG")
                .pattern("GG")
                .define('G', NirvanaBlocks.HEMP_BURLAP_ITEM)
                .unlockedBy(getHasName(NirvanaBlocks.HEMP_BURLAP_ITEM), has(NirvanaBlocks.HEMP_BURLAP_ITEM))
                .save(output);

        shaped(RecipeCategory.MISC, Items.LEATHER, 1)
                .pattern("GGG")
                .pattern("GGG")
                .define('G', NirvanaItems.HEMP_CLOTH)
                .unlockedBy(getHasName(NirvanaItems.HEMP_CLOTH), has(NirvanaItems.HEMP_CLOTH))
                .save(output);

        shaped(RecipeCategory.MISC, NirvanaItems.HEMP_CLOTH, 1)
                .pattern("GG")
                .pattern("GG")
                .define('G', NirvanaItems.HEMP)
                .unlockedBy(getHasName(NirvanaItems.HEMP), has(NirvanaItems.HEMP))
                .save(output);

        shaped(RecipeCategory.MISC, NirvanaItems.OLD_PIPE, 1)
                .pattern("  S")
                .pattern("GIG")
                .pattern("BG ")
                .define('G', Items.SPRUCE_PLANKS)
                .define('I', Items.RAW_GOLD)
                .define('S', Items.IRON_INGOT)
                .define('B', Items.RESIN_CLUMP)
                .unlockedBy(getHasName(Items.RESIN_CLUMP), has(Items.RESIN_CLUMP))
                .save(output);

        shapeless(RecipeCategory.BUILDING_BLOCKS, NirvanaItems.WEED_BROWNIE, 2)
                .requires(NirvanaItems.HEMP_SEEDS)
                .requires(Items.WHEAT)
                .requires(Items.COCOA_BEANS)
                .unlockedBy(getHasName(NirvanaItems.HEMP_SEEDS), has(NirvanaItems.HEMP_SEEDS))
                .save(output);

        shapeless(RecipeCategory.FOOD, NirvanaItems.JOINT, 1)
                .requires(NirvanaItems.WEED)
                .requires(Items.PAPER)
                .unlockedBy(getHasName(NirvanaItems.WEED), has(NirvanaItems.WEED))
                .save(output);

        shapeless(RecipeCategory.MISC, NirvanaItems.PEACE_BANNER_PATTERN, 1)
                .requires(Items.PAPER)
                .requires(NirvanaItems.HEMP)
                .unlockedBy(getHasName(NirvanaItems.HEMP), has(NirvanaItems.HEMP))
                .save(output);

        // Alternate recipe for the vanilla lead, alongside its usual slimeball one.
        shaped(RecipeCategory.TOOLS, Items.LEAD, 1)
                .pattern("SH ")
                .pattern("HS ")
                .pattern("  S")
                .define('S', Items.STRING)
                .define('H', NirvanaItems.HEMP)
                .unlockedBy(getHasName(NirvanaItems.HEMP), has(NirvanaItems.HEMP))
                .save(output);
    }

    private void compressBlockRecipe(Item blockItem, Item item){
        shaped(RecipeCategory.DECORATIONS, blockItem, 1)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', item)
                .unlockedBy(getHasName(item), has(item))
                .save(output);
        shapeless(RecipeCategory.FOOD, item, 9)
                .requires(blockItem, 1)
                .unlockedBy(getHasName(blockItem), has(blockItem))
                .save(output, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, getRecipeName(blockItem) + "_to_" + getRecipeName(item)).toString());
    }

    private void campfireCookingRecipe(Item input, Item output) {
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD, output, 0, 600)
                .unlockedBy(getHasName(input), has(input))
                .save(this.output);
    }

    private void smeltingRecipe(Item input, Item result, float experience, int cookingTime, String suffix) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, CookingBookCategory.FOOD, result, experience, cookingTime)
                .unlockedBy(getHasName(input), has(input))
                .save(output, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, suffix + "_from_smelting").toString());
    }

    private void burlapColored(Item item, Item ingredient, Item ingredient2){
        shapeless(RecipeCategory.BUILDING_BLOCKS, item, 1)
                .requires(ingredient)
                .requires(ingredient2)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, getRecipeName(ingredient) + "_to_" + getRecipeName(item)).toString());
    }

    private void wovenColored(Item item, Item ingredient, Item ingredient2){
        shapeless(RecipeCategory.BUILDING_BLOCKS, item, 1)
                .requires(ingredient)
                .requires(ingredient2)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, getRecipeName(ingredient) + "_to_" + getRecipeName(item)).toString());
    }

    private void wovenRecipe(Item item, Item item2){
        shaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', item2)
                .unlockedBy(getHasName(item2), has(item2))
                .save(output);
    }
    private void seedsRecipe(Item item, Item seeds, int count){
        shapeless(RecipeCategory.FOOD, seeds, count)
                .requires(item)
                .unlockedBy(getHasName(item), has(item))
                .save(output, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, getRecipeName(item) + "_to_" + getRecipeName(seeds)).toString());
    }

    private static String getRecipeName(net.minecraft.world.level.ItemLike item) {
        return getItemName(item);
    }

}
