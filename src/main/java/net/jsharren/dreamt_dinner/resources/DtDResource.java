package net.jsharren.dreamt_dinner.resources;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.blockentity.DtDBlockEntityPool;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;

public class DtDResource {
    public DtDItemPool itemPool;
    public DtDBlockPool blockPool;
    public DtDBlockEntityPool blockEntityPool;

    private DtDResource() {
        itemPool = DtDItemPool.createItemPool();
        blockPool = DtDBlockPool.createBlockPool();
        blockEntityPool = DtDBlockEntityPool.create(blockPool);
    }

    public static DtDResource createResource() {
        return new DtDResource();
    } 
}
