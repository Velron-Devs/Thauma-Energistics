package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
import com.leclowndu93150.thaumaturge.api.research.scan.ScanEntry;
import com.leclowndu93150.thaumaturge.content.research.ResearchCategory;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.config.TUUCompatConfig;

public final class TUUResearch {

    public record Lookups(HolderGetter<IAspect> aspects, HolderGetter<Item> items, HolderGetter<Block> blocks) {
    }

    private record FormulaEntry(String aspect, int amount) {
    }

    public static final String CATEGORY = "energistics";
    public static final String SCAN_KEY = "f_ae_core";
    public static final String SCAN_TITLE_ENGLISH = "Examine something related to Applied Energistics.";
    public static final String SCAN_TITLE_SPANISH = "Examina algo relacionado con Applied Energistics.";
    public static final String CATEGORY_ENGLISH = "Energistics";
    public static final String CATEGORY_SPANISH = "Energística";

    private static final int CATEGORY_INDEX = 7;
    private static final List<FormulaEntry> FORMULA = List.of(
            new FormulaEntry("machina", 15),
            new FormulaEntry("fabrico", 15),
            new FormulaEntry("potentia", 20),
            new FormulaEntry("permutatio", 20),
            new FormulaEntry("praecantatio", 15),
            new FormulaEntry("metallum", 5));

    public static final ResourceLocation BASE = mod("base_energistics");
    public static final ResourceLocation DIGISENTIA = mod("digisentia");
    public static final ResourceLocation STORAGE_1K = mod("essentia_storage_1k");
    public static final ResourceLocation STORAGE_4K = mod("essentia_storage_4k");
    public static final ResourceLocation STORAGE_16K = mod("essentia_storage_16k");
    public static final ResourceLocation STORAGE_64K = mod("essentia_storage_64k");
    public static final ResourceLocation STORAGE_256K = mod("essentia_storage_256k");
    public static final ResourceLocation ARCANE_TERMINAL = mod("arcane_terminal");
    public static final ResourceLocation ESSENTIA_TERMINAL = mod("essentia_terminal");
    public static final ResourceLocation ESSENTIA_IMPORT_BUS = mod("essentia_import_bus");
    public static final ResourceLocation ESSENTIA_EXPORT_BUS = mod("essentia_export_bus");
    public static final ResourceLocation ESSENTIA_STORAGE_BUS = mod("essentia_storage_bus");
    public static final ResourceLocation INFUSION_PROVIDER = mod("infusion_provider");
    public static final ResourceLocation WIRELESS_ARCANE_TERMINAL = mod("wireless_arcane_terminal");
    public static final ResourceLocation WIRELESS_ESSENTIA_TERMINAL = mod("wireless_essentia_terminal");
    public static final ResourceLocation ARCANE_CHARGING_UPGRADE = mod("arcane_charging_upgrade");
    public static final ResourceLocation THAUMIC_PROCESSORS = mod("thaumic_processors");
    public static final ResourceLocation ESSENTIA_DISKS = mod("essentia_disks");
    public static final ResourceLocation MEGA_ESSENTIA_DISKS = mod("mega_essentia_disks");
    public static final ResourceLocation MEGA_ESSENTIA_CELLS = mod("mega_essentia_cells");
    public static final ResourceLocation QUANTUM_ESSENTIA = mod("quantum_essentia");
    public static final ResourceLocation MATRIX_TIERS = mod("matrix_tiers");
    public static final ResourceLocation INFINITY_CELLS = mod("infinity_cells");

    private static final String[] INFINITY_ASPECTS = {"terra", "ignis", "aer", "aqua", "ordo", "perditio"};
    private static final String[] METALS = {"thaumium", "brass", "void"};
    private static final String[] MATRIX_KINDS = {"crafter", "pattern", "speed"};

    private TUUResearch() {
    }

