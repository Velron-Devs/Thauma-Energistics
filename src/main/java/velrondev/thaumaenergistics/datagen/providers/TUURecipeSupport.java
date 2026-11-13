package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import velrondev.thaumaenergistics.condition.TUUFeatureCondition;

public abstract class TUURecipeSupport extends RecipeProvider {

    private TUURecipeSupport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
        return RecipeProvider.has(item);
    }

    public static RecipeOutput when(RecipeOutput output, String feature) {
        return output.withConditions(new TUUFeatureCondition(feature));
    }

    public static RecipeOutput unless(RecipeOutput output, String feature) {
        return output.withConditions(new NotCondition(new TUUFeatureCondition(feature)));
    }

    public static RecipeOutput modAndFeature(RecipeOutput output, String modId, String feature) {
        return output.withConditions(new ModLoadedCondition(modId), new TUUFeatureCondition(feature));
    }
}
