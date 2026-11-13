package velrondev.thaumaenergistics.core;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import velrondev.thaumaenergistics.compat.advancedae.AdvancedAeCompat;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;

public final class TECompat {

    public static final String THINGS_ID = "ae2things";
    public static final String MEGA_THINGS_ID = "ae2_mega_things";
    public static final String MEGA_CELLS_ID = "megacells";

    public static final String THINGS_COMPAT = "velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat";
    public static final String MEGA_THINGS_COMPAT = "velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat";
    public static final String MEGA_CELLS_COMPAT = "velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat";

    public static final String THINGS_MODELS = "velrondev.thaumaenergistics.compat.ae2things.datagen.Ae2ThingsModels";
    public static final String MEGA_THINGS_MODELS = "velrondev.thaumaenergistics.compat.ae2megathings.datagen.Ae2MegaThingsModels";
    public static final String MEGA_CELLS_MODELS = "velrondev.thaumaenergistics.compat.megacells.datagen.MegaCellsModels";

    public static final String THINGS_RECIPES = "velrondev.thaumaenergistics.compat.ae2things.datagen.Ae2ThingsRecipes";
    public static final String MEGA_THINGS_RECIPES = "velrondev.thaumaenergistics.compat.ae2megathings.datagen.Ae2MegaThingsRecipes";
    public static final String MEGA_CELLS_RECIPES = "velrondev.thaumaenergistics.compat.megacells.datagen.MegaCellsRecipes";

    private TECompat() {
    }

    public static boolean isLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean thingsActive() {
        return isLoaded(THINGS_ID);
    }

    public static boolean megaThingsActive() {
        return thingsActive() && isLoaded(MEGA_THINGS_ID) && isLoaded(MEGA_CELLS_ID);
    }

    public static boolean megaCellsActive() {
        return isLoaded(MEGA_CELLS_ID);
    }

    public static boolean extendedAeActive() {
        return isLoaded(ExtendedAeCompat.MOD_ID);
    }

    public static boolean advancedAeActive() {
        return isLoaded(AdvancedAeCompat.MOD_ID);
    }

    public static void register(IEventBus bus) {
        if (thingsActive()) {
            TECompatLoader.register(THINGS_COMPAT, bus);
        }
        if (megaThingsActive()) {
            TECompatLoader.register(MEGA_THINGS_COMPAT, bus);
        }
        if (megaCellsActive()) {
            TECompatLoader.register(MEGA_CELLS_COMPAT, bus);
        }
        if (extendedAeActive()) {
            ExtendedAeCompat.register(bus);
        }
    }
}
