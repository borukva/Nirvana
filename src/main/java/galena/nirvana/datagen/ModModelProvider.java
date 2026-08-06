package galena.nirvana.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

/**
 * STUBBED OUT for the 26.2 migration - the whole client model-datagen surface was redesigned far
 * beyond a rename. {@code BlockModelGenerators} lost every one of the helper methods this used
 * (registerTintableCross/registerSouthDefaultHorizontalFacing/createLogTexturePool/
 * registerCubeWithCustomTextures are gone; its public surface is now just createTintedLeaves/
 * createParticleOnlyBlock/run(), fed by a constructor taking a
 * Consumer&lt;BlockModelDefinitionGenerator&gt;/ItemModelOutput/BiConsumer&lt;Identifier,ModelInstance&gt;
 * instead of the old mutable-builder style). Likewise {@code ItemModelGenerators} lost its
 * {@code register(Item, ModelTemplate)} method entirely.
 * <p>
 * This needs real research into the new declarative model-definition system before it can be
 * ported properly. Until then, the already-committed {@code src/main/generated/assets/...}
 * blockstate/model JSON from the last successful generation stays in place (stale but still
 * valid) - the mod still loads and renders using those, just won't pick up new textures/models
 * added since without a manual JSON edit.
 */
public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        // TODO: port to the new declarative model-definition API (see class javadoc).
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        // TODO: port to the new declarative model-definition API (see class javadoc).
    }
}
