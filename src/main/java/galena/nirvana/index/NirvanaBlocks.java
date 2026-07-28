package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.index.crop.BlissBloom;
import galena.nirvana.index.crop.HempCrop;
import galena.nirvana.index.crop.PottedWildHemp;
//import galena.nirvana.index.crop.WildHemp;
import galena.nirvana.index.crop.WildHemp;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

import java.util.function.BiFunction;
import java.util.function.Function;

import static galena.nirvana.Nirvana.id;

public class NirvanaBlocks {
    public static final Block HEMP = registerBlock("hemp_crop", HempCrop::new, Block.Settings.copy(Blocks.WHEAT));
    public static final Block WILD_HEMP = registerBlock("wild_hemp", settings -> new WildHemp(StatusEffects.REGENERATION, 8, settings, "wild_hemp"), Block.Settings.copy(Blocks.POPPY));

    public static final Block THC = registerBlock("thc", ThcBlock::new, Block.Settings.copy(Blocks.TNT));
    public static final BlockItem THC_ITEM = registerBlockItem("thc", settings -> new ThcItem(THC, settings), new Item.Settings());

    public static final Block BLISS_BLOOM = registerBlock("bliss_bloom", BlissBloom::new, Block.Settings.copy(Blocks.ROSE_BUSH));
    public static final BlockItem BLISS_BLOOM_ITEM = registerBlockItem("bliss_bloom", settings -> new BlissBloomItem(BLISS_BLOOM, settings), new Item.Settings());

    // Both explicitly get their own loot table: Settings.copy() carries the creeper head's along
    // with everything else, so without this they'd drop a vanilla creeper head.
    public static final Block REEFER_HEAD = registerBlock("reefer_head", ReeferHeadBlock::new,
            Block.Settings.copy(Blocks.CREEPER_HEAD).lootTable(lootTableOf("reefer_head")));
    public static final Block REEFER_WALL_HEAD = registerBlock("reefer_wall_head", ReeferWallHeadBlock::new,
            Block.Settings.copy(Blocks.CREEPER_WALL_HEAD).lootTable(lootTableOf("reefer_wall_head")));
    public static final BlockItem REEFER_HEAD_ITEM = registerBlockItem("reefer_head",
            settings -> new ReeferHeadItem(REEFER_HEAD, REEFER_WALL_HEAD, settings),
            // Wearable on the head, same as every vanilla mob head.
            new Item.Settings().component(DataComponentTypes.EQUIPPABLE,
                    EquippableComponent.builder(EquipmentSlot.HEAD).swappable(false).build()));

    public static final Block POTTED_WILD_HEMP = registerBlock("potted_wild_hemp", settings -> new PottedWildHemp(WILD_HEMP, settings), Block.Settings.copy(Blocks.POTTED_FERN));

