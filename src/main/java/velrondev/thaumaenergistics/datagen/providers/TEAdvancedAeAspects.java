package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.leclowndu93150.thaumaturge.api.aspect.AspectDataMaps;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import velrondev.thaumaenergistics.compat.advancedae.AdvancedAeCompat;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;
import velrondev.thaumaenergistics.config.TECompatConfig;

public class TEAdvancedAeAspects extends DataMapProvider {

    public TEAdvancedAeAspects(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static Set<ResourceKey<IAspect>> aspectKeys() {
        Set<ResourceKey<IAspect>> keys = new LinkedHashSet<>();
        for (AdvancedAeCompat.Entry entry : AdvancedAeCompat.entries()) {
            entry.aspects().keySet().forEach(name -> keys.add(TEResearch.aspect(name)));
        }
        return keys;
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        HolderLookup.RegistryLookup<IAspect> aspects = provider.lookupOrThrow(IAspect.REGISTRY_KEY);
        var builder = builder(AspectDataMaps.BASE_ASPECTS);
        for (AdvancedAeCompat.Entry entry : AdvancedAeCompat.entries()) {
            List<AspectInstance> instances = new ArrayList<>();
            for (Map.Entry<String, Integer> aspect : entry.aspects().entrySet()) {
                instances.add(new AspectInstance(aspects.getOrThrow(TEResearch.aspect(aspect.getKey())), aspect.getValue()));
            }
            Identifier item = Identifier.fromNamespaceAndPath(AdvancedAeCompat.MOD_ID, entry.path());
            builder.add(item, AspectList.ofEntries(instances), false,
                    new ModLoadedCondition(AdvancedAeCompat.MOD_ID), new TEFeatureCondition(TECompatConfig.FEATURE_ADVANCED_AE_ASPECTS));
        }
    }

    @Override
    public String getName() {
        return "Advanced AE Aspects";
    }
}
