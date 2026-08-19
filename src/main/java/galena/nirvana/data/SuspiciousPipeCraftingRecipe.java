package galena.nirvana.data;

import com.mojang.serialization.MapCodec;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;

/**
 * 1 old_pipe + 1 weed + 6 of a single flower type -> suspicious_pipe. Stateless (a singleton,
 * same as vanilla's own {@code CustomRecipe} subclasses like {@code RepairItemRecipe}) - none of
 * its behaviour actually varies per-instance, only the recipe book category is fixed.
 */
public class SuspiciousPipeCraftingRecipe extends SuspiciousCraftingRecipe {
    public static final SuspiciousPipeCraftingRecipe INSTANCE = new SuspiciousPipeCraftingRecipe();
    public static final MapCodec<SuspiciousPipeCraftingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousPipeCraftingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<SuspiciousPipeCraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private static final Ingredient BASE_ITEM = Ingredient.of(NirvanaItems.OLD_PIPE);

    @Override
    protected Ingredient getBase() {
        return BASE_ITEM;
    }

    @Override
    protected int getRequiredFlowers() {
        return 6;
    }

    @Override
    protected int getRequiredWeed() {
        return 2;
    }

    @Override
    protected int getDurationFactor() {
        return 4;
    }

    @Override
    protected Item getResult() {
        return NirvanaItems.SUSPICIOUS_PIPE;
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
