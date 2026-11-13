package velrondev.thaumaenergistics.core;

import com.leclowndu93150.thaumaturge.api.recipe.RegisterWorkbenchAuraSourcesEvent;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.crafting.TerminalArcaneAuraSource;

@EventBusSubscriber(modid = TUU.MOD_ID)
public final class TUUArcaneEvents {

    private TUUArcaneEvents() {
    }

    @SubscribeEvent
    public static void registerAuraSources(RegisterWorkbenchAuraSourcesEvent event) {
        event.register(new TerminalArcaneAuraSource());
    }
}
