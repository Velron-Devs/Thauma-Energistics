package velrondev.thaumaenergistics.compat.ae2wtlib;

import de.mari_023.ae2wtlib.api.terminal.ItemWT;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.api.upgrades.Upgrades;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import velrondev.thaumaenergistics.core.TEWirelessArcane;

public class WtlibArcaneTerminalItem extends ItemWT {

    public WtlibArcaneTerminalItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, TEWirelessArcane.UPGRADE_SLOTS, this::onUpgradesChanged);
    }

    private void onUpgradesChanged(ItemStack stack, IUpgradeInventory upgrades) {
        setAEMaxPowerMultiplier(stack, 1 + Upgrades.getEnergyCardMultiplier(upgrades));
    }

    @Override
    public MenuType<?> getMenuType(ItemMenuHostLocator locator, Player player) {
        return Ae2wtlibCompat.menuType();
    }

    @Override
    public MenuType<?> getMenuType() {
        return Ae2wtlibCompat.menuType();
    }
}
