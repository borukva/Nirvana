package galena.nirvana.index;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import galena.nirvana.platform.Services;
import galena.nirvana.world.recipe.SuspiciousCraftingRecipe;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBlock;

public class NirvanaRecipeTypes {

    private static final AbstractRegistrate<?> REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final RegistryEntry<RecipeSerializer<?>, ? extends RecipeSerializer<?>> SUSPICIOUS_RECIPE_SERIALIZER = REGISTRATE
            .generic("suspicious_crafting", Registries.RECIPE_SERIALIZER, SuspiciousCraftingRecipe.Serializer::new)
            .register();

    public static Stream<Pair<ItemLike, ItemStack>> getSuspiciousVariants(ItemLike output, int factor) {
        var tagIterable = BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.SMALL_FLOWERS);
        return StreamSupport.stream(tagIterable.spliterator(), false)
                .map(Holder::value)
                .filter(BlockItem.class::isInstance)
                .map(item -> ((BlockItem) item).getBlock())
                .filter(FlowerBlock.class::isInstance)
                .map(FlowerBlock.class::cast)
                .map(flower -> {
                    var outputStack = new ItemStack(output);
                    var effects = flower.getSuspiciousEffects().effects();
                    var modifiedEffects = new SuspiciousStewEffects(effects
                            .stream()
                            .map(it -> new SuspiciousStewEffects.Entry(it.effect(), it.duration() * factor))
                            .toList()
                    );
                    outputStack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, modifiedEffects);

                    return new Pair<>(flower, outputStack);
                });
    }

    private static Stream<RecipeHolder<CraftingRecipe>> createSuspiciousRecipes(RecipeHolder<SuspiciousCraftingRecipe> from) {
        var weed = Ingredient.of(NirvanaItems.WEED);

        return getSuspiciousVariants(from.value().getContainer().getItem(), from.value().durationFactor).map(pair -> {
            var flowerBlock = pair.getFirst().asItem();
            var output = pair.getSecond();
            var type = BuiltInRegistries.ITEM.getKey(flowerBlock);

            Ingredient flower = Ingredient.of(flowerBlock);
            var inputs = new java.util.ArrayList<Ingredient>(from.value().requiredFlowers + from.value().requiredWeed + 1);
            for (int i = 0; i < from.value().requiredFlowers; i++) inputs.add(flower);
            for (int i = 0; i < from.value().requiredWeed; i++) inputs.add(weed);
            inputs.add(from.value().base);

            var baseLocation = from.id().location();
            var newLocation = ResourceLocation.fromNamespaceAndPath(
                    baseLocation.getNamespace(),
                    baseLocation.getPath() + "/" + type.getNamespace() + "/" + type.getPath()
            );
            var id = ResourceKey.create(Registries.RECIPE, newLocation);
            var recipe = new ShapelessRecipe(from.id().location().getPath(), CraftingBookCategory.MISC, output, inputs);
            return new RecipeHolder<CraftingRecipe>(id, recipe);
        });
    }

    public static List<RecipeHolder<CraftingRecipe>> createSuspiciousRecipes() {
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) return List.of();
        // RecipeAccess on the client is backed by RecipeManager; cast to access getRecipes()
        var recipeAccess = connection.recipes();
        if (!(recipeAccess instanceof RecipeManager recipeManager)) return List.of();
        return recipeManager.getRecipes().stream()
                .filter(it -> it.value() instanceof SuspiciousCraftingRecipe)
                .map(it -> {
                    @SuppressWarnings("unchecked")
                    RecipeHolder<SuspiciousCraftingRecipe> holder = (RecipeHolder<SuspiciousCraftingRecipe>) (RecipeHolder<?>) it;
                    return holder;
                })
                .flatMap(NirvanaRecipeTypes::createSuspiciousRecipes)
                .toList();
    }

    public static void register() {
        // loads this class
    }

}
