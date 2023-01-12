package net.jsharren.dreamt_dinner.resources.advancement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.advancement.Advancement;

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

        return pool;
    }
}
