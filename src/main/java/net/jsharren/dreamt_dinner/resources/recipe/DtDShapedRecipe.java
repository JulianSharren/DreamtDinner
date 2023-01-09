package net.jsharren.dreamt_dinner.resources.recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.jsharren.dreamt_dinner.resources.recipe.ingredient.IngredientLike;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;

public class DtDShapedRecipe {
    protected final ItemConvertible output;
    protected final int outputCount;
    protected final Map<Character, IngredientLike> inputs = new LinkedHashMap<Character, IngredientLike>();
    protected final List<String> patterns = new ArrayList<String>();
    protected final Map<String, IngredientLike> criteria = new LinkedHashMap<String, IngredientLike>();
    @Nullable protected String group;

    public DtDShapedRecipe(ItemConvertible output, int outputCount) {
        this.output = output;
        this.outputCount = outputCount;
    }

    static DtDShapedRecipe create(ItemConvertible output, int outputCount) {
        return new DtDShapedRecipe(output, outputCount);
    }

    static DtDShapedRecipe create(ItemConvertible output) {
        return new DtDShapedRecipe(output, 1);
    }

    public DtDShapedRecipe input(char c, IngredientLike ingredient) {
        if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        Character character = Character.valueOf(c);
        if (this.inputs.containsKey(character)) {
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        }
        this.inputs.put(character, ingredient);

        return this;
    }

    public DtDShapedRecipe criterion(String key, IngredientLike ingredient) {
        this.criteria.put(key, ingredient);
        return this;
    }

    public DtDShapedRecipe inputCriterion(char c, String criterionKey, IngredientLike ingredient) {
        return this.input(c, ingredient).criterion(criterionKey, ingredient);
    }

    public DtDShapedRecipe pattern(String pattern) {
        this.patterns.add(pattern);
        return this;
    }

    public DtDShapedRecipe group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public ShapedRecipeJsonBuilder build() {
        ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(output, outputCount);
        this.inputs.forEach((character, ingredient) -> builder.input(character, ingredient.asIngredient()));
        this.patterns.forEach(pattern -> builder.pattern(pattern));
        this.criteria.forEach((key, ingredient) -> builder.criterion(key, ingredient.createCondition()));
        builder.group(this.group);

        return builder;
    }
}
