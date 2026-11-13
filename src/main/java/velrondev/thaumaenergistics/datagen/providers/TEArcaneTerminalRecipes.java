package velrondev.thaumaenergistics.datagen.providers;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEParts;

public class TEArcaneTerminalRecipes extends TERecipeProvider {

    private static final int TERMINAL_VIS = 50;
    private static final int UPGRADE_VIS = 25;

    public TEArcaneTerminalRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<IAspect> aspects = TEThaumaturgeRecipes.aspects(registries);

        TEThaumaturgeRecipes.arcane(itemLookup, aspects, TEParts.ARCANE_TERMINAL.get(), 1, TERMINAL_VIS, TEResearch.ARCANE_TERMINAL)
                .pattern("DTC")
                .pattern(" W ")
                .define('D', TEItems.DIFFUSION_CORE.get())
                .define('T', AEParts.CRAFTING_TERMINAL)
                .define('C', TEItems.COALESCENCE_CORE.get())
                .define('W', TTItems.ARCANE_WORKBENCH.get())
                .save(output, key(TEResearch.mod("arcane_terminal")));

        TEThaumaturgeRecipes.arcane(itemLookup, aspects, TEItems.UPGRADE_ARCANE.get(), 1, UPGRADE_VIS, TEResearch.ARCANE_CHARGING_UPGRADE)
                .pattern("A")
                .pattern("W")
                .define('A', AEItems.ADVANCED_CARD)
                .define('W', TTItems.ARCANE_WORKBENCH_CHARGER.get())
                .save(output, key(TEResearch.mod("upgrade_arcane")));
    }
}
