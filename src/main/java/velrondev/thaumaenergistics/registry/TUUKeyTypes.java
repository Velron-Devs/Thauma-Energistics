package velrondev.thaumaenergistics.registry;

import appeng.api.stacks.AEKeyType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.api.key.EssentiaKeyType;

public final class TUUKeyTypes {

    private static final DeferredRegister<AEKeyType> KEY_TYPES = DeferredRegister.create(AEKeyType.REGISTRY_KEY, TUU.MOD_ID);

    public static final DeferredHolder<AEKeyType, EssentiaKeyType> ESSENTIA = KEY_TYPES.register("essentia", EssentiaKeyType::new);

    private TUUKeyTypes() {
    }

    public static EssentiaKeyType essentia() {
        return ESSENTIA.get();
    }

    public static void register(IEventBus bus) {
        KEY_TYPES.register(bus);
    }
}
