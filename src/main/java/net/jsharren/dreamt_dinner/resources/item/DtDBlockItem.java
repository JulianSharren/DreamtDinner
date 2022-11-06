package net.jsharren.dreamt_dinner.resources.item;

import net.jsharren.dreamt_dinner.utils.Namespace;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.Item;

public class DtDBlockItem extends DtDItem {
    private Boolean hasTexture;

    public DtDBlockItem(String name, Item item, Boolean hasTexture) {
        super(name, item);
        this.hasTexture = hasTexture;
    }

    public void datagen(BlockStateModelGenerator gen) {
        if( hasTexture )  {
            gen.registerItemModel(item);
        } else {
            gen.registerParentedItemModel(item, Namespace.toID("block", name));
        }
    }
}
