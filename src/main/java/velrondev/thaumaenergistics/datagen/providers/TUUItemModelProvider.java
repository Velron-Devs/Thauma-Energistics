package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUItems;

public class TUUItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation CELL_LED = ResourceLocation.fromNamespaceAndPath("ae2", "item/storage_cell_led");

    public TUUItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        cell(TUUItems.ESSENTIA_CELL_1K.get(), "1k");
        cell(TUUItems.ESSENTIA_CELL_4K.get(), "4k");
        cell(TUUItems.ESSENTIA_CELL_16K.get(), "16k");
        cell(TUUItems.ESSENTIA_CELL_64K.get(), "64k");
        cell(TUUItems.ESSENTIA_CELL_256K.get(), "256k");
        flat(TUUItems.ESSENTIA_CELL_CREATIVE.get(), "cell/creative");

        flat(TUUItems.ESSENTIA_COMPONENT_1K.get(), "material/essentia_component_1k");
        flat(TUUItems.ESSENTIA_COMPONENT_4K.get(), "material/essentia_component_4k");
        flat(TUUItems.ESSENTIA_COMPONENT_16K.get(), "material/essentia_component_16k");
        flat(TUUItems.ESSENTIA_COMPONENT_64K.get(), "material/essentia_component_64k");
        flat(TUUItems.ESSENTIA_COMPONENT_256K.get(), "material/essentia_component_256k");
        flat(TUUItems.DIFFUSION_CORE.get(), "material/diffusion_core");
        flat(TUUItems.COALESCENCE_CORE.get(), "material/coalescence_core");

        flat(TUUItems.WIRELESS_ARCANE_TERMINAL.get(), "wireless_arcane_terminal");
        flat(TUUItems.WIRELESS_ESSENTIA_TERMINAL.get(), "wireless_essentia_terminal");
        flat(TUUItems.UPGRADE_ARCANE.get(), "material/upgrade_arcane");
    }

    private void cell(Item item, String size) {
        flat(item, "cell/" + size).texture("layer1", CELL_LED);
    }

    private ItemModelBuilder flat(Item item, String texture) {
        return singleTexture(name(item), mcLoc("item/generated"), "layer0", modLoc("item/" + texture));
    }

    private String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}
