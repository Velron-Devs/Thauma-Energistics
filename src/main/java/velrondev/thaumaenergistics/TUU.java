package velrondev.thaumaenergistics;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.config.TUUConfig;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.core.TUUSetup;
import velrondev.thaumaenergistics.core.TUUWtlib;
import velrondev.thaumaenergistics.registry.TUUBlockEntities;
import velrondev.thaumaenergistics.registry.TUUBlocks;
import velrondev.thaumaenergistics.registry.TUUConditions;
import velrondev.thaumaenergistics.registry.TUUCreativeTabs;
import velrondev.thaumaenergistics.registry.TUUDataComponents;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;
import velrondev.thaumaenergistics.registry.TUUMenus;
import velrondev.thaumaenergistics.registry.TUUParts;
import velrondev.thaumaenergistics.registry.TUUProcessors;

@Mod(TUU.MOD_ID)
public class TUU {

    public static final String MOD_ID = "thaumaenergistics";

    public TUU(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, TUUConfig.SPEC);
        container.registerConfig(ModConfig.Type.COMMON, TUUCompatConfig.SPEC, TUU.MOD_ID + "-compat.toml");

        TUUWtlib.init();

        TUUConditions.register(bus);
        TUUKeyTypes.register(bus);
        TUUDataComponents.register(bus);
        TUUItems.register(bus);
        TUUProcessors.register(bus);
        TUUParts.register(bus);
        TUUBlocks.register(bus);
        TUUBlockEntities.register(bus);
        TUUMenus.register(bus);
        TUUCreativeTabs.register(bus);
        TUUCompat.register(bus);

        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(TUUSetup::init));
    }
}
