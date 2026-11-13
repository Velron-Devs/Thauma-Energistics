package velrondev.thaumaenergistics.compat.ae2wtlib;

import de.mari_023.ae2wtlib.api.gui.AE2wtlibSlotSemantics;
import de.mari_023.ae2wtlib.api.terminal.ItemWUT;
import de.mari_023.ae2wtlib.api.terminal.WTMenuHost;

import appeng.menu.slot.RestrictedInputSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.menu.EssentiaTerminalMenu;

public class WtlibEssentiaTerminalMenu extends EssentiaTerminalMenu {

    private final WtlibEssentiaTerminalHost wtHost;

    public WtlibEssentiaTerminalMenu(MenuType<?> type, int id, Inventory playerInventory, WtlibEssentiaTerminalHost host) {
        super(type, id, playerInventory, host);
        this.wtHost = host;

        addSlot(new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.QE_SINGULARITY, host.getSubInventory(WTMenuHost.INV_SINGULARITY), 0),
                AE2wtlibSlotSemantics.SINGULARITY);
    }

    public boolean isUniversal() {
        return wtHost.getItemStack().getItem() instanceof ItemWUT;
    }

    public WTMenuHost wtHost() {
        return wtHost;
    }
}
