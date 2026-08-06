package galena.nirvana.utils;

import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoodComponents {

    private static FoodProperties baseFood() {
        return new FoodProperties.Builder()
                .nutrition(1)
                .saturationModifier(0.0f)
                .build();
    }

    // 🔹 Нічне бачення 00:15
    public static final FoodProperties NIGHT_VISION = baseFood();
    public static final Consumable NIGHT_VISION_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 300), 1.0f))
            .build();

    // 🔹 Вогнестійкість 00:12
    public static final FoodProperties FIRE_RESISTANCE = baseFood();
    public static final Consumable FIRE_RESISTANCE_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 240), 1.0f))
            .build();

    // 🔹 Слабкість 00:27 (-4 dmg відразу йде від amplifier 1)
    public static final FoodProperties WEAKNESS = baseFood();
    public static final Consumable WEAKNESS_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.WEAKNESS, 540, 1), 1.0f))
            .build();

    // 🔹 Регенерація 00:24
    public static final FoodProperties REGENERATION = baseFood();
    public static final Consumable REGENERATION_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.REGENERATION, 480), 1.0f))
            .build();

    // 🔹 Стрибучість 00:18
    public static final FoodProperties JUMP_BOOST = baseFood();
    public static final Consumable JUMP_BOOST_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.JUMP_BOOST, 360), 1.0f))
            .build();

    // 🔹 Отруєння 00:36
    public static final FoodProperties POISON = baseFood();
    public static final Consumable POISON_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.POISON, 720), 1.0f))
            .build();

    // 🔹 Висушування 00:24
    public static final FoodProperties WITHER = baseFood();
    public static final Consumable WITHER_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.WITHER, 480), 1.0f))
            .build();
}
