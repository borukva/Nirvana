package galena.nirvana.data;

import com.mojang.serialization.MapCodec;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;

/**
 * 1 bowl + 3 weed + 3 of a single flower type -> herbal_salve. Stateless (a singleton, same as
 * vanilla's own {@code CustomRecipe} subclasses like {@code RepairItemRecipe}) - none of its
 * behaviour actually varies per-instance, only the recipe book category is fixed.
 */
public class SuspiciousSalveCraftingRecipe extends SuspiciousCraftingRecipe {
    public static final SuspiciousSalveCraftingRecipe INSTANCE = new SuspiciousSalveCraftingRecipe();
    public static final MapCodec<SuspiciousSalveCraftingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousSalveCraftingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<SuspiciousSalveCraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private static final Ingredient BASE_ITEM = Ingredient.of(Items.BOWL);

    @Override
    protected Ingredient getBase() {
        return BASE_ITEM;
    }

    @Override
    protected int getRequiredFlowers() {
        return 3;
    }

    @Override
    protected int getRequiredWeed() {
        return 3;
    }

    @Override
    protected int getDurationFactor() {
        return 3;
    }

    @Override
    protected Item getResult() {
        return NirvanaItems.HERBAL_SALVE;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
