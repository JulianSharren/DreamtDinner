package net.jsharren.dreamt_dinner.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class DreamPotBlock extends Block {
    protected static final VoxelShape DREAM_POT_SHAPE = VoxelShapes.combineAndSimplify(
        VoxelShapes.fullCube(),
        Block.createCuboidShape(2, 2, 2, 14, 16, 14),
        BooleanBiFunction.ONLY_FIRST
    );

    public DreamPotBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DREAM_POT_SHAPE;
    }

    public static DreamPotBlock create() {
        return new DreamPotBlock(
            FabricBlockSettings.of(Material.STONE, MapColor.LIGHT_BLUE_GRAY)
            .hardness(3).requiresTool()
            .nonOpaque()
        );
    }
}
