package velrondev.thaumaenergistics.datagen.providers;

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
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import velrondev.thaumaenergistics.ThaumaEnergistics;

public final class TEResearch {

    public record Lookups(HolderGetter<IAspect> aspects, HolderGetter<Item> items, HolderGetter<Block> blocks) {
    }

    private record FormulaEntry(String aspect, int amount) {
    }

    public static final String TERMINUS_MOD = "thaumaturgeusefulutilities";
    public static final ResourceKey<IAspect> TERMINUS = ResourceKey.create(IAspect.REGISTRY_KEY, Identifier.fromNamespaceAndPath(TERMINUS_MOD, "terminus"));
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

    public static final Identifier BASE = mod("base_energistics");
    public static final Identifier DIGISENTIA = mod("digisentia");
    public static final Identifier STORAGE_1K = mod("essentia_storage_1k");
    public static final Identifier STORAGE_4K = mod("essentia_storage_4k");
    public static final Identifier STORAGE_16K = mod("essentia_storage_16k");
    public static final Identifier STORAGE_64K = mod("essentia_storage_64k");
    public static final Identifier STORAGE_256K = mod("essentia_storage_256k");
    public static final Identifier INFINITY_CELL = mod("infinity_cell");
    public static final Identifier ARCANE_TERMINAL = mod("arcane_terminal");
    public static final Identifier ESSENTIA_TERMINAL = mod("essentia_terminal");
    public static final Identifier ESSENTIA_IMPORT_BUS = mod("essentia_import_bus");
    public static final Identifier ESSENTIA_EXPORT_BUS = mod("essentia_export_bus");
    public static final Identifier ESSENTIA_STORAGE_BUS = mod("essentia_storage_bus");
    public static final Identifier INFUSION_PROVIDER = mod("infusion_provider");
    public static final Identifier WIRELESS_ARCANE_TERMINAL = mod("wireless_arcane_terminal");
    public static final Identifier WIRELESS_ESSENTIA_TERMINAL = mod("wireless_essentia_terminal");
    public static final Identifier ARCANE_CHARGING_UPGRADE = mod("arcane_charging_upgrade");

    private TEResearch() {
    }

