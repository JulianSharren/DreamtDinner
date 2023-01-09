package net.jsharren.dreamt_dinner.resources.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class DtDFoodComponents {
    public static final FoodComponent AURA_BREAD = (
        new FoodComponent.Builder()
        .hunger(5).saturationModifier(1.0f)
        .build()
    );
    public static final FoodComponent TUNA = (
        new FoodComponent.Builder()
        .hunger(3).saturationModifier(0.1f)
        .build()
    );
    public static final FoodComponent COOKED_TUNA = (
        new FoodComponent.Builder()
        .hunger(8).saturationModifier(0.8f)
        .build()
    );
    public static final FoodComponent REVE_BERRIES = (
        new FoodComponent.Builder()
        .hunger(2).saturationModifier(0.1f)
        .statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600), 1.0f)
        .alwaysEdible()
        .build()
    );
}
