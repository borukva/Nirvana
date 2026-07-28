package galena.nirvana.datagen;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;

import static galena.nirvana.Nirvana.MOD_ID;

public class ModModelProvider extends FabricModelProvider{
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerTintableCross(NirvanaBlocks.WILD_HEMP, BlockStateModelGenerator.CrossType.NOT_TINTED);

        blockStateModelGenerator.registerSouthDefaultHorizontalFacing(
                TexturedModel.TEMPLATE_GLAZED_TERRACOTTA,
                NirvanaBlocks.HEMP_BURLAP,
                NirvanaBlocks.WHITE_HEMP_BURLAP,
                NirvanaBlocks.LIGHT_GRAY_HEMP_BURLAP,
                NirvanaBlocks.GRAY_HEMP_BURLAP,
                NirvanaBlocks.BLACK_HEMP_BURLAP,
                NirvanaBlocks.BROWN_HEMP_BURLAP,
                NirvanaBlocks.RED_HEMP_BURLAP,
                NirvanaBlocks.ORANGE_HEMP_BURLAP,
                NirvanaBlocks.YELLOW_HEMP_BURLAP,
                NirvanaBlocks.LIME_HEMP_BURLAP,
                NirvanaBlocks.GREEN_HEMP_BURLAP,
                NirvanaBlocks.CYAN_HEMP_BURLAP,
                NirvanaBlocks.LIGHT_BLUE_HEMP_BURLAP,
                NirvanaBlocks.BLUE_HEMP_BURLAP,
                NirvanaBlocks.PURPLE_HEMP_BURLAP,
                NirvanaBlocks.MAGENTA_HEMP_BURLAP,
                NirvanaBlocks.PINK_HEMP_BURLAP);

        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.WOVEN_BURLAP).log(NirvanaBlocks.WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.WHITE_WOVEN_BURLAP).log(NirvanaBlocks.WHITE_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.LIGHT_GRAY_WOVEN_BURLAP).log(NirvanaBlocks.LIGHT_GRAY_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.GRAY_WOVEN_BURLAP).log(NirvanaBlocks.GRAY_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.BLACK_WOVEN_BURLAP).log(NirvanaBlocks.BLACK_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.BROWN_WOVEN_BURLAP).log(NirvanaBlocks.BROWN_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.RED_WOVEN_BURLAP).log(NirvanaBlocks.RED_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.ORANGE_WOVEN_BURLAP).log(NirvanaBlocks.ORANGE_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.YELLOW_WOVEN_BURLAP).log(NirvanaBlocks.YELLOW_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.LIME_WOVEN_BURLAP).log(NirvanaBlocks.LIME_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.GREEN_WOVEN_BURLAP).log(NirvanaBlocks.GREEN_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.CYAN_WOVEN_BURLAP).log(NirvanaBlocks.CYAN_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.LIGHT_BLUE_WOVEN_BURLAP).log(NirvanaBlocks.LIGHT_BLUE_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.BLUE_WOVEN_BURLAP).log(NirvanaBlocks.BLUE_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.PURPLE_WOVEN_BURLAP).log(NirvanaBlocks.PURPLE_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.MAGENTA_WOVEN_BURLAP).log(NirvanaBlocks.MAGENTA_WOVEN_BURLAP);
        blockStateModelGenerator.createLogTexturePool(NirvanaBlocks.PINK_WOVEN_BURLAP).log(NirvanaBlocks.PINK_WOVEN_BURLAP);

        generateCrate(blockStateModelGenerator, NirvanaBlocks.HEMP_CRATE, "hemp_crate");
        generateCrate(blockStateModelGenerator, NirvanaBlocks.WEED_CRATE, "weed_crate");
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(NirvanaItems.HEMP_SEEDS, Models.GENERATED);
        itemModelGenerator.register(NirvanaItems.HEMP, Models.GENERATED);
        itemModelGenerator.register(NirvanaItems.HEMP_CLOTH, Models.GENERATED);
        itemModelGenerator.register(NirvanaItems.WEED, Models.GENERATED);
        itemModelGenerator.register(NirvanaItems.WEED_BROWNIE, Models.GENERATED);
        itemModelGenerator.register(NirvanaItems.OLD_PIPE, Models.GENERATED);
        itemModelGenerator.register(NirvanaItems.MUSIC_DISC_JAM, Models.GENERATED);
    }
    private static void generateCrate(BlockStateModelGenerator generator, Block generatedBlock, String path) {
        generator.registerCubeWithCustomTextures(
                generatedBlock,
                generatedBlock,
                (block, otherTextureSource) -> new TextureMap()
                        .put(TextureKey.DOWN, Identifier.of(MOD_ID, "block/" + path + "_bottom"))
                        .put(TextureKey.UP, Identifier.of(MOD_ID, "block/" + path + "_top"))
                        .put(TextureKey.NORTH, Identifier.of(MOD_ID, "block/" + path + "_front"))
                        .put(TextureKey.EAST, Identifier.of(MOD_ID, "block/" + path + "_side"))
                        .put(TextureKey.SOUTH, Identifier.of(MOD_ID, "block/" + path + "_back"))
                        .put(TextureKey.WEST, Identifier.of(MOD_ID, "block/" + path + "_side"))
                        .put(TextureKey.PARTICLE, Identifier.of(MOD_ID, "block/" + path + "_top"))
        );
    }
}