package velrondev.thaumaenergistics.compat.ae2megathings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import gripe._90.megacells.definition.MEGAItems;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.EssentiaDiskItem;
import velrondev.thaumaenergistics.config.TECompatConfig;

public final class Ae2MegaThingsCompat {

    public static final String MOD_ID = "ae2_mega_things";
    public static final String HOUSING_ID = "mega_essentia_disk_housing";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);
    private static final DeferredItem<Item> HOUSING = ITEMS.registerItem(HOUSING_ID, properties -> new Item(properties.stacksTo(64).fireResistant()));
    private static final List<DeferredItem<EssentiaDiskItem>> DISKS = new ArrayList<>();

    static {
        List<Supplier<Item>> components = List.of(
                () -> MEGAItems.CELL_COMPONENT_1M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_4M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_16M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_64M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_256M.asItem());
        for (int i = 0; i < TECompatConfig.MEGA_SIZE_NAMES.length; i++) {
            int index = i;
            DISKS.add(ITEMS.registerItem(diskId(i), properties -> new EssentiaDiskItem(
                    properties,
                    () -> TECompatConfig.MEGA_DISK_KILOBYTES_VALUES.get(index).get(),
                    () -> TECompatConfig.MEGA_DISK_DRAIN_VALUES.get(index).get(),
                    components.get(index),
                    () -> HOUSING.get())));
        }
    }

    private Ae2MegaThingsCompat() {
    }

    public static String diskId(int index) {
        return "essentia_disk_" + TECompatConfig.MEGA_SIZE_NAMES[index];
    }

    public static DeferredItem<Item> housing() {
        return HOUSING;
    }

    public static List<DeferredItem<EssentiaDiskItem>> disks() {
        return DISKS;
    }

    public static Identifier driveModel(int index) {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "block/drive/" + diskId(index));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(Ae2MegaThingsCompat::setup));
    }

    private static void setup() {
        for (DeferredItem<EssentiaDiskItem> disk : DISKS) {
            Upgrades.add(AEItems.INVERTER_CARD, disk.get(), 1, Ae2ThingsCompat.UPGRADE_GROUP);
        }
    }
}
