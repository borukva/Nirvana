package galena.nirvana.mixins;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.world.block.renderer.ReeferHeadRenderer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkullBlockRenderer.class)
public class SkullBlockRendererMixin {

    @Inject(
            method = "createModel",
            cancellable = true,
            at = @At("HEAD")
    )
    private static void reeferModel(EntityModelSet set, SkullBlock.Type type, CallbackInfoReturnable<SkullModelBase> cir) {
        if (type == NirvanaBlocks.REEFER_SKULL_TYPE) {
            cir.setReturnValue(new SkullModel(set.bakeLayer(ReeferHeadRenderer.LAYER)));
        }
    }

    // In 1.21.10, resolveSkullRenderType calls getSkullRenderType(type, null) for non-player skulls.
    // That looks up SKIN_BY_TYPE which doesn't contain REEFER_SKULL_TYPE, then passes null to
    // entityCutoutNoCullZOffset → TextureStateShard(null) → Optional.of(null) → NPE.
    // Intercept getSkullRenderType before the crash and return the correct render type.
    @Inject(
            method = "getSkullRenderType",
            cancellable = true,
            at = @At("HEAD")
    )
    private static void reeferRenderType(SkullBlock.Type type, ResourceLocation texture, CallbackInfoReturnable<RenderType> cir) {
        if (type == NirvanaBlocks.REEFER_SKULL_TYPE) {
            cir.setReturnValue(RenderType.entityCutoutNoCullZOffset(ReeferHeadRenderer.TEXTURE));
        }
    }

}
