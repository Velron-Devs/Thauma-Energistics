package velrondev.thaumaenergistics.compat.ae2wtlib;

import appeng.client.gui.style.StyleManager;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.menu.ArcaneTerminalMenu;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;

public final class Ae2wtlibClient {

    public static final String STYLE = "/screens/wtlib/tuu_wireless_arcane_terminal.json";
    public static final String ESSENTIA_STYLE = "/screens/wtlib/tuu_wireless_essentia_terminal.json";

    private Ae2wtlibClient() {
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<ArcaneTerminalMenu, WtlibArcaneTerminalScreen>register(Ae2wtlibCompat.typedMenuType(),
                (menu, inventory, title) -> new WtlibArcaneTerminalScreen((WtlibArcaneTerminalMenu) menu, inventory, title, StyleManager.loadStyleDoc(STYLE)));
        event.<EssentiaTerminalMenu, WtlibEssentiaTerminalScreen>register(Ae2wtlibCompat.typedEssentiaMenuType(),
                (menu, inventory, title) -> new WtlibEssentiaTerminalScreen((WtlibEssentiaTerminalMenu) menu, inventory, title, StyleManager.loadStyleDoc(ESSENTIA_STYLE)));
    }
}
