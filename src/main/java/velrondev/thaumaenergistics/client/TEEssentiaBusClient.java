package velrondev.thaumaenergistics.client;

import appeng.client.gui.implementations.IOBusScreen;
import appeng.client.gui.style.StyleManager;
import appeng.menu.implementations.IOBusMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.registry.TEMenus;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID, value = Dist.CLIENT)
public final class TEEssentiaBusClient {

    public static final String IMPORT_STYLE = "/screens/te_essentia_import_bus.json";
    public static final String EXPORT_STYLE = "/screens/te_essentia_export_bus.json";

    private TEEssentiaBusClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<IOBusMenu, IOBusScreen>register(TEMenus.ESSENTIA_IMPORT_BUS.get(),
                (menu, inventory, title) -> new IOBusScreen(menu, inventory, title, StyleManager.loadStyleDoc(IMPORT_STYLE)));
        event.<IOBusMenu, IOBusScreen>register(TEMenus.ESSENTIA_EXPORT_BUS.get(),
                (menu, inventory, title) -> new IOBusScreen(menu, inventory, title, StyleManager.loadStyleDoc(EXPORT_STYLE)));
    }
}
