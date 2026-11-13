package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;
import java.util.Locale;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.cell.InfinityEssentiaCellItem;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.menu.ArcaneTerminalKeys;
import velrondev.thaumaenergistics.menu.EssentiaBusKeys;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TEParts;
import velrondev.thaumaenergistics.registry.TEProcessors;

public class TELangProvider extends LanguageProvider {

    public static final List<String> LOCALES = List.of("en_us", "es_es", "es_ar", "es_mx");

    private final String locale;

    public TELangProvider(PackOutput output, String locale) {
        super(output, ThaumaEnergistics.MOD_ID, locale);
        this.locale = locale;
    }

    private void tr(String key, String english, String spanish) {
        add(key, locale.startsWith("es_") ? spanish : english);
    }

    @Override
    protected void addTranslations() {
        addEssentiaTerminalLang();
        addWirelessArcaneLang();
        addWtlibLang();
        addImportExportBusLang();
        addStorageBusLang();
        addProcessorLang();
        addCompatLang();
        tr("itemGroup." + ThaumaEnergistics.MOD_ID, "Thauma Energistics", "Thauma Energistics");
        tr("gui." + ThaumaEnergistics.MOD_ID + ".essentia", "Essentia", "Esencia");

        tr(TEItems.DIFFUSION_CORE.get().getDescriptionId(), "Diffusion Core", "Núcleo de difusión");
        tr(TEItems.COALESCENCE_CORE.get().getDescriptionId(), "Coalescence Core", "Núcleo de coalescencia");
        tr(TEItems.ESSENTIA_COMPONENT_1K.get().getDescriptionId(), "1k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 1k");
        tr(TEItems.ESSENTIA_COMPONENT_4K.get().getDescriptionId(), "4k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 4k");
        tr(TEItems.ESSENTIA_COMPONENT_16K.get().getDescriptionId(), "16k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 16k");
        tr(TEItems.ESSENTIA_COMPONENT_64K.get().getDescriptionId(), "64k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 64k");
        tr(TEItems.ESSENTIA_COMPONENT_256K.get().getDescriptionId(), "256k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 256k");
        tr(TEItems.ESSENTIA_CELL_1K.get().getDescriptionId(), "1k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 1k");
        tr(TEItems.ESSENTIA_CELL_4K.get().getDescriptionId(), "4k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 4k");
        tr(TEItems.ESSENTIA_CELL_16K.get().getDescriptionId(), "16k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 16k");
        tr(TEItems.ESSENTIA_CELL_64K.get().getDescriptionId(), "64k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 64k");
        tr(TEItems.ESSENTIA_CELL_256K.get().getDescriptionId(), "256k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 256k");
        tr(TEItems.ESSENTIA_CELL_INFINITY.get().getDescriptionId(), "Infinity Essentia Storage Cell", "Celda de Almacenamiento de Esencia Infinita");
        tr(InfinityEssentiaCellItem.TOOLTIP_KEY, "Stores an unlimited amount of essentia", "Almacena una cantidad ilimitada de esencia");
        tr(TEItems.ESSENTIA_CELL_CREATIVE.get().getDescriptionId(), "Creative ME Essentia Storage Cell", "Celda creativa de almacenamiento de esencia ME");

        tr(TEParts.ARCANE_TERMINAL.get().getDescriptionId(), "ME Arcane Crafting Terminal", "Terminal de crafteo arcano ME");
        tr(ArcaneTerminalKeys.title(), "Arcane Terminal", "Terminal arcana");
        tr(ArcaneTerminalKeys.CRAFTING, "Arcane Crafting", "Crafteo arcano");
        tr(ArcaneTerminalKeys.VIS, "Vis: %s / %s", "Vis: %s / %s");
        tr(ArcaneTerminalKeys.DISCOUNT, "(-%s%%)", "(-%s%%)");
        tr(ArcaneTerminalKeys.CRYSTALS, "Crystals: %s", "Cristales: %s");
        tr(ArcaneTerminalKeys.LOCKED, "Research required", "Investigación necesaria");
        tr(ArcaneTerminalKeys.DISABLED, "Terminal disabled", "Terminal desactivada");
        tr(ArcaneTerminalKeys.MESSAGE_VIS, "There is not enough vis in this area", "No hay suficiente vis en esta zona");
        tr(ArcaneTerminalKeys.MESSAGE_CRYSTALS, "There are not enough crystals in the terminal or the network", "No hay suficientes cristales en la terminal ni en la red");
        tr(ArcaneTerminalKeys.MESSAGE_LOCKED, "You have not unlocked this recipe yet", "Aún no has desbloqueado esta receta");
        tr(ArcaneTerminalKeys.MESSAGE_FAILED, "The recipe could not be crafted", "No se pudo fabricar la receta");

        tr("block." + ThaumaEnergistics.MOD_ID + ".infusion_provider", "Infusion Provider", "Proveedor de Infusión");
        tr("message." + ThaumaEnergistics.MOD_ID + ".infusion_provider.stored", "Stored Essentia:", "Esencia almacenada:");
        tr("message." + ThaumaEnergistics.MOD_ID + ".infusion_provider.empty", "No essentia found", "No se encontró esencia");
        tr("message." + ThaumaEnergistics.MOD_ID + ".infusion_provider.entry", "%s = %s", "%s = %s");

        tr("research_category." + ThaumaEnergistics.MOD_ID + "." + TEResearch.CATEGORY, TEResearch.CATEGORY_ENGLISH, TEResearch.CATEGORY_SPANISH);
        tr("research." + ThaumaEnergistics.MOD_ID + "." + TEResearch.SCAN_KEY + ".title", TEResearch.SCAN_TITLE_ENGLISH, TEResearch.SCAN_TITLE_SPANISH);
        for (TEResearchEntry entry : TEResearch.entries()) {
            tr(entry.titleKey(), entry.titleEnglish(), entry.titleSpanish());
            List<TEResearchStage> stages = entry.stages();
            for (int i = 0; i < stages.size(); i++) {
                tr(entry.stageKey(i), stages.get(i).english(), stages.get(i).spanish());
            }
        }
    }

