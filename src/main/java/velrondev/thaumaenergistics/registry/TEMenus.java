package velrondev.thaumaenergistics.registry;

import appeng.api.storage.ITerminalHost;
import appeng.menu.implementations.IOBusMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.core.TEWtlib;
import velrondev.thaumaenergistics.menu.ArcaneTerminalMenu;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;
import velrondev.thaumaenergistics.menu.EssentiaStorageBusMenu;
import velrondev.thaumaenergistics.menu.ArcaneTerminalHost;
import velrondev.thaumaenergistics.part.EssentiaExportBusPart;
import velrondev.thaumaenergistics.part.EssentiaImportBusPart;
import velrondev.thaumaenergistics.part.EssentiaStorageBusPart;

public final class TEMenus {

    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ThaumaEnergistics.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneTerminalMenu>> ARCANE_TERMINAL = MENUS.register("arcane_terminal",
            () -> MenuTypeBuilder.create(ArcaneTerminalMenu::new, ArcaneTerminalHost.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "arcane_terminal")));

    public static final DeferredHolder<MenuType<?>, MenuType<EssentiaTerminalMenu>> ESSENTIA_TERMINAL = MENUS.register("essentia_terminal",
            () -> MenuTypeBuilder.create(EssentiaTerminalMenu::new, ITerminalHost.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "essentia_terminal")));

    public static final DeferredHolder<MenuType<?>, MenuType<EssentiaStorageBusMenu>> ESSENTIA_STORAGE_BUS = MENUS.register("essentia_storage_bus",
            () -> MenuTypeBuilder.create(EssentiaStorageBusMenu::new, EssentiaStorageBusPart.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "essentia_storage_bus")));

    public static final DeferredHolder<MenuType<?>, MenuType<IOBusMenu>> ESSENTIA_IMPORT_BUS = MENUS.register("essentia_import_bus",
            () -> MenuTypeBuilder.create(IOBusMenu::new, EssentiaImportBusPart.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "essentia_import_bus")));

    public static final DeferredHolder<MenuType<?>, MenuType<IOBusMenu>> ESSENTIA_EXPORT_BUS = MENUS.register("essentia_export_bus",
            () -> MenuTypeBuilder.create(IOBusMenu::new, EssentiaExportBusPart.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "essentia_export_bus")));

    private TEMenus() {
    }

    public static void register(IEventBus bus) {
        if (TEWtlib.isLoaded()) {
            MENUS.register("wireless_arcane_terminal", TEWtlib::wirelessArcaneMenuType);
            MENUS.register("wireless_essentia_terminal", TEWtlib::wirelessEssentiaMenuType);
        }
        MENUS.register(bus);
    }
}
