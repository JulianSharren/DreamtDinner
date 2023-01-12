package net.jsharren.dreamt_dinner.resources.advancement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.jsharren.dreamt_dinner.resources.block.DtDBlockPool;
import net.jsharren.dreamt_dinner.resources.item.DtDItemPool;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
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
        
        DtDAdvancement villagerAdvancement = rootAdvancement.createChild(
            "villager_dream",
            Advancement.Builder.create().criterion(
                "get_aura_wheat_from_dream_pot", InventoryChangedCriterion.Conditions.items(DtDItemPool.AURA_WHEAT)
            )
        ).display(DtDItemPool.AURA_WHEAT, AdvancementFrame.TASK);
        pool.add(rootAdvancement);
        pool.add(villagerAdvancement);

        return pool;
    }
}
