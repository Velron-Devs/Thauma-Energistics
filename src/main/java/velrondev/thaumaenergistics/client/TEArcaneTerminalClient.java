package velrondev.thaumaenergistics.client;

import appeng.client.gui.style.StyleManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.client.screen.ArcaneTerminalScreen;
import velrondev.thaumaenergistics.menu.ArcaneTerminalMenu;
import velrondev.thaumaenergistics.registry.TEMenus;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID, value = Dist.CLIENT)
public final class TEArcaneTerminalClient {

    public static final String STYLE = "/screens/terminals/te_arcane_terminal.json";

    private TEArcaneTerminalClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<ArcaneTerminalMenu, ArcaneTerminalScreen>register(TEMenus.ARCANE_TERMINAL.get(),
                (menu, inventory, title) -> new ArcaneTerminalScreen(menu, inventory, title, StyleManager.loadStyleDoc(STYLE)));
    }
}
