package velrondev.thaumaenergistics.datagen.providers;

import appeng.core.definitions.AEItems;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TEProcessors;

public class TEProcessorRecipes extends TERecipeProvider {

    public TEProcessorRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        RecipeOutput gated = TERecipeSupport.when(output, TECompatConfig.FEATURE_PROCESSORS);
        Ingredient redstone = Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(Tags.Items.DUSTS_REDSTONE));

        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                DeferredItem<Item> printed = TEProcessors.printed(metal, kind);
                DeferredItem<Item> processor = TEProcessors.processor(metal, kind);

                InscriberRecipeBuilder.inscribe(Ingredient.of(metal.plate()), printed.get(), 1)
                        .setTop(Ingredient.of(kind.press()))
                        .save(gated, id("inscriber/" + printed.getId().getPath()));

                Ingredient middle = metal.previous()
                        .map(previous -> Ingredient.of(TEProcessors.processor(previous, kind).get()))
                        .orElse(redstone);
                InscriberRecipeBuilder.inscribe(middle, processor.get(), 1)
                        .setTop(Ingredient.of(printed.get()))
                        .setBottom(Ingredient.of(AEItems.SILICON_PRINT))
                        .setMode(InscriberProcessType.PRESS)
                        .save(gated, id("inscriber/" + processor.getId().getPath()));
            }
        }
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path);
    }
}
