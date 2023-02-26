package net.jsharren.dreamt_dinner.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;


public class ReveCakeBlock extends Block {
    protected static final VoxelShape[] REVE_CAKE_SHAPES = {
        Block.createCuboidShape(5, 0, 5, 11, 6, 11)
    };

    public ReveCakeBlock(Settings settings){
        super(settings);
    }

    public static ReveCakeBlock create() {
        return new ReveCakeBlock(
            FabricBlockSettings.of(Material.CAKE, MapColor.MAGENTA)
            .hardness(0.2f).nonOpaque().collidable(false)
        );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return REVE_CAKE_SHAPES[0];
    }

}
