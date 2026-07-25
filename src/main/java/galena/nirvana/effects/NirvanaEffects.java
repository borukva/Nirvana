package galena.nirvana.effects;

import galena.nirvana.Nirvana;
import galena.nirvana.index.PeacefulAuraStatusEffect;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class NirvanaEffects implements PolymerStatusEffect {
    public static final RegistryEntry<StatusEffect> PEACE = register("peace",
            new PeacefulAuraStatusEffect(StatusEffectCategory.BENEFICIAL, 0xb4cb22));

    public static void register() {
    }

    public static RegistryEntry<StatusEffect> register(String path, StatusEffect effect) {
        Identifier id = Identifier.of(Nirvana.MOD_ID, path);
        return Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
    }

    public static boolean arePeaceful(Entity target, LivingEntity attacker) {
        if (!(target instanceof LivingEntity living)) return false;
        return attacker.hasStatusEffect(NirvanaEffects.PEACE) || living.hasStatusEffect(NirvanaEffects.PEACE);
    }
}
