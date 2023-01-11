package net.jsharren.dreamt_dinner.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
@Mixin(BeaconBlockEntityRenderer.class)
public interface BeaconBeamRenderer {
    @Invoker("renderBeamLayer")
    public static void renderBeaconBeamLayer(
        MatrixStack matrices, VertexConsumer vertices,
        float red, float green, float blue, float alpha,
        int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
        float u1, float u2, float v1, float v2
    ) {
        throw new AssertionError();
    }
}
