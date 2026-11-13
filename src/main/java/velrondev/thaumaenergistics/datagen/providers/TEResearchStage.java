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
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class TEResearchStage {

    private record Knowledge(KnowledgeType type, int amount) {
    }

    private record Requirement(Identifier item, int amount) {
    }

    private final String english;
    private final String spanish;
    private final List<Identifier> recipes = new ArrayList<>();
    private final List<Requirement> obtain = new ArrayList<>();
    private final List<Knowledge> knowledge = new ArrayList<>();
    private final List<Identifier> research = new ArrayList<>();
    private int warp;

    private TEResearchStage(String english, String spanish) {
        this.english = english;
        this.spanish = spanish;
    }

    public static TEResearchStage of(String english, String spanish) {
        return new TEResearchStage(english, spanish);
    }

    public TEResearchStage recipes(Identifier... ids) {
        recipes.addAll(List.of(ids));
        return this;
    }

    public TEResearchStage obtain(Identifier item, int amount) {
        obtain.add(new Requirement(item, amount));
        return this;
    }

    public TEResearchStage theory(int amount) {
        knowledge.add(new Knowledge(KnowledgeType.THEORY, amount));
        return this;
    }

    public TEResearchStage observation(int amount) {
        knowledge.add(new Knowledge(KnowledgeType.OBSERVATION, amount));
        return this;
    }

    public TEResearchStage research(Identifier id) {
        research.add(id);
        return this;
    }

    public TEResearchStage warp(int amount) {
        warp = amount;
        return this;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public ResearchStage build(String textKey, Holder<IResearchCategory> category, TEResearch.Lookups lookups) {
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
