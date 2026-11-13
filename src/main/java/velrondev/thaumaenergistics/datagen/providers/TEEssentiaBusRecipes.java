package velrondev.thaumaenergistics.datagen.providers;

import appeng.core.definitions.AEParts;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEParts;

public class TEEssentiaBusRecipes extends TERecipeProvider {

    public TEEssentiaBusRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, TEParts.ESSENTIA_IMPORT_BUS.get())
                .pattern("D")
                .pattern("B")
                .define('D', TEItems.DIFFUSION_CORE.get())
                .define('B', AEParts.IMPORT_BUS)
                .unlockedBy("has_import_bus", has(AEParts.IMPORT_BUS))
                .unlockedBy("has_diffusion_core", has(TEItems.DIFFUSION_CORE.get()))
                .save(output, key("parts/essentia_import_bus"));

        shaped(RecipeCategory.MISC, TEParts.ESSENTIA_EXPORT_BUS.get())
                .pattern("C")
                .pattern("B")
                .define('C', TEItems.COALESCENCE_CORE.get())
                .define('B', AEParts.EXPORT_BUS)
                .unlockedBy("has_export_bus", has(AEParts.EXPORT_BUS))
                .unlockedBy("has_coalescence_core", has(TEItems.COALESCENCE_CORE.get()))
                .save(output, key("parts/essentia_export_bus"));
    }
}
