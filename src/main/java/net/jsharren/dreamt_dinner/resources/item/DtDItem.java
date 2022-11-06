package net.jsharren.dreamt_dinner.resources.item;

import net.jsharren.dreamt_dinner.utils.Namespace;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class DtDItem {
    public String name;
    public Item item;

    public DtDItem(String name, Item item) {
        this.name = name;
        this.item = item;
    }

    public void register() {
        Registry.register(Registry.ITEM, Namespace.toID(name), item);
    }

    public void datagen(ItemModelGenerator gen) {
        gen.register(item, Models.GENERATED);
    }

}
