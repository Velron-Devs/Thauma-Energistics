package velrondev.thaumaenergistics.compat.ae2wtlib;

import java.util.function.BiConsumer;

import de.mari_023.ae2wtlib.api.terminal.ItemWT;
import de.mari_023.ae2wtlib.api.terminal.WTMenuHost;

import appeng.api.util.KeyTypeSelection;
import appeng.menu.ISubMenu;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.world.entity.player.Player;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public class WtlibEssentiaTerminalHost extends WTMenuHost {

    private KeyTypeSelection essentiaOnly;

    public WtlibEssentiaTerminalHost(ItemWT item, Player player, ItemMenuHostLocator locator, BiConsumer<Player, ISubMenu> returnToMainMenu) {
        super(item, player, locator, returnToMainMenu);
    }

    @Override
    public KeyTypeSelection getKeyTypeSelection() {
        if (essentiaOnly == null) {
            essentiaOnly = KeyTypeSelection.forStack(getItemStack(), keyType -> keyType == TUUKeyTypes.essentia());
        }
        return essentiaOnly;
    }
}
