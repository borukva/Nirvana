package galena.nirvana.data;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

/**
 * Same idea as vanilla Suspicious Stew: a base container + some weed + N of a single flower type
 * produces an item whose effects come from that flower's own vanilla suspicious-stew effect
 * table, with duration multiplied by a per-recipe factor. Used for both
 * {@link SuspiciousPipeCraftingRecipe} (base: old_pipe) and {@link SuspiciousSalveCraftingRecipe}
 * (base: bowl).
 */
public abstract class SuspiciousCraftingRecipe extends CustomRecipe {
    private static final Ingredient WEED_ITEM = Ingredient.of(NirvanaItems.WEED);

    protected abstract Item getBase();
    protected abstract int getRequiredFlowers();
    protected abstract int getRequiredWeed();
    protected abstract int getDurationFactor();
    protected abstract Item getResult();

    /** Shared by crafting and creative variants so recipe viewers see identical components. */
    public static SuspiciousStewEffects createEffects(Item result, SuspiciousEffectHolder ingredient,
                                                     int durationFactor) {
        return new SuspiciousStewEffects(ingredient.getSuspiciousEffects().effects().stream()
                .map(effect -> new SuspiciousStewEffects.Entry(effect.effect(),
                        result == NirvanaItems.SUSPICIOUS_PIPE
                                ? Math.max(20, effect.duration() * durationFactor)
                                : effect.duration() * durationFactor))
                .toList());
    }

    /** A real crafting grid for recipe viewers; quantities stay owned by the recipe. */
    public CraftingInput createDisplayInput(Item flower) {
        var stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(getBase()));
        for (int i = 0; i < getRequiredWeed(); i++) {
            stacks.add(new ItemStack(NirvanaItems.WEED));
        }
        for (int i = 0; i < getRequiredFlowers(); i++) {
            stacks.add(new ItemStack(flower));
        }
        while (stacks.size() < 9) {
            stacks.add(ItemStack.EMPTY);
        }
        return CraftingInput.of(3, 3, stacks);
    }

    // Not a static Ingredient like WEED_ITEM: tags aren't populated yet at mod-init time (when
    // static fields are evaluated), so this is checked lazily per-stack instead.
    //
    // There's no ItemTags.SMALL_FLOWERS anymore (26.2 only kept the block-side tag), so this
    // goes through the item's own block form instead - every small flower is a BlockItem anyway.
    private static boolean isFlower(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock().defaultBlockState().is(BlockTags.SMALL_FLOWERS);
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        boolean hasBase = false;
        int weedCount = 0;
        int flowerCount = 0;
        Item flowerType = null;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(getBase())) {
                    if (hasBase) {
                        return false;
                    }
                    hasBase = true;
                } else if (WEED_ITEM.test(stack)) {
                    weedCount++;
                } else if (isFlower(stack)) {
                    if (flowerType == null) {
                        flowerType = stack.getItem();
                    } else if (flowerType != stack.getItem()) {
                        return false;
                    }
                    flowerCount++;
                } else {
                    return false;
                }
            }
        }

        return hasBase && weedCount == getRequiredWeed() && flowerCount == getRequiredFlowers();
    }

    @Override
    public ItemStack assemble(CraftingInput container) {
        ItemStack result = new ItemStack(getResult(), 1);
        SuspiciousEffectHolder effectIngredient = null;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (isFlower(stack)) {
                effectIngredient = SuspiciousEffectHolder.tryGet(stack.getItem());
                break;
            }
        }

        if (effectIngredient != null) {
            var effects = createEffects(getResult(), effectIngredient, getDurationFactor());
            result.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects);
        }

        return result;
    }
}
