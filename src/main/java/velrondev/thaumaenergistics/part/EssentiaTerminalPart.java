package velrondev.thaumaenergistics.part;

import appeng.api.parts.IPartItem;
import appeng.api.util.KeyTypeSelection;
import appeng.parts.reporting.AbstractTerminalPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.registry.TEKeyTypes;
import velrondev.thaumaenergistics.registry.TEMenus;

public class EssentiaTerminalPart extends AbstractTerminalPart {

    private final KeyTypeSelection essentiaOnly = new KeyTypeSelection(this::saveChanges, keyType -> keyType == TEKeyTypes.essentia());

    public EssentiaTerminalPart(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public KeyTypeSelection getKeyTypeSelection() {
        return essentiaOnly;
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return TEMenus.ESSENTIA_TERMINAL.get();
    }
}
