package velrondev.thaumaenergistics.registry;

import appeng.api.storage.ITerminalHost;
import appeng.menu.implementations.IOBusMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.core.TUUWtlib;
import velrondev.thaumaenergistics.menu.ArcaneTerminalMenu;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;
import velrondev.thaumaenergistics.menu.EssentiaStorageBusMenu;
import velrondev.thaumaenergistics.menu.ArcaneTerminalHost;
import velrondev.thaumaenergistics.part.EssentiaExportBusPart;
import velrondev.thaumaenergistics.part.EssentiaImportBusPart;
import velrondev.thaumaenergistics.part.EssentiaStorageBusPart;

public final class TUUMenus {

    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TUU.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneTerminalMenu>> ARCANE_TERMINAL = MENUS.register("arcane_terminal",
            () -> MenuTypeBuilder.create(ArcaneTerminalMenu::new, ArcaneTerminalHost.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "arcane_terminal")));

    public static final DeferredHolder<MenuType<?>, MenuType<EssentiaTerminalMenu>> ESSENTIA_TERMINAL = MENUS.register("essentia_terminal",
            () -> MenuTypeBuilder.create(EssentiaTerminalMenu::new, ITerminalHost.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "essentia_terminal")));

    public static final DeferredHolder<MenuType<?>, MenuType<EssentiaStorageBusMenu>> ESSENTIA_STORAGE_BUS = MENUS.register("essentia_storage_bus",
            () -> MenuTypeBuilder.create(EssentiaStorageBusMenu::new, EssentiaStorageBusPart.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "essentia_storage_bus")));

    public static final DeferredHolder<MenuType<?>, MenuType<IOBusMenu>> ESSENTIA_IMPORT_BUS = MENUS.register("essentia_import_bus",
            () -> MenuTypeBuilder.create(IOBusMenu::new, EssentiaImportBusPart.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "essentia_import_bus")));

    public static final DeferredHolder<MenuType<?>, MenuType<IOBusMenu>> ESSENTIA_EXPORT_BUS = MENUS.register("essentia_export_bus",
            () -> MenuTypeBuilder.create(IOBusMenu::new, EssentiaExportBusPart.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "essentia_export_bus")));

    private TUUMenus() {
    }

    public static void register(IEventBus bus) {
        if (TUUWtlib.isLoaded()) {
            MENUS.register("wireless_arcane_terminal", TUUWtlib::wirelessArcaneMenuType);
            MENUS.register("wireless_essentia_terminal", TUUWtlib::wirelessEssentiaMenuType);
        }
        MENUS.register(bus);
    }
}
