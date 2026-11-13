package velrondev.thaumaenergistics.client.screen;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.style.ScreenStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;

public class EssentiaTerminalScreen extends MEStorageScreen<EssentiaTerminalMenu> {

    public EssentiaTerminalScreen(EssentiaTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }
}
