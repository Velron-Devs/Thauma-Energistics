package velrondev.thaumaenergistics.menu;

import appeng.menu.SlotSemantic;
import appeng.menu.SlotSemantics;

public final class ArcaneSlots {

    public static final SlotSemantic ARMOR = SlotSemantics.register("TUU_ARMOR", true);
    public static final SlotSemantic WAND = SlotSemantics.register("TUU_WAND", false);
    public static final SlotSemantic CRYSTALS = SlotSemantics.register("TUU_CRYSTALS", false);

    private ArcaneSlots() {
    }
}
