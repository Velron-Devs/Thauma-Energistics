package velrondev.thaumaenergistics.datagen.providers;

import appeng.core.definitions.AEParts;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEParts;

public class TEEssentiaStorageBusRecipes extends TERecipeProvider {

    public TEEssentiaStorageBusRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, TEParts.ESSENTIA_STORAGE_BUS.get())
                .pattern("DSC")
                .define('D', TEItems.DIFFUSION_CORE.get())
                .define('S', AEParts.STORAGE_BUS)
                .define('C', TEItems.COALESCENCE_CORE.get())
                .unlockedBy("has_storage_bus", has(AEParts.STORAGE_BUS))
                .save(output, key("parts/essentia_storage_bus"));
    }
}
