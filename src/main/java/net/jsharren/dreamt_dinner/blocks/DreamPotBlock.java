package net.jsharren.dreamt_dinner.blocks;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jsharren.dreamt_dinner.blockentities.DreamPotBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DreamPotBlock extends Block implements BlockEntityProvider {
    protected static final VoxelShape DREAM_POT_SHAPE = VoxelShapes.combineAndSimplify(
        VoxelShapes.fullCube(),
        Block.createCuboidShape(2, 2, 2, 14, 16, 14),
        BooleanBiFunction.ONLY_FIRST
    );

    public DreamPotBlock(Settings settings) {
        super(settings);
    }

    public static DreamPotBlock create() {
        return new DreamPotBlock(
            FabricBlockSettings.of(Material.STONE, MapColor.LIGHT_BLUE_GRAY)
            .hardness(3).requiresTool()
            .nonOpaque()
        );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DREAM_POT_SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DreamPotBlockEntity(pos, state);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
    
    @Override
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(
        World world, BlockState state, BlockEntityType<U> type
    ) {
        return RESOURCE.blockEntityPool.getTicker(type, DreamPotBlockEntity.class);
    }

    




}
