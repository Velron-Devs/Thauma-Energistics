package velrondev.thaumaenergistics.compat.ae2megathings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import appeng.api.client.StorageCellModels;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import gripe._90.megacells.definition.MEGAItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.EssentiaDiskItem;
import velrondev.thaumaenergistics.config.TUUCompatConfig;

public final class Ae2MegaThingsCompat {

    public static final String MOD_ID = "ae2_mega_things";
    public static final String HOUSING_ID = "mega_essentia_disk_housing";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TUU.MOD_ID);
    private static final DeferredItem<Item> HOUSING = ITEMS.register(HOUSING_ID, () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    private static final List<DeferredItem<EssentiaDiskItem>> DISKS = new ArrayList<>();

    static {
        List<Supplier<Item>> components = List.of(
                () -> MEGAItems.CELL_COMPONENT_1M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_4M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_16M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_64M.asItem(),
                () -> MEGAItems.CELL_COMPONENT_256M.asItem());
        for (int i = 0; i < TUUCompatConfig.MEGA_SIZE_NAMES.length; i++) {
            int index = i;
            DISKS.add(ITEMS.register(diskId(i), () -> new EssentiaDiskItem(
                    () -> TUUCompatConfig.MEGA_DISK_KILOBYTES_VALUES.get(index).get(),
                    () -> TUUCompatConfig.MEGA_DISK_DRAIN_VALUES.get(index).get(),
                    components.get(index),
                    () -> HOUSING.get())));
        }
    }

    private Ae2MegaThingsCompat() {
    }

    public static String diskId(int index) {
        return "essentia_disk_" + TUUCompatConfig.MEGA_SIZE_NAMES[index];
    }

    public static DeferredItem<Item> housing() {
        return HOUSING;
    }

    public static List<DeferredItem<EssentiaDiskItem>> disks() {
        return DISKS;
    }

    public static ResourceLocation driveModel(int index) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "block/drive/" + diskId(index));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(Ae2MegaThingsCompat::setup));
    }

    private static void setup() {
        for (int i = 0; i < DISKS.size(); i++) {
            Item disk = DISKS.get(i).get();
            Upgrades.add(AEItems.INVERTER_CARD, disk, 1, Ae2ThingsCompat.UPGRADE_GROUP);
            StorageCellModels.registerModel(disk, driveModel(i));
        }
    }
}
