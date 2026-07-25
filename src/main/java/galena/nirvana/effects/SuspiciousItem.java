package galena.nirvana.effects;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SuspiciousItem {
    public static List<StatusEffectInstance> getEffects(ItemStack stack) {
        var effects = stack.getOrDefault(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffectsComponent(List.of()));
        return effects.effects().stream()
                .map(SuspiciousStewEffectsComponent.StewEffect::createStatusEffectInstance)
                .toList();
    }
}
