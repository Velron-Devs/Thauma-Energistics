package velrondev.thaumaenergistics.core;

import com.leclowndu93150.thaumaturge.api.aspect.AspectCapabilities;

import appeng.api.AECapabilities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.registry.TEBlockEntities;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID)
public final class TEInfusionCapabilities {

    private TEInfusionCapabilities() {
    }

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(AspectCapabilities.CONTAINER, TEBlockEntities.INFUSION_PROVIDER.get(), (provider, side) -> provider);
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, TEBlockEntities.INFUSION_PROVIDER.get(), (provider, side) -> provider);
    }
}
