package velrondev.thaumaenergistics.client.screen;

import appeng.api.config.AccessRestriction;
import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.api.config.StorageFilter;
import appeng.api.config.YesNo;
import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ActionButton;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.localization.GuiText;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import velrondev.thaumaenergistics.menu.EssentiaStorageBusMenu;

public class EssentiaStorageBusScreen extends UpgradeableScreen<EssentiaStorageBusMenu> {

    private static final int ATTACHED_LEFT = 10;
    private static final int ATTACHED_TOP = 17;
    private static final float ATTACHED_SCALE = 0.6F;

    private final SettingToggleButton<AccessRestriction> rwMode;
    private final SettingToggleButton<StorageFilter> storageFilter;
    private final SettingToggleButton<YesNo> filterOnExtract;

    public EssentiaStorageBusScreen(EssentiaStorageBusMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);

        widgets.addOpenPriorityButton();

        addToLeftToolbar(new ActionButton(ActionItems.CLOSE, button -> menu.clear()));
        addToLeftToolbar(new ActionButton(ActionItems.COG, button -> menu.partition()));
        this.rwMode = new ServerSettingToggleButton<>(Settings.ACCESS, AccessRestriction.READ_WRITE);
        this.storageFilter = new ServerSettingToggleButton<>(Settings.STORAGE_FILTER, StorageFilter.EXTRACTABLE_ONLY);
        this.filterOnExtract = new ServerSettingToggleButton<>(Settings.FILTER_ON_EXTRACT, YesNo.YES);

        addToLeftToolbar(storageFilter);
        addToLeftToolbar(filterOnExtract);
        addToLeftToolbar(rwMode);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();
        storageFilter.set(menu.getStorageFilter());
        rwMode.set(menu.getReadWriteMode());
        filterOnExtract.set(menu.getFilterOnExtract());
    }

    @Override
    public void drawFG(GuiGraphicsExtractor guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawFG(guiGraphics, offsetX, offsetY, mouseX, mouseY);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(ATTACHED_LEFT, ATTACHED_TOP);
        guiGraphics.pose().scale(ATTACHED_SCALE);
        int color = style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB();
        Component line = menu.getConnectedTo() != null
                ? GuiText.AttachedTo.text(menu.getConnectedTo())
                : GuiText.Unattached.text();
        guiGraphics.text(font, line, 0, 0, color, false);
        guiGraphics.pose().popMatrix();
    }
}
