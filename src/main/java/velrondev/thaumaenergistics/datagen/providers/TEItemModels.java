package velrondev.thaumaenergistics.datagen.providers;

import appeng.client.item.StorageCellStateTintSource;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.registry.TEItems;

public final class TEItemModels {

    private static final Identifier CELL_LED = Identifier.fromNamespaceAndPath("ae2", "item/storage_cell_led");

    private final ItemModelGenerators itemModels;

    public TEItemModels(ItemModelGenerators itemModels) {
        this.itemModels = itemModels;
    }

    public void register() {
        cell(TEItems.ESSENTIA_CELL_1K.get(), "1k");
        cell(TEItems.ESSENTIA_CELL_4K.get(), "4k");
        cell(TEItems.ESSENTIA_CELL_16K.get(), "16k");
        cell(TEItems.ESSENTIA_CELL_64K.get(), "64k");
        cell(TEItems.ESSENTIA_CELL_256K.get(), "256k");
        cell(TEItems.ESSENTIA_CELL_INFINITY.get(), "infinity");
        flat(TEItems.ESSENTIA_CELL_CREATIVE.get(), "cell/creative");

        flat(TEItems.ESSENTIA_COMPONENT_1K.get(), "material/essentia_component_1k");
        flat(TEItems.ESSENTIA_COMPONENT_4K.get(), "material/essentia_component_4k");
        flat(TEItems.ESSENTIA_COMPONENT_16K.get(), "material/essentia_component_16k");
        flat(TEItems.ESSENTIA_COMPONENT_64K.get(), "material/essentia_component_64k");
        flat(TEItems.ESSENTIA_COMPONENT_256K.get(), "material/essentia_component_256k");
        flat(TEItems.DIFFUSION_CORE.get(), "material/diffusion_core");
        flat(TEItems.COALESCENCE_CORE.get(), "material/coalescence_core");

        flat(TEItems.WIRELESS_ARCANE_TERMINAL.get(), "wireless_arcane_terminal");
        flat(TEItems.WIRELESS_ESSENTIA_TERMINAL.get(), "wireless_essentia_terminal");
        flat(TEItems.UPGRADE_ARCANE.get(), "material/upgrade_arcane");
    }

    private void cell(Item item, String size) {
        Identifier model = itemModels.generateLayeredItem(
                item,
                material("item/cell/" + size),
                new Material(CELL_LED));
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(
                model,
                new Constant(-1),
                new StorageCellStateTintSource()));
    }

    private void flat(Item item, String texture) {
        Identifier model = ModelTemplates.FLAT_ITEM.create(
                item,
                TextureMapping.layer0(material("item/" + texture)),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }

    private static Material material(String path) {
        return new Material(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path));
    }
}
