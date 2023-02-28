package net.jsharren.dreamt_dinner.resources.advancement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.ThrownItemPickedUpByEntityCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;

public class DtDAdvancementPool {
    private final List<DtDAdvancement> advancements = new ArrayList<DtDAdvancement>();

    private DtDAdvancementPool() {}

    public void add(DtDAdvancement adv) {
        advancements.add(adv);
    }

    public void accept(Consumer<Advancement> exporter) {
        advancements.forEach(adv -> adv.accept(exporter));
    }

    public static DtDAdvancementPool createAdvancementPool() {
        DtDAdvancementPool pool = new DtDAdvancementPool();

        DtDAdvancement rootAdvancement = DtDAdvancement.createRoot(
            "root",
            new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
            Advancement.Builder.create().criterion("slept_in_bed", TickCriterion.Conditions.createSleptInBed())
        ).display(DtDBlockPool.DREAM_POT, AdvancementFrame.TASK);
        
        DtDAdvancement oneDreamFoodAdvancement = rootAdvancement.createChild(
            "one_dream_food",
            ConditionUtil.createDreamPotCriteriaBuilder().criteriaMerger(CriterionMerger.OR)
        ).display(DtDItemPool.AURA_WHEAT, AdvancementFrame.TASK);

        DtDAdvancement allDreamFoodAdvancement = oneDreamFoodAdvancement.createChild(
            "all_dream_food",
            ConditionUtil.createDreamPotCriteriaBuilder().criteriaMerger(CriterionMerger.AND)
        ).display(DtDItemPool.AURA_BREAD, AdvancementFrame.CHALLENGE);

        pool.add(rootAdvancement);
        pool.add(oneDreamFoodAdvancement);
        pool.add(allDreamFoodAdvancement);
        
        return pool;
    }

    public static class ConditionUtil {
        private static CriterionConditions createThrownItemCondition(ItemConvertible item, EntityType<?> entityType) {
            return ThrownItemPickedUpByEntityCriterion.Conditions.createThrownItemPickedUpByPlayer(
                EntityPredicate.Extended.EMPTY,
                ItemPredicate.Builder.create().items(item).build(), 
                EntityPredicate.Extended.ofLegacy(EntityPredicate.Builder.create().type(entityType).build())
            );
        }

        public static Advancement.Builder createDreamPotCriteriaBuilder() {
            return (
                Advancement.Builder.create()
                .criterion("pick_up_aura_wheat", createThrownItemCondition(DtDItemPool.AURA_WHEAT, EntityType.VILLAGER))
                .criterion("pick_up_reve_berries", createThrownItemCondition(DtDItemPool.REVE_BERRIES, EntityType.FOX))
                .criterion("pick_up_tuna_fillet", createThrownItemCondition(DtDItemPool.TUNA_FILLET, EntityType.CAT))
            );
        }
    }

    
}
