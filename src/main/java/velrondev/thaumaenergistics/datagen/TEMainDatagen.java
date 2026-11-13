package velrondev.thaumaenergistics.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.core.TECompat;
import velrondev.thaumaenergistics.datagen.providers.TEAdvancedAeAspects;
import velrondev.thaumaenergistics.datagen.providers.TEAspectLookups;
import velrondev.thaumaenergistics.datagen.providers.TEBlockTags;
import velrondev.thaumaenergistics.datagen.providers.TELangProvider;
import velrondev.thaumaenergistics.datagen.providers.TELootProvider;
import velrondev.thaumaenergistics.datagen.providers.TEModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TERecipeProvider;
import velrondev.thaumaenergistics.datagen.providers.TEResearchProvider;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID)
public final class TEMainDatagen {

    private TEMainDatagen() {
    }

    @SubscribeEvent
    public static void gather(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        CompletableFuture<HolderLookup.Provider> registries = RegistryPatchGenerator.createLookup(lookup, TEAspectLookups.builder())
                .thenApply(RegistrySetBuilder.PatchedRegistries::full);
        DataGenerator.PackGenerator pack = generator.getVanillaPack(true);

        pack.addProvider(TEModelProvider::new);
        pack.addProvider(output -> new TELootProvider(output, lookup));
        pack.addProvider(output -> new TEBlockTags(output, lookup));
        pack.addProvider(output -> new TERecipeProvider.Runner(output, registries));
        pack.addProvider(TEResearchProvider::new);
        if (TECompat.advancedAeActive()) {
            pack.addProvider(output -> new TEAdvancedAeAspects(output, registries));
        }

        for (String locale : TELangProvider.LOCALES) {
            pack.addProvider(output -> new TELangProvider(output, locale));
        }
    }
}
