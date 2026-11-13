package velrondev.thaumaenergistics.compat.ae2things;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import appeng.api.storage.StorageCells;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import io.github.projectet.ae2things.item.AETItems;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.registry.TEItems;

public final class Ae2ThingsCompat {

    public static final String MOD_ID = "ae2things";
    public static final String UPGRADE_GROUP = "text.ae2things.disk_drives";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);
    private static final List<DeferredItem<EssentiaDiskItem>> DISKS = new ArrayList<>();

    static {
        List<Supplier<Item>> components = List.of(
                TEItems.ESSENTIA_COMPONENT_1K,
                TEItems.ESSENTIA_COMPONENT_4K,
                TEItems.ESSENTIA_COMPONENT_16K,
                TEItems.ESSENTIA_COMPONENT_64K,
                TEItems.ESSENTIA_COMPONENT_256K);
        for (int i = 0; i < TECompatConfig.SIZE_NAMES.length; i++) {
            int index = i;
            DISKS.add(ITEMS.registerItem(diskId(i), properties -> new EssentiaDiskItem(
                    properties,
                    () -> TECompatConfig.DISK_KILOBYTES_VALUES.get(index).get(),
                    () -> TECompatConfig.DISK_DRAIN_VALUES.get(index).get(),
                    components.get(index),
                    () -> AETItems.DISK_HOUSING.get())));
        }
    }

    private Ae2ThingsCompat() {
    }

    public static String diskId(int index) {
        return "essentia_disk_" + TECompatConfig.SIZE_NAMES[index];
    }

    public static List<DeferredItem<EssentiaDiskItem>> disks() {
        return DISKS;
    }

    public static Identifier driveModel(int index) {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "block/drive/" + diskId(index));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(Ae2ThingsCompat::setup));
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            Ae2ThingsClient.register(bus);
        }
    }

    private static void setup() {
        StorageCells.addCellHandler(EssentiaDiskCellHandler.INSTANCE);
        for (DeferredItem<EssentiaDiskItem> disk : DISKS) {
            Upgrades.add(AEItems.INVERTER_CARD, disk.get(), 1, UPGRADE_GROUP);
        }
    }
}
