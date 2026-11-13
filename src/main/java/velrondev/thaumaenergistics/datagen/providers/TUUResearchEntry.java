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
import net.minecraft.resources.ResourceLocation;
import velrondev.thaumaenergistics.TUU;

public final class TUUResearchEntry {

    private final String id;
    private final int column;
    private final int row;
    private final String titleEnglish;
    private final String titleSpanish;
    private final List<ResourceLocation> parents = new ArrayList<>();
    private final List<ResearchEntryMeta> meta = new ArrayList<>();
    private final List<ResourceLocation> icons = new ArrayList<>();
    private final List<TUUResearchStage> stages = new ArrayList<>();
    private final Map<String, Integer> notes = new LinkedHashMap<>();
    private int complexity = 1;

    private TUUResearchEntry(String id, int column, int row, String titleEnglish, String titleSpanish) {
        this.id = id;
        this.column = column;
        this.row = row;
        this.titleEnglish = titleEnglish;
        this.titleSpanish = titleSpanish;
    }

    public static TUUResearchEntry of(String id, int column, int row, String titleEnglish, String titleSpanish) {
        return new TUUResearchEntry(id, column, row, titleEnglish, titleSpanish);
    }

    public TUUResearchEntry parents(ResourceLocation... ids) {
        parents.addAll(List.of(ids));
        return this;
    }

    public TUUResearchEntry meta(ResearchEntryMeta... values) {
        meta.addAll(List.of(values));
        return this;
    }

    public TUUResearchEntry icons(ResourceLocation... ids) {
        icons.addAll(List.of(ids));
        return this;
    }

    public TUUResearchEntry stage(TUUResearchStage stage) {
        stages.add(stage);
        return this;
    }

    public TUUResearchEntry note(String aspect, int amount) {
        notes.put(aspect, amount);
        return this;
    }

    public TUUResearchEntry complexity(int value) {
        complexity = value;
        return this;
    }

    public String id() {
        return id;
    }

    public ResourceLocation location() {
        return ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, id);
    }

    public String titleKey() {
        return "research." + TUU.MOD_ID + "." + id + ".title";
    }

    public String stageKey(int index) {
        return "research." + TUU.MOD_ID + "." + id + ".stage_" + index;
    }

    public String titleEnglish() {
        return titleEnglish;
    }

    public String titleSpanish() {
        return titleSpanish;
    }

    public List<TUUResearchStage> stages() {
        return stages;
    }

    public Set<String> noteAspects() {
        return notes.keySet();
    }

    public ResearchEntry build(Holder<IResearchCategory> category, TUUResearch.Lookups lookups) {
        Set<ResearchParent> parentSet = new LinkedHashSet<>();
        for (ResourceLocation parent : parents) {
            parentSet.add(ResearchParent.of(parent));
        }
        List<ResearchIcon> iconList = new ArrayList<>();
        for (ResourceLocation icon : icons) {
            iconList.add(ResearchIcon.ofItem(icon));
        }
        List<IResearchStage> stageList = new ArrayList<>();
        for (int i = 0; i < stages.size(); i++) {
            stageList.add(stages.get(i).build(stageKey(i), category, lookups));
        }
        List<AspectInstance> noteList = new ArrayList<>();
        notes.forEach((aspect, amount) -> noteList.add(new AspectInstance(lookups.aspects().getOrThrow(TUUResearch.aspect(aspect)), amount)));
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
