package velrondev.thaumaenergistics.compat.extendedae;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;

import net.minecraft.resources.ResourceKey;

public final class ExtendedAeAspects {

    public static final List<ResourceKey<IAspect>> CRYSTALS = List.of(
            TTAspects.TERRA, TTAspects.IGNIS, TTAspects.AER, TTAspects.AQUA, TTAspects.ORDO, TTAspects.PERDITIO);

    private ExtendedAeAspects() {
    }
}
