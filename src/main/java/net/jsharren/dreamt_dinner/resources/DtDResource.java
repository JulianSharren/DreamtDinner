package net.jsharren.dreamt_dinner.resources;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;

public class DtDResource {
    public DtDItemPool itemPool;
    public DtDBlockPool blockPool;

    private DtDResource() {
        itemPool = DtDItemPool.createItemPool();
        blockPool = DtDBlockPool.createBlockPool();
    }

    public static DtDResource createResource() {
        return new DtDResource();
    } 
}
