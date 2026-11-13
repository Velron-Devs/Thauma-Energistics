package velrondev.thaumaenergistics.datagen.providers;

import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapedRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TEBlocks;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEProcessors;

public class TEThaumaturgeRecipes extends TERecipeProvider {

    public static final Identifier DIFFUSION_CORE = TEResearch.mod("arcane_workbench/diffusion_core");
    public static final Identifier COALESCENCE_CORE = TEResearch.mod("arcane_workbench/coalescence_core");
    public static final Identifier COMPONENT_1K = TEResearch.mod("arcane_workbench/essentia_component_1k");
    public static final Identifier COMPONENT_4K = TEResearch.mod("arcane_workbench/essentia_component_4k");
    public static final Identifier COMPONENT_16K = TEResearch.mod("arcane_workbench/essentia_component_16k");
    public static final Identifier COMPONENT_64K = TEResearch.mod("arcane_workbench/essentia_component_64k");
    public static final Identifier COMPONENT_256K = TEResearch.mod("arcane_workbench/essentia_component_256k");
    public static final Identifier INFUSION_PROVIDER = TEResearch.mod("infusion/infusion_provider");

    public static final Identifier INFINITY_CELL = TEResearch.mod("infusion/infinity_cell");

    private static final int INFINITY_INSTABILITY = 8;
    private static final int INFINITY_TERMINUS = 50;
    private static final int INFINITY_VACUOS = 60;
    private static final int INFINITY_PRAECANTATIO = 50;
    private static final int INFINITY_POTENTIA = 40;
    private static final int INFINITY_MACHINA = 40;
    private static final int CORE_VIS = 10;
    private static final int COMPONENT_VIS = 10;
    private static final int PROVIDER_INSTABILITY = 2;
    private static final int ASPECT_MACHINA = 25;
    private static final int ASPECT_PRAECANTATIO = 25;
    private static final int ASPECT_PERMUTATIO = 20;

    public TEThaumaturgeRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<IAspect> aspects = aspects(registries);

