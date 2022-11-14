package net.jsharren.dreamt_dinner.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.jsharren.dreamt_dinner.api.IDreamableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.village.VillagerProfession;

@Mixin(VillagerEntity.class)
public class DreamableVillagerMixin implements IDreamableEntity {
    private static LootTable.Builder baseBuilder() {
        return LootTable.builder().pool(
            LootPool.builder()
            .with(ItemEntry.builder(Items.WHEAT_SEEDS))
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
                .with(ItemEntry.builder(Items.WHEAT))
                .rolls(ConstantLootNumberProvider.create(2.0f))
            );
        } else if ( profession == VillagerProfession.FISHERMAN ) {
            builder.pool(
                LootPool.builder()
                .with(ItemEntry.builder(Items.SALMON))
                .rolls(ConstantLootNumberProvider.create(1.0f))
            );
        }

        return builder.build();
    }

}
