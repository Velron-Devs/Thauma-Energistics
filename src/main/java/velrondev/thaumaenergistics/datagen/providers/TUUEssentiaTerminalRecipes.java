package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import appeng.core.definitions.AEParts;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUParts;

public class TUUEssentiaTerminalRecipes implements DataProvider {

    private final RecipeProvider delegate;

    public TUUEssentiaTerminalRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, registries) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput) {
                TUUEssentiaTerminalRecipes.this.build(recipeOutput);
            }
        };
    }

    private void build(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TUUParts.ESSENTIA_TERMINAL.get())
                .pattern("DTC")
                .define('D', TUUItems.DIFFUSION_CORE.get())
                .define('T', AEParts.TERMINAL)
                .define('C', TUUItems.COALESCENCE_CORE.get())
                .unlockedBy("has_terminal", TUURecipeSupport.has(AEParts.TERMINAL))
                .unlockedBy("has_diffusion_core", TUURecipeSupport.has(TUUItems.DIFFUSION_CORE.get()))
                .unlockedBy("has_coalescence_core", TUURecipeSupport.has(TUUItems.COALESCENCE_CORE.get()))
                .save(output, id("parts/essentia_terminal"));
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
        return "Essentia Terminal Recipes";
    }
}
