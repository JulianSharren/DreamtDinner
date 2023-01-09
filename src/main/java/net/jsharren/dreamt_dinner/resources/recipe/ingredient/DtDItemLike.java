package net.jsharren.dreamt_dinner.resources.recipe.ingredient;

import java.util.Arrays;

import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;

public class DtDItemLike implements IngredientLike {
    public final ItemConvertible[] items;

    private DtDItemLike(ItemConvertible[] items) {
        this.items = items;
    }

    public static DtDItemLike of(ItemConvertible... items) {
        return new DtDItemLike(items);
    }

    @Override
    public Ingredient asIngredient() {
        return Ingredient.ofItems(this.items);
    }

    @Override
    public CriterionConditions createCondition() {
        ItemPredicate[] predicates = (
            Arrays.stream(this.items)
            .map(item -> ItemPredicate.Builder.create().items(item).build())
            .toArray(ItemPredicate[]::new)
        );
        return RecipeProvider.conditionsFromItemPredicates(predicates);
    }

    
}
