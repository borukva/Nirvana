package galena.nirvana.datagen;

import com.google.gson.JsonObject;
import galena.nirvana.Nirvana;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Hand-rolled replacement for the vanilla {@code BlockModelGenerators}/{@code ItemModelGenerators}
 * pipeline (see the old stub this replaced in git history for why): in 26.2 those two classes only
 * expose {@code createTintedLeaves}/{@code createParticleOnlyBlock}/{@code run()} publicly - every
 * per-block registration helper that would let a mod add its own entries is private, so there is no
 * way to plug custom blocks into them from outside {@code net.minecraft.client.data.models}.
 * <p>
 * Instead this writes the exact same JSON shape by hand, using the same {@link PackOutput.PathProvider}
 * scheme vanilla's {@code ModelProvider} uses internally (blockstates/items/models under the resource
 * pack target), verified against {@code ModelProvider}'s own constructor bytecode.
 */
public class ModModelProvider implements DataProvider {
    private static final String NS = Nirvana.MOD_ID;

    private final PackOutput.PathProvider blockStatePath;
    private final PackOutput.PathProvider itemModelDefPath;
    private final PackOutput.PathProvider modelPath;

    public ModModelProvider(FabricPackOutput output) {
        this.blockStatePath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.itemModelDefPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        List<String> colorPrefixes = new ArrayList<>();
        colorPrefixes.add("");
        for (DyeColor color : DyeColor.values()) {
            colorPrefixes.add(color.getName() + "_");
        }
        for (String prefix : colorPrefixes) {
            burlap(futures, cachedOutput, prefix + "hemp_burlap");
            wovenBurlap(futures, cachedOutput, prefix + "woven_burlap");
        }

        crate(futures, cachedOutput, "hemp_crate");
        crate(futures, cachedOutput, "weed_crate");
        cross(futures, cachedOutput, "wild_hemp");

        flatItem(futures, cachedOutput, "hemp");
        flatItem(futures, cachedOutput, "hemp_cloth");
        flatItem(futures, cachedOutput, "hemp_seeds");
        flatItem(futures, cachedOutput, "weed");
        flatItem(futures, cachedOutput, "weed_brownie");
        flatItem(futures, cachedOutput, "music_disc_jam");
        flatItem(futures, cachedOutput, "old_pipe");

        // wild_hemp's flat inventory icon reuses the block's own texture instead of a dedicated item one.
        Identifier wildHempItemModel = id("item/wild_hemp");
        futures.add(saveModel(cachedOutput, wildHempItemModel, generatedItemModel(id("block/wild_hemp"))));
        futures.add(saveItemModelDefinition(cachedOutput, id("wild_hemp"), wildHempItemModel));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private void burlap(List<CompletableFuture<?>> futures, CachedOutput cachedOutput, String name) {
        Identifier model = id("block/" + name);

        JsonObject variants = new JsonObject();
        variants.add("facing=east", facingVariant(model, 270));
        variants.add("facing=north", facingVariant(model, 180));
        variants.add("facing=south", facingVariant(model, 0));
        variants.add("facing=west", facingVariant(model, 90));
        JsonObject blockState = new JsonObject();
        blockState.add("variants", variants);
        futures.add(saveBlockState(cachedOutput, id(name), blockState));

        JsonObject textures = new JsonObject();
        textures.addProperty("pattern", model.toString());
        futures.add(saveModel(cachedOutput, model, modelWithParent("minecraft:block/template_glazed_terracotta", textures)));

        futures.add(saveItemModelDefinition(cachedOutput, id(name), model));
    }

    private void wovenBurlap(List<CompletableFuture<?>> futures, CachedOutput cachedOutput, String name) {
        Identifier model = id("block/" + name);
        Identifier horizontalModel = id("block/" + name + "_horizontal");

        JsonObject axisX = facingVariant(horizontalModel, 90);
        axisX.addProperty("x", 90);
        JsonObject axisZ = facingVariant(horizontalModel, 0);
        axisZ.addProperty("x", 90);
        JsonObject variants = new JsonObject();
        variants.add("axis=x", axisX);
        variants.add("axis=y", singleModel(model));
        variants.add("axis=z", axisZ);
        JsonObject blockState = new JsonObject();
        blockState.add("variants", variants);
        futures.add(saveBlockState(cachedOutput, id(name), blockState));

        JsonObject textures = new JsonObject();
        textures.addProperty("end", id("block/" + name + "_top").toString());
        textures.addProperty("side", model.toString());
        futures.add(saveModel(cachedOutput, model, modelWithParent("minecraft:block/cube_column", textures)));
        futures.add(saveModel(cachedOutput, horizontalModel, modelWithParent("minecraft:block/cube_column_horizontal", textures)));

        futures.add(saveItemModelDefinition(cachedOutput, id(name), model));
    }

    private void crate(List<CompletableFuture<?>> futures, CachedOutput cachedOutput, String name) {
        Identifier model = id("block/" + name);

        JsonObject blockState = new JsonObject();
        JsonObject variants = new JsonObject();
        variants.add("", singleModel(model));
        blockState.add("variants", variants);
        futures.add(saveBlockState(cachedOutput, id(name), blockState));

        JsonObject textures = new JsonObject();
        textures.addProperty("down", id("block/" + name + "_bottom").toString());
        textures.addProperty("east", id("block/" + name + "_side").toString());
        textures.addProperty("north", id("block/" + name + "_front").toString());
        textures.addProperty("particle", id("block/" + name + "_top").toString());
        textures.addProperty("south", id("block/" + name + "_back").toString());
        textures.addProperty("up", id("block/" + name + "_top").toString());
        textures.addProperty("west", id("block/" + name + "_side").toString());
        futures.add(saveModel(cachedOutput, model, modelWithParent("minecraft:block/cube", textures)));

        futures.add(saveItemModelDefinition(cachedOutput, id(name), model));
    }

    private void cross(List<CompletableFuture<?>> futures, CachedOutput cachedOutput, String name) {
        Identifier model = id("block/" + name);

        JsonObject blockState = new JsonObject();
        JsonObject variants = new JsonObject();
        variants.add("", singleModel(model));
        blockState.add("variants", variants);
        futures.add(saveBlockState(cachedOutput, id(name), blockState));

        JsonObject textures = new JsonObject();
        textures.addProperty("cross", model.toString());
        futures.add(saveModel(cachedOutput, model, modelWithParent("minecraft:block/cross", textures)));
    }

    private void flatItem(List<CompletableFuture<?>> futures, CachedOutput cachedOutput, String name) {
        Identifier model = id("item/" + name);
        futures.add(saveModel(cachedOutput, model, generatedItemModel(model)));
        futures.add(saveItemModelDefinition(cachedOutput, id(name), model));
    }

    private static JsonObject singleModel(Identifier model) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        return variant;
    }

