package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;

public class TUUEssentiaStorageBusModelProvider extends ModelProvider<BlockModelBuilder> {

    private static final String NAME = "essentia_storage_bus";
    private static final int FULL_LIGHT = 15;
    private static final int TINT_ON = 3;
    private static final int TINT_HAS_CHANNEL = 1;
    private static final Direction[] RING = {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST};
    private static final Direction[] SIDES = {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST};

    public TUUEssentiaStorageBusModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, "part", BlockModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        base();
        indicator(NAME + "_off", "part/monitor_sides_status_off", -1);
        indicator(NAME + "_on", "part/monitor_sides_status_on", TINT_ON);
        indicator(NAME + "_has_channel", "part/monitor_sides_status_has_channel", TINT_HAS_CHANNEL);
    }

    private void base() {
        BlockModelBuilder model = getBuilder(NAME + "_base")
                .texture("sides", ae2("part/storage_bus_sides"))
                .texture("sidesStatus", ae2("part/monitor_sides_status"))
                .texture("back", ae2("part/storage_bus_back"))
                .texture("front", modLoc("part/essentia_storage_bus_face"))
                .texture("particle", ae2("part/storage_bus_back"));

        box(model, 3, 3, 0, 13, 13, 1, true, true);
        box(model, 2, 2, 1, 14, 14, 2, true, true);
        box(model, 5, 5, 3, 11, 11, 4, true, false);
        box(model, 5, 5, 2, 11, 11, 3, false, false);
    }

    private void box(BlockModelBuilder model, int fromX, int fromY, int fromZ, int toX, int toY, int toZ, boolean back, boolean front) {
        BlockModelBuilder.ElementBuilder element = model.element().from(fromX, fromY, fromZ).to(toX, toY, toZ);
        for (Direction side : SIDES) {
            element.face(side).texture("#sides").end();
        }
        if (back) {
            element.face(Direction.SOUTH).texture("#back").end();
        }
        if (front) {
            element.face(Direction.NORTH).texture("#front").end();
        }
        element.end();
    }

    private void indicator(String name, String texture, int tint) {
        BlockModelBuilder model = getBuilder(name).texture("indicator", ae2(texture));
        BlockModelBuilder.ElementBuilder element = model.element().from(5, 5, 2).to(11, 11, 3);
        for (Direction side : RING) {
            BlockModelBuilder.ElementBuilder.FaceBuilder face = element.face(side).texture("#indicator");
            if (tint >= 0) {
                face.tintindex(tint).emissivity(FULL_LIGHT, FULL_LIGHT);
            }
            face.end();
        }
        element.end();
    }

    private static ResourceLocation ae2(String path) {
        return ResourceLocation.fromNamespaceAndPath("ae2", path);
    }

    @Override
    public String getName() {
        return "Essentia Storage Bus Part Models: " + modid;
    }
}
