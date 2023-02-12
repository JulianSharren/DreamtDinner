package net.jsharren.dreamt_dinner.resources.item;

import net.jsharren.dreamt_dinner.utils.NameUitl;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.registry.Registry;

public class DtDItem implements ItemConvertible {
    protected String name;
    protected Item item;

    public String getName() {
        return name;
    }

    public Item asItem() {
        return item;
    }

    public DtDItem(String name, Item item) {
        this.name = name;
        this.item = item;
    }

    public void register() {
        Registry.register(Registry.ITEM, NameUitl.toID(name), item);
    }

    public void datagen(ItemModelGenerator gen) {
        gen.register(item, Models.GENERATED);
    }

}
