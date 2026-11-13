package velrondev.thaumaenergistics.datagen.providers;

import com.glodblock.github.extendedae.common.EAESingletons;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTBlocks;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TEProcessors;

public class TEExtendedAeRecipes extends TERecipeProvider {

    public TEExtendedAeRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        if (!ModList.get().isLoaded(ExtendedAeCompat.MOD_ID)) {
            return;
        }
        matrixTiers();
        infiniteCells();
    }

    private void matrixTiers() {
        ICondition mod = new ModLoadedCondition(ExtendedAeCompat.MOD_ID);
        ICondition feature = new TEFeatureCondition(TECompatConfig.FEATURE_MATRIX_TIERS);
        ICondition processors = new TEFeatureCondition(TECompatConfig.FEATURE_PROCESSORS);
        RecipeOutput withProcessors = output.withConditions(mod, feature, processors);
        RecipeOutput withoutProcessors = output.withConditions(mod, feature, new NotCondition(processors));
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            matrixBlock(withProcessors, metal, ExtendedAeCompat.CRAFTER, ProcessorKind.CALCULATION, true);
            matrixBlock(withProcessors, metal, ExtendedAeCompat.PATTERN, ProcessorKind.LOGIC, true);
            matrixBlock(withProcessors, metal, ExtendedAeCompat.SPEED, ProcessorKind.ENGINEERING, true);
            matrixBlock(withoutProcessors, metal, ExtendedAeCompat.CRAFTER, ProcessorKind.CALCULATION, false);
            matrixBlock(withoutProcessors, metal, ExtendedAeCompat.PATTERN, ProcessorKind.LOGIC, false);
            matrixBlock(withoutProcessors, metal, ExtendedAeCompat.SPEED, ProcessorKind.ENGINEERING, false);
        }
    }

    private void matrixBlock(RecipeOutput target, ProcessorMetal metal, String kind, ProcessorKind processorKind, boolean metalProcessors) {
        ItemLike previous = metal.previous()
                .<ItemLike>map(prev -> ExtendedAeCompat.blockItemHolder(kind, prev).get())
                .orElseGet(() -> baseMatrixBlock(kind));
        ItemLike processor = metalProcessors ? TEProcessors.processor(metal, processorKind).get() : processorKind.baseProcessor();
        ItemLike result = ExtendedAeCompat.blockItemHolder(kind, metal).get();
        String path = "matrix/" + ExtendedAeCompat.matrixId(kind, metal) + (metalProcessors ? "" : "_ae2");
        shaped(RecipeCategory.MISC, result)
                .pattern("PXP")
                .pattern("FCF")
                .pattern("PXP")
                .define('P', metal.plate())
                .define('X', processor)
                .define('F', AEItems.FLUIX_CRYSTAL)
                .define('C', previous)
                .unlockedBy("has_previous", has(previous))
                .save(target, key(path));
    }

    private static ItemLike baseMatrixBlock(String kind) {
        return switch (kind) {
            case ExtendedAeCompat.CRAFTER -> EAESingletons.ASSEMBLER_MATRIX_CRAFTER;
            case ExtendedAeCompat.PATTERN -> EAESingletons.ASSEMBLER_MATRIX_PATTERN;
            default -> EAESingletons.ASSEMBLER_MATRIX_SPEED;
        };
    }

    private void infiniteCells() {
        RecipeOutput gated = TERecipeSupport.modAndFeature(output, ExtendedAeCompat.MOD_ID, TECompatConfig.FEATURE_INFINITY_CELLS);
        Item arcaneStone = TTBlocks.STONE_ARCANE.get().asItem();
        infiniteCell(gated, ExtendedAeCompat.ARCANE_STONE, Ingredient.of(arcaneStone), has(arcaneStone));
        for (ResourceKey<IAspect> aspect : ExtendedAeCompat.CRYSTAL_ASPECTS) {
            Holder<IAspect> holder = registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(aspect);
            Ingredient crystal = DataComponentIngredient.of(false, TTDataComponents.CRYSTAL_ASPECT, new AspectInstance(holder, 1), TTItems.ESSENTIA_CRYSTAL.get());
            infiniteCell(gated, aspect.identifier().getPath(), crystal, has(TTItems.ESSENTIA_CRYSTAL.get()));
        }
    }

    private void infiniteCell(RecipeOutput target, String name, Ingredient material, Criterion<InventoryChangeTrigger.TriggerInstance> unlock) {
        Item cell = ExtendedAeCompat.infiniteCells().get(name).get();
        shaped(RecipeCategory.MISC, cell)
                .pattern("CMC")
                .pattern("MXM")
                .pattern("III")
                .define('C', AEBlocks.QUARTZ_GLASS)
                .define('M', material)
                .define('X', AEItems.CELL_COMPONENT_16K)
                .define('I', ProcessorMetal.THAUMIUM.plate())
                .unlockedBy("has_material", unlock)
                .save(target, key("infinity_cells/" + ExtendedAeCompat.infiniteCellId(name)));
    }
}
