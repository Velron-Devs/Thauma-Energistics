package velrondev.thaumaenergistics.registry;

import java.util.function.Supplier;

import appeng.items.tools.powered.WirelessTerminalItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.cell.EssentiaCellItem;
import velrondev.thaumaenergistics.cell.EssentiaCreativeCellItem;
import velrondev.thaumaenergistics.cell.InfinityEssentiaCellItem;
import velrondev.thaumaenergistics.config.TEConfig;
import velrondev.thaumaenergistics.core.TEWtlib;
import velrondev.thaumaenergistics.item.ArcaneChargingUpgradeItem;

public final class TEItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);

    public static final DeferredItem<Item> ESSENTIA_COMPONENT_1K = ITEMS.registerSimpleItem("essentia_component_1k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_4K = ITEMS.registerSimpleItem("essentia_component_4k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_16K = ITEMS.registerSimpleItem("essentia_component_16k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_64K = ITEMS.registerSimpleItem("essentia_component_64k");
    public static final DeferredItem<Item> ESSENTIA_COMPONENT_256K = ITEMS.registerSimpleItem("essentia_component_256k");

    public static final DeferredItem<Item> DIFFUSION_CORE = ITEMS.registerSimpleItem("diffusion_core");
    public static final DeferredItem<Item> COALESCENCE_CORE = ITEMS.registerSimpleItem("coalescence_core");

    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_1K = cell("1k", 1, TEConfig.CELL_1K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_4K = cell("4k", 4, TEConfig.CELL_4K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_16K = cell("16k", 16, TEConfig.CELL_16K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_64K = cell("64k", 64, TEConfig.CELL_64K_IDLE_DRAIN::get);
    public static final DeferredItem<EssentiaCellItem> ESSENTIA_CELL_256K = cell("256k", 256, TEConfig.CELL_256K_IDLE_DRAIN::get);

    public static final DeferredItem<InfinityEssentiaCellItem> ESSENTIA_CELL_INFINITY = ITEMS.registerItem("essentia_cell_infinity", properties -> new InfinityEssentiaCellItem(properties.stacksTo(1).rarity(Rarity.EPIC), TEConfig.INFINITY_CELL_IDLE_DRAIN::get));

    public static final DeferredItem<EssentiaCreativeCellItem> ESSENTIA_CELL_CREATIVE = ITEMS.registerItem("essentia_cell_creative", properties -> new EssentiaCreativeCellItem(properties.stacksTo(1)));

    public static final DeferredItem<WirelessTerminalItem> WIRELESS_ARCANE_TERMINAL = ITEMS.registerItem("wireless_arcane_terminal", properties -> TEWtlib.createWirelessArcaneTerminal(properties.stacksTo(1)));

    public static final DeferredItem<WirelessTerminalItem> WIRELESS_ESSENTIA_TERMINAL = ITEMS.registerItem("wireless_essentia_terminal", properties -> TEWtlib.createWirelessEssentiaTerminal(properties.stacksTo(1)));

    public static final DeferredItem<ArcaneChargingUpgradeItem> UPGRADE_ARCANE = ITEMS.registerItem("upgrade_arcane", ArcaneChargingUpgradeItem::new);

    private TEItems() {
    }

    private static DeferredItem<EssentiaCellItem> cell(String size, int kilobytes, Supplier<Double> idleDrain) {
        return ITEMS.registerItem("essentia_cell_" + size, properties -> new EssentiaCellItem(properties.stacksTo(1), kilobytes, idleDrain));
    }

    public static EssentiaCellItem[] cells() {
        return new EssentiaCellItem[] {ESSENTIA_CELL_1K.get(), ESSENTIA_CELL_4K.get(), ESSENTIA_CELL_16K.get(), ESSENTIA_CELL_64K.get(), ESSENTIA_CELL_256K.get(), ESSENTIA_CELL_INFINITY.get()};
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
