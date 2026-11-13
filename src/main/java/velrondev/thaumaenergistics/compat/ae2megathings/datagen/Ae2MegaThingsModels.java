package velrondev.thaumaenergistics.compat.ae2megathings.datagen;

import java.util.Optional;

import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.EssentiaDiskTintSource;
import velrondev.thaumaenergistics.config.TECompatConfig;

public final class Ae2MegaThingsModels {

    private static final TextureSlot CELL = TextureSlot.create("cell");
    private static final Identifier CELL_LED = Identifier.fromNamespaceAndPath("ae2", "item/storage_cell_led");
    private static final Identifier ATLAS = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "block/drive/essentia_mega_disk_cells");

    private Ae2MegaThingsModels() {
    }

    public static void register(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        Item housing = Ae2MegaThingsCompat.housing().get();
        Identifier housingModel = ModelTemplates.FLAT_ITEM.create(
                housing,
                TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "item/disk/" + Ae2MegaThingsCompat.HOUSING_ID))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(housing, ItemModelUtils.plainModel(housingModel));
        for (int i = 0; i < Ae2MegaThingsCompat.disks().size(); i++) {
            Identifier parent = Identifier.fromNamespaceAndPath(Ae2MegaThingsCompat.MOD_ID, "block/drive/cells/" + TECompatConfig.MEGA_SIZE_NAMES[i] + "_item_cell");
            driveModel(blockModels, Ae2MegaThingsCompat.driveModel(i), parent);
            Item disk = Ae2MegaThingsCompat.disks().get(i).get();
            Identifier model = itemModels.generateLayeredItem(
                    disk,
                    new Material(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "item/disk/" + Ae2MegaThingsCompat.diskId(i))),
                    new Material(CELL_LED));
            itemModels.itemModelOutput.accept(disk, ItemModelUtils.tintedModel(model, new Constant(-1), new EssentiaDiskTintSource()));
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
