package galena.nirvana.index;

import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import galena.nirvana.data.NirvanaBrewingRecipes;
import galena.nirvana.data.SuspiciousCraftingRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

import java.util.HashSet;
import java.util.Set;

import static galena.nirvana.Nirvana.id;
import static galena.nirvana.index.NirvanaBlocks.*;

public final class NirvanaCreativeTabs {
    private NirvanaCreativeTabs() {
    }

    public static void register() {
        var tab = PolymerCreativeModeTabUtils.builder()
                .icon(() -> new ItemStack(HEMP_CRATE_ITEM))
                .title(Component.translatable("item-group.nirvana.blocks"))
                .displayItems(NirvanaCreativeTabs::displayItems)
                .build();
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(id("blocks"), tab);
    }

    private static void displayItems(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
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
        entries.accept(NirvanaItems.REEFER_SPAWN_EGG);
        entries.accept(NirvanaItems.THC_MINECART);
        entries.accept(NirvanaItems.MUSIC_DISC_JAM);
        entries.accept(paintingStack(context));
        entries.accept(NirvanaItems.PEACE_BANNER_PATTERN);
        addSuspiciousStacks(entries, NirvanaItems.HERBAL_SALVE, 3);
        // entries.accept(NirvanaItems.PEACE_SALVE); // archived, see NirvanaItems
        entries.accept(NirvanaItems.OLD_PIPE);
        entries.accept(NirvanaItems.STUFFED_PIPE);
        addSuspiciousStacks(entries, NirvanaItems.SUSPICIOUS_PIPE, 2);
        entries.accept(HEMP_CRATE_ITEM);
        entries.accept(WEED_CRATE_ITEM);
        for (var item : new BlockItem[]{
                HEMP_BURLAP_ITEM, WHITE_HEMP_BURLAP_ITEM, LIGHT_GRAY_HEMP_BURLAP_ITEM,
                GRAY_HEMP_BURLAP_ITEM, BLACK_HEMP_BURLAP_ITEM, BROWN_HEMP_BURLAP_ITEM,
                RED_HEMP_BURLAP_ITEM, ORANGE_HEMP_BURLAP_ITEM, YELLOW_HEMP_BURLAP_ITEM,
                LIME_HEMP_BURLAP_ITEM, GREEN_HEMP_BURLAP_ITEM, CYAN_HEMP_BURLAP_ITEM,
                LIGHT_BLUE_HEMP_BURLAP_ITEM, BLUE_HEMP_BURLAP_ITEM, PURPLE_HEMP_BURLAP_ITEM,
                MAGENTA_HEMP_BURLAP_ITEM, PINK_HEMP_BURLAP_ITEM,
                WOVEN_BURLAP_ITEM, WHITE_WOVEN_BURLAP_ITEM, LIGHT_GRAY_WOVEN_BURLAP_ITEM,
                GRAY_WOVEN_BURLAP_ITEM, BLACK_WOVEN_BURLAP_ITEM, BROWN_WOVEN_BURLAP_ITEM,
                RED_WOVEN_BURLAP_ITEM, ORANGE_WOVEN_BURLAP_ITEM, YELLOW_WOVEN_BURLAP_ITEM,
                LIME_WOVEN_BURLAP_ITEM, GREEN_WOVEN_BURLAP_ITEM, CYAN_WOVEN_BURLAP_ITEM,
                LIGHT_BLUE_WOVEN_BURLAP_ITEM, BLUE_WOVEN_BURLAP_ITEM, PURPLE_WOVEN_BURLAP_ITEM,
                MAGENTA_WOVEN_BURLAP_ITEM, PINK_WOVEN_BURLAP_ITEM}) {
            entries.accept(item);
        }
        addPotionBongStacks(context, entries);
    }

    /** Resolve the datapack-loaded painting variant only when the tab is displayed. */
    private static ItemStack paintingStack(CreativeModeTab.ItemDisplayParameters context) {
        var variant = context.holders().lookupOrThrow(Registries.PAINTING_VARIANT)
                .getOrThrow(ResourceKey.create(Registries.PAINTING_VARIANT, id("this_is_not_a_horn")));
        var stack = new ItemStack(Items.PAINTING);
        stack.set(DataComponents.PAINTING_VARIANT, variant);
        return stack;
    }

    /** One stack per distinct effect combination, with the recipe's duration multiplier. */
    private static void addSuspiciousStacks(CreativeModeTab.Output entries, Item item, int durationFactor) {
        Set<SuspiciousStewEffects> seen = new HashSet<>();
        BuiltInRegistries.ITEM.stream()
                // 26.2 only has the block-side small flowers tag.
                .filter(flower -> flower instanceof BlockItem blockItem
                        && blockItem.getBlock().defaultBlockState().is(BlockTags.SMALL_FLOWERS))
                .forEach(flower -> {
                    var ingredient = SuspiciousEffectHolder.tryGet(flower);
                    if (ingredient == null) return;
                    var effects = SuspiciousCraftingRecipe.createEffects(item, ingredient, durationFactor);
                    if (effects.effects().isEmpty() || !seen.add(effects)) return;
                    var stack = new ItemStack(item);
                    stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects);
                    entries.accept(stack);
                });
    }

    private static void addPotionBongStacks(CreativeModeTab.ItemDisplayParameters context,
                                           CreativeModeTab.Output entries) {
        var seen = new HashSet<PotionContents>();
        var brewing = PotionBrewing.bootstrap(context.enabledFeatures());
        for (var recipe : NirvanaBrewingRecipes.getReachableBongRecipes(brewing)) {
            var stack = recipe.output();
            if (stack.is(NirvanaItems.POTION_BONG) && seen.add(stack.get(DataComponents.POTION_CONTENTS))) {
                entries.accept(stack);
            }
        }
    }
}
