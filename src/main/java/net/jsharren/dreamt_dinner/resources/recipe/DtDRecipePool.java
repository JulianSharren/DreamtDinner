package net.jsharren.dreamt_dinner.resources.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.recipe.ingredient.DtDItemLike;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;

public class DtDRecipePool {
    private List<DtDShapedRecipe> pool;

    private DtDRecipePool() {
        pool = new ArrayList<DtDShapedRecipe>();
    }
    
    public void add(DtDShapedRecipe builder) {
        pool.add(builder);
    }

    public void accept(Consumer<RecipeJsonProvider> exporter) {
        pool.forEach(recipe -> recipe.build().offerTo(exporter));
    }

    public static DtDRecipePool createRecipePool() {
        DtDRecipePool recipePool = new DtDRecipePool();

        recipePool.add(
            DtDShapedRecipe.create(DtDBlockPool.DREAM_POT)
            .inputCriterion('0', "has_polished_deepslate", DtDItemLike.of(Items.POLISHED_DEEPSLATE))
            .pattern("0 0").pattern("0 0").pattern("000")
        );

        return recipePool;
    }
}
