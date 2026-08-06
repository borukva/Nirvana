package galena.nirvana.data;

import galena.nirvana.index.NirvanaItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.jetbrains.annotations.Nullable;

/**
 * Bongs aren't crafted at a crafting table - same as the original mod, they're brewed, mirroring
 * vanilla's own water -> awkward -> effect potion tree:
 * <p>
 * water bottle + weed -> {@code bong} (plain, gives Peace) -> (+ nether wart) -> {@code
 * potion_bong} (awkward) -> (+ any vanilla catalyst: sugar, glowstone dust, redstone, magma
 * cream, fermented spider eye, ...) -> potion_bong of whatever effect that catalyst produces in
 * vanilla.
 * <p>
 * None of these three steps can be expressed via vanilla's real item/potion recipe registration:
 * {@code registerItemRecipe} requires BOTH the source and result item to be a genuine vanilla
 * {@code PotionItem} ({@code assertPotion}, confirmed by a real server crash the first time this
 * was tried), and {@code bong}/{@code potion_bong} extend {@code BowItem} instead (needed for
 * their draw-back "smoking" animation) - so they can never pass that check. All three steps are
 * therefore hooked in manually via {@link galena.nirvana.mixin.BrewingRecipeRegistryMixin}, which
 * delegates to {@link #getCustomResult}.
 * <p>
 * {@code weed} still needs to be a *valid ingredient* for the GUI/hopper to accept it in the top
 * slot at all - {@link galena.nirvana.mixin.PotionBrewingMixin} registers a harmless water ->
 * water no-op recipe on vanilla's own {@code PotionBrewing.Builder} purely to get {@code weed}
 * into the registry's valid-ingredient list; {@link galena.nirvana.mixin.BrewingRecipeRegistryMixin}'s
 * HEAD injection always intercepts water + weed before vanilla's own no-op recipe would ever run.
 * <p>
 * Once a bong/potion_bong is sitting in a stand's slot (placed there by the stand's own internal
 * brew-completion logic, which bypasses the normal slot-insertion check entirely), a hopper still
 * can't move it into another stand's slot without {@link
 * galena.nirvana.mixin.BrewingStandBlockEntityMixin} teaching that same insertion check to accept
 * it too.
 */
public class NirvanaBrewingRecipes {
    /**
     * No-op - kept only so {@code Nirvana.java}'s init order stays readable. The water -> water
     * no-op registration itself now lives in {@link galena.nirvana.mixin.PotionBrewingMixin}
     * (piggybacking on vanilla's own {@code PotionBrewing.addVanillaMixes} instead of the Fabric
     * API registry this used to go through, which doesn't exist anymore).
     */
    public static void register() {
    }

    public static boolean isBongRelated(ItemStack stack) {
        return stack.getItem() == (NirvanaItems.BONG) || stack.getItem() == (NirvanaItems.POTION_BONG);
    }

    @Nullable
    public static ItemStack getCustomResult(ItemStack precursor, ItemStack ingredient, PotionBrewing registry) {
        if (precursor.getItem() == (Items.POTION) && ingredient.getItem() == (NirvanaItems.WEED)) {
            var contents = precursor.get(DataComponents.POTION_CONTENTS);
            if (contents != null && contents.is(Potions.WATER)) {
                return new ItemStack(NirvanaItems.BONG);
            }
            return null;
        }

        if (precursor.getItem() == (NirvanaItems.BONG) && ingredient.getItem() == (Items.NETHER_WART)) {
            var stack = withSameDamage(new ItemStack(NirvanaItems.POTION_BONG), precursor);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.AWKWARD));
            return stack;
        }

        if (precursor.getItem() == (NirvanaItems.POTION_BONG)) {
            var contents = precursor.get(DataComponents.POTION_CONTENTS);
            if (contents == null) return null;

            var probe = new ItemStack(Items.POTION);
            probe.set(DataComponents.POTION_CONTENTS, contents);

            if (!registry.hasPotionMix(probe, ingredient)) return null;

            var newContents = registry.mix(ingredient, probe).get(DataComponents.POTION_CONTENTS);
            if (newContents == null) return null;

            var stack = withSameDamage(new ItemStack(NirvanaItems.POTION_BONG), precursor);
            stack.set(DataComponents.POTION_CONTENTS, newContents);
            return stack;
        }

        return null;
    }

    private static ItemStack withSameDamage(ItemStack fresh, ItemStack precursor) {
        if (fresh.isDamageableItem() && precursor.isDamageableItem()) {
            fresh.setDamageValue(precursor.getDamageValue());
        }
        return fresh;
    }
}
