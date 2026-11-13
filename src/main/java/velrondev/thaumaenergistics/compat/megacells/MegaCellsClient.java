package velrondev.thaumaenergistics.compat.megacells;

import appeng.api.client.StorageCellModels;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.InitializeClientRegistriesEvent;

public final class MegaCellsClient {

    private MegaCellsClient() {
    }

    public static void register(IEventBus bus) {
        bus.addListener(InitializeClientRegistriesEvent.class, MegaCellsClient::registerCellModels);
    }

    private static void registerCellModels(InitializeClientRegistriesEvent event) {
        for (int i = 0; i < MegaCellsCompat.cells().size(); i++) {
            StorageCellModels.registerModel(MegaCellsCompat.cells().get(i).get(), MegaCellsCompat.driveModel(i));
        }
    }
}
