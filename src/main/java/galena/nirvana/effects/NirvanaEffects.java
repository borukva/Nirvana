package galena.nirvana.effects;

import galena.nirvana.Nirvana;
import galena.nirvana.index.PeacefulAuraStatusEffect;
import eu.pb4.polymer.core.api.other.PolymerMobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public class NirvanaEffects implements PolymerMobEffect {
    public static final Holder<MobEffect> PEACE = register("peace",
            new PeacefulAuraStatusEffect(MobEffectCategory.BENEFICIAL, 0xb4cb22));

    public static void register() {
    }

    public static Holder<MobEffect> register(String path, MobEffect effect) {
        Identifier id = Identifier.fromNamespaceAndPath(Nirvana.MOD_ID, path);
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id, effect);
    }

    public static boolean arePeaceful(Entity target, LivingEntity attacker) {
        if (!(target instanceof LivingEntity living)) return false;
        return attacker.hasEffect(NirvanaEffects.PEACE) || living.hasEffect(NirvanaEffects.PEACE);
    }
}
