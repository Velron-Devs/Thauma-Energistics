package velrondev.thaumaenergistics.registry;

import java.util.function.UnaryOperator;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;

public final class TUUDataComponents {

    private static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TUU.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ARCANE_CRAFTING_INV = contents("arcane_crafting_inv");
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ARCANE_WAND_INV = contents("arcane_wand_inv");
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ARCANE_CRYSTALS_INV = contents("arcane_crystals_inv");

    private TUUDataComponents() {
    }

    private static DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> contents(String name) {
        UnaryOperator<DataComponentType.Builder<ItemContainerContents>> builder = type -> type
                .persistent(ItemContainerContents.CODEC)
                .networkSynchronized(ItemContainerContents.STREAM_CODEC);
        return COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
