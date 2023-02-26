package net.jsharren.dreamt_dinner.blocks;

import java.util.stream.Stream;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jsharren.dreamt_dinner.resources.block.DtDPlacedBlock;
import net.jsharren.dreamt_dinner.utils.NameUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;


public class ReveCakeBlock extends Block {
    public static final IntProperty PIECES = IntProperty.of("pieces", 1, 4);

    protected static final VoxelShape[] REVE_CAKE_SHAPES = {
        Block.createCuboidShape(5, 0, 5, 11, 6, 11),
        Block.createCuboidShape(1, 0, 3, 15, 6, 13),
        Block.createCuboidShape(1, 0, 1, 15, 6, 15),
        Block.createCuboidShape(1, 0, 1, 15, 6, 15)
    };

    public ReveCakeBlock(Settings settings){
        super(settings);
        setDefaultState(getDefaultState().with(PIECES, 1));
    }

    public static ReveCakeBlock create() {
        return new ReveCakeBlock(
            FabricBlockSettings.of(Material.CAKE, MapColor.MAGENTA)
            .hardness(0.2f).nonOpaque().collidable(false)
        );
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(PIECES);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return REVE_CAKE_SHAPES[state.get(PIECES) - 1];
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int pieces = state.get(PIECES);
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (Block.getBlockFromItem(item) instanceof ReveCakeBlock) {
            if (pieces >= 4) {
                return ActionResult.PASS;
            }
            if (!player.isCreative()) {
                itemStack.decrement(1);
            }
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.setBlockState(pos, state.with(PIECES, pieces + 1));
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            return ActionResult.SUCCESS;
        }
        // Eat()
        world.playSound(player, pos, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (pieces <= 1) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, state.with(PIECES, pieces - 1));
        }
        return ActionResult.SUCCESS;
    }

    public static class ResourceBlock extends DtDPlacedBlock {
        public ResourceBlock() {
            super("reve_cake", ReveCakeBlock.create());
            removeDrop();
        }

        @Override
        public LootTable.Builder getLootTable() {
            Stream<LootPool> lootPoolStream = (
                PIECES.getValues().stream().map(
                    pieces -> LootPool.builder()
                    .conditionally(
                        BlockStatePropertyLootCondition.builder(block)
                        .properties(StatePredicate.Builder.create().exactMatch(PIECES, pieces))
                    )
                    .with(ItemEntry.builder(block))
                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create((float)pieces)))
                    .build()
                )   
            );

            LootTable.Builder builder = LootTable.builder();
            lootPoolStream.sequential().forEach(builder::pool);

            return BlockLootTableGenerator.applyExplosionDecay(block, builder);
        }

        @Override
        public void datagen(BlockStateModelGenerator gen) {
            BlockStateVariantMap variantMap = BlockStateVariantMap.create(PIECES).register(
                i -> BlockStateVariant.create().put(
                    VariantSettings.MODEL, NameUtil.toID(
                        "block", (i == 1) ? name : name + String.format("_%d", i)
                    )
                )
            );

            gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        }

        
    }
}
