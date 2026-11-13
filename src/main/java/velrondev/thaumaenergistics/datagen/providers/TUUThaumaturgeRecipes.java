package velrondev.thaumaenergistics.datagen.providers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapedRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TUUBlocks;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUProcessors;


public class TUUThaumaturgeRecipes implements DataProvider {

    public static final ResourceLocation DIFFUSION_CORE = TUUResearch.mod("arcane_workbench/diffusion_core");
    public static final ResourceLocation COALESCENCE_CORE = TUUResearch.mod("arcane_workbench/coalescence_core");
    public static final ResourceLocation COMPONENT_1K = TUUResearch.mod("arcane_workbench/essentia_component_1k");
    public static final ResourceLocation COMPONENT_4K = TUUResearch.mod("arcane_workbench/essentia_component_4k");
    public static final ResourceLocation COMPONENT_16K = TUUResearch.mod("arcane_workbench/essentia_component_16k");
    public static final ResourceLocation COMPONENT_64K = TUUResearch.mod("arcane_workbench/essentia_component_64k");
    public static final ResourceLocation COMPONENT_256K = TUUResearch.mod("arcane_workbench/essentia_component_256k");
    public static final ResourceLocation INFUSION_PROVIDER = TUUResearch.mod("infusion/infusion_provider");

    private static final int CORE_VIS = 10;
    private static final int COMPONENT_VIS = 10;
    private static final int PROVIDER_INSTABILITY = 2;
    private static final int ASPECT_MACHINA = 25;
    private static final int ASPECT_PRAECANTATIO = 25;
    private static final int ASPECT_PERMUTATIO = 20;

    private final RecipeProvider delegate;

