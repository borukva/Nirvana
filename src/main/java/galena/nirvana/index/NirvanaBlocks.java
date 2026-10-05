package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.index.crop.BlissBloom;
import galena.nirvana.index.crop.HempCrop;
import galena.nirvana.index.crop.PottedWildHemp;
import galena.nirvana.index.crop.WildHemp;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.SoundType;

import java.util.function.BiFunction;
import java.util.function.Function;

import static galena.nirvana.Nirvana.id;

public class NirvanaBlocks {
    public static final Block HEMP = registerBlock("hemp_crop", HempCrop::new, Block.Properties.ofFullCopy(Blocks.WHEAT));
    public static final Block WILD_HEMP = registerBlock("wild_hemp", settings -> new WildHemp(MobEffects.REGENERATION, 8, settings, "wild_hemp"), Block.Properties.ofFullCopy(Blocks.POPPY));

    public static final Block THC = registerBlock("thc", ThcBlock::new, Block.Properties.ofFullCopy(Blocks.TNT).sound(SoundType.GRASS));
    public static final BlockItem THC_ITEM = registerBlockItem("thc", settings -> new ThcItem(THC, settings), new Item.Properties());

    public static final Block BLISS_BLOOM = registerBlock("bliss_bloom", BlissBloom::new, Block.Properties.ofFullCopy(Blocks.ROSE_BUSH));
    public static final BlockItem BLISS_BLOOM_ITEM = registerBlockItem("bliss_bloom", settings -> new BlissBloomItem(BLISS_BLOOM, settings), new Item.Properties());

    // Both explicitly get their own loot table: Properties.copy() carries the creeper head's along
    // with everything else, so without this they'd drop a vanilla creeper head.
    public static final Block REEFER_HEAD = registerBlock("reefer_head", ReeferHeadBlock::new,
            Block.Properties.ofFullCopy(Blocks.CREEPER_HEAD).overrideLootTable(lootTableOf("reefer_head")));
    public static final Block REEFER_WALL_HEAD = registerBlock("reefer_wall_head", ReeferWallHeadBlock::new,
            Block.Properties.ofFullCopy(Blocks.CREEPER_WALL_HEAD).overrideLootTable(lootTableOf("reefer_wall_head")));
    public static final BlockItem REEFER_HEAD_ITEM = registerBlockItem("reefer_head",
            settings -> new ReeferHeadItem(REEFER_HEAD, REEFER_WALL_HEAD, settings),
            // Wearable on the head, same as every vanilla mob head.
            new Item.Properties().component(DataComponents.EQUIPPABLE,
                    Equippable.builder(EquipmentSlot.HEAD).setSwappable(false).build()));

    // Own loot table for the same reason as reefer_head above - copy() alone didn't leave it
    // dropping anything when broken directly. Drops both the pot and the plant, matching
    // vanilla's own potted flowers (confirmed - breaking one directly returns both, not just
    // the pot).
    public static final Block POTTED_WILD_HEMP = registerBlock("potted_wild_hemp", settings -> new PottedWildHemp(WILD_HEMP, settings),
            Block.Properties.ofFullCopy(Blocks.POTTED_FERN).overrideLootTable(lootTableOf("potted_wild_hemp")));

    public static final Block HEMP_CRATE = registerCrate("hemp_crate");
    public static final BlockItem HEMP_CRATE_ITEM = registerTexturedBlockItem("hemp_crate", HEMP_CRATE);

    public static final Block WEED_CRATE = registerCrate("weed_crate");
    public static final BlockItem WEED_CRATE_ITEM = registerTexturedBlockItem("weed_crate", WEED_CRATE);

