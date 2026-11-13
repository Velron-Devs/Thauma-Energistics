package velrondev.thaumaenergistics.config;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.neoforged.neoforge.common.ModConfigSpec;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public final class TUUCompatConfig {

    public static final String FEATURE_PROCESSORS = "processors";
    public static final String FEATURE_ESSENTIA_DISKS = "essentia_disks";
    public static final String FEATURE_MEGA_ESSENTIA_DISKS = "mega_essentia_disks";
    public static final String FEATURE_MEGA_CELLS = "mega_cells";
    public static final String FEATURE_MATRIX_TIERS = "matrix_tiers";
    public static final String FEATURE_INFINITY_CELLS = "infinity_cells";
    public static final String FEATURE_ADVANCED_AE_ASPECTS = "advanced_ae_aspects";

    public static final String[] SIZE_NAMES = {"1k", "4k", "16k", "64k", "256k"};
    public static final String[] MEGA_SIZE_NAMES = {"1m", "4m", "16m", "64m", "256m"};
    public static final double[] DISK_DRAIN_DEFAULTS = {0.5, 1.0, 1.5, 2.0, 2.5};
    public static final double[] MEGA_DRAIN_DEFAULTS = {3.0, 3.5, 4.0, 4.5, 5.0};
    public static final int[] DISK_KILOBYTES = {1, 4, 16, 64, 256};
    public static final int[] MEGA_KILOBYTES = {1000, 4000, 16000, 64000, 256000};
    public static final int[] MEGA_CELL_KILOBYTES = {1024, 4096, 16384, 65536, 262144};
    public static final int[] DEFAULT_SPEED_CORES = {2, 3, 5};
    public static final int[] DEFAULT_CRAFTER_BOOST = {2, 4, 8};
    public static final int[] DEFAULT_EXTRA_PATTERN_SLOTS = {36, 72, 144};

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue PROCESSORS_ENABLED;

    public static final ModConfigSpec.BooleanValue ESSENTIA_DISKS_ENABLED;
    public static final List<ModConfigSpec.IntValue> DISK_KILOBYTES_VALUES = new ArrayList<>();
    public static final List<ModConfigSpec.DoubleValue> DISK_DRAIN_VALUES = new ArrayList<>();

    public static final ModConfigSpec.BooleanValue MEGA_ESSENTIA_DISKS_ENABLED;
    public static final List<ModConfigSpec.IntValue> MEGA_DISK_KILOBYTES_VALUES = new ArrayList<>();
    public static final List<ModConfigSpec.DoubleValue> MEGA_DISK_DRAIN_VALUES = new ArrayList<>();

    public static final ModConfigSpec.BooleanValue MEGA_CELLS_ENABLED;
    public static final List<ModConfigSpec.IntValue> MEGA_CELL_KILOBYTES_VALUES = new ArrayList<>();
    public static final List<ModConfigSpec.DoubleValue> MEGA_CELL_DRAIN_VALUES = new ArrayList<>();

    public static final ModConfigSpec.BooleanValue MATRIX_TIERS_ENABLED;
    public static final Map<ProcessorMetal, ModConfigSpec.IntValue> MATRIX_SPEED_CORES = new EnumMap<>(ProcessorMetal.class);
    public static final Map<ProcessorMetal, ModConfigSpec.IntValue> MATRIX_CRAFTER_BOOST = new EnumMap<>(ProcessorMetal.class);
    public static final Map<ProcessorMetal, ModConfigSpec.IntValue> MATRIX_EXTRA_PATTERN_SLOTS = new EnumMap<>(ProcessorMetal.class);
    public static final ModConfigSpec.DoubleValue MATRIX_IDLE_POWER;

    public static final ModConfigSpec.BooleanValue INFINITY_CELLS_ENABLED;
    public static final ModConfigSpec.DoubleValue INFINITY_CELL_IDLE_DRAIN;

    public static final ModConfigSpec.BooleanValue ADVANCED_AE_ASPECTS_ENABLED;

    private static final Map<String, ModConfigSpec.BooleanValue> FEATURES = new LinkedHashMap<>();

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Thaumium, Alchemical Brass and Void Metal AE2 circuits and processors used by the essentia cells").push("processors");
        PROCESSORS_ENABLED = feature(builder, FEATURE_PROCESSORS, "Whether the metal processors are craftable and required by the essentia storage components, otherwise the AE2 processors are used");
        builder.pop();

        builder.comment("Essentia DISKs (needs AE2 Things)").push("essentiaDisks");
        ESSENTIA_DISKS_ENABLED = feature(builder, FEATURE_ESSENTIA_DISKS, "Whether the essentia DISK recipes are enabled");
        sizes(builder, DISK_KILOBYTES_VALUES, DISK_DRAIN_VALUES, SIZE_NAMES, DISK_KILOBYTES, DISK_DRAIN_DEFAULTS);
        builder.pop();

        builder.comment("MEGA essentia DISKs (needs AE2 MEGA Things)").push("megaEssentiaDisks");
        MEGA_ESSENTIA_DISKS_ENABLED = feature(builder, FEATURE_MEGA_ESSENTIA_DISKS, "Whether the MEGA essentia DISK recipes are enabled");
        sizes(builder, MEGA_DISK_KILOBYTES_VALUES, MEGA_DISK_DRAIN_VALUES, MEGA_SIZE_NAMES, MEGA_KILOBYTES, MEGA_DRAIN_DEFAULTS);
        builder.pop();

        builder.comment("MEGA essentia storage cells (needs MEGA Cells)").push("megaCells");
        MEGA_CELLS_ENABLED = feature(builder, FEATURE_MEGA_CELLS, "Whether the MEGA essentia cell recipes are enabled");
        sizes(builder, MEGA_CELL_KILOBYTES_VALUES, MEGA_CELL_DRAIN_VALUES, MEGA_SIZE_NAMES, MEGA_CELL_KILOBYTES, MEGA_DRAIN_DEFAULTS);
        builder.pop();

        builder.comment("Thaumium, Alchemical Brass and Void Metal Assembler Matrix cores (needs ExtendedAE)").push("matrixTiers");
        MATRIX_TIERS_ENABLED = feature(builder, FEATURE_MATRIX_TIERS, "Whether the metal Assembler Matrix crafter, pattern and speed cores can be crafted");
        MATRIX_IDLE_POWER = builder.comment("AE power drained every tick by each metal matrix core")
                .defineInRange("idlePower", 1.0, 0.0, 1024.0);
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            builder.push(metal.id());
            MATRIX_SPEED_CORES.put(metal, builder.comment("Speed cores this block adds to the matrix, the matrix caps the total at 5")
                    .defineInRange("speedCores", DEFAULT_SPEED_CORES[metal.ordinal()], 1, 5));
            MATRIX_CRAFTER_BOOST.put(metal, builder.comment("Multiplier applied to the time the crafter core advances every tick")
                    .defineInRange("crafterBoost", DEFAULT_CRAFTER_BOOST[metal.ordinal()], 1, 64));
            MATRIX_EXTRA_PATTERN_SLOTS.put(metal, builder.comment("Pattern slots added by the pattern core on top of the 36 of the original, only reachable through the pattern listing of the network")
                    .defineInRange("extraPatternSlots", DEFAULT_EXTRA_PATTERN_SLOTS[metal.ordinal()], 0, 1024));
            builder.pop();
        }
        builder.pop();

        builder.comment("Infinite cells of arcane stone and vis crystals (needs ExtendedAE)").push("infinityCells");
        INFINITY_CELLS_ENABLED = feature(builder, FEATURE_INFINITY_CELLS, "Whether the infinite cell recipes are enabled");
        INFINITY_CELL_IDLE_DRAIN = builder.comment("AE power drained every tick by each infinite cell")
                .defineInRange("idleDrain", 8.0, 0.0, 1024.0);
        builder.pop();

        builder.comment("Essentia values for the items of Advanced AE").push("advancedAe");
        ADVANCED_AE_ASPECTS_ENABLED = feature(builder, FEATURE_ADVANCED_AE_ASPECTS, "Whether the Advanced AE items get essentia values in the aspect index");
        builder.pop();

        SPEC = builder.build();
    }

    private TUUCompatConfig() {
    }

    public static boolean enabled(String feature) {
        ModConfigSpec.BooleanValue value = FEATURES.get(feature);
        return value == null || !SPEC.isLoaded() || value.get();
    }

    private static ModConfigSpec.BooleanValue feature(ModConfigSpec.Builder builder, String feature, String comment) {
        ModConfigSpec.BooleanValue value = builder.comment(comment).define("enabled", true);
        FEATURES.put(feature, value);
        return value;
    }

    private static void sizes(ModConfigSpec.Builder builder, List<ModConfigSpec.IntValue> kilobytes, List<ModConfigSpec.DoubleValue> drains,
            String[] names, int[] kilobyteDefaults, double[] drainDefaults) {
        for (int i = 0; i < names.length; i++) {
            builder.push(names[i]);
            kilobytes.add(builder.comment("Capacity of this size, in thousands of bytes").defineInRange("kilobytes", kilobyteDefaults[i], 1, 1_000_000));
            drains.add(builder.comment("AE power drained every tick").defineInRange("idleDrain", drainDefaults[i], 0.0, 1024.0));
            builder.pop();
        }
    }
}
