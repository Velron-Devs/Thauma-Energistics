package velrondev.thaumaenergistics.core;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.advancedae.AdvancedAeCompat;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;

public final class TUUCompat {

    private TUUCompat() {
    }

    public static boolean isLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean thingsActive() {
        return isLoaded(Ae2ThingsCompat.MOD_ID);
    }

    public static boolean megaThingsActive() {
        return thingsActive() && isLoaded(Ae2MegaThingsCompat.MOD_ID) && isLoaded(MegaCellsCompat.MOD_ID);
    }

    public static boolean megaCellsActive() {
        return isLoaded(MegaCellsCompat.MOD_ID);
    }

    public static boolean extendedAeActive() {
        return isLoaded(ExtendedAeCompat.MOD_ID);
    }

    public static boolean advancedAeActive() {
        return isLoaded(AdvancedAeCompat.MOD_ID);
    }

    public static void register(IEventBus bus) {
        if (thingsActive()) {
            Ae2ThingsCompat.register(bus);
        }
        if (megaThingsActive()) {
            Ae2MegaThingsCompat.register(bus);
        }
        if (megaCellsActive()) {
            MegaCellsCompat.register(bus);
        }
        if (extendedAeActive()) {
            ExtendedAeCompat.register(bus);
        }
    }
}
