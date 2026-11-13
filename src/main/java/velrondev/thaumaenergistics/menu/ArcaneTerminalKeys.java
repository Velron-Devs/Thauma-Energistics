package velrondev.thaumaenergistics.menu;

import velrondev.thaumaenergistics.TUU;

public final class ArcaneTerminalKeys {

    public static final String CRAFTING = key("crafting");
    public static final String VIS = key("vis");
    public static final String DISCOUNT = key("discount");
    public static final String CRYSTALS = key("crystals");
    public static final String LOCKED = key("locked");
    public static final String DISABLED = key("disabled");
    public static final String MESSAGE_VIS = key("message.vis");
    public static final String MESSAGE_CRYSTALS = key("message.crystals");
    public static final String MESSAGE_LOCKED = key("message.locked");
    public static final String MESSAGE_FAILED = key("message.failed");

    private ArcaneTerminalKeys() {
    }

    public static String title() {
        return "gui." + TUU.MOD_ID + ".arcane_terminal";
    }

    private static String key(String suffix) {
        return title() + "." + suffix;
    }
}
