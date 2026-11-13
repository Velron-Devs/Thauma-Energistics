package velrondev.thaumaenergistics.compat.ae2things.datagen;

import java.util.List;

import appeng.core.definitions.AEBlocks;
import io.github.projectet.ae2things.item.AETItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.registries.DeferredItem;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.datagen.providers.TERecipeProvider;
import velrondev.thaumaenergistics.registry.TEItems;

public class Ae2ThingsRecipes extends TERecipeProvider {

    private static final List<DeferredItem<Item>> COMPONENTS = List.of(
            TEItems.ESSENTIA_COMPONENT_1K,
            TEItems.ESSENTIA_COMPONENT_4K,
            TEItems.ESSENTIA_COMPONENT_16K,
            TEItems.ESSENTIA_COMPONENT_64K,
            TEItems.ESSENTIA_COMPONENT_256K);

    public Ae2ThingsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        RecipeOutput gated = output.withConditions(
                new ModLoadedCondition(Ae2ThingsCompat.MOD_ID),
                new TEFeatureCondition(TECompatConfig.FEATURE_ESSENTIA_DISKS));
        for (int i = 0; i < COMPONENTS.size(); i++) {
            Item disk = Ae2ThingsCompat.disks().get(i).get();
            Item component = COMPONENTS.get(i).get();
            shaped(RecipeCategory.MISC, disk)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ded")
                    .define('a', AEBlocks.QUARTZ_GLASS)
                    .define('b', Tags.Items.DUSTS_REDSTONE)
                    .define('c', component)
                    .define('d', Tags.Items.INGOTS_NETHERITE)
                    .define('e', Tags.Items.GEMS_AMETHYST)
                    .unlockedBy("has_component", has(component))
                    .save(gated, key("disks/" + Ae2ThingsCompat.diskId(i)));
            shaped(RecipeCategory.MISC, disk)
                    .pattern("h")
                    .pattern("c")
                    .define('h', AETItems.DISK_HOUSING)
                    .define('c', component)
                    .unlockedBy("has_component", has(component))
                    .unlockedBy("has_housing", has(AETItems.DISK_HOUSING))
                    .save(gated, key("disks/" + Ae2ThingsCompat.diskId(i) + "_from_housing"));
        }
    }
}
