package velrondev.thaumaenergistics.compat.megacells;

import java.util.ArrayList;
import java.util.List;

import appeng.api.client.StorageCellModels;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import appeng.items.storage.BasicStorageCell;
import gripe._90.megacells.definition.MEGAItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.cell.EssentiaCellItem;
import velrondev.thaumaenergistics.config.TUUCompatConfig;

public final class MegaCellsCompat {

    public static final String MOD_ID = "megacells";
    public static final String HOUSING_ID = "mega_essentia_cell_housing";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TUU.MOD_ID);
    private static final DeferredItem<Item> HOUSING = ITEMS.registerSimpleItem(HOUSING_ID);
    private static final List<DeferredItem<EssentiaCellItem>> CELLS = new ArrayList<>();

    static {
        for (int i = 0; i < TUUCompatConfig.MEGA_SIZE_NAMES.length; i++) {
            int index = i;
            CELLS.add(ITEMS.register(cellId(i), () -> new EssentiaCellItem(new Item.Properties().stacksTo(1),
                    () -> TUUCompatConfig.MEGA_CELL_KILOBYTES_VALUES.get(index).get(),
                    () -> TUUCompatConfig.MEGA_CELL_DRAIN_VALUES.get(index).get())));
        }
    }

    private MegaCellsCompat() {
    }

    public static String cellId(int index) {
        return "essentia_cell_" + TUUCompatConfig.MEGA_SIZE_NAMES[index];
    }

    public static ResourceLocation driveModel(int index) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "block/drive/" + cellId(index));
    }

    public static DeferredItem<Item> housing() {
        return HOUSING;
    }

    public static List<DeferredItem<EssentiaCellItem>> cells() {
        return CELLS;
    }

    public static ItemLike component(int index) {
        return switch (index) {
            case 0 -> MEGAItems.CELL_COMPONENT_1M;
            case 1 -> MEGAItems.CELL_COMPONENT_4M;
            case 2 -> MEGAItems.CELL_COMPONENT_16M;
            case 3 -> MEGAItems.CELL_COMPONENT_64M;
            default -> MEGAItems.CELL_COMPONENT_256M;
        };
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(MegaCellsCompat::setup));
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(RegisterColorHandlersEvent.Item.class, MegaCellsCompat::registerItemColors);
        }
    }

    private static void setup() {
        String group = GuiText.StorageCells.getTranslationKey();
        for (int i = 0; i < CELLS.size(); i++) {
            Item cell = CELLS.get(i).get();
            Upgrades.add(AEItems.INVERTER_CARD, cell, 1, group);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell, 1, group);
            Upgrades.add(AEItems.VOID_CARD, cell, 1, group);
            StorageCellModels.registerModel(cell, driveModel(i));
        }
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemLike[] cells = CELLS.stream().map(DeferredItem::get).toArray(ItemLike[]::new);
        event.register((stack, tintIndex) -> FastColor.ARGB32.opaque(BasicStorageCell.getColor(stack, tintIndex)), cells);
    }
}
