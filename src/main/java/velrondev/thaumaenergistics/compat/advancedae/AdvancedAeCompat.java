package velrondev.thaumaenergistics.compat.advancedae;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AdvancedAeCompat {

    public static final String MOD_ID = "advanced_ae";

    public record Entry(String path, Map<String, Integer> aspects) {
    }

    private AdvancedAeCompat() {
    }

    public static List<Entry> entries() {
        return List.of(
                entry("quantum_alloy", "metallum", 24, "potentia", 12, "alienis", 6),
                entry("quantum_alloy_plate", "metallum", 12, "potentia", 6, "alienis", 3),
                entry("quantum_alloy_block", "metallum", 24, "potentia", 14, "alienis", 8, "ordo", 4),
                entry("quantum_alloy_slab", "metallum", 12, "potentia", 7, "alienis", 4),
                entry("quantum_alloy_stairs", "metallum", 18, "potentia", 10, "alienis", 6),
                entry("quantum_alloy_wall", "metallum", 18, "potentia", 10, "alienis", 6),
                entry("quantum_infused_dust", "potentia", 14, "alienis", 6, "perditio", 4),
                entry("shattered_singularity", "alienis", 20, "potentia", 14, "vacuos", 10, "perditio", 6),
                entry("quantum_processor", "machina", 14, "potentia", 8, "alienis", 6, "cognitio", 4),
                entry("printed_quantum_processor", "machina", 8, "metallum", 6, "alienis", 4),
                entry("quantum_processor_press", "machina", 6, "metallum", 8, "ordo", 4),
                entry("quantum_storage_component", "machina", 12, "vacuos", 12, "alienis", 6, "ordo", 4),
                entry("quantum_upgrade_base", "machina", 8, "praecantatio", 6, "alienis", 4, "ordo", 3),
                entry("adv_pattern_encoder", "machina", 10, "cognitio", 8, "ordo", 6, "metallum", 4),
                entry("adv_processing_pattern", "cognitio", 8, "ordo", 6, "machina", 4),
                entry("adv_pattern_provider_upgrade", "machina", 10, "ordo", 6, "permutatio", 6),
                entry("adv_pattern_provider_capacity_upgrade", "machina", 10, "vacuos", 8, "ordo", 6),
                entry("adv_pattern_provider", "machina", 16, "ordo", 10, "permutatio", 8, "metallum", 6),
                entry("small_adv_pattern_provider", "machina", 12, "ordo", 8, "permutatio", 6, "metallum", 4),
                entry("adv_pattern_provider_part", "machina", 14, "ordo", 8, "permutatio", 6),
                entry("small_adv_pattern_provider_part", "machina", 10, "ordo", 6, "permutatio", 4),
                entry("import_export_bus_part", "machina", 10, "motus", 8, "permutatio", 4),
                entry("advanced_io_bus_part", "machina", 12, "motus", 8, "permutatio", 6, "potentia", 3),
                entry("stock_export_bus_part", "machina", 10, "motus", 6, "cognitio", 4, "ordo", 3),
                entry("throughput_monitor", "machina", 8, "cognitio", 8, "lux", 4),
                entry("throughput_monitor_configurator", "machina", 6, "cognitio", 6, "ordo", 4),
                entry("quantum_crafter", "machina", 16, "fabrico", 14, "potentia", 8, "alienis", 4),
                entry("quantum_crafter_terminal", "machina", 12, "fabrico", 10, "cognitio", 6),
                entry("wireless_quantum_crafter_terminal", "machina", 14, "fabrico", 10, "cognitio", 6, "motus", 6),
                entry("quantum_core", "machina", 20, "potentia", 16, "alienis", 10, "ordo", 8),
                entry("quantum_unit", "machina", 14, "potentia", 8, "alienis", 6, "ordo", 6),
                entry("quantum_accelerator", "machina", 16, "motus", 12, "potentia", 10, "alienis", 6),
                entry("quantum_multi_threader", "machina", 18, "motus", 12, "potentia", 12, "alienis", 8),
                entry("quantum_structure", "vitreus", 12, "alienis", 6, "ordo", 4),
                entry("quantum_storage_128", "machina", 20, "vacuos", 20, "alienis", 10, "ordo", 6),
                entry("quantum_storage_256", "machina", 24, "vacuos", 24, "alienis", 14, "ordo", 8),
                entry("data_entangler", "machina", 16, "alienis", 14, "cognitio", 10, "permutatio", 8),
                entry("reaction_chamber", "machina", 14, "permutatio", 12, "alienis", 6, "praecantatio", 6),
                entry("quantum_infusion_block", "aqua", 10, "alienis", 8, "potentia", 8, "permutatio", 6),
                entry("quantum_infusion_bucket", "aqua", 14, "alienis", 8, "potentia", 8, "metallum", 4),
                entry("quantum_helmet", "praemunio", 16, "alienis", 8, "potentia", 8, "cognitio", 6),
                entry("quantum_chestplate", "praemunio", 24, "alienis", 12, "potentia", 12, "victus", 6),
                entry("quantum_leggings", "praemunio", 20, "alienis", 10, "potentia", 10, "motus", 6),
                entry("quantum_boots", "praemunio", 14, "alienis", 8, "potentia", 8, "motus", 8),
                entry("attack_speed_card", "machina", 6, "motus", 6, "aversio", 4),
                entry("auto_feeding_card", "machina", 6, "victus", 6, "desiderium", 4),
                entry("auto_stock_card", "machina", 6, "ordo", 6, "permutatio", 4),
                entry("camo_card", "machina", 6, "lux", 6, "tenebrae", 4),
                entry("evasion_card", "machina", 6, "motus", 6, "praemunio", 4),
                entry("flight_card", "machina", 8, "volatus", 10, "potentia", 4),
                entry("flight_drift_card", "machina", 6, "volatus", 8, "motus", 4),
                entry("hp_buffer_card", "machina", 6, "victus", 8, "praemunio", 4),
                entry("jump_height_card", "machina", 6, "motus", 6, "aer", 4),
                entry("lava_immunity_card", "machina", 6, "praemunio", 6, "ignis", 6),
                entry("luck_card", "machina", 6, "auram", 8, "praecantatio", 4),
                entry("magnet_card", "machina", 6, "vinculum", 8, "motus", 4),
                entry("night_vision_card", "machina", 6, "lux", 8, "sensus", 4),
                entry("pick_craft_card", "machina", 6, "fabrico", 6, "instrumentum", 4),
                entry("portable_workbench_card", "machina", 6, "fabrico", 8, "instrumentum", 4),
                entry("reach_card", "machina", 6, "motus", 6, "cognitio", 4),
                entry("recharging_card", "machina", 6, "potentia", 10, "motus", 4),
                entry("regeneration_card", "machina", 6, "victus", 10, "praecantatio", 4),
                entry("sprint_speed_card", "machina", 6, "motus", 8, "aer", 4),
                entry("step_assist_card", "machina", 6, "motus", 6, "terra", 4),
                entry("strength_card", "machina", 6, "aversio", 8, "potentia", 4),
                entry("swim_speed_card", "machina", 6, "motus", 6, "aqua", 6),
                entry("walk_speed_card", "machina", 6, "motus", 8, "terra", 4),
                entry("water_breathing_card", "machina", 6, "aqua", 8, "aer", 4));
    }

    private static Entry entry(String path, Object... pairs) {
        Map<String, Integer> aspects = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            aspects.put((String) pairs[i], (Integer) pairs[i + 1]);
        }
        return new Entry(path, aspects);
    }
}
