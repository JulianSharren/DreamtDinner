package net.jsharren.dreamt_dinner;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

public class DreamtDinnerDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(DtDModelGenerator::new);
        fabricDataGenerator.addProvider(DtDBlockLootTables::new);
    }


    private static class DtDModelGenerator extends FabricModelProvider {
        private DtDModelGenerator(FabricDataGenerator gen) {
            super(gen);
        }
     
        @Override
        public void generateBlockStateModels(BlockStateModelGenerator gen) {
            RESOURCE.blockPool.datagen(gen);
        }
     
        @Override
        public void generateItemModels(ItemModelGenerator gen) {
            RESOURCE.itemPool.datagen(gen);
        }
    }

    private static class DtDBlockLootTables extends SimpleFabricLootTableProvider {
        public DtDBlockLootTables(FabricDataGenerator dataGenerator) {
            super(dataGenerator, LootContextTypes.BLOCK);
        }
     
        @Override
        public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
            RESOURCE.blockPool.accept(biConsumer);
        }
    }    
}
