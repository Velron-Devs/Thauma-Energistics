package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.compat.ae2things.Ae2ThingsCompat;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.compat.megacells.MegaCellsCompat;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TUUProcessors;

public class TUUCompatItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation CELL_LED = TUUTextures.of("ae2", "item/storage_cell_led");

    private final TUUTextures textures;

    public TUUCompatItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
        this.textures = new TUUTextures(existingFileHelper);
    }

    @Override
    protected void registerModels() {
        processors();
        if (TUUCompat.thingsActive()) {
            essentiaDisks();
        }
        if (TUUCompat.megaThingsActive()) {
            megaEssentiaDisks();
        }
        if (TUUCompat.megaCellsActive()) {
            megaEssentiaCells();
        }
        if (TUUCompat.extendedAeActive()) {
            infiniteCells();
        }
    }

    private void processors() {
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                DeferredItem<Item> printed = TUUProcessors.printed(metal, kind);
                flat(printed, textures.item("processor/" + printed.getId().getPath(), TUUTextures.of("ae2", "item/printed_" + kind.id() + "_processor")));
                DeferredItem<Item> processor = TUUProcessors.processor(metal, kind);
                flat(processor, textures.item("processor/" + processor.getId().getPath(), TUUTextures.of("ae2", "item/" + kind.id() + "_processor")));
            }
        }
    }

    private void essentiaDisks() {
        for (int i = 0; i < Ae2ThingsCompat.disks().size(); i++) {
            DeferredItem<?> disk = Ae2ThingsCompat.disks().get(i);
            ResourceLocation placeholder = TUUTextures.of(Ae2ThingsCompat.MOD_ID, "item/disk_drive_" + TUUCompatConfig.SIZE_NAMES[i]);
            flat(disk, textures.item("disk/" + disk.getId().getPath(), placeholder)).texture("layer1", CELL_LED);
        }
    }

    private void megaEssentiaDisks() {
        DeferredItem<Item> housing = Ae2MegaThingsCompat.housing();
        flat(housing, textures.item("disk/" + housing.getId().getPath(), TUUTextures.of(Ae2MegaThingsCompat.MOD_ID, "item/mega_item_disk_housing")));
        for (int i = 0; i < Ae2MegaThingsCompat.disks().size(); i++) {
            DeferredItem<?> disk = Ae2MegaThingsCompat.disks().get(i);
            ResourceLocation placeholder = TUUTextures.of(Ae2MegaThingsCompat.MOD_ID, "item/item_disk_drive_" + TUUCompatConfig.MEGA_SIZE_NAMES[i]);
            flat(disk, textures.item("disk/" + disk.getId().getPath(), placeholder)).texture("layer1", CELL_LED);
        }
    }

    private void megaEssentiaCells() {
        DeferredItem<Item> housing = MegaCellsCompat.housing();
        ResourceLocation housingTexture = textures.item("cell/" + housing.getId().getPath(), TUUTextures.of(MegaCellsCompat.MOD_ID, "item/mega_fluid_cell_housing"));
        flat(housing, housingTexture);
        for (int i = 0; i < MegaCellsCompat.cells().size(); i++) {
            DeferredItem<?> cell = MegaCellsCompat.cells().get(i);
            flat(cell, housingTexture)
                    .texture("layer1", CELL_LED)
                    .texture("layer2", TUUTextures.of(MegaCellsCompat.MOD_ID, "item/storage_cell_side_" + TUUCompatConfig.MEGA_SIZE_NAMES[i]));
        }
    }

    private void infiniteCells() {
        ResourceLocation placeholder = TUUTextures.of(ExtendedAeCompat.MOD_ID, "item/infinity_cell_cobble");
        ExtendedAeCompat.infiniteCells().forEach((name, cell) ->
                flat(cell, textures.item("cell/" + cell.getId().getPath(), placeholder)).texture("layer1", CELL_LED));
    }

    private ItemModelBuilder flat(DeferredItem<?> item, ResourceLocation texture) {
        return singleTexture(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), mcLoc("item/generated"), "layer0", texture);
    }

    @Override
    public String getName() {
        return "Compat Item Models: " + modid;
    }
}
