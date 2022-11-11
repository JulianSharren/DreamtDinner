package net.jsharren.dreamt_dinner.blockentities;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DreamPotBlockEntity extends BlockEntity {
    private static final Integer MAX_COOLDOWN = 100;

    private static ConcurrentMap<UUID, DreamPotBlockEntity> catalystMap = new MapMaker().concurrencyLevel(4).weakValues().makeMap();
    private Boolean isActive;
    private Vec3d reactantPosition;
    private Optional<UUID> reactantEntity;
    private Integer durationOrCooldown;
    private Integer schedulerParam;

    public DreamPotBlockEntity(BlockPos pos, BlockState state) {
        super(RESOURCE.blockEntityPool.getType(DreamPotBlockEntity.class), pos, state);
        isActive = false;
        reactantEntity = Optional.empty();
        reactantPosition = Vec3d.ZERO;
        durationOrCooldown = MAX_COOLDOWN;
        schedulerParam = 0;
    }

    public static void serverTick(ServerWorld world, BlockPos pos, BlockState state, DreamPotBlockEntity blockEntity) {
        // serverTick
    }
    public static void tick(World world, BlockPos pos, BlockState state, DreamPotBlockEntity blockEntity) {
        if ( !world.isClient() && world instanceof ServerWorld) {
            serverTick((ServerWorld)world, pos, state, blockEntity);
        }
    }
}

