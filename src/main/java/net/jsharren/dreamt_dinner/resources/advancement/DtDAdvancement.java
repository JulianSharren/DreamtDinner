package net.jsharren.dreamt_dinner.resources.advancement;

import java.util.function.Consumer;

import net.jsharren.dreamt_dinner.utils.Namespace;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.Advancement.Builder;

public class DtDAdvancement {
    protected final String name;
    protected final Advancement.Builder builder;

    public DtDAdvancement(String name, Builder builder) {
        this.name = name;
        this.builder = builder;
    }

    public void accept(Consumer<Advancement> exporter) {
        exporter.accept(this.builder.build(Namespace.toID(this.name)));
    }
}
