package velrondev.thaumaenergistics.compat.megacells.datagen;

import java.util.Optional;

import appeng.client.item.StorageCellStateTintSource;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.config.TECompatConfig;

public final class MegaCellsModels {

    private static final TextureSlot CELL = TextureSlot.create("cell");
    private static final Identifier CELL_LED = Identifier.fromNamespaceAndPath("ae2", "item/storage_cell_led");
    private static final Identifier ATLAS = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "block/drive/essentia_mega_cell");
    private static final Identifier HOUSING_TEXTURE = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "item/cell/" + MegaCellsCompat.HOUSING_ID);

    private MegaCellsModels() {
    }

    public static void register(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        Item housing = MegaCellsCompat.housing().get();
        Identifier housingModel = ModelTemplates.FLAT_ITEM.create(housing, TextureMapping.layer0(new Material(HOUSING_TEXTURE)), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(housing, ItemModelUtils.plainModel(housingModel));
        for (int i = 0; i < MegaCellsCompat.cells().size(); i++) {
            Identifier parent = Identifier.fromNamespaceAndPath(MegaCellsCompat.MOD_ID, "block/drive/cells/" + TECompatConfig.MEGA_SIZE_NAMES[i] + "_item_cell");
            driveModel(blockModels, MegaCellsCompat.driveModel(i), parent);
            Item cell = MegaCellsCompat.cells().get(i).get();
            Identifier model = ModelLocationUtils.getModelLocation(cell);
            itemModels.generateLayeredItem(
                    model,
                    new Material(HOUSING_TEXTURE),
                    new Material(CELL_LED),
                    new Material(Identifier.fromNamespaceAndPath(MegaCellsCompat.MOD_ID, "item/storage_cell_side_" + TECompatConfig.MEGA_SIZE_NAMES[i])));
            itemModels.itemModelOutput.accept(cell, ItemModelUtils.tintedModel(model, new Constant(-1), new StorageCellStateTintSource()));
        }
    }

    private static void driveModel(BlockModelGenerators blockModels, Identifier target, Identifier parent) {
        ModelTemplate template = new ModelTemplate(Optional.of(parent), Optional.empty(), TextureSlot.PARTICLE, CELL);
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, new Material(ATLAS))
                .put(CELL, new Material(ATLAS));
        template.create(target, mapping, blockModels.modelOutput);
    }
}