        Item salis = TTItems.SALIS_MUNDUS.get();
        Item nuggetQuicksilver = TTItems.NUGGET_QUICKSILVER.get();
        TagKey<Item> certusQuartz = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("ae2", "all_certus_quartz"));
        TagKey<Item> netherQuartz = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("ae2", "all_nether_quartz"));

        arcane(itemLookup, aspects, TEItems.COALESCENCE_CORE.get(), 2, CORE_VIS, TEResearch.DIGISENTIA)
                .pattern("SSS")
                .pattern("QFL")
                .pattern("SSS")
                .define('S', nuggetQuicksilver)
                .define('Q', certusQuartz)
                .define('F', AEItems.FLUIX_DUST)
                .define('L', AEItems.LOGIC_PROCESSOR)
                .save(output, key(COALESCENCE_CORE));

        arcane(itemLookup, aspects, TEItems.DIFFUSION_CORE.get(), 2, CORE_VIS, TEResearch.DIGISENTIA)
                .pattern("SSS")
                .pattern("QFL")
                .pattern("SSS")
                .define('S', nuggetQuicksilver)
                .define('Q', netherQuartz)
                .define('F', AEItems.FLUIX_DUST)
                .define('L', AEItems.LOGIC_PROCESSOR)
                .save(output, key(DIFFUSION_CORE));

        firstComponent(TERecipeSupport.when(output, TECompatConfig.FEATURE_PROCESSORS), itemLookup, aspects, certusQuartz,
                TEProcessors.processor(ProcessorMetal.THAUMIUM, ProcessorKind.LOGIC).get(), COMPONENT_1K, salis);
        firstComponent(TERecipeSupport.unless(output, TECompatConfig.FEATURE_PROCESSORS), itemLookup, aspects, certusQuartz,
                AEItems.LOGIC_PROCESSOR, COMPONENT_1K.withSuffix("_ae2"), salis);

        component(itemLookup, aspects, TEItems.ESSENTIA_COMPONENT_4K.get(), TEItems.ESSENTIA_COMPONENT_1K.get(),
                ProcessorMetal.THAUMIUM, ProcessorKind.CALCULATION, TEResearch.STORAGE_4K, COMPONENT_4K, salis);
        component(itemLookup, aspects, TEItems.ESSENTIA_COMPONENT_16K.get(), TEItems.ESSENTIA_COMPONENT_4K.get(),
                ProcessorMetal.THAUMIUM, ProcessorKind.ENGINEERING, TEResearch.STORAGE_16K, COMPONENT_16K, salis);
        component(itemLookup, aspects, TEItems.ESSENTIA_COMPONENT_64K.get(), TEItems.ESSENTIA_COMPONENT_16K.get(),
                ProcessorMetal.BRASS, ProcessorKind.ENGINEERING, TEResearch.STORAGE_64K, COMPONENT_64K, salis);
        component(itemLookup, aspects, TEItems.ESSENTIA_COMPONENT_256K.get(), TEItems.ESSENTIA_COMPONENT_64K.get(),
                ProcessorMetal.VOID, ProcessorKind.ENGINEERING, TEResearch.STORAGE_256K, COMPONENT_256K, salis);

        Ingredient coalescenceCore = Ingredient.of(TEItems.COALESCENCE_CORE.get());
        Ingredient salisMundus = Ingredient.of(salis);
        new InfusionRecipeBuilder(aspects, RecipeCategory.MISC, new ItemStackTemplate(TEBlocks.INFUSION_PROVIDER_ITEM.get()),
                Ingredient.of(AEBlocks.INTERFACE))
                .aspect(TTAspects.MACHINA, ASPECT_MACHINA)
                .aspect(TTAspects.PRAECANTATIO, ASPECT_PRAECANTATIO)
                .aspect(TTAspects.PERMUTATIO, ASPECT_PERMUTATIO)
                .component(coalescenceCore)
                .component(salisMundus)
                .component(coalescenceCore)
                .component(salisMundus)
                .instability(PROVIDER_INSTABILITY)
                .gate(gate(TEResearch.INFUSION_PROVIDER))
                .unlockedBy("has_interface", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.INTERFACE))
                .save(output, key(INFUSION_PROVIDER));

        Ingredient diffusionCore = Ingredient.of(TEItems.DIFFUSION_CORE.get());
        Ingredient component256k = Ingredient.of(TEItems.ESSENTIA_COMPONENT_256K.get());
        new InfusionRecipeBuilder(aspects, RecipeCategory.MISC, new ItemStackTemplate(TEItems.ESSENTIA_CELL_INFINITY.get()),
                Ingredient.of(TEItems.ESSENTIA_CELL_256K.get()))
                .aspect(TEResearch.TERMINUS, INFINITY_TERMINUS)
                .aspect(TTAspects.VACUOS, INFINITY_VACUOS)
                .aspect(TTAspects.PRAECANTATIO, INFINITY_PRAECANTATIO)
                .aspect(TTAspects.POTENTIA, INFINITY_POTENTIA)
                .aspect(TTAspects.MACHINA, INFINITY_MACHINA)
                .component(component256k)
                .component(diffusionCore)
                .component(component256k)
                .component(diffusionCore)
                .instability(INFINITY_INSTABILITY)
                .gate(gate(TEResearch.INFINITY_CELL))
                .unlockedBy("has_cell", InventoryChangeTrigger.TriggerInstance.hasItems(TEItems.ESSENTIA_CELL_256K.get()))
                .save(output.withConditions(new ModLoadedCondition(TEResearch.TERMINUS_MOD)), key(INFINITY_CELL));
    }

    private void component(HolderGetter<Item> itemLookup, HolderGetter<IAspect> aspects, Item result, Item previous,
            ProcessorMetal metal, ProcessorKind kind, Identifier research, Identifier id, Item salis) {
        craftComponent(TERecipeSupport.when(output, TECompatConfig.FEATURE_PROCESSORS), itemLookup, aspects, result, previous,
                TEProcessors.processor(metal, kind).get(), research, id, salis);
        craftComponent(TERecipeSupport.unless(output, TECompatConfig.FEATURE_PROCESSORS), itemLookup, aspects, result, previous,
                kind.baseProcessor(), research, id.withSuffix("_ae2"), salis);
    }

    private void firstComponent(RecipeOutput recipeOutput, HolderGetter<Item> itemLookup, HolderGetter<IAspect> aspects,
            TagKey<Item> certusQuartz, ItemLike processor, Identifier id, Item salis) {
        arcane(itemLookup, aspects, TEItems.ESSENTIA_COMPONENT_1K.get(), 1, COMPONENT_VIS, TEResearch.STORAGE_1K)
                .pattern("SQS")
                .pattern("QPQ")
                .pattern("SQS")
                .define('S', salis)
                .define('Q', certusQuartz)
                .define('P', processor)
                .save(recipeOutput, key(id));
    }

    private void craftComponent(RecipeOutput recipeOutput, HolderGetter<Item> itemLookup, HolderGetter<IAspect> aspects, Item result,
            Item previous, ItemLike processor, Identifier research, Identifier id, Item salis) {
        arcane(itemLookup, aspects, result, 1, COMPONENT_VIS, research)
                .pattern("SPS")
                .pattern("CGC")
                .pattern("SCS")
                .define('S', salis)
                .define('C', previous)
                .define('P', processor)
                .define('G', AEBlocks.QUARTZ_GLASS)
                .save(recipeOutput, key(id));
    }

    static ArcaneWorkbenchShapedRecipeBuilder arcane(HolderGetter<Item> itemLookup, HolderGetter<IAspect> aspects, Item result, int count,
            int vis, Identifier research) {
        ArcaneWorkbenchShapedRecipeBuilder builder = new ArcaneWorkbenchShapedRecipeBuilder(RecipeCategory.MISC, new ItemStackTemplate(result, count), itemLookup, aspects, vis)
                .gate(gate(research));
        builder.unlockedBy("has_ingredient_base", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.LOGIC_PROCESSOR));
        return builder;
    }

    static ResearchGate gate(Identifier entry) {
        return new ResearchGate(entry, Optional.empty(), false);
    }

    static HolderGetter<IAspect> aspects(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(IAspect.REGISTRY_KEY);
    }
}
