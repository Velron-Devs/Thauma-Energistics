package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.part.EssentiaExportBusPart;
import velrondev.thaumaenergistics.part.EssentiaImportBusPart;
import velrondev.thaumaenergistics.registry.TUUParts;

public class TUUEssentiaBusModelProvider extends ItemModelProvider {

    public TUUEssentiaBusModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        bus(EssentiaImportBusPart.ESSENTIA_BASE, TUUParts.ESSENTIA_IMPORT_BUS.getId().getPath(), "import_bus");
        bus(EssentiaExportBusPart.ESSENTIA_BASE, TUUParts.ESSENTIA_EXPORT_BUS.getId().getPath(), "export_bus");
    }

    private void bus(ResourceLocation base, String name, String aeName) {
        ResourceLocation face = modLoc("part/" + name + "_face");

        getBuilder(base.getPath())
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("ae2", "part/" + aeName + "_base")))
                .texture("front", face);

        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("ae2", "item/" + aeName)))
                .texture("front", face);
    }

    @Override
    public String getName() {
        return "Essentia Bus Models: " + modid;
    }
}
