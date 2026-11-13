package velrondev.thaumaenergistics.client;

import appeng.api.client.AEKeyRendering;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.api.key.EssentiaKey;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

@EventBusSubscriber(modid = TUU.MOD_ID, value = Dist.CLIENT)
public final class TUUClient {

    private TUUClient() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> AEKeyRendering.register(TUUKeyTypes.essentia(), EssentiaKey.class, new EssentiaKeyRenderHandler()));
    }
}
