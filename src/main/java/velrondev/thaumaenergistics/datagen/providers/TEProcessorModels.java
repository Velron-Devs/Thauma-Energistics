package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TEProcessors;

public final class TEProcessorModels {

    private final ItemModelGenerators itemModels;

    public TEProcessorModels(ItemModelGenerators itemModels) {
        this.itemModels = itemModels;
    }

    public void register() {
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                flat(TEProcessors.printed(metal, kind).get(), TEProcessors.printedId(metal, kind));
                flat(TEProcessors.processor(metal, kind).get(), TEProcessors.processorId(metal, kind));
            }
        }
    }

    private void flat(Item item, String texture) {
        Material material = new Material(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "item/processor/" + texture));
        Identifier model = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(material), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }
}
