package net.jsharren.dreamt_dinner.resources.blockentity;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.jsharren.dreamt_dinner.utils.Namespace;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class DtDBlockEntity <T extends BlockEntity> {
    public String name;
    private FabricBlockEntityTypeBuilder.Factory<T> factory;
    @Nullable private BlockEntityTicker<T> ticker;
    private BlockEntityType<T> bet;


    public BlockEntityType<T> getType() {
        return bet;
    }

    public FabricBlockEntityTypeBuilder.Factory<T> getFactory() {
        return factory;
    }

    @Nullable
    public BlockEntityTicker<T> getTicker() {
        return ticker;
    }

    public DtDBlockEntity(
        String name,
        FabricBlockEntityTypeBuilder.Factory<T> factory,
        @Nullable BlockEntityTicker<T> ticker,
        BlockEntityType<T> bet
    ) {
        this.name = name;
        this.factory = factory;
        this.ticker = ticker;
        this.bet = bet;
    }

    public void register() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Namespace.toID(name), bet);
    }

    // @SuppressWarnings("unchecked")
    // @Nullable
    // public <T1 extends BlockEntity> DtDBlockEntity<T1> matchingType(Class<T1> cls) {
    //     return (cls.getName() == this.cls.getName()) ? (DtDBlockEntity<T1>)this : null;
    // }

    // private static class Factory <T extends BlockEntity> {
    //     private final Constructor<T> CTOR;

    //     public Factory(Class<T> cls) {
    //         try {
    //             CTOR = cls.getDeclaredConstructor(BlockPos.class, BlockState.class);
    //             if( !CTOR.canAccess(null) ) {
    //                 throw new IllegalAccessException("Required constructor is inaccessible");
    //             }
    //         } catch (ReflectiveOperationException exc) {
    //             throw new RuntimeException(exc);
    //         }
    //     }

    //     public FabricBlockEntityTypeBuilder.Factory<T> make() {
    //         return (pos, state) -> {
    //             try {
    //                 return CTOR.newInstance(pos, state);
    //             } catch (ReflectiveOperationException exc) {
    //                 throw new RuntimeException(exc);
    //             }
    //         };
    //     }
    // }

    // private static class Ticker <T extends BlockEntity> {
    //     private final Method METHOD;

    //     public Ticker(Class<T> cls) {
    //         try {
    //             METHOD = cls.getDeclaredMethod("tick", World.class, BlockPos.class, BlockState.class, cls);
    //             if( !METHOD.canAccess(null) ) {
    //                 throw new IllegalAccessException("Required static method is inaccessible");
    //             }
    //         } catch (ReflectiveOperationException exc) {
    //             throw new RuntimeException(exc);
    //         }
    //     }

    //     public BlockEntityTicker<T> make() {
    //         return (world, pos, state, blockEntity) -> {
    //             try {
    //                 METHOD.invoke(null, world, pos, state, blockEntity);
    //             } catch (ReflectiveOperationException exc) {
    //                 throw new RuntimeException(exc);
    //             }
    //         };
    //     }
    // }
}
