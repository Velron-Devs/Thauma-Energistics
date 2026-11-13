package velrondev.thaumaenergistics.compat.ae2things;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import velrondev.thaumaenergistics.compat.ae2megathings.Ae2MegaThingsCompat;
import velrondev.thaumaenergistics.core.TUUCompat;

public final class Ae2ThingsClient {

    private Ae2ThingsClient() {
    }

    public static void register(IEventBus bus) {
        bus.addListener(RegisterColorHandlersEvent.Item.class, Ae2ThingsClient::registerItemColors);
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        List<ItemLike> items = new ArrayList<>();
        Ae2ThingsCompat.disks().forEach(disk -> items.add(disk.get()));
        if (TUUCompat.megaThingsActive()) {
            Ae2MegaThingsCompat.disks().forEach(disk -> items.add(disk.get()));
        }
        event.register((stack, tintIndex) -> EssentiaDiskItem.getColor(stack, tintIndex), items.toArray(new ItemLike[0]));
    }
}
