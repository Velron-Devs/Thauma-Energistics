package velrondev.thaumaenergistics.compat.ae2things;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import appeng.api.client.StorageCellModels;
import appeng.api.storage.StorageCells;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import io.github.projectet.ae2things.item.AETItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.registry.TUUItems;

public final class Ae2ThingsCompat {

    public static final String MOD_ID = "ae2things";
    public static final String UPGRADE_GROUP = "text.ae2things.disk_drives";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TUU.MOD_ID);
    private static final List<DeferredItem<EssentiaDiskItem>> DISKS = new ArrayList<>();

    static {
        List<Supplier<Item>> components = List.of(
                TUUItems.ESSENTIA_COMPONENT_1K,
                TUUItems.ESSENTIA_COMPONENT_4K,
                TUUItems.ESSENTIA_COMPONENT_16K,
                TUUItems.ESSENTIA_COMPONENT_64K,
                TUUItems.ESSENTIA_COMPONENT_256K);
        for (int i = 0; i < TUUCompatConfig.SIZE_NAMES.length; i++) {
            int index = i;
            DISKS.add(ITEMS.register(diskId(i), () -> new EssentiaDiskItem(
                    () -> TUUCompatConfig.DISK_KILOBYTES_VALUES.get(index).get(),
                    () -> TUUCompatConfig.DISK_DRAIN_VALUES.get(index).get(),
                    components.get(index),
                    () -> AETItems.DISK_HOUSING.get())));
        }
    }

    private Ae2ThingsCompat() {
    }

    public static String diskId(int index) {
        return "essentia_disk_" + TUUCompatConfig.SIZE_NAMES[index];
    }

    public static List<DeferredItem<EssentiaDiskItem>> disks() {
        return DISKS;
    }

    public static ResourceLocation driveModel(int index) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "block/drive/" + diskId(index));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(Ae2ThingsCompat::setup));
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Ae2ThingsClient.register(bus);
        }
    }

    private static void setup() {
        StorageCells.addCellHandler(EssentiaDiskCellHandler.INSTANCE);
        for (int i = 0; i < DISKS.size(); i++) {
            Item disk = DISKS.get(i).get();
            Upgrades.add(AEItems.INVERTER_CARD, disk, 1, UPGRADE_GROUP);
            StorageCellModels.registerModel(disk, driveModel(i));
        }
    }
}
