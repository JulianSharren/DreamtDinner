package net.jsharren.dreamt_dinner.resources.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jsharren.dreamt_dinner.blocks.DreamPotBlock;
import net.jsharren.dreamt_dinner.blocks.ReveCakeBlock;
import net.jsharren.dreamt_dinner.resources.item.DtDBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

public class DtDBlockPool {
    public static final DtDPlacedBlock DREAM_POT = new DtDPlacedBlock("dream_pot", DreamPotBlock.create());
    public static final DtDPlacedBlock REVE_CAKE = new DtDPlacedBlock("reve_cake", ReveCakeBlock.create());

    private Map<String, DtDBlock> pool;
    private Map<String, DtDBlockItem> itemPool;

    private DtDBlockPool() {
        pool = new LinkedHashMap<String, DtDBlock>();
        itemPool = new LinkedHashMap<String, DtDBlockItem>();
    }

    public void add(DtDBlock b) {
        pool.put(b.getName(), b);
    }

    public void addPlaced(DtDPlacedBlock b, Item.Settings settings, Boolean hasItemTexture) {
        DtDBlockItem bi = b.createBlockItem(settings, hasItemTexture);
        this.add(b);
        itemPool.put(bi.getName(), bi);
    }

    public void addPlaced(DtDPlacedBlock b, Item.Settings settings) {
        this.addPlaced(b, settings, false);
    }

    public Block getBlock(String name) {
        return Optional.ofNullable(pool.get(name)).map(b -> b.getBlock()).orElse(Blocks.AIR);
    }
    
    public Item getBlockItem(String name) {
        return Optional.ofNullable(itemPool.get(name)).map(i -> i.asItem()).orElse(Items.AIR);
    }
    
    public void register() {
        pool.values().forEach(b -> b.register());
        itemPool.values().forEach(bi -> bi.register());
    }

    public void datagen(BlockStateModelGenerator gen) {
        pool.values().forEach(b -> b.datagen(gen));
        itemPool.values().forEach(bi -> bi.datagen(gen));
    }

    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        pool.values().forEach(b -> b.accept(biConsumer));
    }

    public Block[] getEntitiedBlocks(Class<? extends Block> blockClass) {
        return (
            pool.values()
            .stream()
            .map(b -> b.getBlock())
            .filter(block -> (blockClass.isInstance(block) && (block instanceof BlockEntityProvider)))
            .toArray(Block[]::new)
        );
    }

    public static DtDBlockPool createBlockPool() {
        DtDBlockPool blockPool = new DtDBlockPool();
        blockPool.addPlaced(DREAM_POT, new FabricItemSettings().group(ItemGroup.MISC));
        blockPool.addPlaced(REVE_CAKE, new FabricItemSettings().group(ItemGroup.FOOD));

        return blockPool;
    }
}
