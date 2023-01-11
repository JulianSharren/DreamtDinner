package net.jsharren.dreamt_dinner.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.jsharren.dreamt_dinner.api.IDreamableEntity;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

@Mixin(CatEntity.class)
public abstract class DreamableCatMixin implements IDreamableEntity {
    private static LootTable CAT_LOOT = LootTable.builder().pool(
        LootPool.builder()
        .with(ItemEntry.builder(DtDItemPool.TUNA_FILLET))
        .rolls(UniformLootNumberProvider.create(1.0f, 2.0f))
    ).build();

    private CatEntity cast() {
        return (CatEntity)(Object)this;
    }

    @Override
    public LivingEntity getEntity() {
        return cast();
    }

    @Override
    public LootTable getDreamLoot() {
        return CAT_LOOT;
    }

    @Override
    public Boolean isDreaming() {
        return cast().isInSleepingPose();
    }
}
