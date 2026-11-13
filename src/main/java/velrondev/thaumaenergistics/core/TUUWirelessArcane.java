package velrondev.thaumaenergistics.core;

import appeng.api.features.GridLinkables;
import appeng.api.features.HotkeyAction;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import appeng.hotkeys.HotkeyActions;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.items.tools.powered.powersink.PoweredItemCapabilities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2wtlib.Ae2wtlibCompat;
import velrondev.thaumaenergistics.registry.TUUItems;

@EventBusSubscriber(modid = TUU.MOD_ID)
public final class TUUWirelessArcane {

    public static final int UPGRADE_SLOTS = 3;

    private static final int ENERGY_CARD_LIMIT = 2;
    private static final int ARCANE_CARD_LIMIT = 1;

    private TUUWirelessArcane() {
    }

    public static void init() {
        WirelessTerminalItem item = TUUItems.WIRELESS_ARCANE_TERMINAL.get();
        GridLinkables.register(item, WirelessTerminalItem.LINKABLE_HANDLER);
        String group = GuiText.WirelessTerminals.getTranslationKey();
        Upgrades.add(TUUItems.UPGRADE_ARCANE.get(), item, ARCANE_CARD_LIMIT, group);
        if (TUUWtlib.isLoaded()) {
            Ae2wtlibCompat.addUniversalUpgrades(ARCANE_CARD_LIMIT);
            return;
        }
        Upgrades.add(AEItems.ENERGY_CARD, item, ENERGY_CARD_LIMIT, group);
        HotkeyActions.register(item, (player, locator) -> item.openFromInventory(player, locator), HotkeyAction.WIRELESS_TERMINAL);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new PoweredItemCapabilities(stack, TUUItems.WIRELESS_ARCANE_TERMINAL.get()),
                TUUItems.WIRELESS_ARCANE_TERMINAL.get());
    }
}
