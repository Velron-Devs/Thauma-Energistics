package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.glodblock.github.extendedae.common.EAESingletons;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTBlocks;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.recipes.game.StorageCellDisassemblyRecipe;
import gripe._90.megacells.definition.MEGAItems;
import io.github.projectet.ae2things.item.AETItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.registries.DeferredItem;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.condition.TUUFeatureCondition;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUProcessors;

public class TUUCompatRecipes implements DataProvider {

    private static final List<DeferredItem<Item>> COMPONENTS = List.of(
            TUUItems.ESSENTIA_COMPONENT_1K,
            TUUItems.ESSENTIA_COMPONENT_4K,
            TUUItems.ESSENTIA_COMPONENT_16K,
            TUUItems.ESSENTIA_COMPONENT_64K,
            TUUItems.ESSENTIA_COMPONENT_256K);

    private final RecipeProvider delegate;

    public TUUCompatRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.delegate = new RecipeProvider(output, TUUAspectLookups.with(registries, ExtendedAeCompat.CRYSTAL_ASPECTS)) {
            @Override
            protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider lookup) {
                TUUCompatRecipes.this.build(recipeOutput, lookup);
            }
        };
    }

    private void build(RecipeOutput output, HolderLookup.Provider lookup) {
        if (TUUCompat.thingsActive()) {
            essentiaDisks(output);
        }
        if (TUUCompat.megaThingsActive()) {
            megaEssentiaDisks(output);
        }
        if (TUUCompat.megaCellsActive()) {
            megaEssentiaCells(output);
        }
        if (TUUCompat.extendedAeActive()) {
            matrixTiers(output);
            infiniteCells(output, lookup);
        }
    }

    private void essentiaDisks(RecipeOutput base) {
        RecipeOutput output = gate(base, Ae2ThingsCompat.MOD_ID, TUUCompatConfig.FEATURE_ESSENTIA_DISKS);
        for (int i = 0; i < COMPONENTS.size(); i++) {
            Item disk = Ae2ThingsCompat.disks().get(i).get();
            Item component = COMPONENTS.get(i).get();
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, disk)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ded")
                    .define('a', AEBlocks.QUARTZ_GLASS)
                    .define('b', Tags.Items.DUSTS_REDSTONE)
                    .define('c', component)
                    .define('d', Tags.Items.INGOTS_NETHERITE)
                    .define('e', Tags.Items.GEMS_AMETHYST)
                    .unlockedBy("has_component", TUURecipeSupport.has(component))
                    .save(output, id("disks/" + Ae2ThingsCompat.diskId(i)));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, disk)
                    .pattern("h")
                    .pattern("c")
                    .define('h', AETItems.DISK_HOUSING)
                    .define('c', component)
                    .unlockedBy("has_component", TUURecipeSupport.has(component))
                    .unlockedBy("has_housing", TUURecipeSupport.has(AETItems.DISK_HOUSING))
                    .save(output, id("disks/" + Ae2ThingsCompat.diskId(i) + "_from_housing"));
        }
    }

    private void megaEssentiaDisks(RecipeOutput base) {
        RecipeOutput output = gate(base, Ae2MegaThingsCompat.MOD_ID, TUUCompatConfig.FEATURE_MEGA_ESSENTIA_DISKS);
        Item housing = Ae2MegaThingsCompat.housing().get();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, housing)
                .pattern("qrq")
                .pattern("r r")
                .pattern("nmn")
                .define('q', AEBlocks.QUARTZ_VIBRANT_GLASS)
                .define('r', TTItems.SALIS_MUNDUS.get())
                .define('n', ProcessorMetal.VOID.ingot())
                .define('m', MEGAItems.MEGA_ITEM_CELL_HOUSING)
                .unlockedBy("has_mega_housing", TUURecipeSupport.has(MEGAItems.MEGA_ITEM_CELL_HOUSING))
                .save(output, id("disks/" + Ae2MegaThingsCompat.HOUSING_ID));
        for (int i = 0; i < Ae2MegaThingsCompat.disks().size(); i++) {
            Item disk = Ae2MegaThingsCompat.disks().get(i).get();
            ItemLike component = MegaCellsCompat.component(i);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, disk)
                    .pattern("qrq")
                    .pattern("rcr")
                    .pattern("nmn")
                    .define('q', AEBlocks.QUARTZ_VIBRANT_GLASS)
                    .define('r', Tags.Items.DUSTS_REDSTONE)
                    .define('c', component)
                    .define('n', Tags.Items.INGOTS_NETHERITE)
                    .define('m', MEGAItems.MEGA_ITEM_CELL_HOUSING)
                    .unlockedBy("has_component", TUURecipeSupport.has(component))
                    .save(output, id("disks/" + Ae2MegaThingsCompat.diskId(i)));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, disk)
                    .pattern("h")
                    .pattern("c")
                    .define('h', housing)
                    .define('c', component)
                    .unlockedBy("has_component", TUURecipeSupport.has(component))
                    .unlockedBy("has_housing", TUURecipeSupport.has(housing))
                    .save(output, id("disks/" + Ae2MegaThingsCompat.diskId(i) + "_from_housing"));
        }
    }

    private void megaEssentiaCells(RecipeOutput base) {
        RecipeOutput output = gate(base, MegaCellsCompat.MOD_ID, TUUCompatConfig.FEATURE_MEGA_CELLS);
        Item housing = MegaCellsCompat.housing().get();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, housing)
                .pattern("aba")
                .pattern("b b")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_VIBRANT_GLASS)
                .define('b', TTItems.SALIS_MUNDUS.get())
                .define('d', ProcessorMetal.VOID.ingot())
                .unlockedBy("has_salis_mundus", TUURecipeSupport.has(TTItems.SALIS_MUNDUS.get()))
                .save(output, id("cells/" + MegaCellsCompat.HOUSING_ID));
        for (int i = 0; i < MegaCellsCompat.cells().size(); i++) {
            Item cell = MegaCellsCompat.cells().get(i).get();
            ItemLike component = MegaCellsCompat.component(i);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cell)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ddd")
                    .define('a', AEBlocks.QUARTZ_VIBRANT_GLASS)
                    .define('b', TTItems.SALIS_MUNDUS.get())
                    .define('c', component)
                    .define('d', ProcessorMetal.VOID.ingot())
                    .unlockedBy("has_component", TUURecipeSupport.has(component))
                    .save(output, id("cells/" + MegaCellsCompat.cellId(i)));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cell)
                    .pattern("h")
                    .pattern("c")
                    .define('h', housing)
                    .define('c', component)
                    .unlockedBy("has_component", TUURecipeSupport.has(component))
                    .unlockedBy("has_housing", TUURecipeSupport.has(housing))
                    .save(output, id("cells/" + MegaCellsCompat.cellId(i) + "_from_housing"));
            output.accept(id("cell_disassembly/" + MegaCellsCompat.cellId(i)),
                    new StorageCellDisassemblyRecipe(cell, List.of(new ItemStack(housing), new ItemStack(component))), null);
        }
    }

    private void matrixTiers(RecipeOutput base) {
        ICondition mod = new ModLoadedCondition(ExtendedAeCompat.MOD_ID);
        ICondition feature = new TUUFeatureCondition(TUUCompatConfig.FEATURE_MATRIX_TIERS);
        ICondition processors = new TUUFeatureCondition(TUUCompatConfig.FEATURE_PROCESSORS);
        RecipeOutput withProcessors = base.withConditions(mod, feature, processors);
        RecipeOutput withoutProcessors = base.withConditions(mod, feature, new NotCondition(processors));
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            matrixBlock(withProcessors, metal, ExtendedAeCompat.CRAFTER, ProcessorKind.CALCULATION, true);
            matrixBlock(withProcessors, metal, ExtendedAeCompat.PATTERN, ProcessorKind.LOGIC, true);
            matrixBlock(withProcessors, metal, ExtendedAeCompat.SPEED, ProcessorKind.ENGINEERING, true);
            matrixBlock(withoutProcessors, metal, ExtendedAeCompat.CRAFTER, ProcessorKind.CALCULATION, false);
            matrixBlock(withoutProcessors, metal, ExtendedAeCompat.PATTERN, ProcessorKind.LOGIC, false);
            matrixBlock(withoutProcessors, metal, ExtendedAeCompat.SPEED, ProcessorKind.ENGINEERING, false);
        }
    }

    private void matrixBlock(RecipeOutput output, ProcessorMetal metal, String kind, ProcessorKind processorKind, boolean metalProcessors) {
        ItemLike previous = metal.previous()
                .<ItemLike>map(prev -> ExtendedAeCompat.blockItemHolder(kind, prev).get())
                .orElseGet(() -> baseMatrixBlock(kind));
        ItemLike processor = metalProcessors ? TUUProcessors.processor(metal, processorKind).get() : processorKind.baseProcessor();
        ItemLike result = ExtendedAeCompat.blockItemHolder(kind, metal).get();
        String path = "matrix/" + ExtendedAeCompat.matrixId(kind, metal) + (metalProcessors ? "" : "_ae2");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("PXP")
                .pattern("FCF")
                .pattern("PXP")
                .define('P', metal.plate())
                .define('X', processor)
                .define('F', AEItems.FLUIX_CRYSTAL)
                .define('C', previous)
                .unlockedBy("has_previous", TUURecipeSupport.has(previous))
                .save(output, id(path));
    }

    private static ItemLike baseMatrixBlock(String kind) {
        return switch (kind) {
            case ExtendedAeCompat.CRAFTER -> EAESingletons.ASSEMBLER_MATRIX_CRAFTER;
            case ExtendedAeCompat.PATTERN -> EAESingletons.ASSEMBLER_MATRIX_PATTERN;
            default -> EAESingletons.ASSEMBLER_MATRIX_SPEED;
        };
    }

    private void infiniteCells(RecipeOutput base, HolderLookup.Provider lookup) {
        RecipeOutput output = gate(base, ExtendedAeCompat.MOD_ID, TUUCompatConfig.FEATURE_INFINITY_CELLS);
        Item arcaneStone = TTBlocks.STONE_ARCANE.get().asItem();
        infiniteCell(output, ExtendedAeCompat.ARCANE_STONE, Ingredient.of(arcaneStone), TUURecipeSupport.has(arcaneStone));
        for (ResourceKey<IAspect> aspect : ExtendedAeCompat.CRYSTAL_ASPECTS) {
            Holder<IAspect> holder = lookup.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(aspect);
            Ingredient crystal = DataComponentIngredient.of(false, TTDataComponents.CRYSTAL_ASPECT, new AspectInstance(holder, 1), TTItems.ESSENTIA_CRYSTAL.get());
            infiniteCell(output, aspect.location().getPath(), crystal, TUURecipeSupport.has(TTItems.ESSENTIA_CRYSTAL.get()));
        }
    }

    private void infiniteCell(RecipeOutput output, String name, Ingredient material,
            Criterion<InventoryChangeTrigger.TriggerInstance> unlock) {
        Item cell = ExtendedAeCompat.infiniteCells().get(name).get();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cell)
                .pattern("CMC")
                .pattern("MXM")
                .pattern("III")
                .define('C', AEBlocks.QUARTZ_GLASS)
                .define('M', material)
                .define('X', AEItems.CELL_COMPONENT_16K)
                .define('I', ProcessorMetal.THAUMIUM.plate())
                .unlockedBy("has_material", unlock)
                .save(output, id("infinity_cells/" + ExtendedAeCompat.infiniteCellId(name)));
    }

    private static RecipeOutput gate(RecipeOutput output, String modId, String feature) {
        return TUURecipeSupport.modAndFeature(output, modId, feature);
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
        return "Compat Recipes";
    }
}
