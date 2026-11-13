package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUParts;

public class TUUEssentiaStorageBusItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation PART_BASE = ResourceLocation.fromNamespaceAndPath("ae2", "item/part_base");
    private static final Direction[] ORDER = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN};

    public TUUEssentiaStorageBusItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ItemModelBuilder model = getBuilder(TUUParts.ESSENTIA_STORAGE_BUS.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(PART_BASE))
                .texture("sides", ResourceLocation.fromNamespaceAndPath("ae2", "part/storage_bus_sides"))
                .texture("back", ResourceLocation.fromNamespaceAndPath("ae2", "part/storage_bus_back"))
                .texture("front", modLoc("part/essentia_storage_bus_face"));

        layer(model, 3, 6, 13, 7, new int[][] {{3, 3, 13, 13}, {15, 3, 16, 13}, {3, 3, 13, 13}, {0, 3, 1, 13}, {3, 0, 13, 1}, {3, 15, 13, 16}});
        layer(model, 2, 7, 14, 8, new int[][] {{2, 2, 14, 14}, {14, 2, 15, 14}, {2, 2, 14, 14}, {1, 2, 2, 14}, {2, 1, 14, 2}, {2, 14, 14, 15}});
        layer(model, 5, 8, 11, 10, new int[][] {{5, 5, 11, 11}, {6, 5, 8, 11}, {5, 5, 11, 11}, {8, 5, 10, 11}, {5, 8, 11, 10}, {5, 6, 11, 8}});
    }

    private void layer(ItemModelBuilder model, int min, int fromZ, int max, int toZ, int[][] uvs) {
        ItemModelBuilder.ElementBuilder element = model.element().from(min, min, fromZ).to(max, max, toZ);
        for (int i = 0; i < ORDER.length; i++) {
            String texture = switch (ORDER[i]) {
                case NORTH -> "#front";
                case SOUTH -> "#back";
                default -> "#sides";
            };
            int[] uv = uvs[i];
            element.face(ORDER[i]).uvs(uv[0], uv[1], uv[2], uv[3]).texture(texture).end();
        }
        element.end();
    }

    @Override
    public String getName() {
        return "Essentia Storage Bus Item Model: " + modid;
    }
}
