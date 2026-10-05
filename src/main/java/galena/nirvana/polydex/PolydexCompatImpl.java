package galena.nirvana.polydex;

import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexPage;
import galena.nirvana.Nirvana;
import galena.nirvana.data.NirvanaBrewingRecipes;
import galena.nirvana.data.SuspiciousCraftingRecipe;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

final class PolydexCompatImpl {
    private PolydexCompatImpl() {
    }

    static void register() {
        PolydexPage.register(PolydexCompatImpl::createPages);
        PolydexEntry.registerEntryCreator(NirvanaItems.POTION_BONG, PolydexCompatImpl::createVariantEntry);
        PolydexEntry.registerEntryCreator(NirvanaItems.SUSPICIOUS_PIPE, PolydexCompatImpl::createVariantEntry);
        PolydexEntry.registerEntryCreator(NirvanaItems.HERBAL_SALVE, PolydexCompatImpl::createVariantEntry);
        // Generated again on every cache rebuild, after data-pack tags and potions are loaded.
        PolydexEntry.registerProvider((server, consumer) -> {
            var variants = new LinkedHashMap<Identifier, PolydexEntry>();
            createPages(server, page -> {
                var recipe = (NirvanaPolydexPage) page;
                var entry = createVariantEntry(recipe.output());
                variants.putIfAbsent(entry.identifier(), entry);
                // Keep recipe precursors linked to the same component-aware entries as outputs.
                for (var input : recipe.input()) {
                    if (!input.isEmpty()
                            && BuiltInRegistries.ITEM.getKey(input.getItem()).getNamespace().equals(Nirvana.MOD_ID)) {
                        var ingredientEntry = createVariantEntry(input);
                        variants.putIfAbsent(ingredientEntry.identifier(), ingredientEntry);
                    }
                }
            });
            consumer.acceptAll(variants.values());
        });
    }

    private static PolydexEntry createVariantEntry(ItemStack stack) {
        if (!stack.is(NirvanaItems.POTION_BONG) && !stack.is(NirvanaItems.SUSPICIOUS_PIPE)
                && !stack.is(NirvanaItems.HERBAL_SALVE)) {
            return PolydexEntry.of(stack);
        }
        var variant = new StringBuilder(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath());
        var potion = stack.get(DataComponents.POTION_CONTENTS);
        if (potion != null) {
            potion.potion().ifPresent(holder -> variant.append("/potion/")
                    .append(holder.unwrapKey().orElseThrow().identifier().toDebugFileName()));
        }
        var effects = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (effects != null) {
            for (var effect : effects.effects()) {
                variant.append("/effect/").append(effect.effect().unwrapKey().orElseThrow().identifier().toDebugFileName())
                        .append('/').append(effect.duration());
            }
        }
        return PolydexEntry.of(Nirvana.id(variant.toString()), stack,
                (_, candidate) -> candidate.getBacking() instanceof ItemStack actual
                        && NirvanaPolydexPage.sameVariant(stack, actual));
    }

    private static void createPages(MinecraftServer server, Consumer<PolydexPage> consumer) {
        for (var holder : server.getRecipeManager().getRecipes()) {
            if (holder.value() instanceof SuspiciousCraftingRecipe recipe) {
                for (var flower : BuiltInRegistries.ITEM) {
                    var input = recipe.createDisplayInput(flower);
                    if (recipe.matches(input, server.overworld())) {
                        var id = Nirvana.id("crafting/" + holder.id().identifier().toDebugFileName()
                                + "/" + BuiltInRegistries.ITEM.getKey(flower).toDebugFileName());
                        consumer.accept(new NirvanaPolydexPage(id, List.copyOf(input.items()), recipe.assemble(input), false));
                    }
                }
            }
        }

        for (var recipe : NirvanaBrewingRecipes.getReachableBongRecipes(server.potionBrewing())) {
            var input = recipe.input();
            var source = BuiltInRegistries.ITEM.getKey(input.getItem()).toDebugFileName();
            var contents = input.get(DataComponents.POTION_CONTENTS);
            if (contents != null && contents.potion().isPresent()) {
                source += "/" + contents.potion().orElseThrow().unwrapKey().orElseThrow()
                        .identifier().toDebugFileName();
            }
            var catalyst = BuiltInRegistries.ITEM.getKey(recipe.ingredient().getItem()).toDebugFileName();
            consumer.accept(new NirvanaPolydexPage(Nirvana.id("brewing/" + source + "/" + catalyst),
                    List.of(input, recipe.ingredient()), recipe.output(), true));
        }
    }
}
