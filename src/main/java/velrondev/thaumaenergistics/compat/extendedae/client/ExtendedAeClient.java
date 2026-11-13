package velrondev.thaumaenergistics.compat.extendedae.client;

import appeng.api.client.StorageCellModels;
import net.neoforged.neoforge.client.event.InitializeClientRegistriesEvent;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;

public final class ExtendedAeClient {

    private ExtendedAeClient() {
    }

    public static void registerCellModels(InitializeClientRegistriesEvent event) {
        ExtendedAeCompat.infiniteCells().forEach((name, cell) -> StorageCellModels.registerModel(cell.get(), ExtendedAeCompat.infiniteDriveModel(name)));
    }
}
