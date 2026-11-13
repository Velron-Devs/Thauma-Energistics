package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import appeng.core.definitions.AEItems;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TUUProcessors;

public class TUUProcessorRecipes implements DataProvider {

    private final RecipeProvider delegate;

    public TUUProcessorRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, registries) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider lookup) {
                TUUProcessorRecipes.this.build(TUURecipeSupport.when(recipeOutput, TUUCompatConfig.FEATURE_PROCESSORS));
            }
        };
    }

    private void build(RecipeOutput output) {
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                var printed = TUUProcessors.printed(metal, kind);
                var processor = TUUProcessors.processor(metal, kind);

                InscriberRecipeBuilder.inscribe(Ingredient.of(metal.plate()), printed.get(), 1)
                        .setTop(Ingredient.of(kind.press()))
                        .save(output, id("inscriber/" + printed.getId().getPath()));

                Ingredient middle = metal.previous()
                        .map(previous -> Ingredient.of(TUUProcessors.processor(previous, kind).get()))
                        .orElseGet(() -> Ingredient.of(Tags.Items.DUSTS_REDSTONE));
                InscriberRecipeBuilder.inscribe(middle, processor.get(), 1)
                        .setTop(Ingredient.of(printed.get()))
                        .setBottom(Ingredient.of(AEItems.SILICON_PRINT))
                        .setMode(InscriberProcessType.PRESS)
                        .save(output, id("inscriber/" + processor.getId().getPath()));
            }
        }
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, path);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return delegate.run(cache);
    }

    @Override
    public String getName() {
        return "Metal Processor Recipes";
    }
}
