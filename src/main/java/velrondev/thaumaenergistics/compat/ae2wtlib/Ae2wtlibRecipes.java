package velrondev.thaumaenergistics.compat.ae2wtlib;

import java.util.List;

import de.mari_023.ae2wtlib.api.registration.WTDefinition;
import de.mari_023.ae2wtlib.wut.recipe.Combine;
import de.mari_023.ae2wtlib.wut.recipe.Upgrade;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.core.TUUWtlib;
import velrondev.thaumaenergistics.registry.TUUItems;

public final class Ae2wtlibRecipes {

    private static final List<String> COMBINED_WITH = List.of("crafting", "pattern_access", "pattern_encoding");
    private static final List<String> ESSENTIA_COMBINED_WITH = List.of("crafting", "pattern_access", "pattern_encoding", Ae2wtlibCompat.TERMINAL_NAME);

    private Ae2wtlibRecipes() {
    }

    public static void build(RecipeOutput output) {
        ICondition condition = new ModLoadedCondition(TUUWtlib.MOD_ID);
        Ingredient arcane = Ingredient.of(TUUItems.WIRELESS_ARCANE_TERMINAL.get());
        WTDefinition arcaneDefinition = WTDefinition.of(Ae2wtlibCompat.TERMINAL_NAME);

        output.accept(id("wtlib/upgrade_arcane"), new Upgrade(arcane, arcaneDefinition), null, condition);

        for (String name : COMBINED_WITH) {
            WTDefinition other = WTDefinition.of(name);
            output.accept(id("wtlib/combine_arcane_" + name), new Combine(arcane, Ingredient.of(other.item()), arcaneDefinition, other), null, condition);
        }

        Ingredient essentia = Ingredient.of(TUUItems.WIRELESS_ESSENTIA_TERMINAL.get());
        WTDefinition essentiaDefinition = WTDefinition.of(Ae2wtlibCompat.ESSENTIA_TERMINAL_NAME);

        output.accept(id("wtlib/upgrade_essentia"), new Upgrade(essentia, essentiaDefinition), null, condition);

        for (String name : ESSENTIA_COMBINED_WITH) {
            WTDefinition other = WTDefinition.of(name);
            output.accept(id("wtlib/combine_essentia_" + name), new Combine(essentia, Ingredient.of(other.item()), essentiaDefinition, other), null, condition);
        }
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, path);
    }
}
