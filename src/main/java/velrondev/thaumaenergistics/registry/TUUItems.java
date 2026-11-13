package velrondev.thaumaenergistics.registry;

import java.util.function.Supplier;

import appeng.items.tools.powered.WirelessTerminalItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.cell.EssentiaCellItem;
import velrondev.thaumaenergistics.cell.EssentiaCreativeCellItem;
import velrondev.thaumaenergistics.config.TUUConfig;
import velrondev.thaumaenergistics.core.TUUWtlib;
import velrondev.thaumaenergistics.item.ArcaneChargingUpgradeItem;

public final class TUUItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TUU.MOD_ID);

    public static final DeferredItem<Item> ESSENTIA_COMPONENT_1K = ITEMS.registerSimpleItem("essentia_component_1k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_4K = ITEMS.registerSimpleItem("essentia_component_4k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_16K = ITEMS.registerSimpleItem("essentia_component_16k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_64K = ITEMS.registerSimpleItem("essentia_component_64k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_256K = ITEMS.registerSimpleItem("essentia_component_256k");

    public static final DeferredItem<Item> DIFFUSION_CORE = ITEMS.registerSimpleItem("diffusion_core");
    public static final DeferredItem<Item> COALESCENCE_CORE = ITEMS.registerSimpleItem("coalescence_core");

    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_1K = cell("1k", 1, TUUConfig.CELL_1K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_4K = cell("4k", 4, TUUConfig.CELL_4K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_16K = cell("16k", 16, TUUConfig.CELL_16K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_64K = cell("64k", 64, TUUConfig.CELL_64K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_256K = cell("256k", 256, TUUConfig.CELL_256K_IDLE_DRAIN::get);

    public static final DeferredItem<EssentiaCreativeCellItem> ESSENTIA_CELL_CREATIVE = ITEMS.register("essentia_cell_creative",
            () -> new EssentiaCreativeCellItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<WirelessTerminalItem> WIRELESS_ARCANE_TERMINAL = ITEMS.register("wireless_arcane_terminal",
            TUUWtlib::createWirelessArcaneTerminal);

    public static final DeferredItem<WirelessTerminalItem> WIRELESS_ESSENTIA_TERMINAL = ITEMS.register("wireless_essentia_terminal",
            TUUWtlib::createWirelessEssentiaTerminal);

    public static final DeferredItem<ArcaneChargingUpgradeItem> UPGRADE_ARCANE = ITEMS.register("upgrade_arcane",
            () -> new ArcaneChargingUpgradeItem(new Item.Properties()));

    private TUUItems() {
    }

    private static DeferredItem<EssentiaCellItem> cell(String size, int kilobytes, Supplier<Double> idleDrain) {
        return ITEMS.register("essentia_cell_" + size, () -> new EssentiaCellItem(new Item.Properties().stacksTo(1), kilobytes, idleDrain));
    }

    public static EssentiaCellItem[] cells() {
        return new EssentiaCellItem[] {ESSENTIA_CELL_1K.get(), ESSENTIA_CELL_4K.get(), ESSENTIA_CELL_16K.get(), ESSENTIA_CELL_64K.get(), ESSENTIA_CELL_256K.get()};
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
