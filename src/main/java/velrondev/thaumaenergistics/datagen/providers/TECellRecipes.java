package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.recipes.game.StorageCellDisassemblyRecipe;
import appeng.recipes.game.StorageCellUpgradeRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.neoforged.neoforge.common.Tags;
import velrondev.thaumaenergistics.registry.TEItems;

public class TECellRecipes extends TERecipeProvider {

    private record Tier(String suffix, Item cell, Item component) {
    }

    public TECellRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        List<Tier> tiers = List.of(
                new Tier("1k", TEItems.ESSENTIA_CELL_1K.get(), TEItems.ESSENTIA_COMPONENT_1K.get()),
                new Tier("4k", TEItems.ESSENTIA_CELL_4K.get(), TEItems.ESSENTIA_COMPONENT_4K.get()),
                new Tier("16k", TEItems.ESSENTIA_CELL_16K.get(), TEItems.ESSENTIA_COMPONENT_16K.get()),
                new Tier("64k", TEItems.ESSENTIA_CELL_64K.get(), TEItems.ESSENTIA_COMPONENT_64K.get()),
                new Tier("256k", TEItems.ESSENTIA_CELL_256K.get(), TEItems.ESSENTIA_COMPONENT_256K.get()));

        for (int i = 0; i < tiers.size(); i++) {
            Tier tier = tiers.get(i);
            craftCell(tier);
            housingCell(tier);
            disassembly(tier);
            for (int j = i + 1; j < tiers.size(); j++) {
                Tier target = tiers.get(j);
                output.accept(key("cell_upgrade/essentia_cell_" + tier.suffix() + "_to_" + target.suffix()),
                        new StorageCellUpgradeRecipe(tier.cell(), target.component(), target.cell(), tier.component()), null);
            }
        }
    }

    private void craftCell(Tier tier) {
        shaped(RecipeCategory.MISC, tier.cell())
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('c', tier.component())
                .define('d', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_component", has(tier.component()))
                .save(output, key("cells/essentia_cell_" + tier.suffix()));
    }

    private void housingCell(Tier tier) {
        shaped(RecipeCategory.MISC, tier.cell())
                .pattern("h")
                .pattern("c")
                .define('h', AEItems.ITEM_CELL_HOUSING)
                .define('c', tier.component())
                .unlockedBy("has_component", has(tier.component()))
                .unlockedBy("has_housing", has(AEItems.ITEM_CELL_HOUSING))
                .save(output, key("cells/essentia_cell_" + tier.suffix() + "_from_housing"));
    }

    private void disassembly(Tier tier) {
        output.accept(key("cell_disassembly/essentia_cell_" + tier.suffix()),
                new StorageCellDisassemblyRecipe(tier.cell(),
                        List.of(new ItemStackTemplate(AEItems.ITEM_CELL_HOUSING.asItem()), new ItemStackTemplate(tier.component()))),
                null);
    }
}
