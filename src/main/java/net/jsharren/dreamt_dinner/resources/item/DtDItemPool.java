package net.jsharren.dreamt_dinner.resources.item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;

public class DtDItemPool {
    private Map<String, DtDItem> pool;

    private DtDItemPool() {
        pool = new LinkedHashMap<String, DtDItem>();
    }
    
    public void add(DtDItem i) {
        pool.put(i.getName(), i);
    }

    public Item getItem(String name) {
        return Optional.ofNullable(pool.get(name)).map(i -> i.getItem()).orElse(Items.AIR);
    }

    public void register() {
        pool.values().forEach(i -> i.register());
    }

    public void datagen(ItemModelGenerator gen) {
        pool.values().forEach(i -> i.datagen(gen));
    }

    public static DtDItemPool createItemPool() {
        DtDItemPool itemPool = new DtDItemPool();

        itemPool.add(new DtDItem("reve_berries", new Item(new FabricItemSettings().group(ItemGroup.MISC))));

        return itemPool;
    }
}
