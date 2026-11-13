package velrondev.thaumaenergistics.core;

import appeng.api.behaviors.ContainerItemStrategy;
import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.behaviors.StackExportStrategy;
import appeng.api.behaviors.StackImportStrategy;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.api.key.EssentiaKey;
import velrondev.thaumaenergistics.behavior.EssentiaContainerItemStrategy;
import velrondev.thaumaenergistics.behavior.EssentiaExportStrategy;
import velrondev.thaumaenergistics.behavior.EssentiaExternalStorage;
import velrondev.thaumaenergistics.behavior.EssentiaImportStrategy;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;
import velrondev.thaumaenergistics.registry.TUUParts;

public final class TUUSetup {

    private TUUSetup() {
    }

    public static void init() {
        StackImportStrategy.register(TUUKeyTypes.essentia(), EssentiaImportStrategy::new);
        StackExportStrategy.register(TUUKeyTypes.essentia(), EssentiaExportStrategy::new);
        ExternalStorageStrategy.register(TUUKeyTypes.essentia(), EssentiaExternalStorage::new);
        ContainerItemStrategy.register(TUUKeyTypes.essentia(), EssentiaKey.class, new EssentiaContainerItemStrategy());

        Upgrades.add(AEItems.INVERTER_CARD, TUUParts.ESSENTIA_STORAGE_BUS, 1);
        Upgrades.add(AEItems.CAPACITY_CARD, TUUParts.ESSENTIA_STORAGE_BUS, 5);
        Upgrades.add(AEItems.VOID_CARD, TUUParts.ESSENTIA_STORAGE_BUS, 1);

        TUUWirelessArcane.init();
        TUUWirelessEssentia.init();
        Upgrades.add(TUUItems.UPGRADE_ARCANE.get(), TUUParts.ARCANE_TERMINAL, 1, GuiText.CraftingTerminal.getTranslationKey());

        String busGroup = GuiText.IOBuses.getTranslationKey();
        Upgrades.add(AEItems.REDSTONE_CARD, TUUParts.ESSENTIA_IMPORT_BUS, 1, busGroup);
        Upgrades.add(AEItems.CAPACITY_CARD, TUUParts.ESSENTIA_IMPORT_BUS, 5, busGroup);
        Upgrades.add(AEItems.SPEED_CARD, TUUParts.ESSENTIA_IMPORT_BUS, 4, busGroup);
        Upgrades.add(AEItems.INVERTER_CARD, TUUParts.ESSENTIA_IMPORT_BUS, 1, busGroup);
        Upgrades.add(AEItems.REDSTONE_CARD, TUUParts.ESSENTIA_EXPORT_BUS, 1, busGroup);
        Upgrades.add(AEItems.CAPACITY_CARD, TUUParts.ESSENTIA_EXPORT_BUS, 5, busGroup);
        Upgrades.add(AEItems.SPEED_CARD, TUUParts.ESSENTIA_EXPORT_BUS, 4, busGroup);

        String group = GuiText.StorageCells.getTranslationKey();
        for (Item cell : TUUItems.cells()) {
            Upgrades.add(AEItems.INVERTER_CARD, cell, 1, group);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell, 1, group);
            Upgrades.add(AEItems.VOID_CARD, cell, 1, group);
        }
    }
}
