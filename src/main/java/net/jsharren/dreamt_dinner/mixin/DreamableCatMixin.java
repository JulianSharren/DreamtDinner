package net.jsharren.dreamt_dinner.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.jsharren.dreamt_dinner.api.IDreamableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

@Mixin(CatEntity.class)
public class DreamableCatMixin implements IDreamableEntity {
    private static LootTable CAT_LOOT = LootTable.builder().pool(
        LootPool.builder()
        .with(ItemEntry.builder(Items.SALMON))
        .rolls(ConstantLootNumberProvider.create(2.0f))
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
