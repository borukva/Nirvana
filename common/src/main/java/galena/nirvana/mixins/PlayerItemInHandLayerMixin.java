package galena.nirvana.mixins;

import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Stubbed out for MC 1.21.10 — renderArmWithItem and renderArmWithSpyglass no longer exist.
 * The joint item spyglass arm rendering needs to be reworked using the new submitArmWithItem
 * system with ItemStackRenderState-based rendering pipeline.
 * TODO: Rewrite spyglass arm animation for JOINT items.
 */
@Mixin(PlayerItemInHandLayer.class)
public class PlayerItemInHandLayerMixin {

}
