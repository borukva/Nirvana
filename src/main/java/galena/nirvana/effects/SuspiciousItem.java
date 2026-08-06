package galena.nirvana.effects;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SuspiciousItem {
    public static List<MobEffectInstance> getEffects(ItemStack stack) {
        var effects = stack.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffects(List.of()));
        return effects.effects().stream()
                .map(SuspiciousStewEffects.Entry::createEffectInstance)
                .toList();
    }
}
