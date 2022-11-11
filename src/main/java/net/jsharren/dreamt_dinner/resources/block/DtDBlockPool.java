package net.jsharren.dreamt_dinner.resources.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jsharren.dreamt_dinner.blocks.DreamPotBlock;
import net.jsharren.dreamt_dinner.resources.item.DtDBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

public class DtDBlockPool {
    private List<DtDBlock> pool;
    private List<DtDBlockItem> itemPool;

    private DtDBlockPool() {
        pool = new ArrayList<DtDBlock>();
        itemPool = new ArrayList<DtDBlockItem>();
    }

    public void add(DtDBlock b) {
        pool.add(b);
    }

    public void addPlaced(DtDPlacedBlock b, Item.Settings settings, Boolean hasItemTexture) {
        this.add(b);
        itemPool.add(b.createBlockItem(settings, hasItemTexture));
    }

    public void addPlaced(DtDPlacedBlock b, Item.Settings settings) {
        this.addPlaced(b, settings, false);
    }

    public void register() {
        pool.forEach(b -> b.register());
        itemPool.forEach(bi -> bi.register());
    }

    public void datagen(BlockStateModelGenerator gen) {
        pool.forEach(b -> b.datagen(gen));
        itemPool.forEach(bi -> bi.datagen(gen));
    }

    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        pool.forEach(b -> b.accept(biConsumer));
    }

    public Block[] getEntitiedBlocks(Class<? extends Block> blockClass) {
        return (
            pool.stream()
            .map(b -> b.block)
            .filter(block -> (blockClass.isInstance(block) && (block instanceof BlockEntityProvider)))
            .toArray(Block[]::new)
        );
    }

    public static DtDBlockPool createBlockPool() {
        DtDBlockPool blockPool = new DtDBlockPool();
        blockPool.addPlaced(
            new DtDPlacedBlock("dream_pot", DreamPotBlock.create()),
            new FabricItemSettings().group(ItemGroup.MISC)
        );

        return blockPool;
    }
}
