package galena.nirvana.data;

import galena.nirvana.index.NirvanaItems;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
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
 * slot at all - {@code registerPotionRecipe} doesn't have the same PotionItem restriction on its
 * ingredient parameter (vanilla itself registers plain items like glowstone dust/sugar/nether
 * wart this way), so {@link #register} registers a harmless water -> water no-op recipe purely to
 * get {@code weed} into the registry's valid-ingredient list; the mixin's HEAD injection always
 * intercepts water + weed before vanilla's own no-op recipe would ever run.
 * <p>
 * Once a bong/potion_bong is sitting in a stand's slot (placed there by the stand's own internal
 * brew-completion logic, which bypasses the normal slot-insertion check entirely), a hopper still
 * can't move it into another stand's slot without {@link
 * galena.nirvana.mixin.BrewingStandBlockEntityMixin} teaching that same insertion check to accept
 * it too.
 */
public class NirvanaBrewingRecipes {
    public static void register() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder ->
                builder.registerPotionRecipe(Potions.WATER, NirvanaItems.WEED, Potions.WATER)
        );
    }

    public static boolean isBongRelated(ItemStack stack) {
        return stack.isOf(NirvanaItems.BONG) || stack.isOf(NirvanaItems.POTION_BONG);
    }

    @Nullable
    public static ItemStack getCustomResult(ItemStack precursor, ItemStack ingredient, BrewingRecipeRegistry registry) {
        if (precursor.isOf(Items.POTION) && ingredient.isOf(NirvanaItems.WEED)) {
            var contents = precursor.get(DataComponentTypes.POTION_CONTENTS);
            if (contents != null && contents.matches(Potions.WATER)) {
                return new ItemStack(NirvanaItems.BONG);
            }
            return null;
        }

        if (precursor.isOf(NirvanaItems.BONG) && ingredient.isOf(Items.NETHER_WART)) {
            var stack = withSameDamage(new ItemStack(NirvanaItems.POTION_BONG), precursor);
            stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.AWKWARD));
            return stack;
        }

        if (precursor.isOf(NirvanaItems.POTION_BONG)) {
            var contents = precursor.get(DataComponentTypes.POTION_CONTENTS);
            if (contents == null) return null;

            var probe = new ItemStack(Items.POTION);
            probe.set(DataComponentTypes.POTION_CONTENTS, contents);

            if (!registry.hasRecipe(probe, ingredient)) return null;

            var newContents = registry.craft(ingredient, probe).get(DataComponentTypes.POTION_CONTENTS);
            if (newContents == null) return null;

            var stack = withSameDamage(new ItemStack(NirvanaItems.POTION_BONG), precursor);
            stack.set(DataComponentTypes.POTION_CONTENTS, newContents);
            return stack;
        }

        return null;
    }

    private static ItemStack withSameDamage(ItemStack fresh, ItemStack precursor) {
        if (fresh.isDamageable() && precursor.isDamageable()) {
            fresh.setDamage(precursor.getDamage());
        }
        return fresh;
    }
}