    private static JsonObject facingVariant(Identifier model, int yRotation) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        if (yRotation != 0) {
            variant.addProperty("y", yRotation);
        }
        return variant;
    }

    private static JsonObject modelWithParent(String parent, JsonObject textures) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);
        model.add("textures", textures);
        return model;
    }

    private static JsonObject generatedItemModel(Identifier texture) {
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", texture.toString());
        return modelWithParent("minecraft:item/generated", textures);
    }

    private static JsonObject itemModelDefinition(Identifier model) {
        JsonObject modelEntry = new JsonObject();
        modelEntry.addProperty("type", "minecraft:model");
        modelEntry.addProperty("model", model.toString());
        JsonObject root = new JsonObject();
        root.add("model", modelEntry);
        return root;
    }

    private CompletableFuture<?> saveBlockState(CachedOutput cachedOutput, Identifier blockId, JsonObject json) {
        return DataProvider.saveStable(cachedOutput, json, blockStatePath.json(blockId));
    }

    private CompletableFuture<?> saveItemModelDefinition(CachedOutput cachedOutput, Identifier itemId, Identifier model) {
        return DataProvider.saveStable(cachedOutput, itemModelDefinition(model), itemModelDefPath.json(itemId));
    }

    private CompletableFuture<?> saveModel(CachedOutput cachedOutput, Identifier modelId, JsonObject json) {
        return DataProvider.saveStable(cachedOutput, json, modelPath.json(modelId));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(NS, path);
    }

    @Override
    public String getName() {
        return "Nirvana Block/Item Models";
    }
}
