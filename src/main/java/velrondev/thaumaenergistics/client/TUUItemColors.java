package velrondev.thaumaenergistics.client;

import appeng.items.storage.BasicStorageCell;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUItems;

@EventBusSubscriber(modid = TUU.MOD_ID, value = Dist.CLIENT)
public final class TUUItemColors {

    private TUUItemColors() {
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> FastColor.ARGB32.opaque(BasicStorageCell.getColor(stack, tintIndex)), TUUItems.cells());
    }
}
