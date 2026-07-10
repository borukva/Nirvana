package galena.nirvana.mixins;

import galena.nirvana.NirvanaConstants;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatRenderer.class)
public class CatMixin {

    @Unique
    private static final ResourceLocation SPRIGATITO_TEXTURE = NirvanaConstants.createId("textures/entity/cat/sprigatito.png");

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/Cat;Lnet/minecraft/client/renderer/entity/state/CatRenderState;F)V", at = @At("TAIL"))
    private void overwriteSprigatitoTexture(Cat cat, CatRenderState renderState, float partialTick, CallbackInfo ci) {
        var customName = cat.getCustomName();
        if (customName == null) return;
        var name = ChatFormatting.stripFormatting(customName.getString());
        if (name == null) return;
        if (name.toLowerCase(Locale.ROOT).equals("sprigatito")) {
            renderState.texture = SPRIGATITO_TEXTURE;
        }
    }

}
