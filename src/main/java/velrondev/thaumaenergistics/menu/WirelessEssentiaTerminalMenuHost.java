package velrondev.thaumaenergistics.menu;

import java.util.function.BiConsumer;

import appeng.api.util.KeyTypeSelection;
import appeng.helpers.WirelessTerminalMenuHost;
import appeng.menu.ISubMenu;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.world.entity.player.Player;
import velrondev.thaumaenergistics.item.WirelessEssentiaTerminalItem;
import velrondev.thaumaenergistics.registry.TEKeyTypes;

public class WirelessEssentiaTerminalMenuHost extends WirelessTerminalMenuHost<WirelessEssentiaTerminalItem> {

    private KeyTypeSelection essentiaOnly;

    public WirelessEssentiaTerminalMenuHost(WirelessEssentiaTerminalItem item, Player player, ItemMenuHostLocator locator, BiConsumer<Player, ISubMenu> returnToMainMenu) {
        super(item, player, locator, returnToMainMenu);
    }

    @Override
    public KeyTypeSelection getKeyTypeSelection() {
        if (essentiaOnly == null) {
            essentiaOnly = KeyTypeSelection.forStack(getItemStack(), keyType -> keyType == TEKeyTypes.essentia());
        }
        return essentiaOnly;
    }
}
