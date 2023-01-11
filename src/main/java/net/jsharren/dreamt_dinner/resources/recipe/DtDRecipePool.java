package net.jsharren.dreamt_dinner.resources.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;
import net.jsharren.dreamt_dinner.resources.recipe.ingredient.DtDItemLike;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;

public class DtDRecipePool {
    private static final DtDShapedRecipe DREAM_POT = (
        DtDShapedRecipe.create(DtDBlockPool.DREAM_POT)
        .inputCriterion('D', "has_polished_deepslate", DtDItemLike.of(Items.POLISHED_DEEPSLATE)).putSlots('D', "1346789")
        .input('T', DtDItemLike.of(Items.TORCH)).putSlots('T', "5")
    );
    private static final DtDShapedRecipe AURA_BREAD = (
        DtDShapedRecipe.create(DtDItemPool.AURA_BREAD)
        .inputCriterion('W', "has_aura_wheat", DtDItemLike.of(DtDItemPool.AURA_WHEAT)).putSlots('W', "123")
    );
    private static final DtDCookingRecipe COOKED_TUNA_FILLET = (
        DtDCookingRecipe.create(DtDItemPool.COOKED_TUNA_FILLET)
        .inputCriterion("has_tuna_fillet", DtDItemLike.of(DtDItemPool.TUNA_FILLET))
    );

    private List<DtDRecipe> pool;

    private DtDRecipePool() {
        pool = new ArrayList<DtDRecipe>();
    }
    
    public void add(DtDRecipe builder) {
        pool.add(builder);
    }

    public void accept(Consumer<RecipeJsonProvider> exporter) {
        pool.forEach(recipe -> recipe.offerTo(exporter));
    }

    public static DtDRecipePool createRecipePool() {
        DtDRecipePool recipePool = new DtDRecipePool();

        recipePool.add(DREAM_POT);
        recipePool.add(AURA_BREAD);
        recipePool.add(COOKED_TUNA_FILLET);
        recipePool.add(COOKED_TUNA_FILLET.toSmoking());
        recipePool.add(COOKED_TUNA_FILLET.toCampBaking());

        return recipePool;
    }
}
