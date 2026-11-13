package velrondev.thaumaenergistics.compat.megacells.datagen;

import java.util.List;

import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEBlocks;
import appeng.recipes.game.StorageCellDisassemblyRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.datagen.providers.TERecipeProvider;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class MegaCellsRecipes extends TERecipeProvider {

    public MegaCellsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        RecipeOutput gated = output.withConditions(
                new ModLoadedCondition(MegaCellsCompat.MOD_ID),
                new TEFeatureCondition(TECompatConfig.FEATURE_MEGA_CELLS));
        Item housing = MegaCellsCompat.housing().get();
        shaped(RecipeCategory.MISC, housing)
                .pattern("aba")
                .pattern("b b")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_VIBRANT_GLASS)
                .define('b', TTItems.SALIS_MUNDUS.get())
                .define('d', ProcessorMetal.VOID.ingot())
                .unlockedBy("has_salis_mundus", has(TTItems.SALIS_MUNDUS.get()))
                .save(gated, key("cells/" + MegaCellsCompat.HOUSING_ID));
        for (int i = 0; i < MegaCellsCompat.cells().size(); i++) {
            Item cell = MegaCellsCompat.cells().get(i).get();
            ItemLike component = MegaCellsCompat.component(i);
            shaped(RecipeCategory.MISC, cell)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ddd")
                    .define('a', AEBlocks.QUARTZ_VIBRANT_GLASS)
                    .define('b', TTItems.SALIS_MUNDUS.get())
                    .define('c', component)
                    .define('d', ProcessorMetal.VOID.ingot())
                    .unlockedBy("has_component", has(component))
                    .save(gated, key("cells/" + MegaCellsCompat.cellId(i)));
            shaped(RecipeCategory.MISC, cell)
                    .pattern("h")
                    .pattern("c")
                    .define('h', housing)
                    .define('c', component)
                    .unlockedBy("has_component", has(component))
                    .unlockedBy("has_housing", has(housing))
                    .save(gated, key("cells/" + MegaCellsCompat.cellId(i) + "_from_housing"));
            gated.accept(key("cell_disassembly/" + MegaCellsCompat.cellId(i)),
                    new StorageCellDisassemblyRecipe(cell, List.of(new ItemStackTemplate(housing), new ItemStackTemplate(component.asItem()))),
                    null);
        }
    }
}
