package net.jsharren.dreamt_dinner.resources.recipe.ingredient;

import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.TagKey;

public class DtDItemTag implements IngredientLike {
    private final TagKey<Item> tag;

    private DtDItemTag(TagKey<Item> tag) {
        this.tag = tag;
    }

    public static DtDItemTag of(TagKey<Item> tag) {
        return new DtDItemTag(tag);
    }

    @Override
    public Ingredient asIngredient() {
        return Ingredient.fromTag(tag);
    }

    @Override
    public CriterionConditions createCondition() {
        return RecipeProvider.conditionsFromTag(tag);
    }
}
