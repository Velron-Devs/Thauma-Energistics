package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.recipes.game.StorageCellDisassemblyRecipe;
import appeng.recipes.game.StorageCellUpgradeRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUItems;

public class TUUCellRecipes implements DataProvider {

    private final RecipeProvider delegate;

    private record Tier(String suffix, Item cell, Item component) {
    }

    public TUUCellRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, registries) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput) {
                TUUCellRecipes.this.build(recipeOutput);
            }
        };
    }

    private void build(RecipeOutput output) {
        List<Tier> tiers = List.of(
                new Tier("1k", TUUItems.ESSENTIA_CELL_1K.get(), TUUItems.ESSENTIA_COMPONENT_1K.get()),
                new Tier("4k", TUUItems.ESSENTIA_CELL_4K.get(), TUUItems.ESSENTIA_COMPONENT_4K.get()),
                new Tier("16k", TUUItems.ESSENTIA_CELL_16K.get(), TUUItems.ESSENTIA_COMPONENT_16K.get()),
                new Tier("64k", TUUItems.ESSENTIA_CELL_64K.get(), TUUItems.ESSENTIA_COMPONENT_64K.get()),
                new Tier("256k", TUUItems.ESSENTIA_CELL_256K.get(), TUUItems.ESSENTIA_COMPONENT_256K.get()));

        for (int i = 0; i < tiers.size(); i++) {
            Tier tier = tiers.get(i);
            craftCell(output, tier);
            housingCell(output, tier);
            disassembly(output, tier);
            for (int j = i + 1; j < tiers.size(); j++) {
                Tier target = tiers.get(j);
                output.accept(id("cell_upgrade/essentia_cell_" + tier.suffix() + "_to_" + target.suffix()),
                        new StorageCellUpgradeRecipe(tier.cell(), target.component(), target.cell(), tier.component()), null);
            }
        }
    }

    private void craftCell(RecipeOutput output, Tier tier) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tier.cell())
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('c', tier.component())
                .define('d', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_component", TUURecipeSupport.has(tier.component()))
                .save(output, id("cells/essentia_cell_" + tier.suffix()));
    }

    private void housingCell(RecipeOutput output, Tier tier) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tier.cell())
                .pattern("h")
                .pattern("c")
                .define('h', AEItems.ITEM_CELL_HOUSING)
                .define('c', tier.component())
                .unlockedBy("has_component", TUURecipeSupport.has(tier.component()))
                .unlockedBy("has_housing", TUURecipeSupport.has(AEItems.ITEM_CELL_HOUSING))
                .save(output, id("cells/essentia_cell_" + tier.suffix() + "_from_housing"));
    }

    private void disassembly(RecipeOutput output, Tier tier) {
        output.accept(id("cell_disassembly/essentia_cell_" + tier.suffix()),
                new StorageCellDisassemblyRecipe(tier.cell(),
                        List.of(AEItems.ITEM_CELL_HOUSING.stack(), new ItemStack(tier.component()))),
                null);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, path);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return delegate.run(cache);
    }

    @Override
    public String getName() {
        return "Cell Recipes";
    }
}
