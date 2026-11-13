package velrondev.thaumaenergistics.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2wtlib.Ae2wtlibClient;
import velrondev.thaumaenergistics.core.TUUWtlib;

@EventBusSubscriber(modid = TUU.MOD_ID, value = Dist.CLIENT)
public final class TUUWtlibClient {

    private TUUWtlibClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        if (TUUWtlib.isLoaded()) {
            Ae2wtlibClient.registerScreens(event);
        }
    }
}
