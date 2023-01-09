package net.jsharren.dreamt_dinner.resources.recipe.ingredient;

import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.recipe.Ingredient;

public interface IngredientLike {
    public Ingredient asIngredient();

    public CriterionConditions createCondition();
}
