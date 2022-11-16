package net.jsharren.dreamt_dinner.mixin;

import static net.jsharren.dreamt_dinner.DreamtDinner.RESOURCE;

import org.spongepowered.asm.mixin.Mixin;

import net.jsharren.dreamt_dinner.api.IDreamableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

@Mixin(FoxEntity.class)
public abstract class DreamableFoxMixin implements IDreamableEntity {
    private static LootTable FOX_LOOT = LootTable.builder().pool(
        LootPool.builder()
        .with(ItemEntry.builder(RESOURCE.itemPool.getItem("reve_berries")))
        .rolls(UniformLootNumberProvider.create(2.0f, 4.0f))
    ).build();

    private FoxEntity cast() {
        return (FoxEntity)(Object)this;
    }

    @Override
    public LivingEntity getEntity() {
        return cast();
    }

    @Override
    public Boolean isDreaming() {
        return cast().isSleeping();
    }

    @Override
    public Integer getDreamDuration() {
        return 1200;
    }

    @Override
    public LootTable getDreamLoot() {
        return FOX_LOOT;
    }
}
