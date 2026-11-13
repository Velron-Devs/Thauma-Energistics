package velrondev.thaumaenergistics.compat.ae2wtlib;

import de.mari_023.ae2wtlib.api.gui.ScrollingUpgradesPanel;
import de.mari_023.ae2wtlib.api.terminal.IUniversalTerminalCapable;
import de.mari_023.ae2wtlib.api.terminal.WTMenuHost;

import appeng.client.gui.style.ScreenStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import velrondev.thaumaenergistics.client.screen.ArcaneTerminalScreen;

public class WtlibArcaneTerminalScreen extends ArcaneTerminalScreen implements IUniversalTerminalCapable {

    private static final int MIN_UPGRADE_ROWS = 2;

    private final WtlibArcaneTerminalMenu wtMenu;
    private final ScrollingUpgradesPanel upgradesPanel;

    public WtlibArcaneTerminalScreen(WtlibArcaneTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.wtMenu = menu;
        if (menu.isUniversal()) {
            addToLeftToolbar(cycleTerminalButton());
        }
        this.upgradesPanel = addUpgradePanel(widgets, menu);
    }

    @Override
    public void init() {
        super.init();
        upgradesPanel.setMaxRows(Math.max(MIN_UPGRADE_ROWS, getVisibleRows()));
    }

    @Override
    public WTMenuHost getHost() {
        return wtMenu.wtHost();
    }

    @Override
    public void storeState() {
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || checkForTerminalKeys(keyCode, scanCode);
    }
}
