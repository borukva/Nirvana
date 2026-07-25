package galena.nirvana.utils;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

public class ModFoodComponents {

    private static FoodComponent baseFood() {
        return new FoodComponent.Builder()
                .nutrition(1)
                .saturationModifier(0.0f)
                .build();
    }

    // 🔹 Нічне бачення 00:15
    public static final FoodComponent NIGHT_VISION = baseFood();
    public static final ConsumableComponent NIGHT_VISION_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300), 1.0f))
            .build();

    // 🔹 Вогнестійкість 00:12
    public static final FoodComponent FIRE_RESISTANCE = baseFood();
    public static final ConsumableComponent FIRE_RESISTANCE_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 240), 1.0f))
            .build();

    // 🔹 Слабкість 00:27 (-4 dmg відразу йде від amplifier 1)
    public static final FoodComponent WEAKNESS = baseFood();
    public static final ConsumableComponent WEAKNESS_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.WEAKNESS, 540, 1), 1.0f))
            .build();

    // 🔹 Регенерація 00:24
    public static final FoodComponent REGENERATION = baseFood();
    public static final ConsumableComponent REGENERATION_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.REGENERATION, 480), 1.0f))
            .build();

    // 🔹 Стрибучість 00:18
    public static final FoodComponent JUMP_BOOST = baseFood();
    public static final ConsumableComponent JUMP_BOOST_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.JUMP_BOOST, 360), 1.0f))
            .build();

    // 🔹 Отруєння 00:36
    public static final FoodComponent POISON = baseFood();
    public static final ConsumableComponent POISON_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.POISON, 720), 1.0f))
            .build();

    // 🔹 Висушування 00:24
    public static final FoodComponent WITHER = baseFood();
    public static final ConsumableComponent WITHER_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.WITHER, 480), 1.0f))
            .build();
}
