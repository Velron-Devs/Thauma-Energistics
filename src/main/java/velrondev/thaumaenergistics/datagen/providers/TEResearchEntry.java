package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.IResearchStage;
import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
import com.leclowndu93150.thaumaturge.api.research.ResearchIcon;
import com.leclowndu93150.thaumaturge.api.research.ResearchParent;
import com.leclowndu93150.thaumaturge.content.research.ResearchEntry;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import velrondev.thaumaenergistics.ThaumaEnergistics;

public final class TEResearchEntry {

    private final String id;
    private final int column;
    private final int row;
    private final String titleEnglish;
    private final String titleSpanish;
    private final List<Identifier> parents = new ArrayList<>();
    private final List<ResearchEntryMeta> meta = new ArrayList<>();
    private final List<Identifier> icons = new ArrayList<>();
    private final List<TEResearchStage> stages = new ArrayList<>();
    private final Map<String, Integer> notes = new LinkedHashMap<>();
    private final List<String> requiredMods = new ArrayList<>();
    private int complexity = 1;

    private TEResearchEntry(String id, int column, int row, String titleEnglish, String titleSpanish) {
        this.id = id;
        this.column = column;
        this.row = row;
        this.titleEnglish = titleEnglish;
        this.titleSpanish = titleSpanish;
    }

    public static TEResearchEntry of(String id, int column, int row, String titleEnglish, String titleSpanish) {
        return new TEResearchEntry(id, column, row, titleEnglish, titleSpanish);
    }

    public TEResearchEntry parents(Identifier... ids) {
        parents.addAll(List.of(ids));
        return this;
    }

    public TEResearchEntry meta(ResearchEntryMeta... values) {
        meta.addAll(List.of(values));
        return this;
    }

    public TEResearchEntry icons(Identifier... ids) {
        icons.addAll(List.of(ids));
        return this;
    }

    public TEResearchEntry stage(TEResearchStage stage) {
        stages.add(stage);
        return this;
    }

    public TEResearchEntry note(String aspect, int amount) {
        notes.put(aspect, amount);
        return this;
    }

    public TEResearchEntry condition(String modId) {
        requiredMods.add(modId);
        return this;
    }

    public List<String> requiredMods() {
        return requiredMods;
    }

    public TEResearchEntry complexity(int value) {
        complexity = value;
        return this;
    }

    public String id() {
        return id;
    }

    public Identifier location() {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, id);
    }

    public String titleKey() {
        return "research." + ThaumaEnergistics.MOD_ID + "." + id + ".title";
    }

    public String stageKey(int index) {
        return "research." + ThaumaEnergistics.MOD_ID + "." + id + ".stage_" + index;
    }

    public String titleEnglish() {
        return titleEnglish;
    }

    public String titleSpanish() {
        return titleSpanish;
    }

    public List<TEResearchStage> stages() {
        return stages;
    }

    public Set<String> noteAspects() {
        return notes.keySet();
    }

    public ResearchEntry build(Holder<IResearchCategory> category, TEResearch.Lookups lookups) {
        Set<ResearchParent> parentSet = new LinkedHashSet<>();
        for (Identifier parent : parents) {
            parentSet.add(ResearchParent.of(parent));
        }
        List<ResearchIcon> iconList = new ArrayList<>();
        for (Identifier icon : icons) {
            iconList.add(ResearchIcon.ofItem(icon));
        }
        List<IResearchStage> stageList = new ArrayList<>();
        for (int i = 0; i < stages.size(); i++) {
            stageList.add(stages.get(i).build(stageKey(i), category, lookups));
        }
        List<AspectInstance> noteList = new ArrayList<>();
        notes.forEach((aspect, amount) -> noteList.add(new AspectInstance(lookups.aspects().getOrThrow(TEResearch.aspect(aspect)), amount)));
        return new ResearchEntry(
                category,
                titleKey(),
                parentSet,
                Set.of(),
                column,
                row,
                stageList,
                new LinkedHashSet<>(meta),
                iconList,
                List.of(),
                AspectList.ofEntries(noteList),
                complexity);
    }
}
