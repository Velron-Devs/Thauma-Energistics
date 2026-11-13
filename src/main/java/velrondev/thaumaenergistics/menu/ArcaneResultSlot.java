package velrondev.thaumaenergistics.menu;

import java.util.List;

import appeng.api.inventories.InternalInventory;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.storage.MEStorage;
import appeng.helpers.InventoryAction;
import appeng.menu.slot.CraftingTermSlot;
import appeng.util.Platform;
import appeng.util.inv.CarriedItemInventory;
import appeng.util.inv.PlayerInternalInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArcaneResultSlot extends CraftingTermSlot {

    private final ArcaneTerminalMenu terminalMenu;

    public ArcaneResultSlot(ArcaneTerminalMenu menu, IEnergySource energy, MEStorage storage, InternalInventory grid) {
        super(menu.getPlayer(), menu.getActionSource(), energy, storage, grid, grid, menu);
        this.terminalMenu = menu;
    }

    @Override
    public void doClick(InventoryAction action, Player who) {
        if (getItem().isEmpty() || !(who instanceof ServerPlayer player)) {
            return;
        }

        int perCraft = getItem().getCount();
        int maxStack = getItem().getMaxStackSize();
        InternalInventory target;
        int maxTimes;

        if (action == InventoryAction.CRAFT_SHIFT || action == InventoryAction.CRAFT_ALL) {
            target = new PlayerInternalInventory(player.getInventory());
            double stacks = action == InventoryAction.CRAFT_SHIFT ? 1.0 : Inventory.INVENTORY_SIZE;
            maxTimes = (int) Math.floor((double) maxStack / perCraft * stacks);
        } else if (action == InventoryAction.CRAFT_STACK) {
            target = new CarriedItemInventory(getMenu());
            maxTimes = (int) Math.floor((double) maxStack / perCraft);
        } else {
            if (getMenu().getCarried().isEmpty()) {
                getMenu().setCarried(terminalMenu.craftOnce(player));
                return;
            }
            target = new CarriedItemInventory(getMenu());
            maxTimes = 1;
        }

        ItemStack first = getItem().copy();
        for (int i = 0; i < Math.max(1, maxTimes); i++) {
            if (!ItemStack.isSameItemSameComponents(first, getItem())) {
                return;
            }
            if (!target.simulateAdd(first).isEmpty()) {
                return;
            }
            ItemStack crafted = terminalMenu.craftOnce(player);
            if (crafted.isEmpty()) {
                return;
            }
            ItemStack extra = target.addItems(crafted);
            if (!extra.isEmpty()) {
                Platform.spawnDrops(player.level(), player.blockPosition(), List.of(extra));
                return;
            }
        }
    }
}
