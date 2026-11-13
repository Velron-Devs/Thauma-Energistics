package velrondev.thaumaenergistics.part;

import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.util.KeyTypeSelection;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractTerminalPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;
import velrondev.thaumaenergistics.registry.TUUMenus;

public class EssentiaTerminalPart extends AbstractTerminalPart {

    @PartModels
    public static final ResourceLocation MODEL_OFF = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_terminal_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_terminal_on");

    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

    private final KeyTypeSelection essentiaOnly = new KeyTypeSelection(this::saveChanges, keyType -> keyType == TUUKeyTypes.essentia());

    public EssentiaTerminalPart(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public KeyTypeSelection getKeyTypeSelection() {
        return essentiaOnly;
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return TUUMenus.ESSENTIA_TERMINAL.get();
    }

    @Override
    public IPartModel getStaticModels() {
        return selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
    }
}
