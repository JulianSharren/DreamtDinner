package net.jsharren.dreamt_dinner;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.jsharren.dreamt_dinner.blockentities.DreamPotBlockEntity;
import net.jsharren.dreamt_dinner.client.render.DreamPotBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class DreamtDinnerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(
            RESOURCE.blockEntityPool.getType(DreamPotBlockEntity.class),
            DreamPotBlockEntityRenderer::new
        );
    }
    
}
