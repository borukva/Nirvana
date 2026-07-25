package galena.nirvana.index;

import galena.nirvana.effects.NirvanaEffects;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

import static net.minecraft.component.type.ConsumableComponents.food;

public class ModConsumableComponents {
    public static final ConsumableComponent BROWNIE = food()
            .consumeEffect(
                    new ApplyEffectsConsumeEffect(
                            new StatusEffectInstance(NirvanaEffects.PEACE, 20 * 40, 0)
                    )
            )
            .build();
}
