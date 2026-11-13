package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
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

public class TUUArcaneTerminalRecipes implements DataProvider {

    private static final int TERMINAL_VIS = 50;
    private static final int UPGRADE_VIS = 25;

    private final RecipeProvider delegate;

    public TUUArcaneTerminalRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
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

        TUUThaumaturgeRecipes.arcane(items, aspects, TUUParts.ARCANE_TERMINAL.get(), 1, TERMINAL_VIS, TUUResearch.ARCANE_TERMINAL)
                .pattern("DTC")
                .pattern(" W ")
                .define('D', TUUItems.DIFFUSION_CORE.get())
                .define('T', AEParts.CRAFTING_TERMINAL)
                .define('C', TUUItems.COALESCENCE_CORE.get())
                .define('W', TTItems.ARCANE_WORKBENCH.get())
                .save(output, TUUResearch.mod("arcane_terminal"));

        TUUThaumaturgeRecipes.arcane(items, aspects, TUUItems.UPGRADE_ARCANE.get(), 1, UPGRADE_VIS, TUUResearch.ARCANE_CHARGING_UPGRADE)
                .pattern("A")
                .pattern("W")
                .define('A', AEItems.ADVANCED_CARD)
                .define('W', TTItems.ARCANE_WORKBENCH_CHARGER.get())
                .save(output, TUUResearch.mod("upgrade_arcane"));
    }

    @Override
    public String getName() {
        return "Arcane Terminal Recipes";
    }
}