    public static ResourceLocation mod(String path) {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, path);
    }

    public static ResourceLocation thaumaturge(String path) {
        return ResourceLocation.fromNamespaceAndPath("thaumaturge", path);
    }

    public static ResourceKey<IAspect> aspect(String path) {
        return ResourceKey.create(IAspect.REGISTRY_KEY, thaumaturge(path));
    }

    public static Set<ResourceKey<IAspect>> aspectKeys() {
        Set<ResourceKey<IAspect>> keys = new LinkedHashSet<>();
        FORMULA.forEach(entry -> keys.add(aspect(entry.aspect())));
        for (TUUResearchEntry entry : entries()) {
            entry.noteAspects().forEach(name -> keys.add(aspect(name)));
        }
        return keys;
    }

    public static ResearchCategory category(Lookups lookups) {
        List<AspectInstance> formula = FORMULA.stream()
                .map(entry -> new AspectInstance(lookups.aspects().getOrThrow(aspect(entry.aspect())), entry.amount()))
                .toList();
        return new ResearchCategory(
                Optional.of(thaumaturge("unlock_artifice")),
                AspectList.ofEntries(formula),
                mod("textures/research/tab_icon.png"),
                thaumaturge("textures/gui/gui_research_back_5_smooth.png"),
                Optional.of(thaumaturge("textures/gui/gui_research_back_over.png")),
                CATEGORY_INDEX);
    }

    public static ScanEntry scan(Lookups lookups) {
        HolderSet<Block> blocks = HolderSet.direct(
                block(lookups, AEBlocks.CONTROLLER.id()),
                block(lookups, AEBlocks.INTERFACE.id()),
                block(lookups, AEBlocks.QUARTZ_GLASS.id()));
        HolderSet<Item> items = HolderSet.direct(
                item(lookups, AEBlocks.CONTROLLER.id()),
                item(lookups, AEBlocks.INTERFACE.id()),
                item(lookups, AEItems.CERTUS_QUARTZ_CRYSTAL.id()),
                item(lookups, AEItems.FLUIX_CRYSTAL.id()),
                item(lookups, AEItems.LOGIC_PROCESSOR.id()),
                item(lookups, AEItems.CALCULATION_PROCESSOR.id()),
                item(lookups, AEItems.ENGINEERING_PROCESSOR.id()));
        return new ScanEntry(mod(SCAN_KEY), Optional.of(blocks), Optional.of(items), Optional.empty());
    }

    private static Holder<Block> block(Lookups lookups, ResourceLocation id) {
        return lookups.blocks().getOrThrow(ResourceKey.create(Registries.BLOCK, id));
    }

    private static Holder<Item> item(Lookups lookups, ResourceLocation id) {
        return lookups.items().getOrThrow(ResourceKey.create(Registries.ITEM, id));
    }

    public static List<TUUResearchEntry> entries() {
        ResourceLocation salis = thaumaturge("salis_mundus");
        ResourceLocation quicksilver = thaumaturge("quicksilver");
        ResourceLocation certus = AEItems.CERTUS_QUARTZ_CRYSTAL.id();
        ResourceLocation glass = AEBlocks.QUARTZ_GLASS.id();

        return List.of(
                TUUResearchEntry.of("base_energistics", 0, 0, "Thaumic Energistics", "Energística Taumática")
                        .parents(thaumaturge("unlock_artifice"))
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_cell_64k"))
                        .stage(TUUResearchStage.of(
                                "As both a scientist and an arcane scholar, I have postulated that it must be possible to combine technology and magic in a stable and productive way.<BR>I should study the principles of matter-energy conversion in detail to accomplish this. It is likely there are aspects of Thaumaturgy that can accomplish this, as well as technology capable of doing the same.<BR>This will require extensive study, but I am sure that it will be a worthwhile investment of my time.<BR>First I must examine some of this strange networked technology with my Thaumometer.",
                                "Como científico y erudito arcano, he postulado que debe ser posible combinar la tecnología y la magia de forma estable y productiva.<BR>Debo estudiar en detalle los principios de la conversión de materia y energía para lograrlo. Es probable que existan aspectos de la Taumaturgia capaces de hacerlo, así como tecnología capaz de lo mismo.<BR>Requerirá un estudio extenso, pero estoy seguro de que será una inversión valiosa de mi tiempo.<BR>Primero debo examinar con mi Taumómetro algo de esta extraña tecnología en red.")
                                .research(mod(SCAN_KEY)))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("permutatio", 2)
                        .complexity(1),
                TUUResearchEntry.of("digisentia", 0, -2, "Digi-Sentia", "Digi-Sentia")
                        .parents(BASE)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("coalescence_core"), mod("diffusion_core"))
                        .stage(TUUResearchStage.of(
                                "Having discovered how to distill essentia, it occurs to me that I may be able to store it digitally.<BR>Examining the Formation Core and Annihilation Core I realize that with some minor modifications they could serve as a means to convert essentia to and from a digital format.<BR>However, due to the corrosive nature of certain types of essentia, a layer of quicksilver around the circuitry is required to prevent decay.",
                                "Habiendo descubierto cómo destilar esencia, se me ocurre que podría almacenarla de forma digital.<BR>Al examinar el Núcleo de Formación y el Núcleo de Aniquilación me doy cuenta de que, con pequeñas modificaciones, podrían servir para convertir esencia desde y hacia un formato digital.<BR>Sin embargo, debido a la naturaleza corrosiva de ciertos tipos de esencia, hace falta una capa de azogue alrededor de los circuitos para evitar su degradación.")
                                .theory(1)
                                .obtain(AEItems.FORMATION_CORE.id(), 1)
                                .obtain(AEItems.ANNIHILATION_CORE.id(), 1)
                                .obtain(certus, 1)
                                .obtain(ResourceLocation.withDefaultNamespace("quartz"), 1)
                                .obtain(quicksilver, 2))
                        .stage(TUUResearchStage.of(
                                "Diffusion Core<BR>The relatively high volatility and immense energy in distilled essentia requires that it first be diffused then digitized. A buffer chamber of some kind will likely be required to hold the diffused essentia.<PAGE>Coalescence Core<BR>Compresses and binds re-materialized essentia diffusion back into its raw form. A buffer chamber will also likely be needed.",
                                "Núcleo de Difusión<BR>La volatilidad relativamente alta y la inmensa energía de la esencia destilada exigen que primero se difunda y después se digitalice. Probablemente haga falta alguna cámara de almacenamiento temporal para contener la esencia difundida.<PAGE>Núcleo de Coalescencia<BR>Comprime y vuelve a unir la difusión de esencia rematerializada hasta devolverla a su forma original. También hará falta una cámara de almacenamiento temporal.")
                                .recipes(TUUThaumaturgeRecipes.DIFFUSION_CORE, TUUThaumaturgeRecipes.COALESCENCE_CORE))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("cognitio", 2)
                        .complexity(1),
                TUUResearchEntry.of("essentia_storage_1k", -2, 0, "Digisentia Storage", "Almacenamiento Digisentia")
                        .parents(BASE)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_1k"), mod("essentia_cell_1k"))
                        .stage(TUUResearchStage.of(
                                "Upon inspecting existing digital storage mediums I have concluded that I can in fact store digitized essentia.<BR>By infusing Certus Quartz with Salis Mundus, it should gain the ability to store the essentia, which remains remarkably active even in digital form.<BR>I theorize that the higher the concentration of Salis Mundus, the more it should be able to store, although there are inherent limits as to how much the quartz itself can store.",
                                "Tras inspeccionar los medios de almacenamiento digital existentes, he concluido que sí puedo almacenar esencia digitalizada.<BR>Al infundir Cuarzo Certus con Salis Mundus, debería ganar la capacidad de guardar la esencia, que sigue siendo notablemente activa incluso en forma digital.<BR>Teorizo que cuanto mayor sea la concentración de Salis Mundus, más podrá guardar, aunque hay límites inherentes a lo que el propio cuarzo puede almacenar.")
                                .theory(1)
                                .obtain(salis, 4)
                                .obtain(certus, 4)
                                .obtain(AEItems.LOGIC_PROCESSOR.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I have managed to create a storage device capable of storing a small amount of essentia digitally. Maybe I could combine multiple components for increased storage?",
                                "He logrado crear un dispositivo capaz de almacenar digitalmente una pequeña cantidad de esencia. ¿Y si combinara varios componentes para aumentar la capacidad?")
                                .recipes(TUUThaumaturgeRecipes.COMPONENT_1K, mod("cells/essentia_cell_1k"), mod("cells/essentia_cell_1k_from_housing")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("vacuos", 2)
                        .complexity(1),
                TUUResearchEntry.of("essentia_storage_4k", -4, 0, "Bigger Digisentia Storage", "Almacenamiento Digisentia Mayor")
                        .parents(STORAGE_1K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_4k"), mod("essentia_cell_4k"))
                        .stage(TUUResearchStage.of(
                                "Maybe I can use some different processing components to combine multiple storage components together.",
                                "Quizás pueda usar otros componentes de procesamiento para combinar varios componentes de almacenamiento.")
                                .theory(1)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_1k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.CALCULATION_PROCESSOR.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I can now store a decent amount of essentia digitally by using some calculation processors to allow the components to talk to each other.",
                                "Ahora puedo almacenar digitalmente una cantidad decente de esencia usando procesadores de cálculo para que los componentes se comuniquen entre sí.")
                                .recipes(TUUThaumaturgeRecipes.COMPONENT_4K, mod("cells/essentia_cell_4k"), mod("cells/essentia_cell_4k_from_housing")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("ordo", 2)
                        .complexity(1),
                TUUResearchEntry.of("essentia_storage_16k", -6, 0, "Larger Digisentia Storage", "Almacenamiento Digisentia Superior")
                        .parents(STORAGE_4K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_16k"), mod("essentia_cell_16k"))
                        .stage(TUUResearchStage.of(
                                "Like I did with my previous attempt to get bigger storage components, I can use some high end processors to combine multiple storage components together.",
                                "Igual que hice en mi intento anterior por conseguir componentes de almacenamiento mayores, puedo usar procesadores de gama alta para combinar varios componentes de almacenamiento.")
                                .theory(1)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_4k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.ENGINEERING_PROCESSOR.id(), 1))
                        .stage(TUUResearchStage.of(
                                "Using another type of processor, I can now store 16 times the amount of essentia on a storage component than when I started with my crude storage system.",
                                "Usando otro tipo de procesador, ahora puedo almacenar en un componente 16 veces la cantidad de esencia con la que empecé con mi tosco sistema de almacenamiento.")
                                .recipes(TUUThaumaturgeRecipes.COMPONENT_16K, mod("cells/essentia_cell_16k"), mod("cells/essentia_cell_16k_from_housing")))
                        .note("machina", 3)
                        .note("praecantatio", 2)
                        .note("ordo", 2)
                        .complexity(2),
                TUUResearchEntry.of("essentia_storage_64k", -8, 0, "Massive Digisentia Storage", "Almacenamiento Digisentia Masivo")
                        .parents(STORAGE_16K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_64k"), mod("essentia_cell_64k"))
                        .stage(TUUResearchStage.of(
                                "My current storage needs are not enough. I hope I can still combine storage components to create a larger one while taking up the same space...",
                                "Mis necesidades actuales de almacenamiento no son suficientes. Espero poder seguir combinando componentes para crear uno mayor que ocupe el mismo espacio...")
                                .theory(2)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_16k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.ENGINEERING_PROCESSOR.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I can now store massive amounts of essentia on a storage component, but I feel that this is the limit of the technology.",
                                "Ahora puedo almacenar cantidades enormes de esencia en un componente, pero siento que este es el límite de la tecnología.")
                                .recipes(TUUThaumaturgeRecipes.COMPONENT_64K, mod("cells/essentia_cell_64k"), mod("cells/essentia_cell_64k_from_housing")))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("potentia", 2)
                        .complexity(2),
                TUUResearchEntry.of("essentia_storage_256k", -10, 0, "Colossal Digisentia Storage", "Almacenamiento Digisentia Colosal")
                        .parents(STORAGE_64K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_256k"), mod("essentia_cell_256k"))
                        .stage(TUUResearchStage.of(
                                "Even the largest storage component I have built will not last forever. If I stack enough of the refined processors, perhaps I can reach one more step.",
                                "Ni siquiera el mayor componente que he construido durará para siempre. Si apilo suficientes procesadores refinados, quizá pueda dar un paso más.")
                                .theory(3)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_64k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.ENGINEERING_PROCESSOR.id(), 2))
                        .stage(TUUResearchStage.of(
                                "A single component now holds a colossal amount of essentia. This must surely be the ceiling of what this technology can do.",
                                "Un solo componente guarda ahora una cantidad colosal de esencia. Seguramente este es el techo de lo que puede hacer esta tecnología.")
                                .recipes(TUUThaumaturgeRecipes.COMPONENT_256K, mod("cells/essentia_cell_256k"), mod("cells/essentia_cell_256k_from_housing")))
                        .note("machina", 4)
                        .note("praecantatio", 3)
                        .note("potentia", 3)
                        .complexity(3),
                TUUResearchEntry.of("thaumic_processors", -2, 2, "Thaumic Circuitry", "Circuitería Taumática")
                        .parents(STORAGE_1K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("thaumium_logic_processor"), mod("void_engineering_processor"))
                        .stage(TUUResearchStage.of(
                                "Plain silicon and gold do not hold up against the pressure of essentia. If I print my circuits on plates of thaumium, alchemical brass and void metal instead, they should endure ever denser storage, each grade built upon the one before.",
                                "El silicio y el oro comunes no resisten la presión de la esencia. Si imprimo mis circuitos sobre placas de taumio, latón alquímico y metal del vacío, deberían soportar un almacenamiento cada vez más denso, cada grado construido sobre el anterior.")
                                .theory(1)
                                .obtain(thaumaturge("plate_thaumium"), 2)
                                .obtain(AEItems.SILICON_PRINT.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I can now press thaumium, alchemical brass and void metal circuits in an inscriber. Each print takes a plate of its metal and the matching AE2 press. The processor then joins the print, a silicon print and either redstone or the processor of the grade below.<PAGE>The essentia storage components ask for them: the 1k component takes a thaumium logic processor, the 4k a thaumium calculation processor, the 16k a thaumium engineering processor, the 64k an alchemical brass engineering processor and the 256k a void metal engineering processor. The DISKs, the MEGA cells and the matrix cores inherit that grade through the components and processors they are built from.",
                                "Ya puedo prensar circuitos de taumio, latón alquímico y metal del vacío en un inscriptor. Cada impreso pide una placa de su metal y la prensa de AE2 correspondiente. Después el procesador une el impreso, un impreso de silicio y redstone o el procesador del grado anterior.<PAGE>Los componentes de almacenamiento de esencia los exigen: el de 1k lleva un procesador lógico de taumio, el de 4k uno de cálculo de taumio, el de 16k uno de ingeniería de taumio, el de 64k uno de ingeniería de latón alquímico y el de 256k uno de ingeniería de metal del vacío. Los discos, las celdas MEGA y los núcleos de matriz heredan ese grado a través de los componentes y procesadores con los que se fabrican."))
                        .note("machina", 2)
                        .note("metallum", 2)
                        .note("praecantatio", 2)
                        .complexity(1),
                TUUResearchEntry.of("arcane_terminal", 2, 0, "Arcane Crafting Terminal", "Terminal de Crafteo Arcano")
                        .parents(BASE)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("arcane_terminal"))
                        .stage(TUUResearchStage.of(
                                "Maybe I can combine a ME Terminal with an Arcane Workbench to get something that will help me with my arcane endeavours.",
                                "Quizás pueda combinar una Terminal ME con una Mesa de Trabajo Arcana para obtener algo que me ayude en mis empeños arcanos.")
                                .theory(2))
                        .stage(TUUResearchStage.of(
                                "I can now make an Arcane Crafting Terminal.",
                                "Ya puedo fabricar una Terminal de Crafteo Arcano.")
                                .recipes(mod("arcane_terminal")))
                        .note("fabrico", 3)
                        .note("praecantatio", 2)
                        .note("machina", 2)
                        .complexity(1),
                TUUResearchEntry.of("wireless_arcane_terminal", 4, 0, "Wireless Arcane Crafting Terminal", "Terminal de Crafteo Arcano Inalámbrica")
                        .parents(ARCANE_TERMINAL)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("wireless_arcane_terminal"))
                        .stage(TUUResearchStage.of(
                                "Being tied to a block is a nuisance when my work takes me far from the lab. If I fit a wireless receiver and a dense energy cell to my Arcane Crafting Terminal, I should be able to carry it with me.",
                                "Estar atado a un bloque es un fastidio cuando mi trabajo me lleva lejos del laboratorio. Si le acoplo un receptor inalámbrico y una celda de energía densa a mi Terminal de Crafteo Arcano, debería poder llevarla conmigo.")
                                .theory(2)
                                .obtain(AEItems.WIRELESS_RECEIVER.id(), 1)
                                .obtain(AEBlocks.DENSE_ENERGY_CELL.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I can now make a Wireless Arcane Crafting Terminal. Linked to a wireless access point it reaches my network from afar, and it draws on the vis around me instead of a fixed place.",
                                "Ya puedo fabricar una Terminal de Crafteo Arcano Inalámbrica. Enlazada a un punto de acceso inalámbrico llega a mi red desde lejos, y aprovecha la vis que me rodea en lugar de la de un lugar fijo.")
                                .recipes(mod("wireless_arcane_terminal")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("potentia", 2)
                        .complexity(1),
                TUUResearchEntry.of("arcane_charging_upgrade", 2, 2, "Arcane Charging Upgrade", "Mejora de carga arcana")
                        .parents(ARCANE_TERMINAL, thaumaturge("workbench_charger"))
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("upgrade_arcane"))
                        .stage(TUUResearchStage.of(
                                "My wand sits idle inside the terminal while I work. The charger I built for my Arcane Workbench draws vis from the surrounding aura; if I bind its workings into an advanced upgrade card, the terminal might top up the wand by itself.",
                                "Mi varita queda ociosa dentro de la terminal mientras trabajo. El cargador que construí para mi Mesa de Trabajo Arcana extrae vis del aura circundante; si ato su funcionamiento a una tarjeta de mejora avanzada, la terminal quizá pueda recargar la varita por sí sola.")
                                .theory(2)
                                .obtain(AEItems.ADVANCED_CARD.id(), 1)
                                .obtain(thaumaturge("arcane_workbench_charger"), 1))
                        .stage(TUUResearchStage.of(
                                "I can now make an Arcane Charging Upgrade. Installed in an Arcane Crafting Terminal, it recharges the wand stored inside with vis from the aura around the terminal.",
                                "Ya puedo fabricar una Mejora de carga arcana. Instalada en una Terminal de Crafteo Arcano, recarga la varita guardada en su interior con vis del aura que rodea la terminal.")
                                .recipes(mod("upgrade_arcane")))
                        .note("potentia", 2)
                        .note("praecantatio", 2)
                        .note("machina", 1)
                        .complexity(1),
                TUUResearchEntry.of("essentia_terminal", -2, -2, "Essentia Terminal", "Terminal de Esencia")
                        .parents(DIGISENTIA)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_terminal"))
                        .stage(TUUResearchStage.of(
                                "With the essentia stored digitally, I need a way to look at it. A terminal made for essentia alone should let me see and pull every aspect in my network.",
                                "Con la esencia guardada en formato digital, necesito una forma de verla. Una terminal hecha solo para esencia debería dejarme ver y sacar cada aspecto de mi red.")
                                .theory(1))
                        .stage(TUUResearchStage.of(
                                "I can now make an Essentia Terminal.",
                                "Ya puedo fabricar una Terminal de Esencia.")
                                .recipes(mod("parts/essentia_terminal")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("cognitio", 1)
                        .complexity(1),
                TUUResearchEntry.of("wireless_essentia_terminal", -2, -4, "Wireless Essentia Terminal", "Terminal de Esencia Inalámbrica")
                        .parents(ESSENTIA_TERMINAL)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("wireless_essentia_terminal"))
                        .stage(TUUResearchStage.of(
                                "Checking my essentia stores means walking back to the lab every time. If I fit a wireless receiver and a dense energy cell to my Essentia Terminal, I should be able to read my network from anywhere.",
                                "Comprobar mis reservas de esencia me obliga a volver al laboratorio cada vez. Si le acoplo un receptor inalámbrico y una celda de energía densa a mi Terminal de Esencia, debería poder consultar mi red desde cualquier lugar.")
                                .theory(1)
                                .obtain(AEItems.WIRELESS_RECEIVER.id(), 1)
                                .obtain(AEBlocks.DENSE_ENERGY_CELL.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I can now make a Wireless Essentia Terminal. Linked to a wireless access point it shows and hands over the essentia in my network from afar.",
                                "Ya puedo fabricar una Terminal de Esencia Inalámbrica. Enlazada a un punto de acceso inalámbrico muestra y entrega la esencia de mi red desde lejos.")
                                .recipes(mod("wireless_essentia_terminal")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("potentia", 1)
                        .complexity(1),
                TUUResearchEntry.of("essentia_import_bus", -4, -2, "Essentia Import Bus", "Bus de Importación de Esencia")
                        .parents(ESSENTIA_TERMINAL)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_import_bus"))
                        .stage(TUUResearchStage.of(
                                "Essentia sits in jars and conduits. A bus that pulls it into the network would save me a lot of carrying.",
                                "La esencia reposa en jarras y conductos. Un bus que la metiera en la red me ahorraría mucho acarreo.")
                                .theory(1))
                        .stage(TUUResearchStage.of(
                                "I can now make an Essentia Import Bus.",
                                "Ya puedo fabricar un Bus de Importación de Esencia.")
                                .recipes(mod("parts/essentia_import_bus")))
                        .note("machina", 2)
                        .note("praecantatio", 1)
                        .note("motus", 1)
                        .complexity(1),
                TUUResearchEntry.of("essentia_export_bus", -6, -2, "Essentia Export Bus", "Bus de Exportación de Esencia")
                        .parents(ESSENTIA_IMPORT_BUS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_export_bus"))
                        .stage(TUUResearchStage.of(
                                "The reverse should also be possible: a bus that pushes essentia from the network into whatever needs it.",
                                "Lo contrario también debería ser posible: un bus que empuje esencia de la red hacia lo que la necesite.")
                                .theory(1))
                        .stage(TUUResearchStage.of(
                                "I can now make an Essentia Export Bus.",
                                "Ya puedo fabricar un Bus de Exportación de Esencia.")
                                .recipes(mod("parts/essentia_export_bus")))
                        .note("machina", 2)
                        .note("praecantatio", 1)
                        .note("motus", 1)
                        .complexity(1),
                TUUResearchEntry.of("essentia_storage_bus", -8, -2, "Essentia Storage Bus", "Bus de Almacenamiento de Esencia")
                        .parents(ESSENTIA_EXPORT_BUS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_storage_bus"))
                        .stage(TUUResearchStage.of(
                                "Instead of moving essentia around, I could present a whole jar to the network as if it were part of it.",
                                "En lugar de mover esencia de un lado a otro, podría presentar una jarra entera a la red como si formara parte de ella.")
                                .theory(1))
                        .stage(TUUResearchStage.of(
                                "I can now make an Essentia Storage Bus.",
                                "Ya puedo fabricar un Bus de Almacenamiento de Esencia.")
                                .recipes(mod("parts/essentia_storage_bus")))
                        .note("machina", 2)
                        .note("vacuos", 1)
                        .note("permutatio", 1)
                        .complexity(1),
                TUUResearchEntry.of("infusion_provider", 2, -2, "Infusion Provider", "Proveedor de Infusión")
                        .parents(DIGISENTIA, thaumaturge("infusion"))
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("infusion_provider"))
                        .stage(TUUResearchStage.of(
                                "Now that I can digitize essentia, it's time to put that technology to work. Perhaps an infusion matrix could draw the essentia it needs straight from my network.",
                                "Ahora que puedo digitalizar esencia, es hora de poner esa tecnología a trabajar. Quizás una matriz de infusión pueda tomar la esencia que necesita directamente de mi red.")
                                .theory(3))
                        .stage(TUUResearchStage.of(
                                "I can now make an Infusion Provider. Placed within reach of an infusion matrix and connected to my network, it hands over the essentia an infusion asks for.",
                                "Ya puedo fabricar un Proveedor de Infusión. Colocado al alcance de una matriz de infusión y conectado a mi red, entrega la esencia que pide la infusión.")
                                .recipes(TUUThaumaturgeRecipes.INFUSION_PROVIDER)
                                .warp(2))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("permutatio", 2)
                        .complexity(2),
                TUUResearchEntry.of("essentia_disks", -4, 2, "Essentia DISKs", "Discos de esencia")
                        .parents(THAUMIC_PROCESSORS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_4k"))
                        .stage(TUUResearchStage.of(
                                "A cell is bound to the drive it sits in, and when I pull one out I can only hope it still holds what I left in it. I have read of another kind of medium that keeps its contents on the item itself. If I fit my storage components into such a housing, the essentia should travel with the disk.",
                                "Una celda queda atada a la unidad en la que está, y cuando saco una solo puedo esperar que conserve lo que dejé dentro. He leído sobre otro tipo de medio que guarda su contenido en el propio objeto. Si acoplo mis componentes de almacenamiento a una carcasa así, la esencia debería viajar con el disco.")
                                .theory(2)
                                .obtain(mod("essentia_component_4k"), 1)
                                .obtain(salis, 4))
                        .stage(TUUResearchStage.of(
                                "I can now make Essentia DISKs of 1k, 4k, 16k, 64k and 256k. They store essentia only, but unlike a cell they keep it inside the disk, so it can be moved from one drive to another without losing a drop.<PAGE>Each DISK is built around the matching essentia storage component, so the circuitry of that component decides how far up the thaumic grades it goes. A DISK can also be assembled from an AE2 Things disk housing and a component. Capacities and power use can be changed in the compat configuration.",
                                "Ya puedo fabricar discos de esencia de 1k, 4k, 16k, 64k y 256k. Solo almacenan esencia, pero a diferencia de una celda la guardan dentro del propio disco, así que se pueden mover de una unidad a otra sin perder una gota.<PAGE>Cada disco se construye alrededor del componente de almacenamiento de esencia correspondiente, así que la circuitería de ese componente decide hasta qué grado taumático llega. Un disco también se puede montar con una carcasa de disco de AE2 Things y un componente. Las capacidades y el consumo de energía se cambian en la configuración de compatibilidad.")
                                .recipes(diskRecipes("disks/essentia_disk_", false)))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("ordo", 2)
                        .complexity(2),
                TUUResearchEntry.of("mega_essentia_disks", -6, 2, "MEGA Essentia DISKs", "Discos de esencia MEGA")
                        .parents(ESSENTIA_DISKS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_256k"), mod("void_engineering_processor"))
                        .stage(TUUResearchStage.of(
                                "The largest DISK I can make is nothing next to what a dense store would swallow. The storage components of another craft pack their cells far tighter, and a housing strong enough to hold them should be forged from void metal.",
                                "El mayor disco que puedo hacer no es nada frente a lo que tragaría un almacén denso. Los componentes de almacenamiento de otro oficio comprimen sus celdas mucho más, y una carcasa lo bastante fuerte para contenerlos debería forjarse con metal del vacío.")
                                .theory(3)
                                .obtain(mod("essentia_component_256k"), 1)
                                .obtain(salis, 4))
                        .stage(TUUResearchStage.of(
                                "I can now make a MEGA Essentia DISK Housing and, from the MEGA storage components, MEGA Essentia DISKs of 1M, 4M, 16M, 64M and 256M.<PAGE>They behave like the smaller DISKs and keep their essentia on the item, but they draw more power from the network. Capacities and power use can be changed in the compat configuration.",
                                "Ya puedo fabricar una Carcasa de disco de esencia MEGA y, con los componentes de almacenamiento MEGA, discos de esencia MEGA de 1M, 4M, 16M, 64M y 256M.<PAGE>Se comportan como los discos menores y guardan su esencia en el objeto, pero consumen más energía de la red. Las capacidades y el consumo de energía se cambian en la configuración de compatibilidad.")
                                .recipes(megaDiskRecipes()))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("vacuos", 3)
                        .complexity(3),
                TUUResearchEntry.of("mega_essentia_cells", -8, 2, "MEGA Essentia Cells", "Celdas de esencia MEGA")
                        .parents(STORAGE_256K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_256k"), mod("essentia_cell_256k"))
                        .stage(TUUResearchStage.of(
                                "My 256k components were once the ceiling, but the MEGA storage components reach into the millions. A cell housing of void metal, lined with Salis Mundus, should be able to hold one.",
                                "Mis componentes de 256k fueron el techo, pero los componentes de almacenamiento MEGA llegan a los millones. Una carcasa de celda de metal del vacío, forrada con Salis Mundus, debería poder contener uno.")
                                .theory(3)
                                .obtain(mod("essentia_component_256k"), 1)
                                .obtain(salis, 4))
                        .stage(TUUResearchStage.of(
                                "I can now make a MEGA Essentia Cell Housing and MEGA Essentia Storage Cells of 1M, 4M, 16M, 64M and 256M.<PAGE>They work like any other essentia cell in a drive and accept the inverter, equal distribution and void cards. Capacities and power use can be changed in the compat configuration.",
                                "Ya puedo fabricar una Carcasa de celda de esencia MEGA y celdas de almacenamiento de esencia MEGA de 1M, 4M, 16M, 64M y 256M.<PAGE>Funcionan como cualquier otra celda de esencia en una unidad y admiten las tarjetas de inversión, distribución equitativa y vacío. Las capacidades y el consumo de energía se cambian en la configuración de compatibilidad.")
                                .recipes(megaCellRecipes()))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("potentia", 3)
                        .complexity(3),
                TUUResearchEntry.of("quantum_essentia", 0, -4, "Quantum Essentia", "Esencia cuántica")
                        .parents(DIGISENTIA)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(salis, AEBlocks.PATTERN_PROVIDER.id())
                        .stage(TUUResearchStage.of(
                                "The strange alloys and circuits of the quantum devices must be made of something. If I study them with my Thaumometer I should be able to learn which essentia they hold.",
                                "Las extrañas aleaciones y circuitos de los dispositivos cuánticos deben de estar hechos de algo. Si los estudio con mi Taumómetro debería poder averiguar qué esencia contienen.")
                                .theory(1))
                        .stage(TUUResearchStage.of(
                                "I can now read the essentia of the quantum alloy and its dust, plates and blocks, the quantum processors and storage components, the advanced pattern providers and buses, the upgrade cards and the quantum armor.<PAGE>Mostly Alienis and Potentia, with Machina and Ordo in the circuitry and Metallum in the alloys. Aspect values can be switched off in the compat configuration.",
                                "Ya puedo leer la esencia de la aleación cuántica y de su polvo, placas y bloques, de los procesadores y componentes de almacenamiento cuánticos, de los proveedores de patrones y buses avanzados, de las tarjetas de mejora y de la armadura cuántica.<PAGE>Sobre todo Alienis y Potentia, con Machina y Ordo en la circuitería y Metallum en las aleaciones. Los valores de aspectos se pueden desactivar en la configuración de compatibilidad."))
                        .note("machina", 2)
                        .note("alienis", 2)
                        .note("cognitio", 2)
                        .complexity(1),
                TUUResearchEntry.of("matrix_tiers", -2, 4, "Metal Assembler Matrix Cores", "Núcleos de matriz ensambladora metálicos")
                        .parents(THAUMIC_PROCESSORS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(AEBlocks.MOLECULAR_ASSEMBLER.id(), mod("thaumium_engineering_processor"))
                        .stage(TUUResearchStage.of(
                                "The Assembler Matrix scales with the cores I stack into it, but its stock cores soon reach their limits. If I rebuild them with plates of thaumium, alchemical brass and void metal and the processors I have already pressed, each core should push the matrix further than the one before.",
                                "La Matriz Ensambladora escala con los núcleos que apilo en ella, pero sus núcleos de serie pronto llegan al límite. Si los reconstruyo con placas de taumio, latón alquímico y metal del vacío y los procesadores que ya he prensado, cada núcleo debería llevar la matriz más lejos que el anterior.")
                                .theory(2)
                                .obtain(thaumaturge("plate_thaumium"), 4)
                                .obtain(AEItems.FLUIX_CRYSTAL.id(), 2)
                                .obtain(mod("thaumium_engineering_processor"), 1))
                        .stage(TUUResearchStage.of(
                                "I can now make craft, pattern and speed cores of thaumium, alchemical brass and void metal. Each one is built from the core of the grade below, with four plates of its metal, two fluix crystals and two calculation, logic or engineering processors respectively.<PAGE>Craft cores multiply the time the matrix advances every tick: by 2, 4 and 8. Speed cores add 2, 3 and 5 speed cores to the matrix, which never goes beyond the 5 it allows. Pattern cores add 36, 72 and 144 pattern slots on top of the original 36, but those extra slots only show in the pattern listing of the network, not in the matrix interface. The numbers can be changed in the compat configuration.",
                                "Ya puedo fabricar núcleos de fabricación, de patrones y de velocidad de taumio, latón alquímico y metal del vacío. Cada uno se construye a partir del núcleo del grado inferior, con cuatro placas de su metal, dos cristales de fluix y dos procesadores de cálculo, lógicos o de ingeniería respectivamente.<PAGE>Los núcleos de fabricación multiplican el tiempo que avanza la matriz cada tic: por 2, 4 y 8. Los de velocidad añaden 2, 3 y 5 núcleos de velocidad a la matriz, que nunca pasa de los 5 que admite. Los de patrones añaden 36, 72 y 144 huecos de patrón además de los 36 originales, pero esos huecos solo aparecen en el listado de patrones de la red, no en la interfaz de la matriz. Las cifras se cambian en la configuración de compatibilidad.")
                                .recipes(matrixRecipes()))
                        .note("machina", 3)
                        .note("fabrico", 3)
                        .note("metallum", 3)
                        .complexity(3),
                TUUResearchEntry.of("infinity_cells", 0, 4, "Infinite Cells", "Celdas infinitas")
                        .parents(THAUMIC_PROCESSORS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(thaumaturge("plate_thaumium"), AEItems.CELL_COMPONENT_16K.id())
                        .stage(TUUResearchStage.of(
                                "Arcane stone and the crystals of the six primal essentia are things I will never run short of. If I bind one of them to a storage component with plates of thaumium, the cell should present an endless supply of it to the whole network.",
                                "La piedra arcana y los cristales de las seis esencias primigenias son cosas que nunca me faltarán. Si ato uno de ellos a un componente de almacenamiento con placas de taumio, la celda debería ofrecer un suministro sin fin a toda la red.")
                                .theory(2)
                                .obtain(thaumaturge("plate_thaumium"), 3)
                                .obtain(AEItems.CELL_COMPONENT_16K.id(), 1))
                        .stage(TUUResearchStage.of(
                                "I can now make an Infinite Cell of arcane stone and one for each of the six vis crystals: terra, ignis, aer, aqua, ordo and perditio.<PAGE>Each cell holds an unlimited amount of exactly one thing: it hands out as much as the network asks for and swallows whatever of that kind is sent to it. It draws a steady trickle of power, which can be changed in the compat configuration.",
                                "Ya puedo fabricar una Celda infinita de piedra arcana y una por cada uno de los seis cristales de vis: terra, ignis, aer, aqua, ordo y perditio.<PAGE>Cada celda guarda una cantidad ilimitada de una única cosa: entrega todo lo que la red le pida y se traga lo que se le envíe de ese tipo. Consume un goteo constante de energía, que se cambia en la configuración de compatibilidad.")
                                .recipes(infinityRecipes()))
                        .note("machina", 2)
                        .note("vacuos", 3)
                        .note("praecantatio", 3)
                        .complexity(2));
    }

    private static ResourceLocation[] diskRecipes(String prefix, boolean mega) {
        String[] sizes = mega ? TUUCompatConfig.MEGA_SIZE_NAMES : TUUCompatConfig.SIZE_NAMES;
        ResourceLocation[] recipes = new ResourceLocation[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            recipes[i] = mod(prefix + sizes[i]);
        }
        return recipes;
    }

    private static ResourceLocation[] megaDiskRecipes() {
        ResourceLocation[] disks = diskRecipes("disks/essentia_disk_", true);
        ResourceLocation[] all = new ResourceLocation[disks.length + 1];
        all[0] = mod("disks/mega_essentia_disk_housing");
        System.arraycopy(disks, 0, all, 1, disks.length);
        return all;
    }

    private static ResourceLocation[] megaCellRecipes() {
        ResourceLocation[] cells = diskRecipes("cells/essentia_cell_", true);
        ResourceLocation[] all = new ResourceLocation[cells.length + 1];
        all[0] = mod("cells/mega_essentia_cell_housing");
        System.arraycopy(cells, 0, all, 1, cells.length);
        return all;
    }

    private static ResourceLocation[] matrixRecipes() {
        List<ResourceLocation> recipes = new ArrayList<>();
        for (String kind : MATRIX_KINDS) {
            for (String metal : METALS) {
                recipes.add(mod("matrix/assembler_matrix_" + kind + "_" + metal));
            }
        }
        return recipes.toArray(ResourceLocation[]::new);
    }

    private static ResourceLocation[] infinityRecipes() {
        List<ResourceLocation> recipes = new ArrayList<>();
        recipes.add(mod("infinity_cells/infinity_cell_arcane_stone"));
        for (String aspect : INFINITY_ASPECTS) {
            recipes.add(mod("infinity_cells/infinity_cell_" + aspect));
        }
        return recipes.toArray(ResourceLocation[]::new);
    }
}
