package velrondev.thaumaenergistics.menu;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

public final class ArcaneCrystals {

    public static final List<ResourceKey<IAspect>> PRIMALS = List.of(TTAspects.AER, TTAspects.TERRA, TTAspects.IGNIS, TTAspects.AQUA, TTAspects.ORDO, TTAspects.PERDITIO);

    private ArcaneCrystals() {
    }

    public static String encode(AspectList needed, AspectList available, HolderLookup.Provider registries) {
        StringBuilder state = new StringBuilder();
        for (ResourceKey<IAspect> primal : PRIMALS) {
            if (state.length() > 0) {
                state.append(';');
            }
            state.append(needed.amountOf(primal, registries)).append(',').append(available.amountOf(primal, registries));
        }
        return state.toString();
    }

    public static int[][] parse(String state) {
        int[][] values = new int[PRIMALS.size()][2];
        String[] entries = state.split(";");
        for (int i = 0; i < entries.length && i < values.length; i++) {
            String[] pair = entries[i].split(",");
            if (pair.length == 2) {
                values[i][0] = parseInt(pair[0]);
                values[i][1] = parseInt(pair[1]);
            }
        }
        return values;
    }

    private static int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}
