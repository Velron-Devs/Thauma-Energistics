package velrondev.thaumaenergistics.client;

import appeng.client.gui.style.StyleManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.client.screen.EssentiaStorageBusScreen;
import velrondev.thaumaenergistics.menu.EssentiaStorageBusMenu;
import velrondev.thaumaenergistics.registry.TEMenus;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID, value = Dist.CLIENT)
public final class TEEssentiaStorageBusClient {

    public static final String STYLE = "/screens/te_essentia_storage_bus.json";

    private TEEssentiaStorageBusClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<EssentiaStorageBusMenu, EssentiaStorageBusScreen>register(TEMenus.ESSENTIA_STORAGE_BUS.get(),
                (menu, inventory, title) -> new EssentiaStorageBusScreen(menu, inventory, title, StyleManager.loadStyleDoc(STYLE)));
    }
}
