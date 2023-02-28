package net.jsharren.dreamt_dinner;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.jsharren.dreamt_dinner.resources.DtDDataGenResource;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

public class DreamtDinnerDataGen implements DataGeneratorEntrypoint {
    public static final DtDDataGenResource DATAGEN_RESOURCE = DtDDataGenResource.createResource();

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(DtDModelGenerator::new);
        fabricDataGenerator.addProvider(DtDBlockLootTables::new);
        fabricDataGenerator.addProvider(DtDRecipeGenerator::new);
        fabricDataGenerator.addProvider(DtDAdvancements::new);
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

    private static class DtDRecipeGenerator extends FabricRecipeProvider {
        public DtDRecipeGenerator(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
            DATAGEN_RESOURCE.recipePool.accept(exporter);
        }
    }

    private static class DtDAdvancements extends FabricAdvancementProvider {
        public DtDAdvancements(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        public void generateAdvancement(Consumer<Advancement> exporter) {
            DATAGEN_RESOURCE.advancementPool.accept(exporter);
        }
    }
}
