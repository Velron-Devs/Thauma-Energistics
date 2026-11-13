package velrondev.thaumaenergistics.compat.jei;

import appeng.client.gui.implementations.IOBusScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.Identifier;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.client.screen.EssentiaStorageBusScreen;
import velrondev.thaumaenergistics.registry.TEMenus;

@JeiPlugin
public class TEJeiPlugin implements IModPlugin {

    private static final Identifier UID = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "jei");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(IOBusScreen.class, new EssentiaGhostIngredientHandler<IOBusScreen>(screen -> {
            var type = screen.getMenu().getType();
            return type == TEMenus.ESSENTIA_IMPORT_BUS.get() || type == TEMenus.ESSENTIA_EXPORT_BUS.get();
        }));
        registration.addGhostIngredientHandler(EssentiaStorageBusScreen.class, new EssentiaGhostIngredientHandler<EssentiaStorageBusScreen>(screen -> true));
    }
}
