package galena.nirvana.mixins;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import galena.nirvana.client.PeaceShader;
import galena.nirvana.world.effects.PeaceEffect;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;

    @Shadow
    public abstract ResourceLocation currentPostEffect();

    @Unique
    private boolean nirvana$shouldRender() {
        var accessor = (GameRendererAccessor) this;
        if (currentPostEffect() != null) return false;
        return PeaceEffect.shouldRenderShader(accessor.getMinecraft().player);
    }

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V")
    )
    private void renderPeaceShader(DeltaTracker delta, boolean bl, CallbackInfo ci) {
        if (!nirvana$shouldRender()) return;
        var accessor = (GameRendererAccessor) this;
        var minecraft = accessor.getMinecraft();

        PostChain postChain = minecraft.getShaderManager().getPostChain(PeaceShader.ID, LevelTargetBundle.MAIN_TARGETS);
        if (postChain != null) {
            postChain.process(minecraft.getMainRenderTarget(), resourcePool);
        }
    }

}
