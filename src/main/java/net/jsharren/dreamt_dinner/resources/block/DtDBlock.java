package net.jsharren.dreamt_dinner.resources.block;

import java.util.function.BiConsumer;

import net.jsharren.dreamt_dinner.utils.Namespace;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DtDBlock {
    protected String name;
    protected Block block;

    public String getName() {
        return name;
    }

    public Block getBlock() {
        return block;
    }

    public DtDBlock(String name, Block block) {
        this.name = name;
        this.block = block;
    }

    public void register() {
        Registry.register(Registry.BLOCK, Namespace.toID(name), block);
    }

    public void datagen(BlockStateModelGenerator gen) {
        gen.registerSimpleState(block);
    }

    public LootTable.Builder getLootTable() {
        return BlockLootTableGenerator.dropsNothing();
    }

    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        biConsumer.accept(Namespace.toID("blocks", name), getLootTable());
    }
}
