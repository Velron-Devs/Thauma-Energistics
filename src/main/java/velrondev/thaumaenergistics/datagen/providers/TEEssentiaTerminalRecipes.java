package velrondev.thaumaenergistics.datagen.providers;

import appeng.core.definitions.AEParts;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEParts;

public class TEEssentiaTerminalRecipes extends TERecipeProvider {

    public TEEssentiaTerminalRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, TEParts.ESSENTIA_TERMINAL.get())
                .pattern("DTC")
                .define('D', TEItems.DIFFUSION_CORE.get())
                .define('T', AEParts.TERMINAL)
                .define('C', TEItems.COALESCENCE_CORE.get())
                .unlockedBy("has_terminal", has(AEParts.TERMINAL))
                .unlockedBy("has_diffusion_core", has(TEItems.DIFFUSION_CORE.get()))
                .unlockedBy("has_coalescence_core", has(TEItems.COALESCENCE_CORE.get()))
                .save(output, key("parts/essentia_terminal"));
    }
}
