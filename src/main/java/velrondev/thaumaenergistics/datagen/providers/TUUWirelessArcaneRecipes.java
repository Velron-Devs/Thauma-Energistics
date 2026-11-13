package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUParts;

public class TUUWirelessArcaneRecipes implements DataProvider {

    private static final int TERMINAL_VIS = 50;

    private final RecipeProvider delegate;

    public TUUWirelessArcaneRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, TUUAspectLookups.with(registries)) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider lookup) {
                build(recipeOutput, lookup);
            }
        };
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return delegate.run(cache);
    }

    private void build(RecipeOutput output, HolderLookup.Provider registries) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<IAspect> aspects = TUUThaumaturgeRecipes.aspects(registries);

        TUUThaumaturgeRecipes.arcane(items, aspects, TUUItems.WIRELESS_ARCANE_TERMINAL.get(), 1, TERMINAL_VIS, TUUResearch.WIRELESS_ARCANE_TERMINAL)
                .pattern("R")
                .pattern("T")
                .pattern("E")
                .define('R', AEItems.WIRELESS_RECEIVER)
                .define('T', TUUParts.ARCANE_TERMINAL.get())
                .define('E', AEBlocks.DENSE_ENERGY_CELL)
                .save(output, TUUResearch.mod("wireless_arcane_terminal"));
    }

    @Override
    public String getName() {
        return "Wireless Arcane Terminal Recipes";
    }
}
