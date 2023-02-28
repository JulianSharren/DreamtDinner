package net.jsharren.dreamt_dinner.resources.advancement;

import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.jsharren.dreamt_dinner.utils.NameUitl;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.Advancement.Builder;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DtDAdvancement {
    public final String name;
    private final Advancement.Builder builder;
    private final Optional<Identifier> bgOptional;
    private final Optional<DtDAdvancement> parentOptional;
    private Text titleText;
    private Text descriptionText;
    private boolean showToast;
    private boolean annouceToChat;
    private boolean hidden;
    private Optional<Advancement> advOptional;

    protected DtDAdvancement(String name, @Nullable DtDAdvancement parent, @Nullable Identifier background, Builder builder) {
        this.name = name;
        this.builder = builder;
        bgOptional = Optional.ofNullable(background);
        parentOptional = Optional.ofNullable(parent);
        advOptional = Optional.empty();
        titleText = Text.translatable(NameUitl.toKey("advancements", name, "title"));
        descriptionText = Text.translatable(NameUitl.toKey("advancements", name, "description"));
        showToast = parent != null;
        annouceToChat = parent != null;
        hidden = false;
    }

    public static DtDAdvancement createRoot(String name, @Nullable Identifier background, Builder builder) {
        return new DtDAdvancement(name, null, background, builder);
    }

    public DtDAdvancement createChild(String name, Builder builder) {
        return new DtDAdvancement(name, this, null, builder);
    }

    public DtDAdvancement titleArgs(Object... args) {
        titleText = Text.translatable(NameUitl.toKey("advancements", name, "title"), args);
        return this;
    }

    public DtDAdvancement descriptionArgs(Object... args) {
        descriptionText = Text.translatable(NameUitl.toKey("advancements", name, "description"), args);
        return this;
    }

    public DtDAdvancement visibility(boolean showToast, boolean annouceToChat, boolean hidden) {
        this.showToast = showToast;
        this.annouceToChat = annouceToChat;
        this.hidden = hidden;
        return this;
    }

    public DtDAdvancement display(ItemStack icon, AdvancementFrame frame) {
        builder.display(icon, titleText, descriptionText, bgOptional.orElse(null), frame, showToast, annouceToChat, hidden);
        return this;
    }

    public DtDAdvancement display(ItemConvertible icon, AdvancementFrame frame) {
        return this.display(new ItemStack(icon), frame);
    }

    public void accept(Consumer<Advancement> exporter) {
        if( parentOptional.isPresent() ) {
            DtDAdvancement parent = parentOptional.get();
            if ( parent.advOptional.isEmpty() ) {
                throw new IllegalArgumentException(
                    String.format("Advancement '%s' must be built after its parent '%s'", name, parent.name)
                );
            }
            builder.parent(parent.advOptional.get());
        }
        advOptional = Optional.of(builder.build(exporter, NameUitl.toPath(name)));
    }
}
