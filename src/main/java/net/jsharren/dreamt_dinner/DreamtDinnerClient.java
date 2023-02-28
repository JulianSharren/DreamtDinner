package net.jsharren.dreamt_dinner;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.jsharren.dreamt_dinner.blockentities.DreamPotBlockEntity;
import net.jsharren.dreamt_dinner.client.color.ReveCakeColorProvider;
import net.jsharren.dreamt_dinner.client.render.DreamPotBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class DreamtDinnerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(
            RESOURCE.blockEntityPool.getType(DreamPotBlockEntity.class),
            DreamPotBlockEntityRenderer::new
        );
        ColorProviderRegistry.BLOCK.register(
            ReveCakeColorProvider::getBlockColor,
            RESOURCE.blockPool.getBlock("reve_cake")
        );
        ColorProviderRegistry.ITEM.register(
            ReveCakeColorProvider::getItemColor,
            RESOURCE.blockPool.getBlockItem("reve_cake")
        );
    }
    
}
