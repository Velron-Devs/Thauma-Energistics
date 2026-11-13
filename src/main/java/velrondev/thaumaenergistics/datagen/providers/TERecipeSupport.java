package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;

public final class TERecipeSupport {

    private TERecipeSupport() {
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, item).build());
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, tag).build());
    }

    public static RecipeOutput when(RecipeOutput output, String feature) {
        return output.withConditions(new TEFeatureCondition(feature));
    }

    public static RecipeOutput unless(RecipeOutput output, String feature) {
        return output.withConditions(new NotCondition(new TEFeatureCondition(feature)));
    }

    public static RecipeOutput modAndFeature(RecipeOutput output, String modId, String feature) {
        return output.withConditions(new ModLoadedCondition(modId), new TEFeatureCondition(feature));
    }
}
