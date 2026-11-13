package velrondev.thaumaenergistics.client;

import appeng.client.api.AEKeyRendering;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.api.key.EssentiaKey;
import velrondev.thaumaenergistics.registry.TEKeyTypes;

@EventBusSubscriber(modid = ThaumaEnergistics.MOD_ID, value = Dist.CLIENT)
public final class TEClient {

    private TEClient() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> AEKeyRendering.register(TEKeyTypes.essentia(), EssentiaKey.class, new EssentiaKeyRenderHandler()));
    }
}
