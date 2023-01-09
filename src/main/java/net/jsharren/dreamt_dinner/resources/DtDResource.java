package net.jsharren.dreamt_dinner.resources;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.blockentity.DtDBlockEntityPool;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;
import net.jsharren.dreamt_dinner.resources.recipe.DtDRecipePool;

public class DtDResource {
    public DtDItemPool itemPool;
    public DtDBlockPool blockPool;
    public DtDBlockEntityPool blockEntityPool;
    public DtDRecipePool recipePool;

    private DtDResource() {
        itemPool = DtDItemPool.createItemPool();
        blockPool = DtDBlockPool.createBlockPool();
        blockEntityPool = DtDBlockEntityPool.create(blockPool);
        recipePool = DtDRecipePool.createRecipePool();
    }

    public static DtDResource createResource() {
        return new DtDResource();
    } 
}
