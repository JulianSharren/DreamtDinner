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

    private final List<DtDRecipe> recipes = new ArrayList<DtDRecipe>();;

    private DtDRecipePool() {}
    
    public void add(DtDRecipe recipe) {
        recipes.add(recipe);
    }

    public void accept(Consumer<RecipeJsonProvider> exporter) {
        recipes.forEach(recipe -> recipe.offerTo(exporter));
    }

    public static DtDRecipePool createRecipePool() {
        DtDRecipePool pool = new DtDRecipePool();

        pool.add(DREAM_POT);
        pool.add(AURA_BREAD);
        pool.add(COOKED_TUNA_FILLET);
        pool.add(COOKED_TUNA_FILLET.toSmoking());
        pool.add(COOKED_TUNA_FILLET.toCampBaking());

        return pool;
    }
}
