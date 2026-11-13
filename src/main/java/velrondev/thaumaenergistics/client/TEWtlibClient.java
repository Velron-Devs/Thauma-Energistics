package velrondev.thaumaenergistics.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.ae2wtlib.Ae2wtlibClient;
import velrondev.thaumaenergistics.core.TEWtlib;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID, value = Dist.CLIENT)
public final class TEWtlibClient {

    private TEWtlibClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        if (TEWtlib.isLoaded()) {
            Ae2wtlibClient.registerScreens(event);
        }
    }
}
