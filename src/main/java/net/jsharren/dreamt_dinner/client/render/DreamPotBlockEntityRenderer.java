package net.jsharren.dreamt_dinner.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jsharren.dreamt_dinner.blockentities.DreamPotBlockEntity;
import net.jsharren.dreamt_dinner.mixin.client.BeaconBeamRenderer;
import net.jsharren.dreamt_dinner.utils.NameUitl;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class DreamPotBlockEntityRenderer implements BlockEntityRenderer<DreamPotBlockEntity> {
    public static final Identifier BEAM_TEXTURE = NameUitl.toID("textures", "entity", "dream_pot_beam.png");
    public static final float BEAM_RADIUS = 0.06f;
    public static final float BEAM_RADIAL_VELOCITY = 0.07f;
    public static final float BEAM_PHASE_VELOCITY = 0.09f;
    public static final float BEAM_SHIFT_VELOCITY = 0.16f;

    public static final float INVSQRT2 = 0.707106781f;
    
    public DreamPotBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(
        DreamPotBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertices, int light, int overlay
    ) {
        float progress = blockEntity.getProgress();
        if (progress < 0) {
            return;
        }

        Vec3d target = blockEntity.getTargetVec();
        float reducedTick = (blockEntity.getWorld().getTime() + tickDelta);

        float r = (float)target.length();
        int intR = (int)Math.ceil(r);

        if (intR <= 0) {
            return;
        }
        
        float shift = 1.0f - MathHelper.fractionalPart(reducedTick * BEAM_SHIFT_VELOCITY);
        float rotationAngle = (float)Math.acos(target.normalize().getY());
        Vec3f rotationAxis = new Vec3f((float)target.getZ(), 0.0f, -(float)target.getX());
        if ( !rotationAxis.normalize() ) {
            rotationAxis = Vec3f.POSITIVE_X;
        }

        matrices.push();

        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(rotationAxis.getRadialQuaternion(rotationAngle));
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(reducedTick * BEAM_RADIAL_VELOCITY));         
        matrices.scale(1.0f, r / intR, 1.0f);

        BeaconBeamRenderer.renderBeaconBeamLayer(
            matrices, vertices.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true)),
            MathHelper.lerp(MathHelper.square(progress), 0.25f, 1.0f),
            MathHelper.lerp(progress, 0.5f, 1.0f), 
            MathHelper.lerp(progress, 0.5f, 1.0f),
            0.7f + 0.25f * MathHelper.sin(reducedTick * BEAM_PHASE_VELOCITY),
            0, intR, 0, BEAM_RADIUS, BEAM_RADIUS, 0, -BEAM_RADIUS, 0, 0, -BEAM_RADIUS, 
            0.0f, 1.0f, shift, shift + r * INVSQRT2 / BEAM_RADIUS
        );
        
        matrices.pop();
    }

}
