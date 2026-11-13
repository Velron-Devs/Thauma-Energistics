package velrondev.thaumaenergistics.datagen.providers;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

public final class TUUAspectLookups {

    private TUUAspectLookups() {
    }

    public static CompletableFuture<HolderLookup.Provider> with(CompletableFuture<HolderLookup.Provider> base) {
        return base.thenApply(TUUAspectLookups::with);
    }

    public static CompletableFuture<HolderLookup.Provider> with(CompletableFuture<HolderLookup.Provider> base, Collection<ResourceKey<IAspect>> extra) {
        return base.thenApply(provider -> with(provider, extra));
    }

    public static HolderLookup.Provider with(HolderLookup.Provider base) {
        return with(base, List.of());
    }

    public static HolderLookup.Provider with(HolderLookup.Provider base, Collection<ResourceKey<IAspect>> extra) {
        MappedRegistry<IAspect> aspects = new MappedRegistry<>(IAspect.REGISTRY_KEY, Lifecycle.stable());
        Set<ResourceKey<IAspect>> keys = new LinkedHashSet<>(TUUResearch.aspectKeys());
        keys.addAll(extra);
        for (ResourceKey<IAspect> key : keys) {
            aspects.register(key, referenceOnly(key), RegistrationInfo.BUILT_IN);
        }
        aspects.freeze();
        HolderLookup.RegistryLookup<IAspect> aspectLookup = aspects.asLookup();
        return new HolderLookup.Provider() {
            @Override
            public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
                return Stream.concat(base.listRegistries(), Stream.of(IAspect.REGISTRY_KEY));
            }

            @Override
            public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> ops) {
                RegistryOps<V> baseOps = base.createSerializationContext(ops);
                return RegistryOps.create(ops, new RegistryOps.RegistryInfoLookup() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                        if (IAspect.REGISTRY_KEY.equals(key)) {
                            HolderLookup.RegistryLookup<T> lookup = (HolderLookup.RegistryLookup<T>) aspectLookup;
                            return Optional.of(new RegistryOps.RegistryInfo<>(lookup, lookup, lookup.registryLifecycle()));
                        }
                        Optional<HolderOwner<T>> owner = baseOps.owner(key);
                        Optional<HolderGetter<T>> getter = baseOps.getter(key);
                        if (owner.isEmpty() || getter.isEmpty()) {
                            return Optional.empty();
                        }
                        return Optional.of(new RegistryOps.RegistryInfo<>(owner.get(), getter.get(), Lifecycle.stable()));
                    }
                });
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                if (IAspect.REGISTRY_KEY.equals(key)) {
                    return Optional.of((HolderLookup.RegistryLookup<T>) aspectLookup);
                }
                return base.lookup(key);
            }
        };
    }

    private static Aspect referenceOnly(ResourceKey<IAspect> key) {
        return new Aspect(key.location().getPath(), 0xFFFFFF, List.of(), Optional.empty(), key.location(), Aspect.DEFAULT_BLEND);
    }
}
