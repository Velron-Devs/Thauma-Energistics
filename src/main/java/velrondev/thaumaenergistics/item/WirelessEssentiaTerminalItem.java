package velrondev.thaumaenergistics.item;

import java.util.function.DoubleSupplier;

import org.jetbrains.annotations.Nullable;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.api.upgrades.Upgrades;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import velrondev.thaumaenergistics.core.TEWirelessEssentia;
import velrondev.thaumaenergistics.menu.WirelessEssentiaTerminalMenuHost;
import velrondev.thaumaenergistics.registry.TEMenus;

public class WirelessEssentiaTerminalItem extends WirelessTerminalItem {

    public WirelessEssentiaTerminalItem(DoubleSupplier powerCapacity, Properties properties) {
        super(powerCapacity, properties);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, TEWirelessEssentia.UPGRADE_SLOTS, this::onUpgradesChanged);
    }

    private void onUpgradesChanged(ItemStack stack, IUpgradeInventory upgrades) {
        setAEMaxPowerMultiplier(stack, 1 + Upgrades.getEnergyCardMultiplier(upgrades));
    }

    @Override
    public MenuType<?> getMenuType() {
        return TEMenus.ESSENTIA_TERMINAL.get();
    }

    @Nullable
    @Override
    public WirelessEssentiaTerminalMenuHost getMenuHost(Player player, ItemMenuHostLocator locator, @Nullable BlockHitResult hitResult) {
        return new WirelessEssentiaTerminalMenuHost(this, player, locator, (p, subMenu) -> openFromInventory(p, locator, true));
    }
}
