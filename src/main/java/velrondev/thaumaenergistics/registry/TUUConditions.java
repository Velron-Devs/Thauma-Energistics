package velrondev.thaumaenergistics.registry;

import com.mojang.serialization.MapCodec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.condition.TUUFeatureCondition;

public final class TUUConditions {

    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, TUU.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<TUUFeatureCondition>> FEATURE = CONDITIONS.register("feature", () -> TUUFeatureCondition.CODEC);

    private TUUConditions() {
    }

    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
    }
}
