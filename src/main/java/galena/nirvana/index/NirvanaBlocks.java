package galena.nirvana.index;

import galena.nirvana.Nirvana;
import galena.nirvana.index.crop.BlissBloom;
import galena.nirvana.index.crop.HempCrop;
import galena.nirvana.index.crop.PottedWildHemp;
//import galena.nirvana.index.crop.WildHemp;
import galena.nirvana.index.crop.WildHemp;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

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

    public static final Block HEMP_CRATE = registerBlock("hemp_crate", NirvanaBasketBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.SAND));
    public static final BlockItem HEMP_CRATE_ITEM = registerBlockItem("hemp_crate", settings -> new NirvanaTexturedBlockItem(HEMP_CRATE, settings), new Item.Properties());

    public static final Block WEED_CRATE = registerBlock("weed_crate", NirvanaBasketBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.SAND));
    public static final BlockItem WEED_CRATE_ITEM = registerBlockItem("weed_crate", settings -> new NirvanaTexturedBlockItem(WEED_CRATE, settings), new Item.Properties());

    public static final Block HEMP_BURLAP = registerBlock("hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem HEMP_BURLAP_ITEM = registerBlockItem("hemp_burlap", settings -> new NirvanaTexturedBlockItem(HEMP_BURLAP, settings), new Item.Properties());

    public static final Block WHITE_HEMP_BURLAP = registerBlock("white_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem WHITE_HEMP_BURLAP_ITEM = registerBlockItem("white_hemp_burlap", settings -> new NirvanaTexturedBlockItem(WHITE_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block LIGHT_GRAY_HEMP_BURLAP = registerBlock("light_gray_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.lightGray()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem LIGHT_GRAY_HEMP_BURLAP_ITEM = registerBlockItem("light_gray_hemp_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_GRAY_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block GRAY_HEMP_BURLAP = registerBlock("gray_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.gray()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem GRAY_HEMP_BURLAP_ITEM = registerBlockItem("gray_hemp_burlap", settings -> new NirvanaTexturedBlockItem(GRAY_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block BLACK_HEMP_BURLAP = registerBlock("black_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.black()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem BLACK_HEMP_BURLAP_ITEM = registerBlockItem("black_hemp_burlap", settings -> new NirvanaTexturedBlockItem(BLACK_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block BROWN_HEMP_BURLAP = registerBlock("brown_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.brown()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem BROWN_HEMP_BURLAP_ITEM = registerBlockItem("brown_hemp_burlap", settings -> new NirvanaTexturedBlockItem(BROWN_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block RED_HEMP_BURLAP = registerBlock("red_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.red()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem RED_HEMP_BURLAP_ITEM = registerBlockItem("red_hemp_burlap", settings -> new NirvanaTexturedBlockItem(RED_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block ORANGE_HEMP_BURLAP = registerBlock("orange_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.orange()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem ORANGE_HEMP_BURLAP_ITEM = registerBlockItem("orange_hemp_burlap", settings -> new NirvanaTexturedBlockItem(ORANGE_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block YELLOW_HEMP_BURLAP = registerBlock("yellow_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.yellow()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem YELLOW_HEMP_BURLAP_ITEM = registerBlockItem("yellow_hemp_burlap", settings -> new NirvanaTexturedBlockItem(YELLOW_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block LIME_HEMP_BURLAP = registerBlock("lime_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.lime()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem LIME_HEMP_BURLAP_ITEM = registerBlockItem("lime_hemp_burlap", settings -> new NirvanaTexturedBlockItem(LIME_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block GREEN_HEMP_BURLAP = registerBlock("green_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.green()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem GREEN_HEMP_BURLAP_ITEM = registerBlockItem("green_hemp_burlap", settings -> new NirvanaTexturedBlockItem(GREEN_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block CYAN_HEMP_BURLAP = registerBlock("cyan_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.cyan()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem CYAN_HEMP_BURLAP_ITEM = registerBlockItem("cyan_hemp_burlap", settings -> new NirvanaTexturedBlockItem(CYAN_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block LIGHT_BLUE_HEMP_BURLAP = registerBlock("light_blue_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.lightBlue()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem LIGHT_BLUE_HEMP_BURLAP_ITEM = registerBlockItem("light_blue_hemp_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_BLUE_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block BLUE_HEMP_BURLAP = registerBlock("blue_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.blue()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem BLUE_HEMP_BURLAP_ITEM = registerBlockItem("blue_hemp_burlap", settings -> new NirvanaTexturedBlockItem(BLUE_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block PURPLE_HEMP_BURLAP = registerBlock("purple_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.purple()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem PURPLE_HEMP_BURLAP_ITEM = registerBlockItem("purple_hemp_burlap", settings -> new NirvanaTexturedBlockItem(PURPLE_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block MAGENTA_HEMP_BURLAP = registerBlock("magenta_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.magenta()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem MAGENTA_HEMP_BURLAP_ITEM = registerBlockItem("magenta_hemp_burlap", settings -> new NirvanaTexturedBlockItem(MAGENTA_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block PINK_HEMP_BURLAP = registerBlock("pink_hemp_burlap", NirvanaBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.pink()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem PINK_HEMP_BURLAP_ITEM = registerBlockItem("pink_hemp_burlap", settings -> new NirvanaTexturedBlockItem(PINK_HEMP_BURLAP, settings), new Item.Properties());

    public static final Block WOVEN_BURLAP = registerBlock("woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem WOVEN_BURLAP_ITEM = registerBlockItem("woven_burlap", settings -> new NirvanaTexturedBlockItem(WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block WHITE_WOVEN_BURLAP = registerBlock("white_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.white()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem WHITE_WOVEN_BURLAP_ITEM = registerBlockItem("white_woven_burlap", settings -> new NirvanaTexturedBlockItem(WHITE_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block LIGHT_GRAY_WOVEN_BURLAP = registerBlock("light_gray_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.lightGray()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem LIGHT_GRAY_WOVEN_BURLAP_ITEM = registerBlockItem("light_gray_woven_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_GRAY_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block GRAY_WOVEN_BURLAP = registerBlock("gray_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.gray()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem GRAY_WOVEN_BURLAP_ITEM = registerBlockItem("gray_woven_burlap", settings -> new NirvanaTexturedBlockItem(GRAY_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block BLACK_WOVEN_BURLAP = registerBlock("black_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.black()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem BLACK_WOVEN_BURLAP_ITEM = registerBlockItem("black_woven_burlap", settings -> new NirvanaTexturedBlockItem(BLACK_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block BROWN_WOVEN_BURLAP = registerBlock("brown_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.brown()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem BROWN_WOVEN_BURLAP_ITEM = registerBlockItem("brown_woven_burlap", settings -> new NirvanaTexturedBlockItem(BROWN_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block RED_WOVEN_BURLAP = registerBlock("red_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.red()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem RED_WOVEN_BURLAP_ITEM = registerBlockItem("red_woven_burlap", settings -> new NirvanaTexturedBlockItem(RED_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block ORANGE_WOVEN_BURLAP = registerBlock("orange_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.orange()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem ORANGE_WOVEN_BURLAP_ITEM = registerBlockItem("orange_woven_burlap", settings -> new NirvanaTexturedBlockItem(ORANGE_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block YELLOW_WOVEN_BURLAP = registerBlock("yellow_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.yellow()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem YELLOW_WOVEN_BURLAP_ITEM = registerBlockItem("yellow_woven_burlap", settings -> new NirvanaTexturedBlockItem(YELLOW_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block LIME_WOVEN_BURLAP = registerBlock("lime_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.lime()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem LIME_WOVEN_BURLAP_ITEM = registerBlockItem("lime_woven_burlap", settings -> new NirvanaTexturedBlockItem(LIME_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block GREEN_WOVEN_BURLAP = registerBlock("green_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.green()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem GREEN_WOVEN_BURLAP_ITEM = registerBlockItem("green_woven_burlap", settings -> new NirvanaTexturedBlockItem(GREEN_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block CYAN_WOVEN_BURLAP = registerBlock("cyan_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.cyan()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem CYAN_WOVEN_BURLAP_ITEM = registerBlockItem("cyan_woven_burlap", settings -> new NirvanaTexturedBlockItem(CYAN_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block LIGHT_BLUE_WOVEN_BURLAP = registerBlock("light_blue_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.lightBlue()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem LIGHT_BLUE_WOVEN_BURLAP_ITEM = registerBlockItem("light_blue_woven_burlap", settings -> new NirvanaTexturedBlockItem(LIGHT_BLUE_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block BLUE_WOVEN_BURLAP = registerBlock("blue_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.blue()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem BLUE_WOVEN_BURLAP_ITEM = registerBlockItem("blue_woven_burlap", settings -> new NirvanaTexturedBlockItem(BLUE_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block PURPLE_WOVEN_BURLAP = registerBlock("purple_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.purple()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem PURPLE_WOVEN_BURLAP_ITEM = registerBlockItem("purple_woven_burlap", settings -> new NirvanaTexturedBlockItem(PURPLE_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block MAGENTA_WOVEN_BURLAP = registerBlock("magenta_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.magenta()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem MAGENTA_WOVEN_BURLAP_ITEM = registerBlockItem("magenta_woven_burlap", settings -> new NirvanaTexturedBlockItem(MAGENTA_WOVEN_BURLAP, settings), new Item.Properties());

    public static final Block PINK_WOVEN_BURLAP = registerBlock("pink_woven_burlap", NirvanaWovenBurlapBlock::new, Block.Properties.ofFullCopy(Blocks.WOOL.pink()).strength(0.5F).sound(SoundType.WOOL));
    public static final BlockItem PINK_WOVEN_BURLAP_ITEM = registerBlockItem("pink_woven_burlap", settings -> new NirvanaTexturedBlockItem(PINK_WOVEN_BURLAP, settings), new Item.Properties());

    public static void registerBlocks() {
        CreativeModeTab.Builder builder = PolymerCreativeModeTabUtils.builder();
        builder.icon(() -> new ItemStack(NirvanaBlocks.HEMP_CRATE_ITEM, 1));
        builder.title(Component.translatable("item-group.nirvana.blocks"));

        builder.displayItems((displayContext, entries) -> {
            entries.accept(NirvanaItems.WILD_HEMP);
            entries.accept(BLISS_BLOOM_ITEM);
            entries.accept(REEFER_HEAD_ITEM);
            entries.accept(THC_ITEM);
            entries.accept(NirvanaItems.HEMP);
            entries.accept(NirvanaItems.HEMP_SEEDS);
            entries.accept(NirvanaItems.HEMP_CLOTH);
            entries.accept(NirvanaItems.WEED);
            entries.accept(NirvanaItems.WEED_BROWNIE);
            entries.accept(NirvanaItems.JOINT);
            entries.accept(NirvanaItems.BONG);
            addPotionBongStacks(entries);
            entries.accept(NirvanaItems.REEFER_SPAWN_EGG);
            entries.accept(NirvanaItems.THC_MINECART);
            entries.accept(NirvanaItems.MUSIC_DISC_JAM);
            entries.accept(paintingStack(displayContext));
            entries.accept(NirvanaItems.PEACE_BANNER_PATTERN);
            addSuspiciousStacks(entries, NirvanaItems.HERBAL_SALVE, 3);
            // entries.accept(NirvanaItems.PEACE_SALVE); // archived, see NirvanaItems

            entries.accept(NirvanaItems.OLD_PIPE);
            entries.accept(NirvanaItems.STUFFED_PIPE);
            addSuspiciousStacks(entries, NirvanaItems.SUSPICIOUS_PIPE, 4);
            entries.accept(HEMP_CRATE_ITEM);
            entries.accept(WEED_CRATE_ITEM);
            entries.accept(HEMP_BURLAP_ITEM);
            entries.accept(WHITE_HEMP_BURLAP_ITEM);
            entries.accept(LIGHT_GRAY_HEMP_BURLAP_ITEM);
            entries.accept(GRAY_HEMP_BURLAP_ITEM);
            entries.accept(BLACK_HEMP_BURLAP_ITEM);
            entries.accept(BROWN_HEMP_BURLAP_ITEM);
            entries.accept(RED_HEMP_BURLAP_ITEM);
            entries.accept(ORANGE_HEMP_BURLAP_ITEM);
            entries.accept(YELLOW_HEMP_BURLAP_ITEM);
            entries.accept(LIME_HEMP_BURLAP_ITEM);
            entries.accept(GREEN_HEMP_BURLAP_ITEM);
            entries.accept(CYAN_HEMP_BURLAP_ITEM);
            entries.accept(LIGHT_BLUE_HEMP_BURLAP_ITEM);
            entries.accept(BLUE_HEMP_BURLAP_ITEM);
            entries.accept(PURPLE_HEMP_BURLAP_ITEM);
            entries.accept(MAGENTA_HEMP_BURLAP_ITEM);
            entries.accept(PINK_HEMP_BURLAP_ITEM);
            entries.accept(WOVEN_BURLAP_ITEM);
            entries.accept(WHITE_WOVEN_BURLAP_ITEM);
            entries.accept(LIGHT_GRAY_WOVEN_BURLAP_ITEM);
            entries.accept(GRAY_WOVEN_BURLAP_ITEM);
            entries.accept(BLACK_WOVEN_BURLAP_ITEM);
            entries.accept(BROWN_WOVEN_BURLAP_ITEM);
            entries.accept(RED_WOVEN_BURLAP_ITEM);
            entries.accept(ORANGE_WOVEN_BURLAP_ITEM);
            entries.accept(YELLOW_WOVEN_BURLAP_ITEM);
            entries.accept(LIME_WOVEN_BURLAP_ITEM);
            entries.accept(GREEN_WOVEN_BURLAP_ITEM);
            entries.accept(CYAN_WOVEN_BURLAP_ITEM);
            entries.accept(LIGHT_BLUE_WOVEN_BURLAP_ITEM);
            entries.accept(BLUE_WOVEN_BURLAP_ITEM);
            entries.accept(PURPLE_WOVEN_BURLAP_ITEM);
            entries.accept(MAGENTA_WOVEN_BURLAP_ITEM);
            entries.accept(PINK_WOVEN_BURLAP_ITEM);
        });
        CreativeModeTab polymerGroup = builder.build();
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(id("blocks"), polymerGroup);

        Nirvana.LOGGER.info("Blocks register");
    }

    /**
     * A normal {@code Items.PAINTING} pre-set to our variant via the same component vanilla's own
     * {@code /give ... painting[minecraft:painting/variant=...]} uses, so it always places "This
     * is not a horn" instead of a random placeable variant. The variant has to be looked up
     * against the display context's own registry lookup rather than resolved once at mod init,
     * since painting_variant is a datapack-loaded registry that doesn't exist yet that early.
     */
    private static ItemStack paintingStack(CreativeModeTab.ItemDisplayParameters displayContext) {
        var variant = displayContext.holders()
                .lookupOrThrow(Registries.PAINTING_VARIANT)
                .getOrThrow(ResourceKey.create(Registries.PAINTING_VARIANT, id("this_is_not_a_horn")));
        var stack = new ItemStack(Items.PAINTING);
        stack.set(DataComponents.PAINTING_VARIANT, variant);
        return stack;
    }

    /**
     * One creative-tab stack per distinct effect combination a dynamic suspicious-crafted item
     * (see {@link galena.nirvana.data.SuspiciousCraftingRecipe}) could be crafted into, matching
     * how the original mod previewed its own dynamic suspicious-effect items in creative.
     */
    private static void addSuspiciousStacks(CreativeModeTab.Output entries, Item item, int durationFactor) {
        Set<SuspiciousStewEffects> seen = new HashSet<>();
        BuiltInRegistries.ITEM.stream()
                // No ItemTags.SMALL_FLOWERS anymore (26.2 only kept the block-side tag) - go
                // through the item's own block form instead, same as SuspiciousCraftingRecipe.
                .filter(flowerItem -> flowerItem instanceof BlockItem blockItem
                        && blockItem.getBlock().builtInRegistryHolder().is(BlockTags.SMALL_FLOWERS))
                .forEach(flower -> {
                    var ingredient = SuspiciousEffectHolder.tryGet(flower);
                    var effects = new SuspiciousStewEffects(
                            ingredient.getSuspiciousEffects().effects()
                                    .stream()
                                    .map(it -> new SuspiciousStewEffects.Entry(it.effect(), it.duration() * durationFactor))
                                    .toList()
                    );
                    if (effects.effects().isEmpty() || !seen.add(effects)) return;

                    var stack = new ItemStack(item);
                    stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects);
                    entries.accept(stack);
                });
    }

    private static void addPotionBongStacks(CreativeModeTab.Output entries) {
        BuiltInRegistries.POTION.stream()
                .filter(potion -> potion != Potions.WATER.value())
                .forEach(potion -> {
                    var stack = new ItemStack(NirvanaItems.POTION_BONG);
                    stack.set(DataComponents.POTION_CONTENTS, new PotionContents(BuiltInRegistries.POTION.wrapAsHolder(potion)));
                    entries.accept(stack);
                });
    }

    private static java.util.Optional<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTableOf(String name) {
        return java.util.Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id("blocks/" + name)));
    }

    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings){
        return registerBlock(name, (set, id) -> factory.apply(settings), settings);
    }

    public static Block registerBlock(String name, BiFunction<BlockBehaviour.Properties, String, Block> factory, BlockBehaviour.Properties settings){
        var key = ResourceKey.create(Registries.BLOCK, id(name));
        Block block = factory.apply(settings.setId(key), name);

        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static BlockItem registerBlockItem(String name, Function<Item.Properties, BlockItem> factory, Item.Properties settings){
        var key = ResourceKey.create(Registries.ITEM, id(name));
        BlockItem item = factory.apply(settings.setId(key).useBlockDescriptionPrefix());

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
