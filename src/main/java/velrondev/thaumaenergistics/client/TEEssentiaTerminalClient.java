package velrondev.thaumaenergistics.client;

import appeng.client.gui.style.StyleManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.client.screen.EssentiaTerminalScreen;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;
import velrondev.thaumaenergistics.registry.TEMenus;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID, value = Dist.CLIENT)
public final class TEEssentiaTerminalClient {

    public static final String STYLE = "/screens/terminals/te_essentia_terminal.json";

    private TEEssentiaTerminalClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<EssentiaTerminalMenu, EssentiaTerminalScreen>register(TEMenus.ESSENTIA_TERMINAL.get(),
                (menu, inventory, title) -> new EssentiaTerminalScreen(menu, inventory, title, StyleManager.loadStyleDoc(STYLE)));
    }
}
