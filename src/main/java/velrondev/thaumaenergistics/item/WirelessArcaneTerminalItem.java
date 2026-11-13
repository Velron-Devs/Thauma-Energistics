package velrondev.thaumaenergistics.item;

import java.util.function.DoubleSupplier;

import org.jetbrains.annotations.Nullable;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.api.upgrades.Upgrades;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import velrondev.thaumaenergistics.core.TUUWirelessArcane;
import velrondev.thaumaenergistics.menu.WirelessArcaneTerminalMenuHost;
import velrondev.thaumaenergistics.registry.TUUMenus;

public class WirelessArcaneTerminalItem extends WirelessTerminalItem {

    public WirelessArcaneTerminalItem(DoubleSupplier powerCapacity, Properties properties) {
        super(powerCapacity, properties);
    }

    @Override
    protected String getOrCreateDescriptionId() {
        return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, TUUWirelessArcane.UPGRADE_SLOTS, this::onUpgradesChanged);
    }

    private void onUpgradesChanged(ItemStack stack, IUpgradeInventory upgrades) {
        setAEMaxPowerMultiplier(stack, 1 + Upgrades.getEnergyCardMultiplier(upgrades));
    }

    @Override
    public MenuType<?> getMenuType() {
        return TUUMenus.ARCANE_TERMINAL.get();
    }

    @Nullable
    @Override
    public WirelessArcaneTerminalMenuHost getMenuHost(Player player, ItemMenuHostLocator locator, @Nullable BlockHitResult hitResult) {
        return new WirelessArcaneTerminalMenuHost(this, player, locator, (p, subMenu) -> openFromInventory(p, locator, true));
    }
}
