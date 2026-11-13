package velrondev.thaumaenergistics.registry;

import appeng.api.parts.PartModels;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.part.ArcaneTerminalPart;
import velrondev.thaumaenergistics.part.EssentiaExportBusPart;
import velrondev.thaumaenergistics.part.EssentiaImportBusPart;
import velrondev.thaumaenergistics.part.EssentiaStorageBusPart;
import velrondev.thaumaenergistics.part.EssentiaTerminalPart;

public final class TUUParts {

    private static final DeferredRegister.Items PART_ITEMS = DeferredRegister.createItems(TUU.MOD_ID);

    public static final DeferredItem<PartItem<ArcaneTerminalPart>> ARCANE_TERMINAL = PART_ITEMS.register("arcane_terminal",
            () -> new PartItem<>(new Item.Properties(), ArcaneTerminalPart.class, ArcaneTerminalPart::new));

    public static final DeferredItem<PartItem<EssentiaTerminalPart>> ESSENTIA_TERMINAL = PART_ITEMS.register("essentia_terminal",
            () -> new PartItem<>(new Item.Properties(), EssentiaTerminalPart.class, EssentiaTerminalPart::new));

    public static final DeferredItem<PartItem<EssentiaStorageBusPart>> ESSENTIA_STORAGE_BUS = PART_ITEMS.register("essentia_storage_bus",
            () -> new PartItem<>(new Item.Properties(), EssentiaStorageBusPart.class, EssentiaStorageBusPart::new));

    public static final DeferredItem<PartItem<EssentiaImportBusPart>> ESSENTIA_IMPORT_BUS = PART_ITEMS.register("essentia_import_bus",
            () -> new PartItem<>(new Item.Properties(), EssentiaImportBusPart.class, EssentiaImportBusPart::new));

    public static final DeferredItem<PartItem<EssentiaExportBusPart>> ESSENTIA_EXPORT_BUS = PART_ITEMS.register("essentia_export_bus",
            () -> new PartItem<>(new Item.Properties(), EssentiaExportBusPart.class, EssentiaExportBusPart::new));

    private TUUParts() {
    }

    public static void register(IEventBus bus) {
        PartModels.registerModels(PartModelsHelper.createModels(EssentiaStorageBusPart.class));
        PartModels.registerModels(PartModelsHelper.createModels(ArcaneTerminalPart.class));
        PartModels.registerModels(PartModelsHelper.createModels(EssentiaTerminalPart.class));
        PartModels.registerModels(PartModelsHelper.createModels(EssentiaImportBusPart.class));
        PartModels.registerModels(PartModelsHelper.createModels(EssentiaExportBusPart.class));
        PART_ITEMS.register(bus);
    }
}
