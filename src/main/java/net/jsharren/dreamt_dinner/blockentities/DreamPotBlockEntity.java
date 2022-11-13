package net.jsharren.dreamt_dinner.blockentities;

import static net.jsharren.dreamt_dinner.DreamtDinner.LOGGER;
import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.MapMaker;

import net.jsharren.dreamt_dinner.api.IScheduler;
import net.jsharren.dreamt_dinner.impl.TimeOfDayScheduler;
import net.jsharren.dreamt_dinner.utils.MathUtil;
import net.jsharren.dreamt_dinner.utils.SerializeUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DreamPotBlockEntity extends BaseInvariantBlockEntity {
    private static final Integer INACTIVE_COOLDOWN = 100;
    private static final Integer SCHEDULE_OVERTIME = 20000;
    private static final Integer SCHEDULE_UNDERTIME = -200;
    private static final Integer LOCK_LAG = 5;

    private static final Double RANGE_H = 7.5;
    private static final Double RANGE_Y = 2.0;
    private static final Double UPDATE_POS_THRESHOLD = 0.025;

    private static final Integer NO_REACTION = 0;
    private static final Integer COMPLETE_REACTION = -1;
    
    private static ConcurrentMap<UUID, DreamPotBlockEntity> catalystMap = new MapMaker().concurrencyLevel(4).weakValues().makeMap();

    private static final String TAG_REACTANT_UUID = "reactantUUID";
    private static final String TAG_REACTANT_POS = "reactantPos";
    private static final String TAG_IS_ACTIVE = "isActive";
    private static final String TAG_ELAPSED = "elapsed";
    private static final String TAG_SCHEDULE_DURATION = "scheduleDuration";
    private static final String TAG_SCHEDULER_PARAM = "schedulerParam";

    private long clock;
    private Optional<UUID> reactantUUID;
    private Vec3d reactantPos;
    private Boolean isActive;
    private Integer elapsed;
    private Integer scheduleDuration;
    private Integer schedulerParam;

    public DreamPotBlockEntity(BlockPos pos, BlockState state) {
        super(RESOURCE.blockEntityPool.getType(DreamPotBlockEntity.class), pos, state);
        clock = MathUtil.initClock(pos);
        reactantUUID = Optional.empty();
        reactantPos = Vec3d.ZERO;
        isActive = false;
        elapsed = 0;
        scheduleDuration = NO_REACTION;
        schedulerParam = 0;
    }

    @Override
    public void markRemoved() {
        if ( hasWorld() && !getWorld().isClient() && reactantUUID.isPresent() ) {
            catalystMap.remove(reactantUUID.get(), this);
        }
        super.markRemoved();
    }

    @Override
    public void packNbt(NbtCompound nbt, Boolean toClient) {
        nbt.putBoolean(TAG_IS_ACTIVE, isActive);
        nbt.putInt(TAG_SCHEDULE_DURATION, scheduleDuration);
        nbt.putInt(TAG_SCHEDULER_PARAM, schedulerParam);
        nbt.putInt(TAG_ELAPSED, elapsed);
        if ( !toClient && reactantUUID.isPresent() ) {
            nbt.putUuid(TAG_REACTANT_UUID, reactantUUID.get());
            nbt.put(TAG_REACTANT_POS, SerializeUtil.toNbt(reactantPos));
        }
    }

    @Override
    public void unpackNbt(NbtCompound nbt) {
        isActive = nbt.getBoolean(TAG_IS_ACTIVE);
        scheduleDuration = nbt.getInt(TAG_SCHEDULE_DURATION);
        schedulerParam = nbt.getInt(TAG_SCHEDULER_PARAM);
        elapsed = nbt.getInt(TAG_ELAPSED);
        if( nbt.containsUuid(TAG_REACTANT_UUID) ) {
            reactantUUID = Optional.of(nbt.getUuid(TAG_REACTANT_UUID));
            NbtList nbtReactantPos = nbt.getList(TAG_REACTANT_POS, NbtCompound.DOUBLE_TYPE);
            if ( nbtReactantPos.size() == 3 ) {
                reactantPos = SerializeUtil.toVec3d(nbtReactantPos);
            }
        } else {
            reactantUUID = Optional.empty();
        }
    }

    public static void serverTick(ServerWorld world, BlockPos pos, BlockState state, DreamPotBlockEntity self) {
        if ( self.reactantUUID.isPresent() ) {
            UUID uUID = self.reactantUUID.get();
            Boolean shouldDeactivate = true;
            if ( world.getEntity(uUID) instanceof BoatEntity boatEntity ) {
                if ( self.isActive && self.scheduleDuration == NO_REACTION ) {
                    LOGGER.warn("Invalid time advancement");
                } else if ( self.isActive && self.scheduleDuration == COMPLETE_REACTION ) {
                    LOGGER.info("Complete Reaction");
                    // spawn Item
                } else if ( isValidReactant(self, pos, boatEntity) ) {
                    shouldDeactivate = false;
                }
            }

            if ( shouldDeactivate ) {
                catalystMap.remove(uUID, self);
            } else if ( self.isActive || self.elapsed > LOCK_LAG ) {
                DreamPotBlockEntity catalyst = catalystMap.putIfAbsent(uUID, self);
                shouldDeactivate = (catalyst != self) && (catalyst != null);
            } else {
                shouldDeactivate = catalystMap.getOrDefault(uUID, self) != self;
            }

            // When shouldDeactivate is true, catalystMap.get(uUID) != self must be also true
            if ( shouldDeactivate ) {
                self.predeactivate();
            }
        } else {
            self.isActive = false;
            if ( self.clock % 8 == 0 ) {
                Collection<UUID> uuids = new HashSet<UUID>(catalystMap.keySet());
                Optional<BoatEntity> reactantOptional = (
                    world.getEntitiesByClass(
                        BoatEntity.class,
                        Box.of(Vec3d.ofBottomCenter(pos), RANGE_H, RANGE_Y, RANGE_H), 
                        boatEntity -> isValidReactant(null, pos, boatEntity) && !uuids.contains(boatEntity.getUuid())
                    )
                    .stream().findFirst()
                );
                if ( reactantOptional.isPresent() ) {
                    self.preactivate(reactantOptional.get());
                }
            }
        }
        if ( self.reactantUUID.isEmpty() ) {
            if( self.isActive || self.elapsed > 0 ) {
                self.deactivate();
                self.markChanges();
            }
        } else if ( !self.isActive ) {
            ++self.elapsed;
            if ( self.elapsed >= INACTIVE_COOLDOWN && self.scheduleDuration > 0 ) {
                self.activate(world);
            }
            self.markChanges();
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, DreamPotBlockEntity self) {
        ++self.clock;

        if ( !world.isClient() && world instanceof ServerWorld serverWorld) {
            serverTick(serverWorld, pos, state, self);
        }
        
        if ( !self.isActive ) return;

        IScheduler<Integer> scheduler = new TimeOfDayScheduler(self.schedulerParam);
        self.elapsed = scheduler.getElapsed(world);
        if ( self.elapsed > self.scheduleDuration + SCHEDULE_OVERTIME || self.elapsed < SCHEDULE_UNDERTIME ) {
            self.scheduleDuration = NO_REACTION;
        } else if ( self.elapsed >= self.scheduleDuration ) {
            self.scheduleDuration = COMPLETE_REACTION;
        }
        self.markChanges();
    }

    private static Boolean isValidReactant(@Nullable DreamPotBlockEntity self, BlockPos pos, BoatEntity boatEntity) {
        if( !boatEntity.isAlive() ) return false;
        
        Vec3d newPos = boatEntity.getPos();
        if ( self != null && newPos.distanceTo(self.reactantPos) >= UPDATE_POS_THRESHOLD ) {
            self.reactantPos = newPos;
            self.markSync();
        }
        
        Vec3d relPos = Vec3d.ofBottomCenter(pos).relativize(newPos);
        return Math.abs(relPos.y) <= RANGE_Y && relPos.horizontalLength() <= RANGE_H;
    }

    private final void preactivate(BoatEntity reactant) {
        reactantUUID = Optional.of(reactant.getUuid());
        reactantPos = reactant.getPos();
        scheduleDuration = 6000;
        LOGGER.info("PreActivated");
    }

    private final void predeactivate() {
        reactantUUID = Optional.empty();
        LOGGER.info("PreDeactivated");
    }

    private final void activate(World world) {
        IScheduler<Integer> scheduler = TimeOfDayScheduler.init(world);
        isActive = true;
        schedulerParam = scheduler.getParam();
        elapsed = scheduler.getElapsed(world);
        markSync();
        LOGGER.info("Activated");
    }

    private final void deactivate() {
        isActive = false;
        elapsed = 0;
        scheduleDuration = NO_REACTION;
        schedulerParam = 0;
        markSync();
        LOGGER.info("Deactivated");
    }
}
