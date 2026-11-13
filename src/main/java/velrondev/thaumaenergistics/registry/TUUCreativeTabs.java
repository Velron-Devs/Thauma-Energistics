package velrondev.thaumaenergistics.registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.processor.ProcessorKind;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public final class TUUCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TUU.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + TUU.MOD_ID))
            .icon(() -> new ItemStack(TUUItems.ESSENTIA_CELL_64K.get()))
            .displayItems((parameters, output) -> ordered().forEach(output::accept))
            .build());

    private TUUCreativeTabs() {
    }

    private static List<Item> ordered() {
        Set<Item> items = new LinkedHashSet<>();
        items.add(TUUItems.ESSENTIA_CELL_1K.get());
        items.add(TUUItems.ESSENTIA_CELL_4K.get());
        items.add(TUUItems.ESSENTIA_CELL_16K.get());
        items.add(TUUItems.ESSENTIA_CELL_64K.get());
        items.add(TUUItems.ESSENTIA_CELL_256K.get());
        items.add(TUUItems.ESSENTIA_CELL_CREATIVE.get());
        items.add(TUUItems.ESSENTIA_COMPONENT_1K.get());
        items.add(TUUItems.ESSENTIA_COMPONENT_4K.get());
        items.add(TUUItems.ESSENTIA_COMPONENT_16K.get());
        items.add(TUUItems.ESSENTIA_COMPONENT_64K.get());
        items.add(TUUItems.ESSENTIA_COMPONENT_256K.get());
        items.add(TUUItems.DIFFUSION_CORE.get());
        items.add(TUUItems.COALESCENCE_CORE.get());
        for (ProcessorMetal metal : ProcessorMetal.values()) {
            for (ProcessorKind kind : ProcessorKind.values()) {
                items.add(TUUProcessors.printed(metal, kind).get());
                items.add(TUUProcessors.processor(metal, kind).get());
            }
        }
        items.add(TUUParts.ESSENTIA_TERMINAL.get());
        items.add(TUUParts.ESSENTIA_IMPORT_BUS.get());
        items.add(TUUParts.ESSENTIA_EXPORT_BUS.get());
        items.add(TUUParts.ESSENTIA_STORAGE_BUS.get());
        items.add(TUUParts.ARCANE_TERMINAL.get());
        items.add(TUUItems.WIRELESS_ESSENTIA_TERMINAL.get());
        items.add(TUUItems.WIRELESS_ARCANE_TERMINAL.get());
        items.add(TUUItems.UPGRADE_ARCANE.get());
        items.add(TUUBlocks.INFUSION_PROVIDER_ITEM.get());

        List<Item> rest = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(TUU.MOD_ID) && !items.contains(item)) {
                rest.add(item);
            }
        }
        rest.sort(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).getPath()));
        items.addAll(rest);
        return new ArrayList<>(items);
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
