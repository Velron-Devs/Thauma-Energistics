package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import velrondev.thaumaenergistics.compat.ae2wtlib.Ae2wtlibRecipes;

public class TUUWtlibRecipes implements DataProvider {

    private final RecipeProvider delegate;

    public TUUWtlibRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, registries) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider lookup) {
                Ae2wtlibRecipes.build(recipeOutput);
            }
        };
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return delegate.run(cache);
    }

    @Override
    public String getName() {
        return "AE2WTLib Wireless Arcane Terminal Recipes";
    }
}
