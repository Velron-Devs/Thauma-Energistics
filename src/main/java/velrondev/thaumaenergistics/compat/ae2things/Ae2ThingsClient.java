package velrondev.thaumaenergistics.compat.ae2things;

import appeng.api.client.StorageCellModels;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.InitializeClientRegistriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.core.TECompat;

public final class Ae2ThingsClient {

    private Ae2ThingsClient() {
    }

    public static void register(IEventBus bus) {
        bus.addListener(RegisterColorHandlersEvent.ItemTintSources.class, Ae2ThingsClient::registerItemTintSources);
        bus.addListener(InitializeClientRegistriesEvent.class, Ae2ThingsClient::registerCellModels);
    }

    private static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(EssentiaDiskTintSource.ID, EssentiaDiskTintSource.MAP_CODEC);
    }

    private static void registerCellModels(InitializeClientRegistriesEvent event) {
        for (int i = 0; i < Ae2ThingsCompat.disks().size(); i++) {
            StorageCellModels.registerModel(Ae2ThingsCompat.disks().get(i).get(), Ae2ThingsCompat.driveModel(i));
        }
        if (TECompat.megaThingsActive()) {
            for (int i = 0; i < Ae2MegaThingsCompat.disks().size(); i++) {
                StorageCellModels.registerModel(Ae2MegaThingsCompat.disks().get(i).get(), Ae2MegaThingsCompat.driveModel(i));
            }
        }
    }
}
