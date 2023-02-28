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
    private static final DtDShapedRecipe DREAM_CHOWDER = (
        DtDShapedRecipe.create(DtDItemPool.DREAM_CHOWDER)
        .input('B', DtDItemLike.of(Items.BOWL)).putSlots('B', "5")
        .input('S', DtDItemLike.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)).putSlots('S', "1")
        .input('T', DtDItemLike.of(DtDItemPool.COOKED_TUNA_FILLET)).putSlots('T', "2")
        .input('A', DtDItemLike.of(DtDItemPool.AURA_BREAD)).putSlots('A', "3")
        .criterion("has_cooked_tuna_fillet", DtDItemLike.of(DtDItemPool.COOKED_TUNA_FILLET))
    );
    private static final DtDShapedRecipe REVE_CAKE = (
        DtDShapedRecipe.create(DtDBlockPool.REVE_CAKE)
        .input('B', DtDItemLike.of(DtDItemPool.REVE_BERRIES)).putSlots('B', "1")
        .input('S', DtDItemLike.of(Items.SUGAR)).putSlots('S', "2")
        .input('A', DtDItemLike.of(DtDItemPool.AURA_WHEAT)).putSlots('A', "4")
        .input('E', DtDItemLike.of(Items.EGG)).putSlots('E', "5")
        .criterion("has_reve_berries", DtDItemLike.of(DtDItemPool.REVE_BERRIES))
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
        pool.add(DREAM_CHOWDER);
        pool.add(REVE_CAKE);

        return pool;
    }
}
