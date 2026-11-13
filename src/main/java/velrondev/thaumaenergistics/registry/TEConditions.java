package velrondev.thaumaenergistics.registry;

import com.mojang.serialization.MapCodec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.condition.TEFeatureCondition;

public final class TEConditions {

    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, ThaumaEnergistics.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<TEFeatureCondition>> FEATURE = CONDITIONS.register("feature", () -> TEFeatureCondition.CODEC);

    private TEConditions() {
    }

    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
    }
}
