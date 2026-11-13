package velrondev.thaumaenergistics.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TUUConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue ESSENTIA_PER_BYTE;
    public static final ModConfigSpec.IntValue ESSENTIA_PER_OPERATION;
    public static final ModConfigSpec.IntValue CELL_TYPES;
    public static final ModConfigSpec.IntValue CELL_BYTES_PER_TYPE;
    public static final ModConfigSpec.DoubleValue CELL_1K_IDLE_DRAIN;
    public static final ModConfigSpec.DoubleValue CELL_4K_IDLE_DRAIN;
    public static final ModConfigSpec.DoubleValue CELL_16K_IDLE_DRAIN;
    public static final ModConfigSpec.DoubleValue CELL_64K_IDLE_DRAIN;
    public static final ModConfigSpec.DoubleValue CELL_256K_IDLE_DRAIN;

    public static final ModConfigSpec.BooleanValue INFUSION_PROVIDER_ENABLED;
    public static final ModConfigSpec.DoubleValue INFUSION_PROVIDER_IDLE_POWER;
    public static final ModConfigSpec.DoubleValue INFUSION_PROVIDER_POWER_PER_ESSENTIA;
    public static final ModConfigSpec.IntValue INFUSION_PROVIDER_PRIORITY;
    public static final ModConfigSpec.IntValue INFUSION_PROVIDER_SYNC_INTERVAL;

    public static final ModConfigSpec.BooleanValue ARCANE_TERMINAL_ENABLED;
    public static final ModConfigSpec.BooleanValue ARCANE_TERMINAL_VANILLA_RECIPES;
    public static final ModConfigSpec.IntValue ARCANE_TERMINAL_REFRESH_TICKS;
    public static final ModConfigSpec.DoubleValue ARCANE_TERMINAL_POWER_PER_INGREDIENT;

    public static final ModConfigSpec.BooleanValue ARCANE_CHARGE_ENABLED;
    public static final ModConfigSpec.IntValue ARCANE_CHARGE_INTERVAL_TICKS;
    public static final ModConfigSpec.IntValue ARCANE_CHARGE_VIS_PER_PULSE;

    public static final ModConfigSpec.BooleanValue ESSENTIA_STORAGE_BUS_ENABLED;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("essentiaStorageBus");
        ESSENTIA_STORAGE_BUS_ENABLED = builder.comment("Whether the essentia storage bus exposes adjacent essentia containers to the network")
                .define("enabled", true);
        builder.pop();

        builder.push("arcaneTerminal");
        ARCANE_TERMINAL_ENABLED = builder.comment("Whether the arcane terminal can craft anything")
                .define("enabled", true);
        ARCANE_TERMINAL_VANILLA_RECIPES = builder.comment("Let the arcane terminal also craft normal crafting table recipes")
                .define("vanillaRecipes", true);
        ARCANE_TERMINAL_REFRESH_TICKS = builder.comment("Ticks between recalculations of the recipe result, vis and crystals shown by the terminal")
                .defineInRange("refreshTicks", 10, 1, 200);
        ARCANE_TERMINAL_POWER_PER_INGREDIENT = builder.comment("AE power spent for every ingredient pulled from the network to refill the crafting grid")
                .defineInRange("powerPerIngredient", 1.0, 0.0, 1024.0);
        builder.pop();

        builder.push("arcaneChargingUpgrade");
        ARCANE_CHARGE_ENABLED = builder.comment("Whether the arcane charging upgrade recharges the wand stored in the arcane terminal")
                .define("enabled", true);
        ARCANE_CHARGE_INTERVAL_TICKS = builder.comment("Ticks between two recharge pulses")
                .defineInRange("intervalTicks", 20, 1, 1200);
        ARCANE_CHARGE_VIS_PER_PULSE = builder.comment("Vis taken from the local aura in every recharge pulse")
                .defineInRange("visPerPulse", 2, 1, 1000);
        builder.pop();

        builder.push("infusionProvider");
        INFUSION_PROVIDER_ENABLED = builder.comment("Whether the infusion provider hands essentia from the network to infusion matrices")
                .define("enabled", true);
        INFUSION_PROVIDER_IDLE_POWER = builder.comment("AE power drained every tick while the infusion provider is connected")
                .defineInRange("idlePower", 1.0, 0.0, 1024.0);
        INFUSION_PROVIDER_POWER_PER_ESSENTIA = builder.comment("AE power spent for every essentia unit taken from the network")
                .defineInRange("powerPerEssentia", 0.5, 0.0, 1024.0);
        INFUSION_PROVIDER_PRIORITY = builder.comment("Source priority offered to infusion matrices, higher values are drained first")
                .defineInRange("sourcePriority", 0, -100, 100);
        INFUSION_PROVIDER_SYNC_INTERVAL = builder.comment("Ticks between essentia snapshots sent to clients for goggles")
                .defineInRange("syncInterval", 40, 1, 1200);
        builder.pop();

        builder.push("essentia");
        ESSENTIA_PER_BYTE = builder.comment("Essentia units stored per cell byte")
                .defineInRange("essentiaPerByte", 8, 1, 1_000_000);
        ESSENTIA_PER_OPERATION = builder.comment("Essentia units moved per bus operation")
                .defineInRange("essentiaPerOperation", 8, 1, 4096);
        builder.pop();

        builder.push("cells");
        CELL_TYPES = builder.comment("Different essentia types a cell can hold")
                .defineInRange("types", 12, 1, 63);
        CELL_BYTES_PER_TYPE = builder.comment("Bytes reserved for every stored essentia type")
                .defineInRange("bytesPerType", 8, 0, 4096);
        CELL_1K_IDLE_DRAIN = builder.defineInRange("idleDrain1k", 0.5, 0.0, 1024.0);
        CELL_4K_IDLE_DRAIN = builder.defineInRange("idleDrain4k", 1.0, 0.0, 1024.0);
        CELL_16K_IDLE_DRAIN = builder.defineInRange("idleDrain16k", 1.5, 0.0, 1024.0);
        CELL_64K_IDLE_DRAIN = builder.defineInRange("idleDrain64k", 2.0, 0.0, 1024.0);
        CELL_256K_IDLE_DRAIN = builder.defineInRange("idleDrain256k", 2.5, 0.0, 1024.0);
        builder.pop();

        SPEC = builder.build();
    }

    private TUUConfig() {
    }
}
