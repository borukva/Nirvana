package galena.nirvana.fabric.mixins;

import com.google.common.collect.Multimap;
import com.tterrag.registrate.fabric.FluidSpriteReloadListener;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

// FluidSpriteReloadListener was compiled for MC 1.21.1 and accesses:
//   InventoryMenu.BLOCK_ATLAS (field_21668) - moved to TextureAtlas.LOCATION_BLOCKS in 1.21.10
//   Minecraft.getTextureAtlas() (method_1549) - replaced by getAtlasManager().getAtlasOrThrow()
// We overwrite the method entirely to use the 1.21.10 API.
@Mixin(value = FluidSpriteReloadListener.class, remap = false)
public class FluidSpriteReloadListenerMixin {

    @Shadow
    private Multimap<ResourceLocation, Consumer<TextureAtlasSprite>> callbacks;

    @Overwrite
    public void onResourceManagerReload(ResourceManager resourceManager) {
        // In 1.21.10, atlasById is keyed by definitionLocation (AtlasIds.BLOCKS = "minecraft:blocks"),
        // not by textureId (TextureAtlas.LOCATION_BLOCKS = "minecraft:textures/atlas/blocks.png").
        TextureAtlas atlas = Minecraft.getInstance().getAtlasManager()
                .getAtlasOrThrow(AtlasIds.BLOCKS);
        for (ResourceLocation location : callbacks.keySet()) {
            TextureAtlasSprite sprite = atlas.getSprite(location);
            for (Consumer<TextureAtlasSprite> callback : callbacks.get(location)) {
                callback.accept(sprite);
            }
        }
    }
}
