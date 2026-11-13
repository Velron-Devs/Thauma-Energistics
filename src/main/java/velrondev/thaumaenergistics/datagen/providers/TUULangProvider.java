package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;
import java.util.Locale;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.compat.extendedae.infinity.InfiniteCellItem;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.menu.ArcaneTerminalKeys;
import velrondev.thaumaenergistics.menu.EssentiaBusKeys;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUProcessors;
import velrondev.thaumaenergistics.registry.TUUParts;

public class TUULangProvider extends LanguageProvider {

    public static final List<String> LOCALES = List.of("en_us", "es_es", "es_ar", "es_mx");

    private final String locale;

    public TUULangProvider(PackOutput output, String locale) {
        super(output, TUU.MOD_ID, locale);
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
        tr("itemGroup." + TUU.MOD_ID, "Thauma Energistics", "Thauma Energistics");
        tr("gui." + TUU.MOD_ID + ".essentia", "Essentia", "Esencia");

        tr(TUUItems.DIFFUSION_CORE.get().getDescriptionId(), "Diffusion Core", "Núcleo de difusión");
        tr(TUUItems.COALESCENCE_CORE.get().getDescriptionId(), "Coalescence Core", "Núcleo de coalescencia");
        tr(TUUItems.ESSENTIA_COMPONENT_1K.get().getDescriptionId(), "1k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 1k");
        tr(TUUItems.ESSENTIA_COMPONENT_4K.get().getDescriptionId(), "4k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 4k");
        tr(TUUItems.ESSENTIA_COMPONENT_16K.get().getDescriptionId(), "16k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 16k");
        tr(TUUItems.ESSENTIA_COMPONENT_64K.get().getDescriptionId(), "64k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 64k");
        tr(TUUItems.ESSENTIA_COMPONENT_256K.get().getDescriptionId(), "256k ME Essentia Storage Component", "Componente de almacenamiento de esencia ME de 256k");
        tr(TUUItems.ESSENTIA_CELL_1K.get().getDescriptionId(), "1k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 1k");
        tr(TUUItems.ESSENTIA_CELL_4K.get().getDescriptionId(), "4k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 4k");
        tr(TUUItems.ESSENTIA_CELL_16K.get().getDescriptionId(), "16k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 16k");
        tr(TUUItems.ESSENTIA_CELL_64K.get().getDescriptionId(), "64k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 64k");
        tr(TUUItems.ESSENTIA_CELL_256K.get().getDescriptionId(), "256k ME Essentia Storage Cell", "Celda de almacenamiento de esencia ME de 256k");
        tr(TUUItems.ESSENTIA_CELL_CREATIVE.get().getDescriptionId(), "Creative ME Essentia Storage Cell", "Celda creativa de almacenamiento de esencia ME");

        tr(TUUParts.ARCANE_TERMINAL.get().getDescriptionId(), "ME Arcane Crafting Terminal", "Terminal de crafteo arcano ME");
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

        tr("block." + TUU.MOD_ID + ".infusion_provider", "Infusion Provider", "Proveedor de Infusión");
        tr("message." + TUU.MOD_ID + ".infusion_provider.stored", "Stored Essentia:", "Esencia almacenada:");
        tr("message." + TUU.MOD_ID + ".infusion_provider.empty", "No essentia found", "No se encontró esencia");
        tr("message." + TUU.MOD_ID + ".infusion_provider.entry", "%s = %s", "%s = %s");

        tr("research_category." + TUU.MOD_ID + "." + TUUResearch.CATEGORY, TUUResearch.CATEGORY_ENGLISH, TUUResearch.CATEGORY_SPANISH);
        tr("research." + TUU.MOD_ID + "." + TUUResearch.SCAN_KEY + ".title", TUUResearch.SCAN_TITLE_ENGLISH, TUUResearch.SCAN_TITLE_SPANISH);
        for (TUUResearchEntry entry : TUUResearch.entries()) {
            tr(entry.titleKey(), entry.titleEnglish(), entry.titleSpanish());
            List<TUUResearchStage> stages = entry.stages();
            for (int i = 0; i < stages.size(); i++) {
                tr(entry.stageKey(i), stages.get(i).english(), stages.get(i).spanish());
            }
        }
    }

private void addProcessorLang() {
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                tr(itemKey(TUUProcessors.printed(metal, kind)),
                        "Printed " + metal.english() + " " + kind.english() + " Circuit",
                        "Circuito " + kind.spanish() + " impreso de " + metal.spanish());
                tr(itemKey(TUUProcessors.processor(metal, kind)),
                        metal.english() + " " + kind.english() + " Processor",
                        "Procesador " + kind.spanish() + " de " + metal.spanish());
            }
        }
    }

    private void addCompatLang() {
        if (TUUCompat.thingsActive()) {
            for (int i = 0; i < Ae2ThingsCompat.disks().size(); i++) {
                String size = TUUCompatConfig.SIZE_NAMES[i];
                tr(itemKey(Ae2ThingsCompat.disks().get(i)), size + " ME Essentia DISK", "Disco de esencia ME de " + size);
            }
        }
        if (TUUCompat.megaThingsActive()) {
            tr(itemKey(Ae2MegaThingsCompat.housing()), "MEGA Essentia DISK Housing", "Carcasa de disco de esencia MEGA");
            for (int i = 0; i < Ae2MegaThingsCompat.disks().size(); i++) {
                String size = TUUCompatConfig.MEGA_SIZE_NAMES[i].toUpperCase(Locale.ROOT);
                tr(itemKey(Ae2MegaThingsCompat.disks().get(i)), size + " MEGA Essentia DISK", "Disco de esencia MEGA de " + size);
            }
        }
        if (TUUCompat.megaCellsActive()) {
            tr(itemKey(MegaCellsCompat.housing()), "MEGA Essentia Cell Housing", "Carcasa de celda de esencia MEGA");
            for (int i = 0; i < MegaCellsCompat.cells().size(); i++) {
                String size = TUUCompatConfig.MEGA_SIZE_NAMES[i].toUpperCase(Locale.ROOT);
                tr(itemKey(MegaCellsCompat.cells().get(i)), size + " MEGA Essentia Storage Cell", "Celda de almacenamiento de esencia MEGA de " + size);
            }
        }
        if (TUUCompat.extendedAeActive()) {
            for (ProcessorMetal metal : ProcessorMetal.values()) {
                tr(blockKey(ExtendedAeCompat.crafter(metal)), metal.english() + " Assembler Matrix Craft Core", "Núcleo de fabricación de matriz ensambladora de " + metal.spanish());
                tr(blockKey(ExtendedAeCompat.pattern(metal)), metal.english() + " Assembler Matrix Pattern Core", "Núcleo de patrón de matriz ensambladora de " + metal.spanish());
                tr(blockKey(ExtendedAeCompat.speed(metal)), metal.english() + " Assembler Matrix Speed Core", "Núcleo de velocidad de matriz ensambladora de " + metal.spanish());
            }
            tr(InfiniteCellItem.NAME_KEY, "ME Infinity %s Cell", "Celda ME infinita de %s");
            tr(InfiniteCellItem.TOOLTIP_KEY, "Provides an unlimited supply", "Ofrece un suministro ilimitado");
        }
    }

    private static String blockKey(DeferredBlock<?> block) {
        return "block." + block.getId().getNamespace() + "." + block.getId().getPath();
    }

    private static String itemKey(DeferredItem<?> item) {
        return "item." + item.getId().getNamespace() + "." + item.getId().getPath();
    }

    private void addWirelessArcaneLang() {
        tr(itemKey(TUUItems.WIRELESS_ARCANE_TERMINAL), "Wireless Arcane Crafting Terminal", "Terminal de crafteo arcano inalámbrica");
        tr(itemKey(TUUItems.WIRELESS_ESSENTIA_TERMINAL), "Wireless Essentia Terminal", "Terminal de esencia inalámbrica");
        tr(TUUItems.UPGRADE_ARCANE.get().getDescriptionId(), "Arcane Charging Upgrade", "Mejora de carga arcana");
        tr(TUUItems.UPGRADE_ARCANE.get().getDescriptionId() + ".tooltip", "Recharges the wand stored in the arcane crafting terminal with vis from the local aura", "Recarga la varita guardada en la terminal de crafteo arcano con vis del aura del lugar");
    }

    private void addWtlibLang() {
        tr("key.ae2.wireless_arcane_terminal", "Open Wireless Arcane Crafting Terminal", "Abrir terminal de crafteo arcano inalámbrica");
        tr("key.ae2.wireless_essentia_terminal", "Open Wireless Essentia Terminal", "Abrir terminal de esencia inalámbrica");
    }

    private void addEssentiaTerminalLang() {
        tr(TUUParts.ESSENTIA_TERMINAL.get().getDescriptionId(), "ME Essentia Terminal", "Terminal de esencia ME");
        tr("gui." + TUU.MOD_ID + ".essentia_terminal", "Essentia", "Esencia");
    }

    private void addImportExportBusLang() {
        tr(TUUParts.ESSENTIA_IMPORT_BUS.get().getDescriptionId(), "ME Essentia Import Bus", "Bus de importación de esencia ME");
        tr(TUUParts.ESSENTIA_EXPORT_BUS.get().getDescriptionId(), "ME Essentia Export Bus", "Bus de exportación de esencia ME");
        tr(EssentiaBusKeys.IMPORT_TITLE, "Essentia Import Bus", "Bus de importación de esencia");
        tr(EssentiaBusKeys.EXPORT_TITLE, "Essentia Export Bus", "Bus de exportación de esencia");
    }

    private void addStorageBusLang() {
        tr(TUUParts.ESSENTIA_STORAGE_BUS.get().getDescriptionId(), "ME Essentia Storage Bus", "Bus de almacenamiento de esencia ME");
        tr("gui." + TUU.MOD_ID + ".essentia_storage_bus", "Essentia Storage Bus", "Bus de almacenamiento de esencia");
    }
}
