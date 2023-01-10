package net.jsharren.dreamt_dinner.resources.recipe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.jsharren.dreamt_dinner.resources.recipe.ingredient.IngredientLike;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;


public abstract class DtDRecipe {
    public static final char EMPTY_CHAR = ' ';
    protected final ItemConvertible output;
    protected final int outputCount;
    protected final Map<Character, IngredientLike> inputs = new LinkedHashMap<Character, IngredientLike>();
    protected final Map<String, IngredientLike> criteria = new LinkedHashMap<String, IngredientLike>();
    protected String suffix;
    @Nullable protected String group;
    
    protected DtDRecipe(ItemConvertible output, int outputCount) {
        this.output = output;
        this.outputCount = outputCount;
        this.suffix = "";
    }

    protected abstract CraftingRecipeJsonBuilder toBuilder();

    public void offerTo(Consumer<RecipeJsonProvider> exporter) {
        Identifier id = CraftingRecipeJsonBuilder.getItemId(output);
        this.toBuilder().offerTo(exporter, Identifier.of(id.getNamespace(), id.getPath() + suffix));
    }

    public DtDRecipe input(char c, IngredientLike ingredient) {
        if (c == EMPTY_CHAR) {
            throw new IllegalArgumentException(String.format("Symbol '%c' is reserved and cannot be defined", EMPTY_CHAR));
        }
        Character character = Character.valueOf(c);
        if (this.inputs.containsKey(character)) {
            throw new IllegalArgumentException(String.format("Symbol '%c' is already defined!", c));
        }
        this.inputs.put(character, ingredient);

        return this;
    }

    public DtDRecipe criterion(String key, IngredientLike ingredient) {
        this.criteria.put(key, ingredient);
        return this;
    }

    public DtDRecipe group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public DtDRecipe suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
}
