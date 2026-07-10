package galena.nirvana.mixins;

import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Stubbed out for MC 1.21.10 — renderArmWithSpyglass no longer exists in PlayerItemInHandLayer.
 * TODO: Rewrite spyglass arm animation for JOINT items using the new rendering pipeline.
 */
@Mixin(PlayerItemInHandLayer.class)
public interface PlayerItemInHandLayerAccessor {

}
