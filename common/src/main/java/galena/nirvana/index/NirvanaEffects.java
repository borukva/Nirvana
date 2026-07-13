package galena.nirvana.index;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import galena.nirvana.NirvanaConstants;
import galena.nirvana.platform.Services;
import galena.nirvana.world.effects.PeaceEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class NirvanaEffects {

    private static final AbstractRegistrate<?> REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final TagKey<MobEffect> STACKING_EFFECTS = TagKey.create(Registries.MOB_EFFECT, NirvanaConstants.createId("stacking"));

    public static final RegistryEntry<MobEffect, PeaceEffect> PEACE = REGISTRATE
            .generic("peace", Registries.MOB_EFFECT, PeaceEffect::new)
            .register();

    /**
     * The registry's real bound {@code Holder.Reference}, as opposed to {@link #PEACE} itself
     * (a Registrate {@code DeferredHolder} shim). Vanilla codecs that persist a {@code Holder}
     * (e.g. {@code MobEffectInstance}'s {@code active_effects} save data) require an actual
     * {@code Holder.Reference} and fail to encode our shim with "Unregistered holder". Use this
     * wherever a {@link net.minecraft.world.effect.MobEffectInstance} is constructed.
     */
    public static Holder<MobEffect> peaceHolder() {
        var holder = BuiltInRegistries.MOB_EFFECT.get(PEACE.getId());
        return holder.isPresent() ? holder.get() : PEACE;
    }

    public static boolean arePeaceful(Entity target, LivingEntity attacker) {
        if (!(target instanceof LivingEntity living)) return false;
        var holder = peaceHolder();
        return attacker.hasEffect(holder) || living.hasEffect(holder);
    }

    public static void register() {
        // loads this class
    }

}