    public static final Block HEMP_BURLAP = registerBurlap("hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.white());
    public static final BlockItem HEMP_BURLAP_ITEM = registerTexturedBlockItem("hemp_burlap", HEMP_BURLAP);

    public static final Block WHITE_HEMP_BURLAP = registerBurlap("white_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.white());
    public static final BlockItem WHITE_HEMP_BURLAP_ITEM = registerTexturedBlockItem("white_hemp_burlap", WHITE_HEMP_BURLAP);

    public static final Block LIGHT_GRAY_HEMP_BURLAP = registerBurlap("light_gray_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.lightGray());
    public static final BlockItem LIGHT_GRAY_HEMP_BURLAP_ITEM = registerTexturedBlockItem("light_gray_hemp_burlap", LIGHT_GRAY_HEMP_BURLAP);

    public static final Block GRAY_HEMP_BURLAP = registerBurlap("gray_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.gray());
    public static final BlockItem GRAY_HEMP_BURLAP_ITEM = registerTexturedBlockItem("gray_hemp_burlap", GRAY_HEMP_BURLAP);

    public static final Block BLACK_HEMP_BURLAP = registerBurlap("black_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.black());
    public static final BlockItem BLACK_HEMP_BURLAP_ITEM = registerTexturedBlockItem("black_hemp_burlap", BLACK_HEMP_BURLAP);

    public static final Block BROWN_HEMP_BURLAP = registerBurlap("brown_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.brown());
    public static final BlockItem BROWN_HEMP_BURLAP_ITEM = registerTexturedBlockItem("brown_hemp_burlap", BROWN_HEMP_BURLAP);

    public static final Block RED_HEMP_BURLAP = registerBurlap("red_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.red());
    public static final BlockItem RED_HEMP_BURLAP_ITEM = registerTexturedBlockItem("red_hemp_burlap", RED_HEMP_BURLAP);

    public static final Block ORANGE_HEMP_BURLAP = registerBurlap("orange_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.orange());
    public static final BlockItem ORANGE_HEMP_BURLAP_ITEM = registerTexturedBlockItem("orange_hemp_burlap", ORANGE_HEMP_BURLAP);

    public static final Block YELLOW_HEMP_BURLAP = registerBurlap("yellow_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.yellow());
    public static final BlockItem YELLOW_HEMP_BURLAP_ITEM = registerTexturedBlockItem("yellow_hemp_burlap", YELLOW_HEMP_BURLAP);

    public static final Block LIME_HEMP_BURLAP = registerBurlap("lime_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.lime());
    public static final BlockItem LIME_HEMP_BURLAP_ITEM = registerTexturedBlockItem("lime_hemp_burlap", LIME_HEMP_BURLAP);

    public static final Block GREEN_HEMP_BURLAP = registerBurlap("green_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.green());
    public static final BlockItem GREEN_HEMP_BURLAP_ITEM = registerTexturedBlockItem("green_hemp_burlap", GREEN_HEMP_BURLAP);

    public static final Block CYAN_HEMP_BURLAP = registerBurlap("cyan_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.cyan());
    public static final BlockItem CYAN_HEMP_BURLAP_ITEM = registerTexturedBlockItem("cyan_hemp_burlap", CYAN_HEMP_BURLAP);

    public static final Block LIGHT_BLUE_HEMP_BURLAP = registerBurlap("light_blue_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.lightBlue());
    public static final BlockItem LIGHT_BLUE_HEMP_BURLAP_ITEM = registerTexturedBlockItem("light_blue_hemp_burlap", LIGHT_BLUE_HEMP_BURLAP);

    public static final Block BLUE_HEMP_BURLAP = registerBurlap("blue_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.blue());
    public static final BlockItem BLUE_HEMP_BURLAP_ITEM = registerTexturedBlockItem("blue_hemp_burlap", BLUE_HEMP_BURLAP);

    public static final Block PURPLE_HEMP_BURLAP = registerBurlap("purple_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.purple());
    public static final BlockItem PURPLE_HEMP_BURLAP_ITEM = registerTexturedBlockItem("purple_hemp_burlap", PURPLE_HEMP_BURLAP);

    public static final Block MAGENTA_HEMP_BURLAP = registerBurlap("magenta_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.magenta());
    public static final BlockItem MAGENTA_HEMP_BURLAP_ITEM = registerTexturedBlockItem("magenta_hemp_burlap", MAGENTA_HEMP_BURLAP);

    public static final Block PINK_HEMP_BURLAP = registerBurlap("pink_hemp_burlap", NirvanaBurlapBlock::new, Blocks.WOOL.pink());
    public static final BlockItem PINK_HEMP_BURLAP_ITEM = registerTexturedBlockItem("pink_hemp_burlap", PINK_HEMP_BURLAP);

    public static final Block WOVEN_BURLAP = registerBurlap("woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.white());
    public static final BlockItem WOVEN_BURLAP_ITEM = registerTexturedBlockItem("woven_burlap", WOVEN_BURLAP);

    public static final Block WHITE_WOVEN_BURLAP = registerBurlap("white_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.white());
    public static final BlockItem WHITE_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("white_woven_burlap", WHITE_WOVEN_BURLAP);

    public static final Block LIGHT_GRAY_WOVEN_BURLAP = registerBurlap("light_gray_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.lightGray());
    public static final BlockItem LIGHT_GRAY_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("light_gray_woven_burlap", LIGHT_GRAY_WOVEN_BURLAP);

    public static final Block GRAY_WOVEN_BURLAP = registerBurlap("gray_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.gray());
    public static final BlockItem GRAY_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("gray_woven_burlap", GRAY_WOVEN_BURLAP);

    public static final Block BLACK_WOVEN_BURLAP = registerBurlap("black_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.black());
    public static final BlockItem BLACK_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("black_woven_burlap", BLACK_WOVEN_BURLAP);

    public static final Block BROWN_WOVEN_BURLAP = registerBurlap("brown_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.brown());
    public static final BlockItem BROWN_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("brown_woven_burlap", BROWN_WOVEN_BURLAP);

    public static final Block RED_WOVEN_BURLAP = registerBurlap("red_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.red());
    public static final BlockItem RED_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("red_woven_burlap", RED_WOVEN_BURLAP);

    public static final Block ORANGE_WOVEN_BURLAP = registerBurlap("orange_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.orange());
    public static final BlockItem ORANGE_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("orange_woven_burlap", ORANGE_WOVEN_BURLAP);

    public static final Block YELLOW_WOVEN_BURLAP = registerBurlap("yellow_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.yellow());
    public static final BlockItem YELLOW_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("yellow_woven_burlap", YELLOW_WOVEN_BURLAP);

    public static final Block LIME_WOVEN_BURLAP = registerBurlap("lime_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.lime());
    public static final BlockItem LIME_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("lime_woven_burlap", LIME_WOVEN_BURLAP);

    public static final Block GREEN_WOVEN_BURLAP = registerBurlap("green_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.green());
    public static final BlockItem GREEN_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("green_woven_burlap", GREEN_WOVEN_BURLAP);

    public static final Block CYAN_WOVEN_BURLAP = registerBurlap("cyan_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.cyan());
    public static final BlockItem CYAN_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("cyan_woven_burlap", CYAN_WOVEN_BURLAP);

    public static final Block LIGHT_BLUE_WOVEN_BURLAP = registerBurlap("light_blue_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.lightBlue());
    public static final BlockItem LIGHT_BLUE_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("light_blue_woven_burlap", LIGHT_BLUE_WOVEN_BURLAP);

    public static final Block BLUE_WOVEN_BURLAP = registerBurlap("blue_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.blue());
    public static final BlockItem BLUE_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("blue_woven_burlap", BLUE_WOVEN_BURLAP);

    public static final Block PURPLE_WOVEN_BURLAP = registerBurlap("purple_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.purple());
    public static final BlockItem PURPLE_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("purple_woven_burlap", PURPLE_WOVEN_BURLAP);

    public static final Block MAGENTA_WOVEN_BURLAP = registerBurlap("magenta_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.magenta());
    public static final BlockItem MAGENTA_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("magenta_woven_burlap", MAGENTA_WOVEN_BURLAP);

    public static final Block PINK_WOVEN_BURLAP = registerBurlap("pink_woven_burlap", NirvanaWovenBurlapBlock::new, Blocks.WOOL.pink());
    public static final BlockItem PINK_WOVEN_BURLAP_ITEM = registerTexturedBlockItem("pink_woven_burlap", PINK_WOVEN_BURLAP);

    public static void registerBlocks() {
        registerComposting();
        Nirvana.LOGGER.info("Blocks register");
    }

    private static void registerComposting() {
        ComposterBlock.COMPOSTABLES.put(BLISS_BLOOM_ITEM, 0.5F);
    }

    private static java.util.Optional<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTableOf(String name) {
        return java.util.Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id("blocks/" + name)));
    }

    private static Block registerCrate(String name) {
        return registerBlock(name, NirvanaBasketBlock::new,
                Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.SAND));
    }

    private static Block registerBurlap(String name, BiFunction<BlockBehaviour.Properties, String, Block> factory, Block wool) {
        return registerBlock(name, factory, Block.Properties.ofFullCopy(wool).strength(0.5F).sound(SoundType.WOOL));
    }

    private static BlockItem registerTexturedBlockItem(String name, Block block) {
        return registerBlockItem(name, settings -> new NirvanaTexturedBlockItem(block, settings), new Item.Properties());
    }

    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        return registerBlock(name, (properties, path) -> factory.apply(properties), settings);
    }

    public static Block registerBlock(String name, BiFunction<BlockBehaviour.Properties, String, Block> factory, BlockBehaviour.Properties settings) {
        var key = ResourceKey.create(Registries.BLOCK, id(name));
        Block block = factory.apply(settings.setId(key), name);

        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static BlockItem registerBlockItem(String name, Function<Item.Properties, BlockItem> factory, Item.Properties settings) {
        var key = ResourceKey.create(Registries.ITEM, id(name));
        BlockItem item = factory.apply(settings.setId(key).useBlockDescriptionPrefix());

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
