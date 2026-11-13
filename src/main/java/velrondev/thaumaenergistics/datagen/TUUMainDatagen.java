package velrondev.thaumaenergistics.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.core.TUUWtlib;
import velrondev.thaumaenergistics.datagen.providers.TUUAdvancedAeAspects;
import velrondev.thaumaenergistics.datagen.providers.TUUCompatBlockStateProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUCompatItemModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUCompatRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUULangProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUProcessorRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUWtlibRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUULootProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUBlockStateProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUItemModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUPartItemModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUPartModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUArcaneTerminalRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUWirelessArcaneRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUWirelessEssentiaRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUCellRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUThaumaturgeRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUResearchProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUBlockTags;
import velrondev.thaumaenergistics.datagen.providers.TUUEssentiaStorageBusItemModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUEssentiaStorageBusModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUEssentiaStorageBusRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUEssentiaBusModelProvider;
import velrondev.thaumaenergistics.datagen.providers.TUUEssentiaBusRecipes;
import velrondev.thaumaenergistics.datagen.providers.TUUEssentiaTerminalRecipes;

@EventBusSubscriber(modid = TUU.MOD_ID)
public final class TUUMainDatagen {

    private TUUMainDatagen() {
    }

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new TUUItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new TUUCellRecipes(output, lookup));
        generator.addProvider(event.includeClient(), new TUUBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new TUULootProvider(output, lookup));
        generator.addProvider(event.includeServer(), new TUUBlockTags(output, lookup, existingFileHelper));
        generator.addProvider(event.includeServer(), new TUUThaumaturgeRecipes(output, lookup));
        generator.addProvider(event.includeServer(), new TUUResearchProvider(output));
        generator.addProvider(event.includeServer(), new TUUArcaneTerminalRecipes(output, lookup));
        generator.addProvider(event.includeServer(), new TUUWirelessArcaneRecipes(output, lookup));
        generator.addProvider(event.includeServer(), new TUUWirelessEssentiaRecipes(output, lookup));
        generator.addProvider(event.includeClient(), new TUUPartModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new TUUPartItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new TUUEssentiaBusModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new TUUEssentiaBusRecipes(output, lookup));
        generator.addProvider(event.includeServer(), new TUUEssentiaTerminalRecipes(output, lookup));
        generator.addProvider(event.includeClient(), new TUUEssentiaStorageBusModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new TUUEssentiaStorageBusItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new TUUEssentiaStorageBusRecipes(output, lookup));
        generator.addProvider(event.includeServer(), new TUUProcessorRecipes(output, lookup));
        generator.addProvider(event.includeClient(), new TUUCompatItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new TUUCompatBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new TUUCompatRecipes(output, lookup));
        if (TUUCompat.advancedAeActive()) {
            generator.addProvider(event.includeServer(), new TUUAdvancedAeAspects(output, lookup));
        }
        if (TUUWtlib.isLoaded()) {
            generator.addProvider(event.includeServer(), new TUUWtlibRecipes(output, lookup));
        }

        for (String locale : TUULangProvider.LOCALES) {
            generator.addProvider(event.includeClient(), new TUULangProvider(output, locale));
        }
    }
}
