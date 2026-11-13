package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;

import appeng.api.util.AEColor;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUParts;

public class TUUPartItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation PART_BASE = ResourceLocation.fromNamespaceAndPath("ae2", "item/part_base");
    private static final int FIXED = -1;

    private record Layer(String texture, int tint) {
    }

    public TUUPartItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        display(TUUParts.ARCANE_TERMINAL.getId().getPath(), List.of(
                new Layer("arcane_terminal_panel", AEColor.TINTINDEX_MEDIUM),
                new Layer("arcane_terminal_frame", FIXED),
                new Layer("arcane_terminal_dots", AEColor.TINTINDEX_BRIGHT),
                new Layer("arcane_terminal_dark", AEColor.TINTINDEX_DARK)));
        display(TUUParts.ESSENTIA_TERMINAL.getId().getPath(), List.of(
                new Layer("essentia_terminal_plate", AEColor.TINTINDEX_BRIGHT),
                new Layer("essentia_terminal_flask", AEColor.TINTINDEX_MEDIUM),
                new Layer("essentia_terminal_frame", FIXED),
                new Layer("essentia_terminal_medium", AEColor.TINTINDEX_MEDIUM),
                new Layer("essentia_terminal_dark", AEColor.TINTINDEX_DARK)));
    }

    private void display(String name, List<Layer> layers) {
        ItemModelBuilder model = getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(PART_BASE))
                .texture("sides", ae2("part/monitor_sides"))
                .texture("back", ae2("part/monitor_back"))
                .texture("front_base", ae2("part/monitor_front"));

        model.element().from(2, 2, 7).to(14, 14, 9)
                .face(Direction.NORTH).uvs(2, 2, 14, 14).texture("#front_base").end()
                .face(Direction.EAST).uvs(14, 2, 16, 14).texture("#sides").end()
                .face(Direction.SOUTH).uvs(2, 2, 14, 14).texture("#back").end()
                .face(Direction.WEST).uvs(0, 2, 2, 14).texture("#sides").end()
                .face(Direction.UP).uvs(2, 0, 14, 2).texture("#sides").end()
                .face(Direction.DOWN).uvs(2, 14, 14, 16).texture("#sides").end()
                .end();

        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            String key = "layer" + i;
            model.texture(key, modLoc("part/" + layer.texture()));
            ItemModelBuilder.ElementBuilder.FaceBuilder face = model.element().from(2, 2, 7).to(14, 14, 8)
                    .face(Direction.NORTH).uvs(2, 2, 14, 14).texture("#" + key);
            if (layer.tint() != FIXED) {
                face.tintindex(layer.tint());
            }
            face.end().end();
        }

        model.element().from(4, 4, 9).to(12, 12, 10)
                .face(Direction.EAST).uvs(6, 4, 7, 12).texture("#sides").end()
                .face(Direction.SOUTH).uvs(4, 4, 12, 12).texture("#back").end()
                .face(Direction.WEST).uvs(9, 4, 10, 12).texture("#sides").end()
                .face(Direction.UP).uvs(4, 9, 12, 10).texture("#sides").end()
                .face(Direction.DOWN).uvs(4, 6, 12, 7).texture("#sides").end()
                .end();
    }

    private static ResourceLocation ae2(String path) {
        return ResourceLocation.fromNamespaceAndPath("ae2", path);
    }

    @Override
    public String getName() {
        return "Part Item Models: " + modid;
    }
}
