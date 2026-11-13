package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.List;

import appeng.client.item.StorageCellStateTintSource;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public final class TEExtendedAeModels {

    private static final TextureSlot CELL = TextureSlot.create("cell");
    private static final Identifier CELL_LED = Identifier.fromNamespaceAndPath("ae2", "item/storage_cell_led");
    private static final Identifier DRIVE_ATLAS = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "block/drive/infinity_cells");

    private final BlockModelGenerators blockModels;
    private final ItemModelGenerators itemModels;

    public TEExtendedAeModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blockModels = blockModels;
        this.itemModels = itemModels;
    }

    public void register() {
        if (!ModList.get().isLoaded(ExtendedAeCompat.MOD_ID)) {
            return;
        }
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            matrix(ExtendedAeCompat.crafter(metal));
            matrix(ExtendedAeCompat.pattern(metal));
            matrix(ExtendedAeCompat.speed(metal));
        }
        List<String> names = new ArrayList<>(ExtendedAeCompat.infiniteCells().keySet());
        for (int row = 0; row < names.size(); row++) {
            String name = names.get(row);
            infiniteCell(ExtendedAeCompat.infiniteCells().get(name).get(), name);
            driveModel(name, row);
        }
    }

    private void matrix(DeferredBlock<? extends Block> block) {
        Material texture = material("block/matrix/" + block.getId().getPath());
        blockModels.createTrivialBlock(block.get(), TexturedModel.createDefault(ignored -> TextureMapping.cube(texture), ModelTemplates.CUBE_ALL));
    }

    private void infiniteCell(Item item, String name) {
        Identifier model = itemModels.generateLayeredItem(
                item,
                material("item/cell/" + ExtendedAeCompat.infiniteCellId(name)),
                new Material(CELL_LED));
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(
                model,
                new Constant(-1),
                new StorageCellStateTintSource()));
    }

    private void driveModel(String name, int row) {
        float top = row * 2;
        float bottom = top + 2;
        ExtendedModelTemplate template = ExtendedModelTemplateBuilder.builder()
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .requiredTextureSlot(CELL)
                .ambientOcclusion(false)
                .element(element -> element
                        .from(0, 0, 0)
                        .to(6, 2, 2)
                        .face(Direction.NORTH, face -> face.uvs(0, top, 6, bottom).texture(CELL).cullface(Direction.NORTH))
                        .face(Direction.UP, face -> face.uvs(6, top, 0, bottom).texture(CELL).cullface(Direction.NORTH))
                        .face(Direction.DOWN, face -> face.uvs(6, top, 0, bottom).texture(CELL).cullface(Direction.NORTH)))
                .build();
        Material atlas = new Material(DRIVE_ATLAS);
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, atlas)
                .put(CELL, atlas);
        template.create(ExtendedAeCompat.infiniteDriveModel(name), mapping, blockModels.modelOutput);
    }

    private static Material material(String path) {
        return new Material(Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path));
    }
}
