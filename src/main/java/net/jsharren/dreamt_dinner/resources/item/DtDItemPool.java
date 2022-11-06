package net.jsharren.dreamt_dinner.resources.item;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class DtDItemPool {
    private List<DtDItem> pool;

    private DtDItemPool() {
        pool = new ArrayList<DtDItem>();
    }
    
    public void add(DtDItem i) {
        pool.add(i);
    }

    public void register() {
        pool.forEach(i -> i.register());
    }

    public void datagen(ItemModelGenerator gen) {
        pool.forEach(i -> i.datagen(gen));
    }

    public static DtDItemPool createItemPool() {
        DtDItemPool itemPool = new DtDItemPool();

        itemPool.add(new DtDItem("reve_berries", new Item(new FabricItemSettings().group(ItemGroup.MISC))));

        return itemPool;
    }
}
