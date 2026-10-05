package galena.nirvana.polydex;

import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexPage;
import galena.nirvana.Nirvana;
import galena.nirvana.data.NirvanaBrewingRecipes;
import galena.nirvana.data.SuspiciousCraftingRecipe;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
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
                // Some usable precursors (e.g. a water potion bong) are not in the creative tab.
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
                (entry, candidate) -> candidate.getBacking() instanceof ItemStack actual
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

        var water = new ItemStack(Items.POTION);
        water.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        addBrewingPage(server, consumer, water, NirvanaItems.WEED, "water", "weed");
        addBrewingPage(server, consumer, new ItemStack(NirvanaItems.BONG), Items.NETHER_WART, "bong", "nether_wart");

        var reagents = new ArrayList<Item>();
        for (var item : BuiltInRegistries.ITEM) {
            if (server.potionBrewing().isIngredient(new ItemStack(item))) {
                reagents.add(item);
            }
        }
        for (var potion : server.registryAccess().lookupOrThrow(Registries.POTION).listElements().toList()) {
            var input = new ItemStack(NirvanaItems.POTION_BONG);
            input.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
            for (var reagent : reagents) {
                addBrewingPage(server, consumer, input, reagent, potion.key().identifier().toDebugFileName(),
                        BuiltInRegistries.ITEM.getKey(reagent).toDebugFileName());
            }
        }
    }

    private static void addBrewingPage(MinecraftServer server, Consumer<PolydexPage> consumer,
                                       ItemStack input, Item reagent, String source, String catalyst) {
        var ingredient = new ItemStack(reagent);
        var output = NirvanaBrewingRecipes.getCustomResult(input, ingredient, server.potionBrewing());
        if (output != null) {
            consumer.accept(new NirvanaPolydexPage(Nirvana.id("brewing/" + source + "/" + catalyst),
                    List.of(input.copy(), ingredient), output, true));
        }
    }
}
