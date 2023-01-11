package net.jsharren.dreamt_dinner.resources;

import net.jsharren.dreamt_dinner.resources.recipe.DtDRecipePool;

public class DtDDataGenResource {
    public DtDRecipePool recipePool;

    private DtDDataGenResource() {
        recipePool = DtDRecipePool.createRecipePool();
    }

    public static DtDDataGenResource createResource() {
        return new DtDDataGenResource();
    } 
}