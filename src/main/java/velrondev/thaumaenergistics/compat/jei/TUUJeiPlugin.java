package velrondev.thaumaenergistics.compat.jei;

import appeng.client.gui.implementations.IOBusScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.client.screen.EssentiaStorageBusScreen;
import velrondev.thaumaenergistics.registry.TUUMenus;

@JeiPlugin
public class TUUJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(IOBusScreen.class, new EssentiaGhostIngredientHandler<IOBusScreen>(screen -> {
            var type = screen.getMenu().getType();
            return type == TUUMenus.ESSENTIA_IMPORT_BUS.get() || type == TUUMenus.ESSENTIA_EXPORT_BUS.get();
        }));
        registration.addGhostIngredientHandler(EssentiaStorageBusScreen.class, new EssentiaGhostIngredientHandler<EssentiaStorageBusScreen>(screen -> true));
    }
}
