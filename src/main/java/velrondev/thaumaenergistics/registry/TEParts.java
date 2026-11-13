package velrondev.thaumaenergistics.registry;

import appeng.items.parts.PartItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.part.ArcaneTerminalPart;
import velrondev.thaumaenergistics.part.EssentiaExportBusPart;
import velrondev.thaumaenergistics.part.EssentiaImportBusPart;
import velrondev.thaumaenergistics.part.EssentiaStorageBusPart;
import velrondev.thaumaenergistics.part.EssentiaTerminalPart;

public final class TEParts {

    private static final DeferredRegister.Items PART_ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);

    public static final DeferredItem<PartItem<ArcaneTerminalPart>> ARCANE_TERMINAL = PART_ITEMS.registerItem("arcane_terminal",
            properties -> new PartItem<>(properties, ArcaneTerminalPart.class, ArcaneTerminalPart::new));

    public static final DeferredItem<PartItem<EssentiaTerminalPart>> ESSENTIA_TERMINAL = PART_ITEMS.registerItem("essentia_terminal",
            properties -> new PartItem<>(properties, EssentiaTerminalPart.class, EssentiaTerminalPart::new));

    public static final DeferredItem<PartItem<EssentiaStorageBusPart>> ESSENTIA_STORAGE_BUS = PART_ITEMS.registerItem("essentia_storage_bus",
            properties -> new PartItem<>(properties, EssentiaStorageBusPart.class, EssentiaStorageBusPart::new));

    public static final DeferredItem<PartItem<EssentiaImportBusPart>> ESSENTIA_IMPORT_BUS = PART_ITEMS.registerItem("essentia_import_bus",
            properties -> new PartItem<>(properties, EssentiaImportBusPart.class, EssentiaImportBusPart::new));

    public static final DeferredItem<PartItem<EssentiaExportBusPart>> ESSENTIA_EXPORT_BUS = PART_ITEMS.registerItem("essentia_export_bus",
            properties -> new PartItem<>(properties, EssentiaExportBusPart.class, EssentiaExportBusPart::new));

    private TEParts() {
    }

    public static void register(IEventBus bus) {
        PART_ITEMS.register(bus);
    }
}
