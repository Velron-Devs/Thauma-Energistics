package velrondev.thaumaenergistics.compat.ae2wtlib;

import de.mari_023.ae2wtlib.api.AE2wtlibAPI;
import de.mari_023.ae2wtlib.api.gui.Icon;
import de.mari_023.ae2wtlib.api.registration.AddTerminalEvent;

import appeng.api.upgrades.Upgrades;
import appeng.core.localization.GuiText;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.implementations.MenuTypeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUItems;

public final class Ae2wtlibCompat {

    public static final String TERMINAL_NAME = "arcane";
    public static final String ESSENTIA_TERMINAL_NAME = "essentia";

    private static WtlibArcaneTerminalItem item;
    private static MenuType<WtlibArcaneTerminalMenu> menuType;
    private static WtlibEssentiaTerminalItem essentiaItem;
    private static MenuType<WtlibEssentiaTerminalMenu> essentiaMenuType;

    private Ae2wtlibCompat() {
    }

    public static void init() {
        AddTerminalEvent.register(Ae2wtlibCompat::addTerminal);
    }

    private static void addTerminal(AddTerminalEvent event) {
        event.builder(TERMINAL_NAME, WtlibArcaneTerminalHost::new, typedMenuType(), buildItem(), Icon.CRAFTING).addTerminal();
        event.builder(ESSENTIA_TERMINAL_NAME, WtlibEssentiaTerminalHost::new, typedEssentiaMenuType(), buildEssentiaItem(), Icon.PATTERN_ACCESS).addTerminal();
    }

    private static synchronized WtlibArcaneTerminalItem buildItem() {
        if (item == null) {
            item = new WtlibArcaneTerminalItem();
        }
        return item;
    }

    private static synchronized WtlibEssentiaTerminalItem buildEssentiaItem() {
        if (essentiaItem == null) {
            essentiaItem = new WtlibEssentiaTerminalItem();
        }
        return essentiaItem;
    }

    public static synchronized MenuType<WtlibArcaneTerminalMenu> typedMenuType() {
        if (menuType == null) {
            menuType = MenuTypeBuilder.create(WtlibArcaneTerminalMenu::new, WtlibArcaneTerminalHost.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "wireless_arcane_terminal"));
        }
        return menuType;
    }

    public static synchronized MenuType<WtlibEssentiaTerminalMenu> typedEssentiaMenuType() {
        if (essentiaMenuType == null) {
            essentiaMenuType = MenuTypeBuilder.create(WtlibEssentiaTerminalMenu::new, WtlibEssentiaTerminalHost.class)
                    .buildUnregistered(ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "wireless_essentia_terminal"));
        }
        return essentiaMenuType;
    }

    public static void addUniversalUpgrades(int arcaneCardLimit) {
        Item universal = AE2wtlibAPI.getWUT();
        if (universal != Items.AIR) {
            Upgrades.add(TUUItems.UPGRADE_ARCANE.get(), universal, arcaneCardLimit, GuiText.WirelessTerminals.getTranslationKey());
        }
    }

    public static WirelessTerminalItem terminalItem() {
        return buildItem();
    }

    public static WirelessTerminalItem essentiaTerminalItem() {
        return buildEssentiaItem();
    }

    public static MenuType<?> menuType() {
        return typedMenuType();
    }

    public static MenuType<?> essentiaMenuType() {
        return typedEssentiaMenuType();
    }
}