    public TUUThaumaturgeRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, TUUAspectLookups.with(registries)) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider lookup) {
                build(recipeOutput, lookup);
            }
        };
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return delegate.run(cache);
    }

    private void build(RecipeOutput output, HolderLookup.Provider registries) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<IAspect> aspects = aspects(registries);

        Item salis = TTItems.SALIS_MUNDUS.get();
        Item nuggetQuicksilver = TTItems.NUGGET_QUICKSILVER.get();
        TagKey<Item> certusQuartz = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("ae2", "all_certus_quartz"));
        TagKey<Item> netherQuartz = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("ae2", "all_nether_quartz"));

        arcane(items, aspects, TUUItems.COALESCENCE_CORE.get(), 2, CORE_VIS, TUUResearch.DIGISENTIA)
                .pattern("SSS")
                .pattern("QFL")
                .pattern("SSS")
                .define('S', nuggetQuicksilver)
                .define('Q', certusQuartz)
                .define('F', AEItems.FLUIX_DUST)
                .define('L', AEItems.LOGIC_PROCESSOR)
                .save(output, COALESCENCE_CORE);

        arcane(items, aspects, TUUItems.DIFFUSION_CORE.get(), 2, CORE_VIS, TUUResearch.DIGISENTIA)
                .pattern("SSS")
                .pattern("QFL")
                .pattern("SSS")
                .define('S', nuggetQuicksilver)
                .define('Q', netherQuartz)
                .define('F', AEItems.FLUIX_DUST)
                .define('L', AEItems.LOGIC_PROCESSOR)
                .save(output, DIFFUSION_CORE);

        firstComponent(TUURecipeSupport.when(output, TUUCompatConfig.FEATURE_PROCESSORS), items, aspects, certusQuartz,
                TUUProcessors.processor(ProcessorMetal.THAUMIUM, ProcessorKind.LOGIC).get(), COMPONENT_1K, salis);
        firstComponent(TUURecipeSupport.unless(output, TUUCompatConfig.FEATURE_PROCESSORS), items, aspects, certusQuartz,
                AEItems.LOGIC_PROCESSOR, COMPONENT_1K.withSuffix("_ae2"), salis);
        component(output, items, aspects, TUUItems.ESSENTIA_COMPONENT_4K.get(), TUUItems.ESSENTIA_COMPONENT_1K.get(),
                ProcessorMetal.THAUMIUM, ProcessorKind.CALCULATION, TUUResearch.STORAGE_4K, COMPONENT_4K, salis);
        component(output, items, aspects, TUUItems.ESSENTIA_COMPONENT_16K.get(), TUUItems.ESSENTIA_COMPONENT_4K.get(),
                ProcessorMetal.THAUMIUM, ProcessorKind.ENGINEERING, TUUResearch.STORAGE_16K, COMPONENT_16K, salis);
        component(output, items, aspects, TUUItems.ESSENTIA_COMPONENT_64K.get(), TUUItems.ESSENTIA_COMPONENT_16K.get(),
                ProcessorMetal.BRASS, ProcessorKind.ENGINEERING, TUUResearch.STORAGE_64K, COMPONENT_64K, salis);
        component(output, items, aspects, TUUItems.ESSENTIA_COMPONENT_256K.get(), TUUItems.ESSENTIA_COMPONENT_64K.get(),
                ProcessorMetal.VOID, ProcessorKind.ENGINEERING, TUUResearch.STORAGE_256K, COMPONENT_256K, salis);

        Ingredient coalescenceCore = Ingredient.of(TUUItems.COALESCENCE_CORE.get());
        Ingredient salisMundus = Ingredient.of(salis);
        new InfusionRecipeBuilder(aspects, RecipeCategory.MISC, new ItemStack(TUUBlocks.INFUSION_PROVIDER_ITEM.get()),
                Ingredient.of(AEBlocks.INTERFACE))
                .aspect(TTAspects.MACHINA, ASPECT_MACHINA)
                .aspect(TTAspects.PRAECANTATIO, ASPECT_PRAECANTATIO)
                .aspect(TTAspects.PERMUTATIO, ASPECT_PERMUTATIO)
                .component(coalescenceCore)
                .component(salisMundus)
                .component(coalescenceCore)
                .component(salisMundus)
                .instability(PROVIDER_INSTABILITY)
                .gate(gate(TUUResearch.INFUSION_PROVIDER))
                .save(output, INFUSION_PROVIDER);
    }

    private void component(RecipeOutput output, HolderGetter<Item> items, HolderGetter<IAspect> aspects, Item result, Item previous,
            ProcessorMetal metal, ProcessorKind kind, ResourceLocation research, ResourceLocation id, Item salis) {
        craftComponent(TUURecipeSupport.when(output, TUUCompatConfig.FEATURE_PROCESSORS), items, aspects, result, previous,
                TUUProcessors.processor(metal, kind).get(), research, id, salis);
        craftComponent(TUURecipeSupport.unless(output, TUUCompatConfig.FEATURE_PROCESSORS), items, aspects, result, previous,
                kind.baseProcessor(), research, id.withSuffix("_ae2"), salis);
    }

    private void firstComponent(RecipeOutput output, HolderGetter<Item> items, HolderGetter<IAspect> aspects, TagKey<Item> certusQuartz,
            ItemLike processor, ResourceLocation id, Item salis) {
        arcane(items, aspects, TUUItems.ESSENTIA_COMPONENT_1K.get(), 1, COMPONENT_VIS, TUUResearch.STORAGE_1K)
                .pattern("SQS")
                .pattern("QPQ")
                .pattern("SQS")
                .define('S', salis)
                .define('Q', certusQuartz)
                .define('P', processor)
                .save(output, id);
    }

    private void craftComponent(RecipeOutput output, HolderGetter<Item> items, HolderGetter<IAspect> aspects, Item result, Item previous,
            ItemLike processor, ResourceLocation research, ResourceLocation id, Item salis) {
        arcane(items, aspects, result, 1, COMPONENT_VIS, research)
                .pattern("SPS")
                .pattern("CGC")
                .pattern("SCS")
                .define('S', salis)
                .define('C', previous)
                .define('P', processor)
                .define('G', AEBlocks.QUARTZ_GLASS)
                .save(output, id);
    }

    static ArcaneWorkbenchShapedRecipeBuilder arcane(HolderGetter<Item> items, HolderGetter<IAspect> aspects, Item result, int count,
            int vis, ResourceLocation research) {
        return new ArcaneWorkbenchShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, count), items, aspects, vis)
                .gate(gate(research));
    }

    static ResearchGate gate(ResourceLocation entry) {
        return new ResearchGate(entry, Optional.empty(), false);
    }

    static HolderGetter<IAspect> aspects(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(IAspect.REGISTRY_KEY);
    }

    @Override
    public String getName() {
        return "Thaumaturge Recipes";
    }
}
