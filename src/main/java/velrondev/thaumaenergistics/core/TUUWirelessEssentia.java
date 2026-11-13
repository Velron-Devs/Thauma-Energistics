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
import velrondev.thaumaenergistics.registry.TUUItems;

@EventBusSubscriber(modid = TUU.MOD_ID)
public final class TUUWirelessEssentia {

    public static final int UPGRADE_SLOTS = 2;

    private static final int ENERGY_CARD_LIMIT = 2;

    private TUUWirelessEssentia() {
    }

    public static void init() {
        WirelessTerminalItem item = TUUItems.WIRELESS_ESSENTIA_TERMINAL.get();
        GridLinkables.register(item, WirelessTerminalItem.LINKABLE_HANDLER);
        if (TUUWtlib.isLoaded()) {
            return;
        }
        Upgrades.add(AEItems.ENERGY_CARD, item, ENERGY_CARD_LIMIT, GuiText.WirelessTerminals.getTranslationKey());
        HotkeyActions.register(item, (player, locator) -> item.openFromInventory(player, locator), HotkeyAction.WIRELESS_TERMINAL);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new PoweredItemCapabilities(stack, TUUItems.WIRELESS_ESSENTIA_TERMINAL.get()),
                TUUItems.WIRELESS_ESSENTIA_TERMINAL.get());
    }
}
