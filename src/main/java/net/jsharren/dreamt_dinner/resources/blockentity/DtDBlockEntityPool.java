package net.jsharren.dreamt_dinner.resources.blockentity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.jsharren.dreamt_dinner.blockentities.DreamPotBlockEntity;
import net.jsharren.dreamt_dinner.blocks.DreamPotBlock;
import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;

public final class DtDBlockEntityPool {
    private Map<String, DtDBlockEntity<?> > blockEntites;
    private DtDBlockPool blockPool;
    
    private DtDBlockEntityPool(DtDBlockPool blockPool) {
        blockEntites = new LinkedHashMap<String, DtDBlockEntity<?> >();
        this.blockPool = blockPool;
    }

    public <T extends BlockEntity, B extends Block> void add(
        String name,
        Class<B> blockClass,
        Class<T> cls,
        FabricBlockEntityTypeBuilder.Factory<T> factory,
        @Nullable BlockEntityTicker<T> ticker
    ) {
        String classKey = cls.getName();
        if( blockEntites.containsKey(classKey) ) {
            throw new RuntimeException("Duplicate BlockEntity was for " + classKey);
        }
        blockEntites.put(
            classKey,
            new DtDBlockEntity<T>(
                name, factory, ticker,
                FabricBlockEntityTypeBuilder.create(factory, blockPool.getEntitiedBlocks(blockClass)).build()
            )
        );
    }

    public void register() {
        blockEntites.values().forEach(be -> be.register());
    }

    @SuppressWarnings("unchecked")
    private <T extends BlockEntity> Optional<DtDBlockEntity<T> > getBlockEntity(Class<T> cls) {
        DtDBlockEntity<?> be = blockEntites.get(cls.getName());
        return Optional.ofNullable((DtDBlockEntity<T>)be);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityType<T> getType(Class<T> cls) {
        Optional<DtDBlockEntity<T> > beOptional = getBlockEntity(cls);
        return beOptional.isPresent() ? beOptional.get().getType() : null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BlockEntity, U extends BlockEntity> BlockEntityTicker<U> getTicker(BlockEntityType<U> givenType, Class<T> cls) {
        Optional<DtDBlockEntity<T> > beOptional = getBlockEntity(cls);
        return (beOptional.isPresent() && beOptional.get().getType() == givenType) ?
            (BlockEntityTicker<U>)(beOptional.get().getTicker()) : null;
    }

    public static DtDBlockEntityPool create(DtDBlockPool blockPool) {
        DtDBlockEntityPool blockEntityPool = new DtDBlockEntityPool(blockPool);
        blockEntityPool.add(
            "dream_pot_entity", DreamPotBlock.class, 
            DreamPotBlockEntity.class, DreamPotBlockEntity::new, DreamPotBlockEntity::tick
        );

        return blockEntityPool;
    }
}
