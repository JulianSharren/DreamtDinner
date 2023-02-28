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

import net.jsharren.dreamt_dinner.api.IDreamableEntity;
import net.jsharren.dreamt_dinner.api.IScheduler;
import net.jsharren.dreamt_dinner.impl.TimeOfDayScheduler;
import net.jsharren.dreamt_dinner.utils.MathUtil;
import net.jsharren.dreamt_dinner.utils.SerializeUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
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
        if ( toClient && !isActive ) {
            return;
        }        
        nbt.putInt(TAG_SCHEDULE_DURATION, scheduleDuration);
        nbt.putInt(TAG_SCHEDULER_PARAM, schedulerParam);
        nbt.putInt(TAG_ELAPSED, elapsed);
        if ( reactantUUID.isPresent() ) {
            if ( !toClient ) {
                nbt.putUuid(TAG_REACTANT_UUID, reactantUUID.get());
            }
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
        } else {
            reactantUUID = Optional.empty();
        }
        NbtList nbtReactantPos = nbt.getList(TAG_REACTANT_POS, NbtCompound.DOUBLE_TYPE);
        if ( nbtReactantPos.size() == 3 ) {
            reactantPos = SerializeUtil.toVec3d(nbtReactantPos);
        }
    }

    public static void serverTick(ServerWorld world, BlockPos pos, BlockState state, DreamPotBlockEntity self) {
        if ( self.reactantUUID.isPresent() ) {
            UUID uUID = self.reactantUUID.get();
            Boolean shouldDeactivate = true;
            @Nullable IDreamableEntity dreamable = reactantFilter(world.getEntity(uUID), pos);

            if ( dreamable != null ) {
                if ( self.isActive && self.scheduleDuration == NO_REACTION ) {
                    LOGGER.info("Invalid time advancement");
                } else if ( self.isActive && self.scheduleDuration == COMPLETE_REACTION ) {
                    LOGGER.debug("Complete Reaction");
                    dreamable.getDreamLoot().generateLoot(lootContext(world)).forEach(stack -> self.spawnItems(stack, world));
                    world.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 1.0f, 0.5f);
                } else {
                    tryUpdatePos(self, dreamable.getEntity().getPos());
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
                Optional<IDreamableEntity> dreamableOptional = (
                    world.getEntitiesByClass(
                        LivingEntity.class,
                        Box.of(Vec3d.ofBottomCenter(pos), RANGE_H * 2, RANGE_Y * 2, RANGE_H * 2), 
                        entity -> !uuids.contains(entity.getUuid())
                    )
                    .stream()
                    .map(entity -> reactantFilter(entity, pos))
                    .filter(dreamable -> dreamable != null)
                    .findFirst()
                );
                if ( dreamableOptional.isPresent() ) {
                    self.preactivate(dreamableOptional.get());
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

    @Nullable
    private static IDreamableEntity reactantFilter(Entity entity, BlockPos pos) {
        if ( entity.isAlive() && entity instanceof IDreamableEntity dreamable && dreamable.isDreaming() ) {
            Vec3d relPos = Vec3d.ofBottomCenter(pos).relativize(entity.getPos());
            if( Math.abs(relPos.getY()) <= RANGE_Y && relPos.horizontalLength() <= RANGE_H ){
                return dreamable;
            }
        }
        return null;
    }

    private static void tryUpdatePos(DreamPotBlockEntity self, Vec3d newPos) {
        if ( newPos.distanceTo(self.reactantPos) >= UPDATE_POS_THRESHOLD ) {
            self.reactantPos = newPos;
            self.markSync();
        }
    }

    private void spawnItems(ItemStack stack, ServerWorld world) {
        if ( stack.isEmpty() ) return;

        BlockPos pos = this.getPos();
        float x = pos.getX() + 0.5f + 0.2f * (world.getRandom().nextFloat() - 0.5f);
        float y = pos.getY() + 0.5f + 0.2f * (world.getRandom().nextFloat() - 0.5f);
        float z = pos.getZ() + 0.5f + 0.2f * (world.getRandom().nextFloat() - 0.5f);
        ItemEntity entity = new ItemEntity(world, x, y, z, stack);

        entity.setThrower(this.reactantUUID.orElse(null));
        world.spawnEntity(entity);
    }

    private final void preactivate(IDreamableEntity dreamable) {
        reactantUUID = Optional.of(dreamable.getEntity().getUuid());
        reactantPos = dreamable.getEntity().getPos();
        scheduleDuration = dreamable.getDreamDuration();            
        LOGGER.debug("PreActivated");
    }

    private final void predeactivate() {
        reactantUUID = Optional.empty();
        LOGGER.debug("PreDeactivated");
    }

    private final void activate(World world) {
        IScheduler<Integer> scheduler = TimeOfDayScheduler.init(world);
        isActive = true;
        schedulerParam = scheduler.getParam();
        elapsed = scheduler.getElapsed(world);
        markSync();
        LOGGER.debug("Activated");
    }

    private final void deactivate() {
        isActive = false;
        elapsed = 0;
        scheduleDuration = NO_REACTION;
        schedulerParam = 0;
        markSync();
        LOGGER.debug("Deactivated");
    }

    private static LootContext lootContext(ServerWorld world) {
        return new LootContext.Builder(world).luck(0.0f).parameter(LootContextParameters.ORIGIN, Vec3d.ZERO).build(LootContextTypes.COMMAND);
    }

    public float getProgress() {
        if ( isActive && scheduleDuration > 0 ) {
            return MathHelper.clamp((float)elapsed / scheduleDuration, 0.0f, 1.0f);
        }
        return -1.0f;
    }

    public Vec3d getTargetVec() {
        return Vec3d.ofCenter(getPos()).relativize(reactantPos);
    }
}
