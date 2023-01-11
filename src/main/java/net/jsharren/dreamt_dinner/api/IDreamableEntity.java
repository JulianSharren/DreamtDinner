package net.jsharren.dreamt_dinner.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.LootTable;

public interface IDreamableEntity {
    
    default LivingEntity getEntity() {
        return (LivingEntity)this;
    }

    default Boolean isDreaming() {
        return false;
    }
    
    default Integer getDreamDuration() {
        return 1200;
    }

    default LootTable getDreamLoot() {
        return LootTable.EMPTY;
    }
}
