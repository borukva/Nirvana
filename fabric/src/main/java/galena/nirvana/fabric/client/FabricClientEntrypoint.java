package galena.nirvana.fabric.client;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.world.block.renderer.ReeferHeadRenderer;
import galena.nirvana.world.entity.renderer.ReeferRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ReeferRenderer.LAYER, ReeferRenderer::createLayers);
        EntityModelLayerRegistry.registerModelLayer(ReeferHeadRenderer.LAYER, ReeferHeadRenderer::createLayers);

        BlockRenderLayerMap.putBlock(NirvanaBlocks.HEMP.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(NirvanaBlocks.BLISS_BLOOM.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(NirvanaBlocks.WILD_HEMP.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(NirvanaBlocks.POTTED_WILD_HEMP.get(), ChunkSectionLayer.CUTOUT);
    }

}
