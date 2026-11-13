package velrondev.thaumaenergistics.menu;

import appeng.menu.SlotSemantic;
import appeng.menu.SlotSemantics;

public final class ArcaneSlots {

    public static final SlotSemantic ARMOR = SlotSemantics.register("TE_ARMOR", true);
    public static final SlotSemantic WAND = SlotSemantics.register("TE_WAND", false);
    public static final SlotSemantic CRYSTALS = SlotSemantics.register("TE_CRYSTALS", false);

    private ArcaneSlots() {
    }
}
