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
    public static final DtDItem AURA_BREAD = new DtDItem("aura_bread", new Item(new FabricItemSettings().group(ItemGroup.FOOD)));
    public static final DtDItem AURA_WHEAT = new DtDItem("aura_wheat", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final DtDItem REVE_BERRIES = new DtDItem("reve_berries", new Item(new FabricItemSettings().group(ItemGroup.FOOD)));

    private Map<String, DtDItem> pool;

    private DtDItemPool() {
        pool = new LinkedHashMap<String, DtDItem>();
    }
    
    public void add(DtDItem i) {
        pool.put(i.getName(), i);
    }

    public Item getItem(String name) {
        return Optional.ofNullable(pool.get(name)).map(i -> i.asItem()).orElse(Items.AIR);
    }

    public void register() {
        pool.values().forEach(i -> i.register());
    }

    public void datagen(ItemModelGenerator gen) {
        pool.values().forEach(i -> i.datagen(gen));
    }

    public static DtDItemPool createItemPool() {
        DtDItemPool itemPool = new DtDItemPool();

        itemPool.add(AURA_BREAD);
        itemPool.add(AURA_WHEAT);
        itemPool.add(REVE_BERRIES);

        return itemPool;
    }
}
