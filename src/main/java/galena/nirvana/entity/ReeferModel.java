package galena.nirvana.entity;

import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import galena.nirvana.Nirvana;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Server-side visual stand-in for the real creeper entity, which is disguised as an (empty,
 * invisible) item display via {@link Reefer#getPolymerEntityType} instead of a real creeper -
 * Polymer has no way to give an actual mob a custom limbed model without a client mod, so this
 * puppet is the entity's only visible representation.
 * <p>
 * Split into six display elements (body, head+leafy crown, four legs) rather than one combined
 * model, mirroring how {@code friends-and-foes-patch-1.21.10}'s {@code SimpleElementHolder}
 * puppets vanilla mobs: each part's own geometry stays local to its own pivot (so rotations
 * happen about the neck/hips rather than about some far-away point), and the skeletal offsets
 * between parts are applied via each element's translation.
 * <p>
 * Each part model is authored so that model point (8,8,8) - the point an item model renderer puts
 * on the display element's own origin - sits exactly on that part's pivot. That's the same
 * convention every ordinary block model already follows, and it's what makes translations here
 * plain skeletal offsets with no half-block fudge factors.
 * <p>
 * Position is synced automatically by the {@code EntityAttachment} this holder is attached to
 * (with interpolation enabled below so it doesn't snap/teleport between server ticks); rotations
 * aren't, so they're computed from the source entity here every tick - head yaw/pitch track the
 * look direction independently of the body exactly like a vanilla creeper, and leg swing reuses
 * {@link net.minecraft.world.entity.LivingEntity#limbAnimator}, the same speed/animation-progress data
 * vanilla's own quadruped models animate off of.
 */
public class ReeferModel extends ElementHolder {
    /**
     * Kept at one tick: rotations are recomputed every tick, and interpolating them over a longer
     * window just averages away fast oscillations like the leg swing.
     */
    private static final int INTERPOLATION_TICKS = 1;
    private static final int TELEPORT_TICKS = 3;
    /** Height of the neck, i.e. the body model's own top edge. */
    private static final float NECK_HEIGHT = 18F / 16F;
    /** Height of the hips, i.e. the body model's own bottom edge. */
    private static final float HIP_HEIGHT = 6F / 16F;
    /** Vanilla's own quadruped leg-swing amplitude. */
    private static final float LEG_SWING_AMPLITUDE_RAD = 1.4F;

    private final Reefer source;
    private final ItemDisplayElement body;
    private final ItemDisplayElement head;
    private final Leg[] legs;
    private final InteractionElement hitbox;

    private double lastX;
    private double lastZ;
    private float walkSpeed;
    private float walkPhase;
    /** Last texture state pushed to the parts, so the item stacks are only swapped on a change. */
    private String appearance = "";
    /** Swelling as of the previous tick, to tell "actively growing" from "holding steady/winding
     * down" ourselves instead of trusting the source entity's own raw fuse-speed field. */
    private float lastSwelling = 0F;

    public ReeferModel(Reefer source) {
        this.source = source;
        this.lastX = source.getX();
        this.lastZ = source.getZ();

        this.body = addPart(modelStack("reefer_mob"));
        this.head = addPart(modelStack("reefer_mob_head"));
        this.head.setTranslation(new Vector3f(0, NECK_HEIGHT, 0));

        this.legs = new Leg[]{
                new Leg(2F / 16F, -4F / 16F, false),  // left front
                new Leg(-2F / 16F, -4F / 16F, true),  // right front
                new Leg(2F / 16F, 4F / 16F, true),    // left hind
                new Leg(-2F / 16F, 4F / 16F, false),  // right hind
        };

        // Vanilla item/block display entities never respond to clicks/attacks regardless of
        // their bounding box, so hitting the puppet needs a real minecraft:interaction entity
        // (the vanilla tool for "invisible but clickable" hitboxes) redirecting hits back to the
        // real (network-hidden) Reefer entity.
        this.hitbox = InteractionElement.redirect(source);
        this.hitbox.setSize(source.getBbWidth(), source.getBbHeight());
        addPassengerElement(this.hitbox);
    }

    private ItemDisplayElement addPart(ItemStack stack) {
        var element = ItemDisplayElementUtil.createSimple(stack);
        element.setInterpolationDuration(INTERPOLATION_TICKS);
        element.setTeleportDuration(TELEPORT_TICKS);
        addElement(element);
        return element;
    }

    /**
     * Vanilla's own creeper visuals for the two states a plain item display can't express by
     * itself: a red flash while hurt, and the white-flashing bulge while the fuse burns. Both are
     * done by swapping each part to a recoloured copy of its model (the same trick
     * friends-and-foes-patch uses for hurt states) since a display entity has no hurt overlay of
     * its own.
     */
    private void updateAppearance(float swelling, boolean rising) {
        String state = "";
        if (this.source.hurtTime > 0) {
            state = "_hurt";
        } else if (rising && (int) (swelling * 10F) % 2 == 1) {
            // Gated on the fuse actually growing tick-over-tick, not merely on a non-zero swell:
            // the swell winds back down again when a creeper thinks better of it, and flashing
            // during that wind-down read as a stray "about to explode" blink after every hit.
            // Computed here from the swell value itself rather than trusting the source entity's
            // own raw fuse-speed field, which didn't reliably reflect flint-and-steel ignition.
            state = "_flash";
        }

        if (state.equals(this.appearance)) return;
        this.appearance = state;

        this.body.setItem(modelStack("reefer_mob" + state));
        this.head.setItem(modelStack("reefer_mob_head" + state));
        for (Leg leg : this.legs) {
            leg.element.setItem(modelStack("reefer_mob_leg" + state));
        }
    }

    private class Leg {
        final ItemDisplayElement element;
        /** Hip position relative to the entity, before the body's own yaw is applied. */
        final Vector3f hipOffset;
        /** Diagonal pairs swing in opposite phase, matching a real quadruped gait. */
        final boolean invertSwing;

        Leg(float x, float z, boolean invertSwing) {
            this.hipOffset = new Vector3f(x, HIP_HEIGHT, z);
            this.invertSwing = invertSwing;
            this.element = addPart(modelStack("reefer_mob_leg"));
        }
    }

    private static ItemStack modelStack(String name) {
        var stack = new ItemStack(Items.PAPER);
        stack.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, name));
        return stack;
    }

    /**
     * The bulge a creeper does while its fuse burns, using vanilla's own CreeperRenderer formula:
     * a fourth-power ease so it stays subtle until the last moment, times a fast wobble, widening
     * the mob while squashing it slightly shorter.
     */
    private void applySwellScale(float swelling) {
        float wobble = 1F + Mth.sin(swelling * 100F) * swelling * 0.01F;
        float eased = Mth.clamp(swelling, 0F, 1F);
        eased = eased * eased;
        eased = eased * eased;

        var scale = new Vector3f((1F + eased * 0.4F) * wobble, (1F + eased * 0.1F) / wobble, (1F + eased * 0.4F) * wobble);
        this.body.setScale(scale);
        this.head.setScale(scale);
        for (Leg leg : this.legs) {
            leg.element.setScale(scale);
        }
    }

    /**
     * Walk cycle driven straight off the entity's own per-tick movement rather than off {@code
     * limbAnimator} - same shape of math as vanilla's quadruped models, but measured here so it
     * can't depend on how that (client-oriented) helper happens to be fed on a dedicated server.
     */
    private void updateWalkCycle() {
        double x = this.source.getX();
        double z = this.source.getZ();
        float moved = (float) Math.sqrt((x - this.lastX) * (x - this.lastX) + (z - this.lastZ) * (z - this.lastZ));
        this.lastX = x;
        this.lastZ = z;

        // Exactly vanilla's own pacing (LimbAnimator: speed lerps by 0.4 toward min(moved*4, 1),
        // animationProgress accumulates that speed, and models read cos(progress * 0.6662)).
        float targetSpeed = Math.min(moved * 4F, 1F);
        this.walkSpeed += (targetSpeed - this.walkSpeed) * 0.4F;
        this.walkPhase += this.walkSpeed * 0.6662F;
    }

    @Override
    protected void onTick() {
        updateWalkCycle();

        float swelling = this.source.getSwelling(0F);
        boolean rising = swelling > this.lastSwelling;
        this.lastSwelling = swelling;
        updateAppearance(swelling, rising);
        applySwellScale(swelling);

        var bodyRotation = new Quaternionf().rotateY(-this.source.yBodyRot * Mth.DEG_TO_RAD);
        this.body.setLeftRotation(bodyRotation);

        this.head.setLeftRotation(new Quaternionf()
                .rotateY(-this.source.getYHeadRot() * Mth.DEG_TO_RAD)
                .rotateX(this.source.getXRot() * Mth.DEG_TO_RAD));

        float swing = Mth.cos(this.walkPhase) * LEG_SWING_AMPLITUDE_RAD * this.walkSpeed;
        for (Leg leg : this.legs) {
            // Translation is applied after rotation, so the hip offset is in world space and has
            // to be turned by the body's yaw itself - otherwise the legs stay put while the body
            // spins around them.
            leg.element.setTranslation(bodyRotation.transform(new Vector3f(leg.hipOffset)));
            leg.element.setLeftRotation(new Quaternionf(bodyRotation).rotateX(leg.invertSwing ? -swing : swing));
        }
    }
}