    private void addProcessorLang() {
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                tr(itemKey(TEProcessors.printed(metal, kind)), "Printed " + metal.english() + " " + kind.english() + " Circuit",
                        "Circuito " + kind.spanish() + " impreso de " + metal.spanish());
                tr(itemKey(TEProcessors.processor(metal, kind)), metal.english() + " " + kind.english() + " Processor",
                        "Procesador " + kind.spanish() + " de " + metal.spanish());
            }
        }
    }

    private void addCompatLang() {
        String prefix = "item." + ThaumaEnergistics.MOD_ID + ".";
        for (String size : TECompatConfig.SIZE_NAMES) {
            tr(prefix + "essentia_disk_" + size, size + " ME Essentia DISK", "Disco de esencia ME de " + size);
        }
        tr(prefix + "mega_essentia_disk_housing", "MEGA Essentia DISK Housing", "Carcasa de disco de esencia MEGA");
        tr(prefix + "mega_essentia_cell_housing", "MEGA Essentia Cell Housing", "Carcasa de celda de esencia MEGA");
        for (String name : TECompatConfig.MEGA_SIZE_NAMES) {
            String size = name.toUpperCase(Locale.ROOT);
            tr(prefix + "essentia_disk_" + name, size + " MEGA Essentia DISK", "Disco de esencia MEGA de " + size);
            tr(prefix + "essentia_cell_" + name, size + " MEGA Essentia Storage Cell", "Celda de almacenamiento de esencia MEGA de " + size);
        }
        String block = "block." + ThaumaEnergistics.MOD_ID + ".";
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            tr(block + "assembler_matrix_crafter_" + metal.id(), metal.english() + " Assembler Matrix Craft Core",
                    "Núcleo de fabricación de matriz ensambladora de " + metal.spanish());
            tr(block + "assembler_matrix_pattern_" + metal.id(), metal.english() + " Assembler Matrix Pattern Core",
                    "Núcleo de patrón de matriz ensambladora de " + metal.spanish());
            tr(block + "assembler_matrix_speed_" + metal.id(), metal.english() + " Assembler Matrix Speed Core",
                    "Núcleo de velocidad de matriz ensambladora de " + metal.spanish());
        }
        tr(prefix + "infinity_cell_name", "ME Infinity %s Cell", "Celda ME infinita de %s");
        tr(prefix + "infinity_cell.tooltip", "Provides an unlimited supply", "Ofrece un suministro ilimitado");
    }

    private static String itemKey(DeferredItem<?> item) {
        return "item." + item.getId().getNamespace() + "." + item.getId().getPath();
    }

    private void addWirelessArcaneLang() {
        tr(itemKey(TEItems.WIRELESS_ARCANE_TERMINAL), "Wireless Arcane Crafting Terminal", "Terminal de crafteo arcano inalámbrica");
        tr(itemKey(TEItems.WIRELESS_ESSENTIA_TERMINAL), "Wireless Essentia Terminal", "Terminal de esencia inalámbrica");
        tr(TEItems.UPGRADE_ARCANE.get().getDescriptionId(), "Arcane Charging Upgrade", "Mejora de carga arcana");
        tr(TEItems.UPGRADE_ARCANE.get().getDescriptionId() + ".tooltip", "Recharges the wand stored in the arcane crafting terminal with vis from the local aura", "Recarga la varita guardada en la terminal de crafteo arcano con vis del aura del lugar");
    }

    private void addWtlibLang() {
        tr("key.ae2.wireless_arcane_terminal", "Open Wireless Arcane Crafting Terminal", "Abrir terminal de crafteo arcano inalámbrica");
        tr("key.ae2.wireless_essentia_terminal", "Open Wireless Essentia Terminal", "Abrir terminal de esencia inalámbrica");
    }

    private void addEssentiaTerminalLang() {
        tr(TEParts.ESSENTIA_TERMINAL.get().getDescriptionId(), "ME Essentia Terminal", "Terminal de esencia ME");
        tr("gui." + ThaumaEnergistics.MOD_ID + ".essentia_terminal", "Essentia", "Esencia");
    }

    private void addImportExportBusLang() {
        tr(TEParts.ESSENTIA_IMPORT_BUS.get().getDescriptionId(), "ME Essentia Import Bus", "Bus de importación de esencia ME");
        tr(TEParts.ESSENTIA_EXPORT_BUS.get().getDescriptionId(), "ME Essentia Export Bus", "Bus de exportación de esencia ME");
        tr(EssentiaBusKeys.IMPORT_TITLE, "Essentia Import Bus", "Bus de importación de esencia");
        tr(EssentiaBusKeys.EXPORT_TITLE, "Essentia Export Bus", "Bus de exportación de esencia");
    }

    private void addStorageBusLang() {
        tr(TEParts.ESSENTIA_STORAGE_BUS.get().getDescriptionId(), "ME Essentia Storage Bus", "Bus de almacenamiento de esencia ME");
        tr("gui." + ThaumaEnergistics.MOD_ID + ".essentia_storage_bus", "Essentia Storage Bus", "Bus de almacenamiento de esencia");
    }
}
