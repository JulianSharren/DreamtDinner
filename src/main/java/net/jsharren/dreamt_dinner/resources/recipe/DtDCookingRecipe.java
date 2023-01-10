package net.jsharren.dreamt_dinner.resources.recipe;

import org.jetbrains.annotations.Nullable;

import net.jsharren.dreamt_dinner.resources.recipe.ingredient.IngredientLike;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.CookingRecipeSerializer;

public class DtDCookingRecipe extends DtDRecipe {
    private float experience;
    private int cookingTime;

    CookingRecipeSerializer<?> serializer;

    private DtDCookingRecipe(ItemConvertible output, float experience, int cookingTime) {
        super(output, 1);
        this.experience = experience;
        this.cookingTime = cookingTime;
        this.serializer = CookingRecipeSerializer.SMELTING;
    }

    public static DtDCookingRecipe create(ItemConvertible output) {
        return new DtDCookingRecipe(output, 0.1f, 200);
    }

    private DtDCookingRecipe toCooker(CookingRecipeSerializer<?> cooker) {
        DtDCookingRecipe recipe = new DtDCookingRecipe(this.output, experience, cookingTime);
        recipe.inputs.putAll(this.inputs);
        recipe.criteria.putAll(this.criteria);
        recipe.serializer = cooker;
        return recipe;
    }

    public DtDCookingRecipe toBlasting() {
        return (
            this.toCooker(CookingRecipeSerializer.BLASTING)
            .suffix("_from_blasting")
            .withCookingTime(this.cookingTime / 2)
        );
    }

    public DtDCookingRecipe toSmoking() {
        return (
            this.toCooker(CookingRecipeSerializer.SMOKING)
            .suffix("_from_smoking")
            .withCookingTime(this.cookingTime / 2)
        );
    }

    public DtDCookingRecipe toCampBaking() {
        return (
            this.toCooker(CookingRecipeSerializer.CAMPFIRE_COOKING)
            .suffix("_from_campfire")
            .withCookingTime(this.cookingTime * 3)
        );
    }

    public DtDCookingRecipe input(IngredientLike ingredient) {
        if (!this.inputs.isEmpty()) {
            throw new IllegalStateException("Cooking recipe do not accept multiple ingredients");
        }
        super.input('0', ingredient);
        return this;
    }

    @Override
    public DtDCookingRecipe input(char c, IngredientLike ingredient) {
        throw new IllegalArgumentException("Cooking recipe do not accept ingredient symbol");
    }

    @Override
    public DtDCookingRecipe criterion(String key, IngredientLike ingredient) {
        super.criterion(key, ingredient);
        return this;
    }

    public DtDCookingRecipe inputCriterion(String key, IngredientLike ingredient) {
        return this.input(ingredient).criterion(key, ingredient);
    }
    
    @Override
    public DtDCookingRecipe group(@Nullable String group) {
        super.group(group);
        return this;
    }

    @Override
    public DtDCookingRecipe suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public DtDCookingRecipe withExperience(float experience) {
        this.experience = experience;
        return this;
    }

    public DtDCookingRecipe withCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    @Override
    protected CraftingRecipeJsonBuilder toBuilder() {
        if (!this.inputs.containsKey('0')) {
            throw new IllegalStateException("Cooking recipe needs exactly one ingredient");
        }
        CookingRecipeJsonBuilder builder = CookingRecipeJsonBuilder.create(
            this.inputs.get('0').asIngredient(), this.output, this.experience, this.cookingTime, this.serializer
        );
        this.criteria.forEach((key, ingredient) -> builder.criterion(key, ingredient.createCondition()));

        return builder;
    }
}
