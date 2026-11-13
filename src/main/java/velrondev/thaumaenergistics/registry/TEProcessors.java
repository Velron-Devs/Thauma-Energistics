package velrondev.thaumaenergistics.registry;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public final class TEProcessors {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);
    private static final Map<ProcessorMetal, Map<ProcessorKind, DeferredItem<Item>>> PRINTED = new EnumMap<>(ProcessorMetal.class);
    private static final Map<ProcessorMetal, Map<ProcessorKind, DeferredItem<Item>>> PROCESSORS = new EnumMap<>(ProcessorMetal.class);

    static {
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            Map<ProcessorKind, DeferredItem<Item>> printed = new EnumMap<>(ProcessorKind.class);
            Map<ProcessorKind, DeferredItem<Item>> processors = new EnumMap<>(ProcessorKind.class);
            for (ProcessorKind kind : ProcessorKind.values()) {
                printed.put(kind, ITEMS.registerSimpleItem(printedId(metal, kind)));
                processors.put(kind, ITEMS.registerSimpleItem(processorId(metal, kind)));
            }
            PRINTED.put(metal, printed);
            PROCESSORS.put(metal, processors);
        }
    }

    private TEProcessors() {
    }

    public static String printedId(ProcessorMetal metal, ProcessorKind kind) {
        return "printed_" + metal.id() + "_" + kind.id() + "_processor";
    }

    public static String processorId(ProcessorMetal metal, ProcessorKind kind) {
        return metal.id() + "_" + kind.id() + "_processor";
    }

    public static DeferredItem<Item> printed(ProcessorMetal metal, ProcessorKind kind) {
        return PRINTED.get(metal).get(kind);
    }

    public static DeferredItem<Item> processor(ProcessorMetal metal, ProcessorKind kind) {
        return PROCESSORS.get(metal).get(kind);
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
