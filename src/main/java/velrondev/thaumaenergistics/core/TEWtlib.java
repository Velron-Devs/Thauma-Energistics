package velrondev.thaumaenergistics.core;

import appeng.core.AEConfig;
import appeng.items.tools.powered.WirelessTerminalItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import velrondev.thaumaenergistics.compat.ae2wtlib.Ae2wtlibCompat;
import velrondev.thaumaenergistics.item.WirelessArcaneTerminalItem;
import velrondev.thaumaenergistics.item.WirelessEssentiaTerminalItem;

public final class TEWtlib {

    public static final String MOD_ID = "ae2wtlib";

    private TEWtlib() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void init() {
        if (isLoaded()) {
            Ae2wtlibCompat.init();
        }
    }

    public static WirelessTerminalItem createWirelessArcaneTerminal(Item.Properties properties) {
        if (isLoaded()) {
            return Ae2wtlibCompat.terminalItem(properties);
        }
        return new WirelessArcaneTerminalItem(AEConfig.instance().getWirelessTerminalBattery(), properties);
    }

    public static WirelessTerminalItem createWirelessEssentiaTerminal(Item.Properties properties) {
        if (isLoaded()) {
            return Ae2wtlibCompat.essentiaTerminalItem(properties);
        }
        return new WirelessEssentiaTerminalItem(AEConfig.instance().getWirelessTerminalBattery(), properties);
    }

    public static MenuType<?> wirelessArcaneMenuType() {
        return Ae2wtlibCompat.menuType();
    }

    public static MenuType<?> wirelessEssentiaMenuType() {
        return Ae2wtlibCompat.essentiaMenuType();
    }
}
