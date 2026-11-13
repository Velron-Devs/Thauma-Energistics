package velrondev.thaumaenergistics.datagen.providers;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEParts;

public class TEWirelessArcaneRecipes extends TERecipeProvider {

    private static final int TERMINAL_VIS = 50;

    public TEWirelessArcaneRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<IAspect> aspects = TEThaumaturgeRecipes.aspects(registries);

        TEThaumaturgeRecipes.arcane(itemLookup, aspects, TEItems.WIRELESS_ARCANE_TERMINAL.get(), 1, TERMINAL_VIS, TEResearch.WIRELESS_ARCANE_TERMINAL)
                .pattern("R")
                .pattern("T")
                .pattern("E")
                .define('R', AEItems.WIRELESS_RECEIVER)
                .define('T', TEParts.ARCANE_TERMINAL.get())
                .define('E', AEBlocks.DENSE_ENERGY_CELL)
                .save(output, key(TEResearch.mod("wireless_arcane_terminal")));
    }
}
