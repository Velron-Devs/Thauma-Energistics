package velrondev.thaumaenergistics.datagen.providers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
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

public class TUUResearchProvider implements DataProvider {

    private final PackOutput.PathProvider categories;
    private final PackOutput.PathProvider entries;
    private final PackOutput.PathProvider scans;

    public TUUResearchProvider(PackOutput output) {
        this.categories = output.createPathProvider(PackOutput.Target.DATA_PACK, "thaumaturge/research_category");
        this.entries = output.createPathProvider(PackOutput.Target.DATA_PACK, "thaumaturge/research_entry");
        this.scans = output.createPathProvider(PackOutput.Target.DATA_PACK, "thaumaturge/scan");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        MappedRegistry<IAspect> aspects = new MappedRegistry<>(IAspect.REGISTRY_KEY, Lifecycle.stable());
        for (ResourceKey<IAspect> key : TUUResearch.aspectKeys()) {
            aspects.register(key, referenceOnly(key), RegistrationInfo.BUILT_IN);
        }
        aspects.freeze();

        MappedRegistry<IResearchCategory> categoryRegistry = new MappedRegistry<>(IResearchCategory.REGISTRY_KEY, Lifecycle.stable());
        TUUResearch.Lookups lookups = new TUUResearch.Lookups(aspects.asLookup(), BuiltInRegistries.ITEM.asLookup(), BuiltInRegistries.BLOCK.asLookup());
        ResearchCategory category = TUUResearch.category(lookups);
        ResourceKey<IResearchCategory> categoryKey = ResourceKey.create(IResearchCategory.REGISTRY_KEY, TUUResearch.mod(TUUResearch.CATEGORY));
        Holder<IResearchCategory> categoryHolder = categoryRegistry.register(categoryKey, category, RegistrationInfo.BUILT_IN);
        categoryRegistry.freeze();

        RegistryAccess registries = new RegistryAccess.ImmutableRegistryAccess(List.<Registry<?>>of(aspects, categoryRegistry, BuiltInRegistries.ITEM, BuiltInRegistries.BLOCK));
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registries);

        List<CompletableFuture<?>> futures = new ArrayList<>();
        futures.add(save(cache, ResearchCategory.CODEC, ops, category, categories.json(TUUResearch.mod(TUUResearch.CATEGORY))));
        futures.add(save(cache, ScanEntry.CODEC, ops, TUUResearch.scan(lookups), scans.json(TUUResearch.mod(TUUResearch.SCAN_KEY))));
        for (TUUResearchEntry entry : TUUResearch.entries()) {
            ResearchEntry value = entry.build(categoryHolder, lookups);
            futures.add(save(cache, ResearchEntry.CODEC, ops, value, entries.json(entry.location())));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static Aspect referenceOnly(ResourceKey<IAspect> key) {
        return new Aspect(key.location().getPath(), 0xFFFFFF, List.of(), Optional.empty(), key.location(), Aspect.DEFAULT_BLEND);
    }

    private static <T> CompletableFuture<?> save(CachedOutput cache, Codec<T> codec, RegistryOps<JsonElement> ops, T value, Path path) {
        JsonElement json = codec.encodeStart(ops, value).getOrThrow(message -> new IllegalStateException("Failed to encode " + path + ": " + message));
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Thaumaturge Research";
    }
}
