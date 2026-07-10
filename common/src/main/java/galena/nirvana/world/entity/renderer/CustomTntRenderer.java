package galena.nirvana.world.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CustomTntRenderer extends TntRenderer {

    private final BlockState block;

    private CustomTntRenderer(EntityRendererProvider.Context context, Block block) {
        super(context);
        this.block = block.defaultBlockState();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends PrimedTnt> NonNullFunction<EntityRendererProvider.Context, EntityRenderer> of(NonNullSupplier<? extends Block> block) {
        return context -> new CustomTntRenderer(context, block.get());
    }

    @Override
    public void extractRenderState(PrimedTnt primedTnt, TntRenderState renderState, float partialTick) {
        super.extractRenderState(primedTnt, renderState, partialTick);
        renderState.blockState = this.block;
    }

    @Override
    public void submit(TntRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        float fuseRemaining = renderState.fuseRemainingInTicks;
        if (fuseRemaining < 10.0F) {
            float h = 1.0F - fuseRemaining / 10.0F;
            h = Mth.clamp(h, 0.0F, 1.0F);
            h *= h;
            h *= h;
            float k = 1.0F + h * 0.3F;
            poseStack.scale(k, k, k);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        int fuse = Math.round(fuseRemaining);
        TntMinecartRenderer.submitWhiteSolidBlock(renderState.blockState, poseStack, submitNodeCollector, 0xF000F0, fuse / 5 % 2 == 0, 0);
        poseStack.popPose();
    }

}
