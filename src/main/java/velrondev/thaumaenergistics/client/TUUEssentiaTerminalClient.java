package velrondev.thaumaenergistics.client;

import appeng.api.util.AEColor;
import appeng.client.gui.style.StyleManager;
import appeng.client.render.StaticItemColor;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.client.screen.EssentiaTerminalScreen;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;
import velrondev.thaumaenergistics.registry.TUUMenus;
import velrondev.thaumaenergistics.registry.TUUParts;

@EventBusSubscriber(modid = TUU.MOD_ID, value = Dist.CLIENT)
public final class TUUEssentiaTerminalClient {

    public static final String STYLE = "/screens/terminals/tuu_essentia_terminal.json";

    private TUUEssentiaTerminalClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<EssentiaTerminalMenu, EssentiaTerminalScreen>register(TUUMenus.ESSENTIA_TERMINAL.get(),
                (menu, inventory, title) -> new EssentiaTerminalScreen(menu, inventory, title, StyleManager.loadStyleDoc(STYLE)));
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        StaticItemColor color = new StaticItemColor(AEColor.TRANSPARENT);
        event.register((stack, tintIndex) -> FastColor.ARGB32.opaque(color.getColor(stack, tintIndex)), TUUParts.ESSENTIA_TERMINAL.get());
    }
}
