package galena.nirvana.index;

import galena.nirvana.effects.NirvanaEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import static net.minecraft.world.item.component.Consumables.defaultFood;

public class ModConsumableComponents {
    public static final Consumable BROWNIE = defaultFood()
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(NirvanaEffects.PEACE, 20 * 40, 0)
                    )
            )
            .build();
}
