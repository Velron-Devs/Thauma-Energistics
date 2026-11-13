package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.ae2wtlib.Ae2wtlibRecipes;
import velrondev.thaumaenergistics.core.TECompat;
import velrondev.thaumaenergistics.core.TECompatLoader;
import velrondev.thaumaenergistics.core.TEWtlib;

public abstract class TERecipeProvider extends RecipeProvider {

    @FunctionalInterface
    public interface RecipeFactory {
        TERecipeProvider create(HolderLookup.Provider registries, RecipeOutput output);
    }

    protected TERecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    protected static ResourceKey<Recipe<?>> key(String path) {
        return key(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path));
    }

    protected static ResourceKey<Recipe<?>> key(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }

    public static final class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            List<RecipeFactory> factories = new ArrayList<>(List.of(
                    TECellRecipes::new,
                    TEThaumaturgeRecipes::new,
                    TEProcessorRecipes::new,
                    TEExtendedAeRecipes::new,
                    TEArcaneTerminalRecipes::new,
                    TEWirelessArcaneRecipes::new,
                    TEWirelessEssentiaRecipes::new,
                    TEEssentiaBusRecipes::new,
                    TEEssentiaTerminalRecipes::new,
                    TEEssentiaStorageBusRecipes::new));
            if (TECompat.thingsActive()) {
                factories.add(TECompatLoader.recipes(TECompat.THINGS_RECIPES));
            }
            if (TECompat.megaThingsActive()) {
                factories.add(TECompatLoader.recipes(TECompat.MEGA_THINGS_RECIPES));
            }
            if (TECompat.megaCellsActive()) {
                factories.add(TECompatLoader.recipes(TECompat.MEGA_CELLS_RECIPES));
            }
            if (TEWtlib.isLoaded()) {
                factories.add((lookup, recipeOutput) -> new TERecipeProvider(lookup, recipeOutput) {
                    @Override
                    protected void buildRecipes() {
                        Ae2wtlibRecipes.build(recipeOutput);
                    }
                });
            }
            return new RecipeProvider(registries, output) {
                @Override
                protected void buildRecipes() {
                    for (RecipeFactory factory : factories) {
                        factory.create(registries, output).buildRecipes();
                    }
                }
            };
        }

        @Override
        public String getName() {
            return "Thauma Energistics Recipes";
        }
    }
}
