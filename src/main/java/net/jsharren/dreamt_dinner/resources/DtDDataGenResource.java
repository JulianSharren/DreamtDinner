package net.jsharren.dreamt_dinner.resources;

import net.jsharren.dreamt_dinner.resources.advancement.DtDAdvancementPool;
import net.jsharren.dreamt_dinner.resources.recipe.DtDRecipePool;

public class DtDDataGenResource {
    public final DtDRecipePool recipePool = DtDRecipePool.createRecipePool();
    public final DtDAdvancementPool advancementPool = DtDAdvancementPool.createAdvancementPool();

    private DtDDataGenResource() {}

    public static DtDDataGenResource createResource() {
        return new DtDDataGenResource();
    } 
}