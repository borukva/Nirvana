package galena.nirvana.entity;

import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import galena.nirvana.Nirvana;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

/**
 * Server-side visual stand-in for the real creeper entity, which is disguised as an (empty,
 * invisible) item display via {@link Reefer#getPolymerEntityType} instead of a real creeper -
 * Polymer has no way to give an actual mob a custom limbed model without a client mod, so this
 * puppet is the entity's only visible representation. Position is synced automatically by the
 * {@code EntityAttachment} this holder is attached to (with interpolation enabled below so it
 * doesn't snap/teleport between server ticks); yaw isn't synced by that, so it's copied from the
 * source entity here every tick.
 */
public class ReeferModel extends ElementHolder {
    private static final int INTERPOLATION_TICKS = 3;

    private final Reefer source;
    private final ItemDisplayElement body;
    private final InteractionElement hitbox;

    public ReeferModel(Reefer source) {
        this.source = source;
        // The model's own geometry (see reefer_mob.json) already spans feet (y=0) to head-top
        // (y=26, i.e. 1.625 blocks) in native block-space units - matching a standing creeper's
        // actual position convention - so no extra scale/translation is needed here.
        this.body = ItemDisplayElementUtil.createSimple(
                ItemDisplayElementUtil.getModel(Identifier.of(Nirvana.MOD_ID, "block/reefer_mob")));
        this.body.setInterpolationDuration(INTERPOLATION_TICKS);
        this.body.setTeleportDuration(INTERPOLATION_TICKS);
        addElement(this.body);

        // Vanilla item/block display entities never respond to clicks/attacks regardless of
        // their bounding box, so hitting the puppet needs a real minecraft:interaction entity
        // (the vanilla tool for "invisible but clickable" hitboxes) redirecting hits back to the
        // real (network-hidden) Reefer entity.
        this.hitbox = InteractionElement.redirect(source);
        this.hitbox.setSize(source.getWidth(), source.getHeight());
        addPassengerElement(this.hitbox);
    }

    @Override
    protected void onTick() {
        this.body.setLeftRotation(new Quaternionf().rotateY(-this.source.getYaw() * MathHelper.RADIANS_PER_DEGREE));
    }
}
