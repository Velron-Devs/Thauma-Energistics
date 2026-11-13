package velrondev.thaumaenergistics.compat.extendedae;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.glodblock.github.extendedae.common.EAESingletons;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixCrafter;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixPattern;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixSpeed;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;

import appeng.api.client.StorageCellModels;
import appeng.api.storage.StorageCells;
import appeng.block.AEBaseBlockItem;
import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.extendedae.infinity.InfiniteCellHandler;
import velrondev.thaumaenergistics.compat.extendedae.infinity.InfiniteCellItem;
import velrondev.thaumaenergistics.compat.extendedae.infinity.InfiniteTarget;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public final class ExtendedAeCompat {

    public static final String MOD_ID = "extendedae";
    public static final String CRAFTER = "crafter";
    public static final String PATTERN = "pattern";
    public static final String SPEED = "speed";
    public static final String ARCANE_STONE = "arcane_stone";

    public static final List<ResourceKey<IAspect>> CRYSTAL_ASPECTS = List.of(
            TTAspects.TERRA, TTAspects.IGNIS, TTAspects.AER, TTAspects.AQUA, TTAspects.ORDO, TTAspects.PERDITIO);

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TUU.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TUU.MOD_ID);

    private static final Map<ProcessorMetal, DeferredBlock<TierMatrixCrafterBlock>> CRAFTERS = new EnumMap<>(ProcessorMetal.class);
    private static final Map<ProcessorMetal, DeferredBlock<TierMatrixPatternBlock>> PATTERNS = new EnumMap<>(ProcessorMetal.class);
    private static final Map<ProcessorMetal, DeferredBlock<TierMatrixSpeedBlock>> SPEEDS = new EnumMap<>(ProcessorMetal.class);
    private static final Map<String, Map<ProcessorMetal, DeferredItem<BlockItem>>> BLOCK_ITEMS = new HashMap<>();
    private static final Map<String, DeferredItem<InfiniteCellItem>> INFINITE_CELLS = new LinkedHashMap<>();

    static {
        BLOCK_ITEMS.put(CRAFTER, new EnumMap<>(ProcessorMetal.class));
        BLOCK_ITEMS.put(PATTERN, new EnumMap<>(ProcessorMetal.class));
        BLOCK_ITEMS.put(SPEED, new EnumMap<>(ProcessorMetal.class));
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            CRAFTERS.put(metal, BLOCKS.register(matrixId(CRAFTER, metal),
                    () -> new TierMatrixCrafterBlock(metal, () -> blockItem(CRAFTER, metal))));
            PATTERNS.put(metal, BLOCKS.register(matrixId(PATTERN, metal),
                    () -> new TierMatrixPatternBlock(metal, () -> blockItem(PATTERN, metal))));
            SPEEDS.put(metal, BLOCKS.register(matrixId(SPEED, metal),
                    () -> new TierMatrixSpeedBlock(metal, () -> blockItem(SPEED, metal))));
            BLOCK_ITEMS.get(CRAFTER).put(metal, ITEMS.<BlockItem>register(matrixId(CRAFTER, metal),
                    () -> new AEBaseBlockItem(CRAFTERS.get(metal).get(), new Item.Properties())));
            BLOCK_ITEMS.get(PATTERN).put(metal, ITEMS.<BlockItem>register(matrixId(PATTERN, metal),
                    () -> new AEBaseBlockItem(PATTERNS.get(metal).get(), new Item.Properties())));
            BLOCK_ITEMS.get(SPEED).put(metal, ITEMS.<BlockItem>register(matrixId(SPEED, metal),
                    () -> new AEBaseBlockItem(SPEEDS.get(metal).get(), new Item.Properties())));
        }
        INFINITE_CELLS.put(ARCANE_STONE, infiniteCell(ARCANE_STONE, InfiniteTarget.arcaneStone()));
        for (ResourceKey<IAspect> aspect : CRYSTAL_ASPECTS) {
            String name = aspect.location().getPath();
            INFINITE_CELLS.put(name, infiniteCell(name, InfiniteTarget.visCrystal(aspect)));
        }
    }

    private ExtendedAeCompat() {
    }

    public static String matrixId(String kind, ProcessorMetal metal) {
        return "assembler_matrix_" + kind + "_" + metal.id();
    }

    public static String infiniteCellId(String name) {
        return "infinity_cell_" + name;
    }

    public static DeferredBlock<TierMatrixCrafterBlock> crafter(ProcessorMetal metal) {
        return CRAFTERS.get(metal);
    }

    public static DeferredBlock<TierMatrixPatternBlock> pattern(ProcessorMetal metal) {
        return PATTERNS.get(metal);
    }

    public static DeferredBlock<TierMatrixSpeedBlock> speed(ProcessorMetal metal) {
        return SPEEDS.get(metal);
    }

    public static DeferredItem<BlockItem> blockItemHolder(String kind, ProcessorMetal metal) {
        return BLOCK_ITEMS.get(kind).get(metal);
    }

    public static Map<String, DeferredItem<InfiniteCellItem>> infiniteCells() {
        return INFINITE_CELLS;
    }

    private static Item blockItem(String kind, ProcessorMetal metal) {
        return BLOCK_ITEMS.get(kind).get(metal).get();
    }

    private static DeferredItem<InfiniteCellItem> infiniteCell(String name, InfiniteTarget target) {
        return ITEMS.register(infiniteCellId(name), () -> new InfiniteCellItem(target, () -> TUUCompatConfig.INFINITY_CELL_IDLE_DRAIN.get()));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        bus.addListener(BlockEntityTypeAddBlocksEvent.class, ExtendedAeCompat::addBlocks);
        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(ExtendedAeCompat::setup));
    }

    private static void addBlocks(BlockEntityTypeAddBlocksEvent event) {
        bind(blocks(CRAFTERS), TileAssemblerMatrixCrafter.class, EAESingletons.ASSEMBLER_MATRIX_CRAFTER.getBlockEntityType(), event);
        bind(blocks(PATTERNS), TileAssemblerMatrixPattern.class, EAESingletons.ASSEMBLER_MATRIX_PATTERN.getBlockEntityType(), event);
        bind(blocks(SPEEDS), TileAssemblerMatrixSpeed.class, EAESingletons.ASSEMBLER_MATRIX_SPEED.getBlockEntityType(), event);
    }

    private static <B extends Block> List<B> blocks(Map<ProcessorMetal, ? extends DeferredBlock<B>> holders) {
        List<B> blocks = new ArrayList<>();
        holders.values().forEach(holder -> blocks.add(holder.get()));
        return blocks;
    }

    private static <T extends AEBaseBlockEntity> void bind(List<? extends AEBaseEntityBlock<T>> blocks, Class<T> clazz,
            BlockEntityType<T> type, BlockEntityTypeAddBlocksEvent event) {
        for (AEBaseEntityBlock<T> block : blocks) {
            block.setBlockEntity(clazz, type, null, null);
            event.modify(type, block);
        }
    }

    private static void setup() {
        StorageCells.addCellHandler(InfiniteCellHandler.INSTANCE);
        INFINITE_CELLS.forEach((name, cell) -> StorageCellModels.registerModel(cell.get(), infiniteDriveModel(name)));
    }

    public static ResourceLocation infiniteDriveModel(String name) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "block/drive/" + infiniteCellId(name));
    }
}
