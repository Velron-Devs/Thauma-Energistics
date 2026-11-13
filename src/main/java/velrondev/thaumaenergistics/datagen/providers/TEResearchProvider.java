package velrondev.thaumaenergistics.datagen.providers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.scan.ScanEntry;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;
import com.leclowndu93150.thaumaturge.content.research.ResearchCategory;
import com.leclowndu93150.thaumaturge.content.research.ResearchEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

public class TEResearchProvider implements DataProvider {

    private final PackOutput.PathProvider categories;
    private final PackOutput.PathProvider entries;
    private final PackOutput.PathProvider scans;

    public TEResearchProvider(PackOutput output) {
        this.categories = output.createPathProvider(PackOutput.Target.DATA_PACK, "thaumaturge/research_category");
        this.entries = output.createPathProvider(PackOutput.Target.DATA_PACK, "thaumaturge/research_entry");
        this.scans = output.createPathProvider(PackOutput.Target.DATA_PACK, "thaumaturge/scan");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        MappedRegistry<IAspect> aspects = new MappedRegistry<>(IAspect.REGISTRY_KEY, Lifecycle.stable());
        for (ResourceKey<IAspect> key : TEResearch.aspectKeys()) {
            aspects.register(key, referenceOnly(key), RegistrationInfo.BUILT_IN);
        }
        aspects.freeze();

        MappedRegistry<IResearchCategory> categoryRegistry = new MappedRegistry<>(IResearchCategory.REGISTRY_KEY, Lifecycle.stable());
        TEResearch.Lookups lookups = new TEResearch.Lookups(aspects, BuiltInRegistries.ITEM, BuiltInRegistries.BLOCK);
        ResearchCategory category = TEResearch.category(lookups);
        ResourceKey<IResearchCategory> categoryKey = ResourceKey.create(IResearchCategory.REGISTRY_KEY, TEResearch.mod(TEResearch.CATEGORY));
        Holder<IResearchCategory> categoryHolder = categoryRegistry.register(categoryKey, category, RegistrationInfo.BUILT_IN);
        categoryRegistry.freeze();

        RegistryAccess registries = new RegistryAccess.ImmutableRegistryAccess(List.<Registry<?>>of(aspects, categoryRegistry, BuiltInRegistries.ITEM, BuiltInRegistries.BLOCK));
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registries);

        List<CompletableFuture<?>> futures = new ArrayList<>();
        futures.add(save(cache, ResearchCategory.CODEC, ops, category, categories.json(TEResearch.mod(TEResearch.CATEGORY)), List.of()));
        futures.add(save(cache, ScanEntry.CODEC, ops, TEResearch.scan(lookups), scans.json(TEResearch.mod(TEResearch.SCAN_KEY)), List.of()));
        for (TEResearchEntry entry : TEResearch.entries()) {
            ResearchEntry value = entry.build(categoryHolder, lookups);
            List<ICondition> conditions = entry.requiredMods().stream().<ICondition>map(ModLoadedCondition::new).toList();
            futures.add(save(cache, ResearchEntry.CODEC, ops, value, entries.json(entry.location()), conditions));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static Aspect referenceOnly(ResourceKey<IAspect> key) {
        return new Aspect(key.identifier().getPath(), 0xFFFFFF, List.of(), Optional.empty(), key.identifier(), Aspect.DEFAULT_BLEND);
    }

    private static <T> CompletableFuture<?> save(CachedOutput cache, Codec<T> codec, RegistryOps<JsonElement> ops, T value, Path path, List<ICondition> conditions) {
        JsonElement json = codec.encodeStart(ops, value).getOrThrow(message -> new IllegalStateException("Failed to encode " + path + ": " + message));
        if (!conditions.isEmpty() && json instanceof JsonObject object) {
            ICondition.writeConditions(ops, object, conditions);
        }
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Thaumaturge Research";
    }
}
