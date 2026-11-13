package velrondev.thaumaenergistics.compat.megacells;

import java.util.ArrayList;
import java.util.List;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import gripe._90.megacells.definition.MEGAItems;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.config.TECompatConfig;

public final class MegaCellsCompat {

    public static final String MOD_ID = "megacells";
    public static final String HOUSING_ID = "mega_essentia_cell_housing";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);
    private static final DeferredItem<Item> HOUSING = ITEMS.registerSimpleItem(HOUSING_ID);
    private static final List<DeferredItem<MegaEssentiaCellItem>> CELLS = new ArrayList<>();

    static {
        for (int i = 0; i < TECompatConfig.MEGA_SIZE_NAMES.length; i++) {
            int index = i;
            CELLS.add(ITEMS.registerItem(cellId(i), properties -> new MegaEssentiaCellItem(properties.stacksTo(1),
                    () -> TECompatConfig.MEGA_CELL_KILOBYTES_VALUES.get(index).get(),
                    () -> TECompatConfig.MEGA_CELL_DRAIN_VALUES.get(index).get())));
        }
    }

    private MegaCellsCompat() {
    }

    public static String cellId(int index) {
        return "essentia_cell_" + TECompatConfig.MEGA_SIZE_NAMES[index];
    }

    public static Identifier driveModel(int index) {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "block/drive/" + cellId(index));
    }

    public static DeferredItem<Item> housing() {
        return HOUSING;
    }

    public static List<DeferredItem<MegaEssentiaCellItem>> cells() {
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
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            MegaCellsClient.register(bus);
        }
    }

    private static void setup() {
        String group = GuiText.StorageCells.getTranslationKey();
        for (DeferredItem<MegaEssentiaCellItem> cell : CELLS) {
            Upgrades.add(AEItems.INVERTER_CARD, cell.get(), 1, group);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell.get(), 1, group);
            Upgrades.add(AEItems.VOID_CARD, cell.get(), 1, group);
        }
    }
}
