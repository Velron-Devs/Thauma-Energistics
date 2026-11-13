package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.KnowledgeReward;
import com.leclowndu93150.thaumaturge.api.research.ResearchRequirement;
import com.leclowndu93150.thaumaturge.content.research.ResearchStage;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class TUUResearchStage {

    private record Knowledge(KnowledgeType type, int amount) {
    }

    private record Requirement(ResourceLocation item, int amount) {
    }

    private final String english;
    private final String spanish;
    private final List<ResourceLocation> recipes = new ArrayList<>();
    private final List<Requirement> obtain = new ArrayList<>();
    private final List<Knowledge> knowledge = new ArrayList<>();
    private final List<ResourceLocation> research = new ArrayList<>();
    private int warp;

    private TUUResearchStage(String english, String spanish) {
        this.english = english;
        this.spanish = spanish;
    }

    public static TUUResearchStage of(String english, String spanish) {
        return new TUUResearchStage(english, spanish);
    }

    public TUUResearchStage recipes(ResourceLocation... ids) {
        recipes.addAll(List.of(ids));
        return this;
    }

    public TUUResearchStage obtain(ResourceLocation item, int amount) {
        obtain.add(new Requirement(item, amount));
        return this;
    }

    public TUUResearchStage theory(int amount) {
        knowledge.add(new Knowledge(KnowledgeType.THEORY, amount));
        return this;
    }

    public TUUResearchStage observation(int amount) {
        knowledge.add(new Knowledge(KnowledgeType.OBSERVATION, amount));
        return this;
    }

    public TUUResearchStage research(ResourceLocation id) {
        research.add(id);
        return this;
    }

    public TUUResearchStage warp(int amount) {
        warp = amount;
        return this;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public ResearchStage build(String textKey, Holder<IResearchCategory> category, TUUResearch.Lookups lookups) {
        List<ResearchRequirement> requirements = new ArrayList<>();
        for (Requirement requirement : obtain) {
            Holder<Item> item = lookups.items().getOrThrow(ResourceKey.create(Registries.ITEM, requirement.item()));
            requirements.add(new ResearchRequirement(HolderSet.direct(item), DataComponentPatch.EMPTY, requirement.amount()));
        }
        List<KnowledgeReward> requiredKnowledge = new ArrayList<>();
        for (Knowledge entry : knowledge) {
            requiredKnowledge.add(new KnowledgeReward(entry.type(), category, entry.amount()));
        }
        return new ResearchStage(
                textKey,
                List.copyOf(recipes),
                Optional.empty(),
                requirements,
                List.of(),
                List.of(),
                requiredKnowledge,
                List.copyOf(research),
                warp);
    }
}
