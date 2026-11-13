package velrondev.thaumaenergistics.client.screen;

import java.util.List;

import appeng.api.config.ActionItems;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ActionButton;
import appeng.core.AEConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import velrondev.thaumaenergistics.menu.ArcaneCrystals;
import velrondev.thaumaenergistics.menu.ArcaneSlots;
import velrondev.thaumaenergistics.menu.ArcaneTerminalMenu;

public class ArcaneTerminalScreen extends MEStorageScreen<ArcaneTerminalMenu> {

    private static final int BOTTOM_HEIGHT = 173;
    private static final int TEXT_LEFT = 32;
    private static final int VIS_TOP = 75;
    private static final int DETAIL_TOP = 84;
    private static final float DETAIL_SCALE = 0.7F;

    public ArcaneTerminalScreen(ArcaneTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);

        ActionButton clearButton = new ActionButton(ActionItems.S_STASH, button -> menu.clearCraftingGrid());
        clearButton.setHalfSize(true);
        clearButton.setDisableBackground(true);
        widgets.add("clearCraftingGrid", clearButton);

        ActionButton clearToPlayerButton = new ActionButton(ActionItems.S_STASH_TO_PLAYER_INV, button -> menu.clearToPlayerInventory());
        clearToPlayerButton.setHalfSize(true);
        clearToPlayerButton.setDisableBackground(true);
        widgets.add("clearToPlayerInv", clearToPlayerButton);
    }

    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawFG(guiGraphics, offsetX, offsetY, mouseX, mouseY);
        int top = imageHeight - BOTTOM_HEIGHT;
        int color = getStyle().getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB();
        guiGraphics.drawString(font, menu.visLine, TEXT_LEFT, top + VIS_TOP, color, false);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(TEXT_LEFT, top + DETAIL_TOP, 1);
        guiGraphics.pose().scale(DETAIL_SCALE, DETAIL_SCALE, 1);
        guiGraphics.drawString(font, menu.detailLine, 0, 0, color, false);
        guiGraphics.pose().popPose();
        drawCrystals(guiGraphics);
    }

    private void drawCrystals(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        int[][] state = ArcaneCrystals.parse(menu.crystalState);
        List<Slot> slots = menu.getSlots(ArcaneSlots.CRYSTALS);
        for (int i = 0; i < ArcaneCrystals.PRIMALS.size() && i < slots.size(); i++) {
            Slot slot = slots.get(i);
            int need = state[i][0];
            if (need > 0) {
                ChatFormatting tint = state[i][1] >= need ? ChatFormatting.WHITE : ChatFormatting.RED;
                String amount = String.valueOf(need);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0, 0, 300);
                guiGraphics.drawString(font, Component.literal(amount).withStyle(tint), slot.x + 17 - font.width(amount), slot.y + 9, 0xFFFFFF, true);
                guiGraphics.pose().popPose();
            }
        }
    }

    @Override
    public void onClose() {
        if (AEConfig.instance().isClearGridOnClose()) {
            getMenu().clearCraftingGrid();
        }
        super.onClose();
    }
}
