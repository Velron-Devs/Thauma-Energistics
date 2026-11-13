package velrondev.thaumaenergistics.processor;

import java.util.Optional;
import java.util.function.Supplier;

import com.leclowndu93150.thaumaturge.registry.TTItems;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public enum ProcessorMetal {

    THAUMIUM("thaumium", "Thaumium", "taumio", () -> TTItems.PLATE_THAUMIUM, () -> TTItems.INGOT_THAUMIUM),
    BRASS("brass", "Alchemical Brass", "latón alquímico", () -> TTItems.PLATE_BRASS, () -> TTItems.INGOT_BRASS),
    VOID("void", "Void Metal", "metal del vacío", () -> TTItems.PLATE_VOID, () -> TTItems.INGOT_VOID);

    private final String id;
    private final String english;
    private final String spanish;
    private final Supplier<DeferredItem<Item>> plate;
    private final Supplier<DeferredItem<Item>> ingot;

    ProcessorMetal(String id, String english, String spanish, Supplier<DeferredItem<Item>> plate, Supplier<DeferredItem<Item>> ingot) {
        this.id = id;
        this.english = english;
        this.spanish = spanish;
        this.plate = plate;
        this.ingot = ingot;
    }

    public String id() {
        return id;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public Item plate() {
        return plate.get().get();
    }

    public Item ingot() {
        return ingot.get().get();
    }

    public Optional<ProcessorMetal> previous() {
        return ordinal() == 0 ? Optional.empty() : Optional.of(values()[ordinal() - 1]);
    }
}
