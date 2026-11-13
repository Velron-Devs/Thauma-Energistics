package velrondev.thaumaenergistics.menu;

import appeng.api.stacks.AEKey;
import appeng.api.storage.ITerminalHost;
import appeng.menu.me.common.MEStorageMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.api.key.EssentiaKey;

public class EssentiaTerminalMenu extends MEStorageMenu {

    public EssentiaTerminalMenu(MenuType<?> type, int id, Inventory playerInventory, ITerminalHost host) {
        super(type, id, playerInventory, host);
    }

    @Override
    public boolean isKeyVisible(AEKey key) {
        return key instanceof EssentiaKey;
    }

    @Override
    public boolean canConfigureTypeFilter() {
        return false;
    }
}
