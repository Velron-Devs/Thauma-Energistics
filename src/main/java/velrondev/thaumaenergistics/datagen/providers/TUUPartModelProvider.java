package velrondev.thaumaenergistics.datagen.providers;

import appeng.api.util.AEColor;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;

public class TUUPartModelProvider extends ModelProvider<BlockModelBuilder> {

    private static final int FULL_LIGHT = 15;
    private static final int FIXED = -1;

    public TUUPartModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, "part", BlockModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        arcane();
        essentia();
    }

    private void arcane() {
        ResourceLocation panel = modLoc("part/arcane_terminal_panel");
        ResourceLocation frame = modLoc("part/arcane_terminal_frame");
        ResourceLocation dots = modLoc("part/arcane_terminal_dots");
        ResourceLocation dark = modLoc("part/arcane_terminal_dark");

        BlockModelBuilder off = getBuilder("arcane_terminal_off")
                .texture("panel", panel)
                .texture("frame", frame)
                .texture("dots", dots)
                .texture("dark", dark);
        offLayer(off, "#panel", AEColor.TINTINDEX_MEDIUM);
        offLayer(off, "#frame", FIXED);
        offLayer(off, "#dots", AEColor.TINTINDEX_BRIGHT);
        offLayer(off, "#dark", AEColor.TINTINDEX_DARK);

        BlockModelBuilder on = getBuilder("arcane_terminal_on")
                .texture("panel", panel)
                .texture("frame", frame)
                .texture("dots", dots)
                .texture("dark", dark);
        layer(on, "#panel", AEColor.TINTINDEX_MEDIUM);
        layer(on, "#frame", FIXED);
        layer(on, "#dots", AEColor.TINTINDEX_BRIGHT);
        layer(on, "#dark", AEColor.TINTINDEX_DARK);
    }

    private void essentia() {
        ResourceLocation plate = modLoc("part/essentia_terminal_plate");
        ResourceLocation flask = modLoc("part/essentia_terminal_flask");
        ResourceLocation frame = modLoc("part/essentia_terminal_frame");
        ResourceLocation medium = modLoc("part/essentia_terminal_medium");
        ResourceLocation dark = modLoc("part/essentia_terminal_dark");

        BlockModelBuilder off = getBuilder("essentia_terminal_off")
                .texture("plate", plate)
                .texture("flask", flask)
                .texture("frame", frame)
                .texture("medium", medium)
                .texture("dark", dark);
        offLayer(off, "#flask", AEColor.TINTINDEX_MEDIUM);
        offLayer(off, "#plate", AEColor.TINTINDEX_BRIGHT);
        offLayer(off, "#frame", FIXED);
        offLayer(off, "#medium", AEColor.TINTINDEX_MEDIUM);
        offLayer(off, "#dark", AEColor.TINTINDEX_DARK);

        BlockModelBuilder on = getBuilder("essentia_terminal_on")
                .texture("plate", plate)
                .texture("flask", flask)
                .texture("frame", frame)
                .texture("medium", medium)
                .texture("dark", dark);
        layer(on, "#plate", AEColor.TINTINDEX_BRIGHT);
        layer(on, "#flask", AEColor.TINTINDEX_MEDIUM);
        layer(on, "#frame", FIXED);
        layer(on, "#medium", AEColor.TINTINDEX_MEDIUM);
        layer(on, "#dark", AEColor.TINTINDEX_DARK);
    }

    private void offLayer(BlockModelBuilder model, String texture, int tintIndex) {
        BlockModelBuilder.ElementBuilder.FaceBuilder face = model.element()
                .from(2, 2, 0)
                .to(14, 14, 2)
                .face(Direction.NORTH).texture(texture);
        if (tintIndex != FIXED) {
            face.tintindex(tintIndex);
        }
        face.end().end();
    }

    private void layer(BlockModelBuilder model, String texture, int tintIndex) {
        BlockModelBuilder.ElementBuilder.FaceBuilder face = model.element()
                .from(2, 2, 0)
                .to(14, 14, 2)
                .face(Direction.NORTH).texture(texture);
        if (tintIndex != FIXED) {
            face.tintindex(tintIndex);
        }
        face.emissivity(FULL_LIGHT, FULL_LIGHT).end().end();
    }

    @Override
    public String getName() {
        return "Part Models: " + modid;
    }
}
