package velrondev.thaumaenergistics.datagen.providers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class TUUCompatBlockStateProvider extends BlockStateProvider {

    private static final String DRIVE = "block/drive/";

    private final TUUTextures textures;

    public TUUCompatBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
        this.textures = new TUUTextures(existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        if (TUUCompat.thingsActive()) {
            essentiaDiskDriveModels();
        }
        if (TUUCompat.megaThingsActive()) {
            megaEssentiaDiskDriveModels();
        }
        if (TUUCompat.megaCellsActive()) {
            megaEssentiaCellDriveModels();
        }
        if (!TUUCompat.extendedAeActive()) {
            return;
        }
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            matrix(ExtendedAeCompat.crafter(metal), "crafter_core");
            matrix(ExtendedAeCompat.pattern(metal), "pattern_core");
            matrix(ExtendedAeCompat.speed(metal), "speed_core");
        }
        infiniteCellDriveModels();
    }

    private void essentiaDiskDriveModels() {
        ResourceLocation atlas = TUUTextures.of(TUU.MOD_ID, "block/drive/essentia_disk_cells");
        for (int i = 0; i < TUUCompatConfig.SIZE_NAMES.length; i++) {
            ResourceLocation parent = TUUTextures.of("ae2", "block/drive/cells/" + TUUCompatConfig.SIZE_NAMES[i] + "_item_cell");
            models().withExistingParent(DRIVE + Ae2ThingsCompat.diskId(i), parent)
                    .texture("particle", atlas)
                    .texture("cell", atlas);
        }
    }

    private void megaEssentiaDiskDriveModels() {
        ResourceLocation atlas = TUUTextures.of(TUU.MOD_ID, "block/drive/essentia_mega_disk_cells");
        for (int i = 0; i < TUUCompatConfig.MEGA_SIZE_NAMES.length; i++) {
            ResourceLocation parent = TUUTextures.of(Ae2MegaThingsCompat.MOD_ID, "block/drive/cells/" + TUUCompatConfig.MEGA_SIZE_NAMES[i] + "_item_cell");
            models().withExistingParent(DRIVE + Ae2MegaThingsCompat.diskId(i), parent)
                    .texture("particle", atlas)
                    .texture("cell", atlas);
        }
    }

    private void megaEssentiaCellDriveModels() {
        ResourceLocation atlas = TUUTextures.of(TUU.MOD_ID, "block/drive/essentia_mega_cell");
        for (int i = 0; i < TUUCompatConfig.MEGA_SIZE_NAMES.length; i++) {
            ResourceLocation parent = TUUTextures.of(MegaCellsCompat.MOD_ID, "block/drive/cells/" + TUUCompatConfig.MEGA_SIZE_NAMES[i] + "_item_cell");
            models().withExistingParent(DRIVE + MegaCellsCompat.cellId(i), parent)
                    .texture("particle", atlas)
                    .texture("cell", atlas);
        }
    }

    private void infiniteCellDriveModels() {
        ResourceLocation atlas = TUUTextures.of(TUU.MOD_ID, "block/drive/infinity_cells");
        List<String> names = new ArrayList<>(ExtendedAeCompat.infiniteCells().keySet());
        for (int row = 0; row < names.size(); row++) {
            float top = row * 2;
            float bottom = top + 2;
            BlockModelBuilder model = models().getBuilder(DRIVE + ExtendedAeCompat.infiniteCellId(names.get(row)))
                    .texture("particle", atlas)
                    .texture("cell", atlas);
            model.ao(false);
            model.element()
                    .from(0, 0, 0)
                    .to(6, 2, 2)
                    .face(Direction.NORTH).uvs(0, top, 6, bottom).texture("#cell").cullface(Direction.NORTH).end()
                    .face(Direction.UP).uvs(6, top, 0, bottom).texture("#cell").cullface(Direction.NORTH).end()
                    .face(Direction.DOWN).uvs(6, top, 0, bottom).texture("#cell").cullface(Direction.NORTH).end()
                    .end();
        }
    }

    private void matrix(DeferredBlock<? extends Block> block, String placeholderCore) {
        String name = block.getId().getPath();
        ResourceLocation placeholder = TUUTextures.of(ExtendedAeCompat.MOD_ID, "block/assembler_matrix/" + placeholderCore);
        simpleBlockWithItem(block.get(), models().cubeAll(name, textures.block("matrix/" + name, placeholder)));
    }

    @Override
    public String getName() {
        return "Compat Block States: " + TUU.MOD_ID;
    }
}
