package velrondev.thaumaenergistics.compat.ae2wtlib;

import de.mari_023.ae2wtlib.api.gui.ScrollingUpgradesPanel;
import de.mari_023.ae2wtlib.api.terminal.IUniversalTerminalCapable;
import de.mari_023.ae2wtlib.api.terminal.WTMenuHost;

import appeng.client.gui.style.ScreenStyle;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import velrondev.thaumaenergistics.client.screen.EssentiaTerminalScreen;

public class WtlibEssentiaTerminalScreen extends EssentiaTerminalScreen implements IUniversalTerminalCapable {

    private static final int MIN_UPGRADE_ROWS = 2;

    private final WtlibEssentiaTerminalMenu wtMenu;
    private final ScrollingUpgradesPanel upgradesPanel;

    public WtlibEssentiaTerminalScreen(WtlibEssentiaTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.wtMenu = menu;
        if (menu.isUniversal()) {
            addTerminalSelectionPanel(widgets);
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
    public boolean keyPressed(KeyEvent event) {
        return super.keyPressed(event) || checkForTerminalKeys(event);
    }
}
