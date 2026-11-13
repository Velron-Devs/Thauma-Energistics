package velrondev.thaumaenergistics.compat.ae2megathings.datagen;

import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEBlocks;
import gripe._90.megacells.definition.MEGAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.datagen.providers.TERecipeProvider;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class Ae2MegaThingsRecipes extends TERecipeProvider {

    public Ae2MegaThingsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        RecipeOutput gated = output.withConditions(
                new ModLoadedCondition(Ae2MegaThingsCompat.MOD_ID),
                new TEFeatureCondition(TECompatConfig.FEATURE_MEGA_ESSENTIA_DISKS));
        Item housing = Ae2MegaThingsCompat.housing().get();
        shaped(RecipeCategory.MISC, housing)
                .pattern("qrq")
                .pattern("r r")
                .pattern("nmn")
                .define('q', AEBlocks.QUARTZ_VIBRANT_GLASS)
                .define('r', TTItems.SALIS_MUNDUS.get())
                .define('n', ProcessorMetal.VOID.ingot())
                .define('m', MEGAItems.MEGA_ITEM_CELL_HOUSING)
                .unlockedBy("has_mega_housing", has(MEGAItems.MEGA_ITEM_CELL_HOUSING))
                .save(gated, key("disks/" + Ae2MegaThingsCompat.HOUSING_ID));
        for (int i = 0; i < Ae2MegaThingsCompat.disks().size(); i++) {
            Item disk = Ae2MegaThingsCompat.disks().get(i).get();
            ItemLike component = MegaCellsCompat.component(i);
            shaped(RecipeCategory.MISC, disk)
                    .pattern("qrq")
                    .pattern("rcr")
                    .pattern("nmn")
                    .define('q', AEBlocks.QUARTZ_VIBRANT_GLASS)
                    .define('r', Tags.Items.DUSTS_REDSTONE)
                    .define('c', component)
                    .define('n', Tags.Items.INGOTS_NETHERITE)
                    .define('m', MEGAItems.MEGA_ITEM_CELL_HOUSING)
                    .unlockedBy("has_component", has(component))
                    .save(gated, key("disks/" + Ae2MegaThingsCompat.diskId(i)));
            shaped(RecipeCategory.MISC, disk)
                    .pattern("h")
                    .pattern("c")
                    .define('h', housing)
                    .define('c', component)
                    .unlockedBy("has_component", has(component))
                    .unlockedBy("has_housing", has(housing))
                    .save(gated, key("disks/" + Ae2MegaThingsCompat.diskId(i) + "_from_housing"));
        }
    }
}
