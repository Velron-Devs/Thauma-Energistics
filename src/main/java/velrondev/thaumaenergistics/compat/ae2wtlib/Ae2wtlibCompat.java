package velrondev.thaumaenergistics.compat.ae2wtlib;

import org.jetbrains.annotations.Nullable;

import de.mari_023.ae2wtlib.api.AE2wtlibAPI;
import de.mari_023.ae2wtlib.api.gui.Icon;
import de.mari_023.ae2wtlib.api.registration.AddTerminalEvent;

import appeng.api.upgrades.Upgrades;
import appeng.core.localization.GuiText;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.implementations.MenuTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.registry.TEItems;

public final class Ae2wtlibCompat {

    public static final String TERMINAL_NAME = "arcane";
    public static final String ESSENTIA_TERMINAL_NAME = "essentia";

    private static final String ITEM_ID = "wireless_arcane_terminal";
    private static final String ESSENTIA_ITEM_ID = "wireless_essentia_terminal";

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
        event.builder(TERMINAL_NAME, WtlibArcaneTerminalHost::new, typedMenuType(), buildItem(null), Icon.CRAFTING).addTerminal();
        event.builder(ESSENTIA_TERMINAL_NAME, WtlibEssentiaTerminalHost::new, typedEssentiaMenuType(), buildEssentiaItem(null), Icon.PATTERN_ACCESS).addTerminal();
    }

    private static Item.Properties properties(@Nullable Item.Properties properties, String path) {
        if (properties != null) {
            return properties;
        }
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path)));
    }

    private static synchronized WtlibArcaneTerminalItem buildItem(@Nullable Item.Properties properties) {
        if (item == null) {
            item = new WtlibArcaneTerminalItem(properties(properties, ITEM_ID));
        }
        return item;
    }

    private static synchronized WtlibEssentiaTerminalItem buildEssentiaItem(@Nullable Item.Properties properties) {
        if (essentiaItem == null) {
            essentiaItem = new WtlibEssentiaTerminalItem(properties(properties, ESSENTIA_ITEM_ID));
        }
        return essentiaItem;
    }

    public static synchronized MenuType<WtlibArcaneTerminalMenu> typedMenuType() {
        if (menuType == null) {
            menuType = MenuTypeBuilder.create(WtlibArcaneTerminalMenu::new, WtlibArcaneTerminalHost.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "wireless_arcane_terminal"));
        }
        return menuType;
    }

    public static synchronized MenuType<WtlibEssentiaTerminalMenu> typedEssentiaMenuType() {
        if (essentiaMenuType == null) {
            essentiaMenuType = MenuTypeBuilder.create(WtlibEssentiaTerminalMenu::new, WtlibEssentiaTerminalHost.class)
                    .buildUnregistered(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "wireless_essentia_terminal"));
        }
        return essentiaMenuType;
    }

    public static void addUniversalUpgrades(int arcaneCardLimit) {
        Item universal = AE2wtlibAPI.getWUT();
        if (universal != Items.AIR) {
            Upgrades.add(TEItems.UPGRADE_ARCANE.get(), universal, arcaneCardLimit, GuiText.WirelessTerminals.getTranslationKey());
        }
    }

    public static WirelessTerminalItem terminalItem() {
        return buildItem(null);
    }

    public static WirelessTerminalItem terminalItem(Item.Properties properties) {
        return buildItem(properties);
    }

    public static WirelessTerminalItem essentiaTerminalItem() {
        return buildEssentiaItem(null);
    }

    public static WirelessTerminalItem essentiaTerminalItem(Item.Properties properties) {
        return buildEssentiaItem(properties);
    }

    public static MenuType<?> menuType() {
        return typedMenuType();
    }

    public static MenuType<?> essentiaMenuType() {
        return typedEssentiaMenuType();
    }
}