    public static Identifier mod(String path) {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path);
    }

    public static Identifier thaumaturge(String path) {
        return Identifier.fromNamespaceAndPath("thaumaturge", path);
    }

    public static ResourceKey<IAspect> aspect(String path) {
        return ResourceKey.create(IAspect.REGISTRY_KEY, thaumaturge(path));
    }

    public static Set<ResourceKey<IAspect>> aspectKeys() {
        Set<ResourceKey<IAspect>> keys = new LinkedHashSet<>();
        keys.add(TERMINUS);
        FORMULA.forEach(entry -> keys.add(aspect(entry.aspect())));
        for (TEResearchEntry entry : entries()) {
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

    private static Holder<Block> block(Lookups lookups, Identifier id) {
        return lookups.blocks().getOrThrow(ResourceKey.create(Registries.BLOCK, id));
    }

    private static Holder<Item> item(Lookups lookups, Identifier id) {
        return lookups.items().getOrThrow(ResourceKey.create(Registries.ITEM, id));
    }

    public static List<TEResearchEntry> entries() {
        Identifier salis = thaumaturge("salis_mundus");
        Identifier quicksilver = thaumaturge("quicksilver");
        Identifier certus = AEItems.CERTUS_QUARTZ_CRYSTAL.id();
        Identifier glass = AEBlocks.QUARTZ_GLASS.id();

        return List.of(
                TEResearchEntry.of("base_energistics", 0, 0, "Thaumic Energistics", "Energística Taumática")
                        .parents(thaumaturge("unlock_artifice"))
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_cell_64k"))
                        .stage(TEResearchStage.of(
                                "As both a scientist and an arcane scholar, I have postulated that it must be possible to combine technology and magic in a stable and productive way.<BR>I should study the principles of matter-energy conversion in detail to accomplish this. It is likely there are aspects of Thaumaturgy that can accomplish this, as well as technology capable of doing the same.<BR>This will require extensive study, but I am sure that it will be a worthwhile investment of my time.<BR>First I must examine some of this strange networked technology with my Thaumometer.",
                                "Como científico y erudito arcano, he postulado que debe ser posible combinar la tecnología y la magia de forma estable y productiva.<BR>Debo estudiar en detalle los principios de la conversión de materia y energía para lograrlo. Es probable que existan aspectos de la Taumaturgia capaces de hacerlo, así como tecnología capaz de lo mismo.<BR>Requerirá un estudio extenso, pero estoy seguro de que será una inversión valiosa de mi tiempo.<BR>Primero debo examinar con mi Taumómetro algo de esta extraña tecnología en red.")
                                .research(mod(SCAN_KEY)))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("permutatio", 2)
                        .complexity(1),
                TEResearchEntry.of("digisentia", 0, -2, "Digi-Sentia", "Digi-Sentia")
                        .parents(BASE)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("coalescence_core"), mod("diffusion_core"))
                        .stage(TEResearchStage.of(
                                "Having discovered how to distill essentia, it occurs to me that I may be able to store it digitally.<BR>Examining the Formation Core and Annihilation Core I realize that with some minor modifications they could serve as a means to convert essentia to and from a digital format.<BR>However, due to the corrosive nature of certain types of essentia, a layer of quicksilver around the circuitry is required to prevent decay.",
                                "Habiendo descubierto cómo destilar esencia, se me ocurre que podría almacenarla de forma digital.<BR>Al examinar el Núcleo de Formación y el Núcleo de Aniquilación me doy cuenta de que, con pequeñas modificaciones, podrían servir para convertir esencia desde y hacia un formato digital.<BR>Sin embargo, debido a la naturaleza corrosiva de ciertos tipos de esencia, hace falta una capa de azogue alrededor de los circuitos para evitar su degradación.")
                                .theory(1)
                                .obtain(AEItems.FORMATION_CORE.id(), 1)
                                .obtain(AEItems.ANNIHILATION_CORE.id(), 1)
                                .obtain(certus, 1)
                                .obtain(Identifier.withDefaultNamespace("quartz"), 1)
                                .obtain(quicksilver, 2))
                        .stage(TEResearchStage.of(
                                "Diffusion Core<BR>The relatively high volatility and immense energy in distilled essentia requires that it first be diffused then digitized. A buffer chamber of some kind will likely be required to hold the diffused essentia.<PAGE>Coalescence Core<BR>Compresses and binds re-materialized essentia diffusion back into its raw form. A buffer chamber will also likely be needed.",
                                "Núcleo de Difusión<BR>La volatilidad relativamente alta y la inmensa energía de la esencia destilada exigen que primero se difunda y después se digitalice. Probablemente haga falta alguna cámara de almacenamiento temporal para contener la esencia difundida.<PAGE>Núcleo de Coalescencia<BR>Comprime y vuelve a unir la difusión de esencia rematerializada hasta devolverla a su forma original. También hará falta una cámara de almacenamiento temporal.")
                                .recipes(TEThaumaturgeRecipes.DIFFUSION_CORE, TEThaumaturgeRecipes.COALESCENCE_CORE))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("cognitio", 2)
                        .complexity(1),
                TEResearchEntry.of("essentia_storage_1k", -2, 0, "Digisentia Storage", "Almacenamiento Digisentia")
                        .parents(BASE)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_1k"), mod("essentia_cell_1k"))
                        .stage(TEResearchStage.of(
                                "Upon inspecting existing digital storage mediums I have concluded that I can in fact store digitized essentia.<BR>By infusing Certus Quartz with Salis Mundus, it should gain the ability to store the essentia, which remains remarkably active even in digital form.<BR>I theorize that the higher the concentration of Salis Mundus, the more it should be able to store, although there are inherent limits as to how much the quartz itself can store.",
                                "Tras inspeccionar los medios de almacenamiento digital existentes, he concluido que sí puedo almacenar esencia digitalizada.<BR>Al infundir Cuarzo Certus con Salis Mundus, debería ganar la capacidad de guardar la esencia, que sigue siendo notablemente activa incluso en forma digital.<BR>Teorizo que cuanto mayor sea la concentración de Salis Mundus, más podrá guardar, aunque hay límites inherentes a lo que el propio cuarzo puede almacenar.")
                                .theory(1)
                                .obtain(salis, 4)
                                .obtain(certus, 4)
                                .obtain(AEItems.LOGIC_PROCESSOR.id(), 1))
                        .stage(TEResearchStage.of(
                                "I have managed to create a storage device capable of storing a small amount of essentia digitally. Maybe I could combine multiple components for increased storage?",
                                "He logrado crear un dispositivo capaz de almacenar digitalmente una pequeña cantidad de esencia. ¿Y si combinara varios componentes para aumentar la capacidad?")
                                .recipes(TEThaumaturgeRecipes.COMPONENT_1K, mod("cells/essentia_cell_1k"), mod("cells/essentia_cell_1k_from_housing")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("vacuos", 2)
                        .complexity(1),
                TEResearchEntry.of("essentia_storage_4k", -4, 0, "Bigger Digisentia Storage", "Almacenamiento Digisentia Mayor")
                        .parents(STORAGE_1K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_4k"), mod("essentia_cell_4k"))
                        .stage(TEResearchStage.of(
                                "Maybe I can use some different processing components to combine multiple storage components together.",
                                "Quizás pueda usar otros componentes de procesamiento para combinar varios componentes de almacenamiento.")
                                .theory(1)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_1k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.CALCULATION_PROCESSOR.id(), 1))
                        .stage(TEResearchStage.of(
                                "I can now store a decent amount of essentia digitally by using some calculation processors to allow the components to talk to each other.",
                                "Ahora puedo almacenar digitalmente una cantidad decente de esencia usando procesadores de cálculo para que los componentes se comuniquen entre sí.")
                                .recipes(TEThaumaturgeRecipes.COMPONENT_4K, mod("cells/essentia_cell_4k"), mod("cells/essentia_cell_4k_from_housing")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("ordo", 2)
                        .complexity(1),
                TEResearchEntry.of("essentia_storage_16k", -6, 0, "Larger Digisentia Storage", "Almacenamiento Digisentia Superior")
                        .parents(STORAGE_4K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_16k"), mod("essentia_cell_16k"))
                        .stage(TEResearchStage.of(
                                "Like I did with my previous attempt to get bigger storage components, I can use some high end processors to combine multiple storage components together.",
                                "Igual que hice en mi intento anterior por conseguir componentes de almacenamiento mayores, puedo usar procesadores de gama alta para combinar varios componentes de almacenamiento.")
                                .theory(1)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_4k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.ENGINEERING_PROCESSOR.id(), 1))
                        .stage(TEResearchStage.of(
                                "Using another type of processor, I can now store 16 times the amount of essentia on a storage component than when I started with my crude storage system.",
                                "Usando otro tipo de procesador, ahora puedo almacenar en un componente 16 veces la cantidad de esencia con la que empecé con mi tosco sistema de almacenamiento.")
                                .recipes(TEThaumaturgeRecipes.COMPONENT_16K, mod("cells/essentia_cell_16k"), mod("cells/essentia_cell_16k_from_housing")))
                        .note("machina", 3)
                        .note("praecantatio", 2)
                        .note("ordo", 2)
                        .complexity(2),
                TEResearchEntry.of("essentia_storage_64k", -8, 0, "Massive Digisentia Storage", "Almacenamiento Digisentia Masivo")
                        .parents(STORAGE_16K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_64k"), mod("essentia_cell_64k"))
                        .stage(TEResearchStage.of(
                                "My current storage needs are not enough. I hope I can still combine storage components to create a larger one while taking up the same space...",
                                "Mis necesidades actuales de almacenamiento no son suficientes. Espero poder seguir combinando componentes para crear uno mayor que ocupe el mismo espacio...")
                                .theory(2)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_16k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.ENGINEERING_PROCESSOR.id(), 1))
                        .stage(TEResearchStage.of(
                                "I can now store massive amounts of essentia on a storage component, but I feel that this is the limit of the technology.",
                                "Ahora puedo almacenar cantidades enormes de esencia en un componente, pero siento que este es el límite de la tecnología.")
                                .recipes(TEThaumaturgeRecipes.COMPONENT_64K, mod("cells/essentia_cell_64k"), mod("cells/essentia_cell_64k_from_housing")))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("potentia", 2)
                        .complexity(2),
                TEResearchEntry.of("essentia_storage_256k", -10, 0, "Colossal Digisentia Storage", "Almacenamiento Digisentia Colosal")
                        .parents(STORAGE_64K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_component_256k"), mod("essentia_cell_256k"))
                        .stage(TEResearchStage.of(
                                "Even the largest storage component I have built will not last forever. If I stack enough of the refined processors, perhaps I can reach one more step.",
                                "Ni siquiera el mayor componente que he construido durará para siempre. Si apilo suficientes procesadores refinados, quizá pueda dar un paso más.")
                                .theory(3)
                                .obtain(salis, 4)
                                .obtain(mod("essentia_component_64k"), 3)
                                .obtain(glass, 1)
                                .obtain(AEItems.ENGINEERING_PROCESSOR.id(), 2))
                        .stage(TEResearchStage.of(
                                "A single component now holds a colossal amount of essentia. This must surely be the ceiling of what this technology can do.",
                                "Un solo componente guarda ahora una cantidad colosal de esencia. Seguramente este es el techo de lo que puede hacer esta tecnología.")
                                .recipes(TEThaumaturgeRecipes.COMPONENT_256K, mod("cells/essentia_cell_256k"), mod("cells/essentia_cell_256k_from_housing")))
                        .note("machina", 4)
                        .note("praecantatio", 3)
                        .note("potentia", 3)
                        .complexity(3),
                TEResearchEntry.of("infinity_cell", -12, 0, "Infinity Digisentia Storage", "Almacenamiento Digisentia Infinito")
                        .parents(STORAGE_256K)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_cell_infinity"))
                        .condition(TERMINUS_MOD)
                        .stage(TEResearchStage.of(
                                "The colossal storage is still a finite vessel, and sooner or later it will fill. Whispers speak of Terminus, an aspect of the end of all bounds, that unmakes limits themselves. If I infuse a colossal cell with it, flanked by diffusion cores and the finest components, perhaps the cell will stop counting at all.",
                                "El almacenamiento colosal sigue siendo un recipiente finito, y tarde o temprano se llenará. Se habla en susurros de Terminus, un aspecto del fin de toda frontera, que deshace los propios límites. Si infundo una celda colosal con él, flanqueada por núcleos de difusión y los mejores componentes, quizá la celda deje de contar por completo.")
                                .theory(3)
                                .obtain(mod("essentia_cell_256k"), 1)
                                .obtain(mod("essentia_component_256k"), 2)
                                .obtain(mod("diffusion_core"), 2))
                        .stage(TEResearchStage.of(
                                "I can now make an Infinity Essentia Storage Cell. It holds essentia without a ceiling, and the Terminus woven into it makes the infusion dangerously unstable.",
                                "Ya puedo fabricar una Celda de Almacenamiento de Esencia Infinita. Guarda esencia sin techo, y el Terminus entretejido en ella vuelve la infusión peligrosamente inestable.")
                                .recipes(TEThaumaturgeRecipes.INFINITY_CELL)
                                .warp(3))
                        .note("machina", 4)
                        .note("praecantatio", 4)
                        .note("vacuos", 3)
                        .note("potentia", 3)
                        .complexity(3),
                TEResearchEntry.of("arcane_terminal", 2, 0,"Arcane Crafting Terminal", "Terminal de Crafteo Arcano")
                        .parents(BASE)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("arcane_terminal"))
                        .stage(TEResearchStage.of(
                                "Maybe I can combine a ME Terminal with an Arcane Workbench to get something that will help me with my arcane endeavours.",
                                "Quizás pueda combinar una Terminal ME con una Mesa de Trabajo Arcana para obtener algo que me ayude en mis empeños arcanos.")
                                .theory(2))
                        .stage(TEResearchStage.of(
                                "I can now make an Arcane Crafting Terminal.",
                                "Ya puedo fabricar una Terminal de Crafteo Arcano.")
                                .recipes(mod("arcane_terminal")))
                        .note("fabrico", 3)
                        .note("praecantatio", 2)
                        .note("machina", 2)
                        .complexity(1),
                TEResearchEntry.of("wireless_arcane_terminal", 4, 0, "Wireless Arcane Crafting Terminal", "Terminal de Crafteo Arcano Inalámbrica")
                        .parents(ARCANE_TERMINAL)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("wireless_arcane_terminal"))
                        .stage(TEResearchStage.of(
                                "Being tied to a block is a nuisance when my work takes me far from the lab. If I fit a wireless receiver and a dense energy cell to my Arcane Crafting Terminal, I should be able to carry it with me.",
                                "Estar atado a un bloque es un fastidio cuando mi trabajo me lleva lejos del laboratorio. Si le acoplo un receptor inalámbrico y una celda de energía densa a mi Terminal de Crafteo Arcano, debería poder llevarla conmigo.")
                                .theory(2)
                                .obtain(AEItems.WIRELESS_RECEIVER.id(), 1)
                                .obtain(AEBlocks.DENSE_ENERGY_CELL.id(), 1))
                        .stage(TEResearchStage.of(
                                "I can now make a Wireless Arcane Crafting Terminal. Linked to a wireless access point it reaches my network from afar, and it draws on the vis around me instead of a fixed place.",
                                "Ya puedo fabricar una Terminal de Crafteo Arcano Inalámbrica. Enlazada a un punto de acceso inalámbrico llega a mi red desde lejos, y aprovecha la vis que me rodea en lugar de la de un lugar fijo.")
                                .recipes(mod("wireless_arcane_terminal")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("potentia", 2)
                        .complexity(1),
                TEResearchEntry.of("arcane_charging_upgrade", 2, 2, "Arcane Charging Upgrade", "Mejora de carga arcana")
                        .parents(ARCANE_TERMINAL, thaumaturge("workbench_charger"))
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("upgrade_arcane"))
                        .stage(TEResearchStage.of(
                                "My wand sits idle inside the terminal while I work. The charger I built for my Arcane Workbench draws vis from the surrounding aura; if I bind its workings into an advanced upgrade card, the terminal might top up the wand by itself.",
                                "Mi varita queda ociosa dentro de la terminal mientras trabajo. El cargador que construí para mi Mesa de Trabajo Arcana extrae vis del aura circundante; si ato su funcionamiento a una tarjeta de mejora avanzada, la terminal quizá pueda recargar la varita por sí sola.")
                                .theory(2)
                                .obtain(AEItems.ADVANCED_CARD.id(), 1)
                                .obtain(thaumaturge("arcane_workbench_charger"), 1))
                        .stage(TEResearchStage.of(
                                "I can now make an Arcane Charging Upgrade. Installed in an Arcane Crafting Terminal, it recharges the wand stored inside with vis from the aura around the terminal.",
                                "Ya puedo fabricar una Mejora de carga arcana. Instalada en una Terminal de Crafteo Arcano, recarga la varita guardada en su interior con vis del aura que rodea la terminal.")
                                .recipes(mod("upgrade_arcane")))
                        .note("potentia", 2)
                        .note("praecantatio", 2)
                        .note("machina", 1)
                        .complexity(1),
                TEResearchEntry.of("essentia_terminal", -2, -2, "Essentia Terminal", "Terminal de Esencia")
                        .parents(DIGISENTIA)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_terminal"))
                        .stage(TEResearchStage.of(
                                "With the essentia stored digitally, I need a way to look at it. A terminal made for essentia alone should let me see and pull every aspect in my network.",
                                "Con la esencia guardada en formato digital, necesito una forma de verla. Una terminal hecha solo para esencia debería dejarme ver y sacar cada aspecto de mi red.")
                                .theory(1))
                        .stage(TEResearchStage.of(
                                "I can now make an Essentia Terminal.",
                                "Ya puedo fabricar una Terminal de Esencia.")
                                .recipes(mod("parts/essentia_terminal")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("cognitio", 1)
                        .complexity(1),
                TEResearchEntry.of("wireless_essentia_terminal", -2, -4, "Wireless Essentia Terminal", "Terminal de Esencia Inalámbrica")
                        .parents(ESSENTIA_TERMINAL)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("wireless_essentia_terminal"))
                        .stage(TEResearchStage.of(
                                "Checking my essentia stores means walking back to the lab every time. If I fit a wireless receiver and a dense energy cell to my Essentia Terminal, I should be able to read my network from anywhere.",
                                "Comprobar mis reservas de esencia me obliga a volver al laboratorio cada vez. Si le acoplo un receptor inalámbrico y una celda de energía densa a mi Terminal de Esencia, debería poder consultar mi red desde cualquier lugar.")
                                .theory(1)
                                .obtain(AEItems.WIRELESS_RECEIVER.id(), 1)
                                .obtain(AEBlocks.DENSE_ENERGY_CELL.id(), 1))
                        .stage(TEResearchStage.of(
                                "I can now make a Wireless Essentia Terminal. Linked to a wireless access point it shows and hands over the essentia in my network from afar.",
                                "Ya puedo fabricar una Terminal de Esencia Inalámbrica. Enlazada a un punto de acceso inalámbrico muestra y entrega la esencia de mi red desde lejos.")
                                .recipes(mod("wireless_essentia_terminal")))
                        .note("machina", 2)
                        .note("praecantatio", 2)
                        .note("potentia", 1)
                        .complexity(1),
                TEResearchEntry.of("essentia_import_bus", -4, -2, "Essentia Import Bus", "Bus de Importación de Esencia")
                        .parents(ESSENTIA_TERMINAL)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_import_bus"))
                        .stage(TEResearchStage.of(
                                "Essentia sits in jars and conduits. A bus that pulls it into the network would save me a lot of carrying.",
                                "La esencia reposa en jarras y conductos. Un bus que la metiera en la red me ahorraría mucho acarreo.")
                                .theory(1))
                        .stage(TEResearchStage.of(
                                "I can now make an Essentia Import Bus.",
                                "Ya puedo fabricar un Bus de Importación de Esencia.")
                                .recipes(mod("parts/essentia_import_bus")))
                        .note("machina", 2)
                        .note("praecantatio", 1)
                        .note("motus", 1)
                        .complexity(1),
                TEResearchEntry.of("essentia_export_bus", -6, -2, "Essentia Export Bus", "Bus de Exportación de Esencia")
                        .parents(ESSENTIA_IMPORT_BUS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_export_bus"))
                        .stage(TEResearchStage.of(
                                "The reverse should also be possible: a bus that pushes essentia from the network into whatever needs it.",
                                "Lo contrario también debería ser posible: un bus que empuje esencia de la red hacia lo que la necesite.")
                                .theory(1))
                        .stage(TEResearchStage.of(
                                "I can now make an Essentia Export Bus.",
                                "Ya puedo fabricar un Bus de Exportación de Esencia.")
                                .recipes(mod("parts/essentia_export_bus")))
                        .note("machina", 2)
                        .note("praecantatio", 1)
                        .note("motus", 1)
                        .complexity(1),
                TEResearchEntry.of("essentia_storage_bus", -8, -2, "Essentia Storage Bus", "Bus de Almacenamiento de Esencia")
                        .parents(ESSENTIA_EXPORT_BUS)
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("essentia_storage_bus"))
                        .stage(TEResearchStage.of(
                                "Instead of moving essentia around, I could present a whole jar to the network as if it were part of it.",
                                "En lugar de mover esencia de un lado a otro, podría presentar una jarra entera a la red como si formara parte de ella.")
                                .theory(1))
                        .stage(TEResearchStage.of(
                                "I can now make an Essentia Storage Bus.",
                                "Ya puedo fabricar un Bus de Almacenamiento de Esencia.")
                                .recipes(mod("parts/essentia_storage_bus")))
                        .note("machina", 2)
                        .note("vacuos", 1)
                        .note("permutatio", 1)
                        .complexity(1),
                TEResearchEntry.of("infusion_provider", 2, -2, "Infusion Provider", "Proveedor de Infusión")
                        .parents(DIGISENTIA, thaumaturge("infusion"))
                        .meta(ResearchEntryMeta.ROUND)
                        .icons(mod("infusion_provider"))
                        .stage(TEResearchStage.of(
                                "Now that I can digitize essentia, it's time to put that technology to work. Perhaps an infusion matrix could draw the essentia it needs straight from my network.",
                                "Ahora que puedo digitalizar esencia, es hora de poner esa tecnología a trabajar. Quizás una matriz de infusión pueda tomar la esencia que necesita directamente de mi red.")
                                .theory(3))
                        .stage(TEResearchStage.of(
                                "I can now make an Infusion Provider. Placed within reach of an infusion matrix and connected to my network, it hands over the essentia an infusion asks for.",
                                "Ya puedo fabricar un Proveedor de Infusión. Colocado al alcance de una matriz de infusión y conectado a mi red, entrega la esencia que pide la infusión.")
                                .recipes(TEThaumaturgeRecipes.INFUSION_PROVIDER)
                                .warp(2))
                        .note("machina", 3)
                        .note("praecantatio", 3)
                        .note("permutatio", 2)
                        .complexity(2));
    }
}
