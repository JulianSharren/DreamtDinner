package net.jsharren.dreamt_dinner.resources.block;

import net.jsharren.dreamt_dinner.resources.item.DtDBlockItem;
import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;

public class DtDPlacedBlock extends DtDBlock {
    private Boolean hasDrop;

    public DtDPlacedBlock(String name, Block block) {
        super(name, block);
        hasDrop = true;
    }

    public DtDBlockItem createBlockItem(Item.Settings itemSettings, Boolean hasItemTexture) {
        return new DtDBlockItem(name,  new BlockItem(block, itemSettings), hasItemTexture);
    }

    public DtDPlacedBlock removeDrop() {
        hasDrop = false;
        return this;
    }

    public LootTable.Builder getLootTable() {
        if( hasDrop ) {
            return BlockLootTableGenerator.drops(block);
        }
        return BlockLootTableGenerator.dropsNothing();
    }

}
