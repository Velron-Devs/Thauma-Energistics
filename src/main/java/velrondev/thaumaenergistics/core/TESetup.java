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
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEKeyTypes;
import velrondev.thaumaenergistics.registry.TEParts;

public final class TESetup {

    private TESetup() {
    }

    public static void init() {
        StackImportStrategy.register(TEKeyTypes.essentia(), EssentiaImportStrategy::new);
        StackExportStrategy.register(TEKeyTypes.essentia(), EssentiaExportStrategy::new);
        ExternalStorageStrategy.register(TEKeyTypes.essentia(), EssentiaExternalStorage::new);
        ContainerItemStrategy.register(TEKeyTypes.essentia(), EssentiaKey.class, new EssentiaContainerItemStrategy());

        Upgrades.add(AEItems.INVERTER_CARD, TEParts.ESSENTIA_STORAGE_BUS, 1);
        Upgrades.add(AEItems.CAPACITY_CARD, TEParts.ESSENTIA_STORAGE_BUS, 5);
        Upgrades.add(AEItems.VOID_CARD, TEParts.ESSENTIA_STORAGE_BUS, 1);

        TEWirelessArcane.init();
        TEWirelessEssentia.init();
        Upgrades.add(TEItems.UPGRADE_ARCANE.get(), TEParts.ARCANE_TERMINAL, 1, GuiText.CraftingTerminal.getTranslationKey());

        String busGroup = GuiText.IOBuses.getTranslationKey();
        Upgrades.add(AEItems.REDSTONE_CARD, TEParts.ESSENTIA_IMPORT_BUS, 1, busGroup);
        Upgrades.add(AEItems.CAPACITY_CARD, TEParts.ESSENTIA_IMPORT_BUS, 5, busGroup);
        Upgrades.add(AEItems.SPEED_CARD, TEParts.ESSENTIA_IMPORT_BUS, 4, busGroup);
        Upgrades.add(AEItems.INVERTER_CARD, TEParts.ESSENTIA_IMPORT_BUS, 1, busGroup);
        Upgrades.add(AEItems.REDSTONE_CARD, TEParts.ESSENTIA_EXPORT_BUS, 1, busGroup);
        Upgrades.add(AEItems.CAPACITY_CARD, TEParts.ESSENTIA_EXPORT_BUS, 5, busGroup);
        Upgrades.add(AEItems.SPEED_CARD, TEParts.ESSENTIA_EXPORT_BUS, 4, busGroup);

        String group = GuiText.StorageCells.getTranslationKey();
        for (Item cell : TEItems.cells()) {
            Upgrades.add(AEItems.INVERTER_CARD, cell, 1, group);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell, 1, group);
            Upgrades.add(AEItems.VOID_CARD, cell, 1, group);
        }
    }
}
