package net.jsharren.dreamt_dinner.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.jsharren.dreamt_dinner.api.IDreamableEntity;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.village.VillagerProfession;

@Mixin(VillagerEntity.class)
public abstract class DreamableVillagerMixin implements IDreamableEntity {
    private static LootTable.Builder baseBuilder() {
        return LootTable.builder().pool(
            LootPool.builder()
            .with(ItemEntry.builder(DtDItemPool.AURA_BREAD))
            .rolls(UniformLootNumberProvider.create(3.0f, 6.0f))
        );
    }

    private VillagerEntity cast() {
        return ((VillagerEntity)(Object)this);
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
    public LootTable getDreamLoot() {
        LootTable.Builder builder = baseBuilder();
        VillagerProfession profession = cast().getVillagerData().getProfession();
        if ( profession == VillagerProfession.FARMER ) {
            builder.pool(
                LootPool.builder()
                .with(ItemEntry.builder(DtDItemPool.AURA_WHEAT))
                .rolls(ConstantLootNumberProvider.create(2.0f))
            );
        } else if ( profession == VillagerProfession.FISHERMAN ) {
            builder.pool(
                LootPool.builder()
                .with(ItemEntry.builder(DtDItemPool.TUNA_FILLET))
                .rolls(ConstantLootNumberProvider.create(1.0f))
            );
        }

        return builder.build();
    }

}
