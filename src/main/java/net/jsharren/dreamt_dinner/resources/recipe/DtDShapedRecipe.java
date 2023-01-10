package net.jsharren.dreamt_dinner.resources.recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import net.jsharren.dreamt_dinner.resources.recipe.ingredient.IngredientLike;
import net.jsharren.dreamt_dinner.utils.Namespace;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;

public class DtDShapedRecipe extends DtDRecipe {
    protected final StringBuilder pattern = new StringBuilder();

    public DtDShapedRecipe(ItemConvertible output, int outputCount) {
        super(output, outputCount);
    }

    static DtDShapedRecipe create(ItemConvertible output, int outputCount) {
        return new DtDShapedRecipe(output, outputCount);
    }

    static DtDShapedRecipe create(ItemConvertible output) {
        return new DtDShapedRecipe(output, 1);
    }

    @Override
    public DtDShapedRecipe input(char c, IngredientLike ingredient) {
        super.input(c, ingredient);
        return this;
    }

    @Override
    public DtDShapedRecipe criterion(String key, IngredientLike ingredient) {
        super.criterion(key, ingredient);
        return this;
    }

    public DtDShapedRecipe inputCriterion(char c, String criterionKey, IngredientLike ingredient) {
        return this.input(c, ingredient).criterion(criterionKey, ingredient);
    }

    public DtDShapedRecipe putSlots(char c, Collection<Integer> slots) {
        if ( slots.stream().anyMatch(i -> i < 0) ) {
            throw new IllegalArgumentException("Negative slot number not allowed!");
        }
        int maxSlot = Collections.max(slots);
        if ( maxSlot >= 256 ) {
            throw new IllegalArgumentException(String.format("Max slot number %d exceeds limit (255)!", maxSlot));
        }
        if ( maxSlot >= this.pattern.length() ) {
            this.pattern.append(
                String.valueOf(EMPTY_CHAR).repeat(maxSlot - this.pattern.length() + 1)
            );
        }
        slots.forEach(i -> this.pattern.setCharAt(i, c));

        return this;
    }

    public DtDShapedRecipe putSlots(char c, String slots) {
        if( slots.chars().anyMatch(i -> (i < '1' || i > '9')) ) {
            throw new IllegalArgumentException(String.format("Slots '%s' may only contain 1-9", slots));
        }
        return this.putSlots(
            c,
            slots.chars()
            .map(i -> ((i - '1') / 3 * 16 + (i - '1') % 3))
            .boxed().toList()
        );
    }

    public Stream<String> computeSlots() {
        String string = StringUtils.stripEnd(this.pattern.toString(), String.valueOf(EMPTY_CHAR));
        int len = string.length();
        if ( len == 0 ) {
            throw new IllegalArgumentException(String.format("Slots are all empty!"));
        }
        int vMax = (len - 1) / 16 + 1;
        List<String> patterns = (
            IntStream.range(0, vMax)
            .boxed()
            .map(i -> StringUtils.stripEnd(StringUtils.substring(string, i * 16, i * 16 + 16), String.valueOf(EMPTY_CHAR)))
            .toList()
        );
        Namespace.getRootLogger().info(patterns.toString());
        int hMax = Collections.max(patterns.stream().map(String::length).toList());
        return patterns.stream().map(s -> StringUtils.rightPad(s, hMax, EMPTY_CHAR));
    }

    @Override
    public DtDShapedRecipe group(@Nullable String group) {
        super.group(group);
        return this;
    }

    @Override
    public DtDShapedRecipe suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    protected ShapedRecipeJsonBuilder toBuilder() {
        ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(output, outputCount);
        this.inputs.forEach((character, ingredient) -> builder.input(character, ingredient.asIngredient()));
        this.computeSlots().forEach(pattern -> builder.pattern(pattern));
        this.criteria.forEach((key, ingredient) -> builder.criterion(key, ingredient.createCondition()));
        builder.group(this.group);

        return builder;
    }
}