    public static final Block HEMP_CRATE = registerBlock("hemp_crate", NirvanaBasketBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem HEMP_CRATE_ITEM = registerBlockItem("hemp_crate", settings -> new NirvanaTexturedBlockItem(HEMP_CRATE, settings), new Item.Settings());

    public static final Block WEED_CRATE = registerBlock("weed_crate", NirvanaBasketBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem WEED_CRATE_ITEM = registerBlockItem("weed_crate", settings -> new NirvanaTexturedBlockItem(WEED_CRATE, settings), new Item.Settings());

    public static final Block HEMP_BURLAP = registerBlock("hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem HEMP_BURLAP_ITEM = registerBlockItem("hemp_burlap", settings -> new NirvanaTexturedBlockItem(HEMP_BURLAP, settings), new Item.Settings());

    public static final Block WHITE_HEMP_BURLAP = registerBlock("white_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem WHITE_HEMP_BURLAP_ITEM = registerBlockItem("white_hemp_burlap", settings -> new NirvanaTexturedBlockItem(WHITE_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block LIGHT_GRAY_HEMP_BURLAP = registerBlock("light_gray_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.LIGHT_GRAY_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem LIGHT_GRAY_HEMP_BURLAP_ITEM = registerBlockItem("light_gray_hemp_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_GRAY_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block GRAY_HEMP_BURLAP = registerBlock("gray_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.GRAY_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem GRAY_HEMP_BURLAP_ITEM = registerBlockItem("gray_hemp_burlap", settings -> new NirvanaTexturedBlockItem(GRAY_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block BLACK_HEMP_BURLAP = registerBlock("black_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.BLACK_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem BLACK_HEMP_BURLAP_ITEM = registerBlockItem("black_hemp_burlap", settings -> new NirvanaTexturedBlockItem(BLACK_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block BROWN_HEMP_BURLAP = registerBlock("brown_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.BROWN_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem BROWN_HEMP_BURLAP_ITEM = registerBlockItem("brown_hemp_burlap", settings -> new NirvanaTexturedBlockItem(BROWN_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block RED_HEMP_BURLAP = registerBlock("red_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.RED_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem RED_HEMP_BURLAP_ITEM = registerBlockItem("red_hemp_burlap", settings -> new NirvanaTexturedBlockItem(RED_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block ORANGE_HEMP_BURLAP = registerBlock("orange_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.ORANGE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem ORANGE_HEMP_BURLAP_ITEM = registerBlockItem("orange_hemp_burlap", settings -> new NirvanaTexturedBlockItem(ORANGE_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block YELLOW_HEMP_BURLAP = registerBlock("yellow_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.YELLOW_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem YELLOW_HEMP_BURLAP_ITEM = registerBlockItem("yellow_hemp_burlap", settings -> new NirvanaTexturedBlockItem(YELLOW_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block LIME_HEMP_BURLAP = registerBlock("lime_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.LIME_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem LIME_HEMP_BURLAP_ITEM = registerBlockItem("lime_hemp_burlap", settings -> new NirvanaTexturedBlockItem(LIME_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block GREEN_HEMP_BURLAP = registerBlock("green_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.GREEN_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem GREEN_HEMP_BURLAP_ITEM = registerBlockItem("green_hemp_burlap", settings -> new NirvanaTexturedBlockItem(GREEN_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block CYAN_HEMP_BURLAP = registerBlock("cyan_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.CYAN_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem CYAN_HEMP_BURLAP_ITEM = registerBlockItem("cyan_hemp_burlap", settings -> new NirvanaTexturedBlockItem(CYAN_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block LIGHT_BLUE_HEMP_BURLAP = registerBlock("light_blue_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.LIGHT_BLUE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem LIGHT_BLUE_HEMP_BURLAP_ITEM = registerBlockItem("light_blue_hemp_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_BLUE_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block BLUE_HEMP_BURLAP = registerBlock("blue_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.BLUE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem BLUE_HEMP_BURLAP_ITEM = registerBlockItem("blue_hemp_burlap", settings -> new NirvanaTexturedBlockItem(BLUE_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block PURPLE_HEMP_BURLAP = registerBlock("purple_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.PURPLE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem PURPLE_HEMP_BURLAP_ITEM = registerBlockItem("purple_hemp_burlap", settings -> new NirvanaTexturedBlockItem(PURPLE_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block MAGENTA_HEMP_BURLAP = registerBlock("magenta_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.MAGENTA_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem MAGENTA_HEMP_BURLAP_ITEM = registerBlockItem("magenta_hemp_burlap", settings -> new NirvanaTexturedBlockItem(MAGENTA_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block PINK_HEMP_BURLAP = registerBlock("pink_hemp_burlap", NirvanaBurlapBlock::new, Block.Settings.copy(Blocks.PINK_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem PINK_HEMP_BURLAP_ITEM = registerBlockItem("pink_hemp_burlap", settings -> new NirvanaTexturedBlockItem(PINK_HEMP_BURLAP, settings), new Item.Settings());

    public static final Block WOVEN_BURLAP = registerBlock("woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem WOVEN_BURLAP_ITEM = registerBlockItem("woven_burlap", settings -> new NirvanaTexturedBlockItem(WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block WHITE_WOVEN_BURLAP = registerBlock("white_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.WHITE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem WHITE_WOVEN_BURLAP_ITEM = registerBlockItem("white_woven_burlap", settings -> new NirvanaTexturedBlockItem(WHITE_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block LIGHT_GRAY_WOVEN_BURLAP = registerBlock("light_gray_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.LIGHT_GRAY_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem LIGHT_GRAY_WOVEN_BURLAP_ITEM = registerBlockItem("light_gray_woven_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_GRAY_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block GRAY_WOVEN_BURLAP = registerBlock("gray_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.GRAY_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem GRAY_WOVEN_BURLAP_ITEM = registerBlockItem("gray_woven_burlap", settings -> new NirvanaTexturedBlockItem(GRAY_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block BLACK_WOVEN_BURLAP = registerBlock("black_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.BLACK_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem BLACK_WOVEN_BURLAP_ITEM = registerBlockItem("black_woven_burlap", settings -> new NirvanaTexturedBlockItem(BLACK_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block BROWN_WOVEN_BURLAP = registerBlock("brown_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.BROWN_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem BROWN_WOVEN_BURLAP_ITEM = registerBlockItem("brown_woven_burlap", settings -> new NirvanaTexturedBlockItem(BROWN_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block RED_WOVEN_BURLAP = registerBlock("red_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.RED_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem RED_WOVEN_BURLAP_ITEM = registerBlockItem("red_woven_burlap", settings -> new NirvanaTexturedBlockItem(RED_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block ORANGE_WOVEN_BURLAP = registerBlock("orange_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.ORANGE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem ORANGE_WOVEN_BURLAP_ITEM = registerBlockItem("orange_woven_burlap", settings -> new NirvanaTexturedBlockItem(ORANGE_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block YELLOW_WOVEN_BURLAP = registerBlock("yellow_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.YELLOW_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem YELLOW_WOVEN_BURLAP_ITEM = registerBlockItem("yellow_woven_burlap", settings -> new NirvanaTexturedBlockItem(YELLOW_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block LIME_WOVEN_BURLAP = registerBlock("lime_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.LIME_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem LIME_WOVEN_BURLAP_ITEM = registerBlockItem("lime_woven_burlap", settings -> new NirvanaTexturedBlockItem(LIME_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block GREEN_WOVEN_BURLAP = registerBlock("green_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.GREEN_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem GREEN_WOVEN_BURLAP_ITEM = registerBlockItem("green_woven_burlap", settings -> new NirvanaTexturedBlockItem(GREEN_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block CYAN_WOVEN_BURLAP = registerBlock("cyan_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.CYAN_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem CYAN_WOVEN_BURLAP_ITEM = registerBlockItem("cyan_woven_burlap", settings -> new NirvanaTexturedBlockItem(CYAN_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block LIGHT_BLUE_WOVEN_BURLAP = registerBlock("light_blue_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.LIGHT_BLUE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem LIGHT_BLUE_WOVEN_BURLAP_ITEM = registerBlockItem("light_blue_woven_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_BLUE_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block BLUE_WOVEN_BURLAP = registerBlock("blue_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.BLUE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem BLUE_WOVEN_BURLAP_ITEM = registerBlockItem("blue_woven_burlap", settings -> new NirvanaTexturedBlockItem(BLUE_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block PURPLE_WOVEN_BURLAP = registerBlock("purple_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.PURPLE_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem PURPLE_WOVEN_BURLAP_ITEM = registerBlockItem("purple_woven_burlap", settings -> new NirvanaTexturedBlockItem(PURPLE_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block MAGENTA_WOVEN_BURLAP = registerBlock("magenta_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.MAGENTA_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem MAGENTA_WOVEN_BURLAP_ITEM = registerBlockItem("magenta_woven_burlap", settings -> new NirvanaTexturedBlockItem(MAGENTA_WOVEN_BURLAP, settings), new Item.Settings());

    public static final Block PINK_WOVEN_BURLAP = registerBlock("pink_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Settings.copy(Blocks.PINK_WOOL).strength(0.5F).sounds(BlockSoundGroup.SAND));
    public static final BlockItem PINK_WOVEN_BURLAP_ITEM = registerBlockItem("pink_woven_burlap", settings -> new NirvanaTexturedBlockItem(PINK_WOVEN_BURLAP, settings), new Item.Settings());

    public static void registerBlocks() {
        ItemGroup.Builder builder = PolymerItemGroupUtils.builder();
        builder.icon(() -> new ItemStack(NirvanaBlocks.HEMP_CRATE_ITEM, 1));
        builder.displayName(Text.translatable("item-group.nirvana.blocks"));

        builder.entries((displayContext, entries) -> {
            entries.add(NirvanaItems.WILD_HEMP);
            entries.add(BLISS_BLOOM_ITEM);
            entries.add(REEFER_HEAD_ITEM);
            entries.add(THC_ITEM);
            entries.add(NirvanaItems.HEMP);
            entries.add(NirvanaItems.HEMP_SEEDS);
            entries.add(NirvanaItems.HEMP_CLOTH);
            entries.add(NirvanaItems.WEED);
            entries.add(NirvanaItems.WEED_BROWNIE);
            entries.add(NirvanaItems.JOINT);
            entries.add(NirvanaItems.BONG);
            addPotionBongStacks(entries);
            entries.add(NirvanaItems.REEFER_SPAWN_EGG);
            entries.add(NirvanaItems.THC_MINECART);
            entries.add(NirvanaItems.MUSIC_DISC_JAM);
            entries.add(paintingStack(displayContext));
            entries.add(NirvanaItems.PEACE_BANNER_PATTERN);
            addSuspiciousStacks(entries, NirvanaItems.HERBAL_SALVE, 3);
            // entries.add(NirvanaItems.PEACE_SALVE); // archived, see NirvanaItems

            entries.add(NirvanaItems.OLD_PIPE);
            entries.add(NirvanaItems.STUFFED_PIPE);
            addSuspiciousStacks(entries, NirvanaItems.SUSPICIOUS_PIPE, 4);
            entries.add(HEMP_CRATE_ITEM);
            entries.add(WEED_CRATE_ITEM);
            entries.add(HEMP_BURLAP_ITEM);
            entries.add(WHITE_HEMP_BURLAP_ITEM);
            entries.add(LIGHT_GRAY_HEMP_BURLAP_ITEM);
            entries.add(GRAY_HEMP_BURLAP_ITEM);
            entries.add(BLACK_HEMP_BURLAP_ITEM);
            entries.add(BROWN_HEMP_BURLAP_ITEM);
            entries.add(RED_HEMP_BURLAP_ITEM);
            entries.add(ORANGE_HEMP_BURLAP_ITEM);
            entries.add(YELLOW_HEMP_BURLAP_ITEM);
            entries.add(LIME_HEMP_BURLAP_ITEM);
            entries.add(GREEN_HEMP_BURLAP_ITEM);
            entries.add(CYAN_HEMP_BURLAP_ITEM);
            entries.add(LIGHT_BLUE_HEMP_BURLAP_ITEM);
            entries.add(BLUE_HEMP_BURLAP_ITEM);
            entries.add(PURPLE_HEMP_BURLAP_ITEM);
            entries.add(MAGENTA_HEMP_BURLAP_ITEM);
            entries.add(PINK_HEMP_BURLAP_ITEM);
            entries.add(WOVEN_BURLAP_ITEM);
            entries.add(WHITE_WOVEN_BURLAP_ITEM);
            entries.add(LIGHT_GRAY_WOVEN_BURLAP_ITEM);
            entries.add(GRAY_WOVEN_BURLAP_ITEM);
            entries.add(BLACK_WOVEN_BURLAP_ITEM);
            entries.add(BROWN_WOVEN_BURLAP_ITEM);
            entries.add(RED_WOVEN_BURLAP_ITEM);
            entries.add(ORANGE_WOVEN_BURLAP_ITEM);
            entries.add(YELLOW_WOVEN_BURLAP_ITEM);
            entries.add(LIME_WOVEN_BURLAP_ITEM);
            entries.add(GREEN_WOVEN_BURLAP_ITEM);
            entries.add(CYAN_WOVEN_BURLAP_ITEM);
            entries.add(LIGHT_BLUE_WOVEN_BURLAP_ITEM);
            entries.add(BLUE_WOVEN_BURLAP_ITEM);
            entries.add(PURPLE_WOVEN_BURLAP_ITEM);
            entries.add(MAGENTA_WOVEN_BURLAP_ITEM);
            entries.add(PINK_WOVEN_BURLAP_ITEM);
        });
        ItemGroup polymerGroup = builder.build();
        PolymerItemGroupUtils.registerPolymerItemGroup(id("blocks"), polymerGroup);

        Nirvana.LOGGER.info("Blocks register");
    }

    /**
     * A normal {@code Items.PAINTING} pre-set to our variant via the same component vanilla's own
     * {@code /give ... painting[minecraft:painting/variant=...]} uses, so it always places "This
     * is not a horn" instead of a random placeable variant. The variant has to be looked up
     * against the display context's own registry lookup rather than resolved once at mod init,
     * since painting_variant is a datapack-loaded registry that doesn't exist yet that early.
     */
    private static ItemStack paintingStack(ItemGroup.DisplayContext displayContext) {
        var variant = displayContext.lookup()
                .getOrThrow(RegistryKeys.PAINTING_VARIANT)
                .getOrThrow(RegistryKey.of(RegistryKeys.PAINTING_VARIANT, id("this_is_not_a_horn")));
        var stack = new ItemStack(Items.PAINTING);
        stack.set(DataComponentTypes.PAINTING_VARIANT, variant);
        return stack;
    }

    /**
     * One creative-tab stack per distinct effect combination a dynamic suspicious-crafted item
     * (see {@link galena.nirvana.data.SuspiciousCraftingRecipe}) could be crafted into, matching
     * how the original mod previewed its own dynamic suspicious-effect items in creative.
     */
    private static void addSuspiciousStacks(ItemGroup.Entries entries, Item item, int durationFactor) {
        Set<SuspiciousStewEffectsComponent> seen = new HashSet<>();
        Registries.ITEM.stream()
                .filter(flowerItem -> Registries.ITEM.getEntry(flowerItem).isIn(ItemTags.SMALL_FLOWERS))
                .forEach(flower -> {
                    var ingredient = SuspiciousStewIngredient.of(flower);
                    var effects = new SuspiciousStewEffectsComponent(
                            ingredient.getStewEffects().effects()
                                    .stream()
                                    .map(it -> new SuspiciousStewEffectsComponent.StewEffect(it.effect(), it.duration() * durationFactor))
                                    .toList()
                    );
                    if (effects.effects().isEmpty() || !seen.add(effects)) return;

                    var stack = new ItemStack(item);
                    stack.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, effects);
                    entries.add(stack);
                });
    }

    private static void addPotionBongStacks(ItemGroup.Entries entries) {
        Registries.POTION.stream()
                .filter(potion -> potion != Potions.WATER.value())
                .forEach(potion -> {
                    var stack = new ItemStack(NirvanaItems.POTION_BONG);
                    stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Registries.POTION.getEntry(potion)));
                    entries.add(stack);
                });
    }

    private static java.util.Optional<RegistryKey<net.minecraft.loot.LootTable>> lootTableOf(String name) {
        return java.util.Optional.of(RegistryKey.of(RegistryKeys.LOOT_TABLE, id("blocks/" + name)));
    }

    public static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings){
        return registerBlock(name, (set, id) -> factory.apply(settings), settings);
    }

    public static Block registerBlock(String name, BiFunction<AbstractBlock.Settings, String, Block> factory, AbstractBlock.Settings settings){
        var key = RegistryKey.of(RegistryKeys.BLOCK, id(name));
        Block block = factory.apply(settings.registryKey(key), name);

        return Registry.register(Registries.BLOCK, key, block);
    }

    public static BlockItem registerBlockItem(String name, Function<Item.Settings, BlockItem> factory, Item.Settings settings){
        var key = RegistryKey.of(RegistryKeys.ITEM, id(name));
        BlockItem item = factory.apply(settings.registryKey(key).useBlockPrefixedTranslationKey());

        return Registry.register(Registries.ITEM, key, item);
    }
}
