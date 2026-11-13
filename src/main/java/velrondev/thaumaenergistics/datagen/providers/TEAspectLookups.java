package velrondev.thaumaenergistics.datagen.providers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.data.worldgen.BootstrapContext;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeAspects;

public final class TEAspectLookups {

    private TEAspectLookups() {
    }

    public static RegistrySetBuilder builder() {
        return new RegistrySetBuilder().add(IAspect.REGISTRY_KEY, TEAspectLookups::bootstrap);
    }

    private static void bootstrap(BootstrapContext<IAspect> context) {
        Set<ResourceKey<IAspect>> keys = new LinkedHashSet<>(TEResearch.aspectKeys());
        keys.addAll(TEAdvancedAeAspects.aspectKeys());
        keys.addAll(ExtendedAeAspects.CRYSTALS);
        for (ResourceKey<IAspect> key : keys) {
            context.register(key, referenceOnly(key));
        }
    }

    private static Aspect referenceOnly(ResourceKey<IAspect> key) {
        return new Aspect(key.identifier().getPath(), 0xFFFFFF, List.of(), Optional.empty(), key.identifier(), Aspect.DEFAULT_BLEND);
    }
}
