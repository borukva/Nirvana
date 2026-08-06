package galena.nirvana.entity;

import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import galena.nirvana.Nirvana;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * The smoke ring a pipe puffs out. The original mod ships this as a real custom particle type,
 * which a server-only mod can't register - but a display entity can imitate one, and this follows
 * that particle's own behaviour ({@code SmokeRingParticle}) rather than inventing its own:
 * <ul>
 *   <li>it does not grow - the widening is drawn into the four sprites themselves, and the
 *       particle advances through them on a square-root curve, so the ring opens up quickly at
 *       first and then lingers wide open;</li>
 *   <li>it drifts along the smoker's line of sight, its speed shaved by friction every tick.</li>
 * </ul>
 * It is shorter-lived here, and starts thinning out well before it runs out of momentum, so it
 * dissipates as it goes instead of parking in mid-air and then blinking out.
 * Free-standing rather than bolted to the smoker, via a ticking chunk attachment, so the ring
 * keeps travelling on its own once exhaled. The holder itself stays put and the quad is moved by
 * its own translation - {@link ChunkAttachment} has no way to move an attached holder.
 */
public class SmokeRing extends ElementHolder {
    private static final int FRAMES = 4;
    /**
     * Cut down from the original particle's eight seconds - it opens up in the first half-second
     * either way, and the rest was just a ring hanging around.
     */
    private static final int LIFETIME_TICKS = 20 * 6;
    /**
     * Long enough that fading starts while the ring is still visibly drifting, rather than after
     * friction has already brought it to a halt.
     */
    private static final int FADE_TICKS = 50;
    /** How many progressively-more-transparent copies of the final sprite exist. */
    private static final int FADE_STEPS = 5;
    private static final double INITIAL_SPEED = 0.1;
    /** The original particle's {@code friction}, applied to its velocity every tick. */
    private static final double FRICTION = 0.98;
    private static final float SCALE = 0.5F;

    private final ItemDisplayElement ring;
    private HolderAttachment attachment;
    private Vec3 velocity;
    private Vec3 offset = Vec3.ZERO;
    private int age;
    private int frame = -1;
    private int fadeStep = -1;

    public static void spawn(ServerLevel world, Vec3 origin, Vec3 direction) {
        var holder = new SmokeRing(direction);
        holder.attachment = ChunkAttachment.ofTicking(holder, world, origin);
    }

    private static ItemStack frameStack(int frame) {
        return modelStack("smoke_ring_" + frame);
    }

    private static ItemStack fadeStack(int step) {
        return modelStack("smoke_ring_fade_" + step);
    }

    private static ItemStack modelStack(String name) {
        var stack = new ItemStack(Items.PAPER);
        stack.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, name));
        return stack;
    }

    private SmokeRing(Vec3 direction) {
        var facing = direction.normalize();
        this.velocity = facing.scale(INITIAL_SPEED);

        this.ring = ItemDisplayElementUtil.createSimple(frameStack(0));
        this.ring.setInterpolationDuration(1);
        this.ring.setTeleportDuration(1);
        // Turn the quad's own facing (its normal points along -Z) to look down the travel path,
        // so the ring flies through its own hole rather than edge-on.
        this.ring.setLeftRotation(new Quaternionf()
                .rotateY((float) Math.atan2(facing.x, facing.z))
                .rotateX((float) -Math.asin(facing.y)));
        this.ring.setScale(new Vector3f(SCALE));
        addElement(this.ring);
    }

    @Override
    protected void onTick() {
        this.age++;
        if (this.age > LIFETIME_TICKS) {
            if (this.attachment != null) {
                this.attachment.destroy();
            }
            return;
        }

        this.offset = this.offset.add(this.velocity);
        this.velocity = this.velocity.scale(FRICTION);
        this.ring.setTranslation(new Vector3f((float) this.offset.x, (float) this.offset.y, (float) this.offset.z));

        int remaining = LIFETIME_TICKS - this.age;
        if (remaining < FADE_TICKS) {
            // A display element has no alpha of its own, so fading is done the way a resource pack
            // would: swapping through progressively more see-through copies of the (by now
            // settled) final sprite. Size stays put - the ring thins out and vanishes rather than
            // shrinking away.
            int step = Math.min((FADE_TICKS - remaining) * FADE_STEPS / FADE_TICKS, FADE_STEPS - 1);
            if (step != this.fadeStep) {
                this.fadeStep = step;
                this.ring.setItem(fadeStack(step));
            }
            return;
        }

        // The original picks its sprite with sqrt(age) * sqrt(lifetime) * 2, clamped to the
        // lifetime, then maps that across the sprite set - a curve that reaches the last, widest
        // frame about a quarter of the way in.
        int spriteAge = (int) Math.min(Math.sqrt(this.age) * Math.sqrt(LIFETIME_TICKS) * 2, LIFETIME_TICKS);
        int wantedFrame = Math.min(spriteAge * FRAMES / (LIFETIME_TICKS + 1), FRAMES - 1);
        if (wantedFrame != this.frame) {
            this.frame = wantedFrame;
            this.ring.setItem(frameStack(wantedFrame));
        }
    }
}
