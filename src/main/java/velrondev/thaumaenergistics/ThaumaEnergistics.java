package velrondev.thaumaenergistics;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.config.TEConfig;
import velrondev.thaumaenergistics.core.TECompat;
import velrondev.thaumaenergistics.core.TESetup;
import velrondev.thaumaenergistics.core.TEWtlib;
import velrondev.thaumaenergistics.registry.TEBlockEntities;
import velrondev.thaumaenergistics.registry.TEBlocks;
import velrondev.thaumaenergistics.registry.TEConditions;
import velrondev.thaumaenergistics.registry.TECreativeTabs;
import velrondev.thaumaenergistics.registry.TEDataComponents;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEKeyTypes;
import velrondev.thaumaenergistics.registry.TEMenus;
import velrondev.thaumaenergistics.registry.TEParts;
import velrondev.thaumaenergistics.registry.TEProcessors;

@Mod(ThaumaEnergistics.MOD_ID)
public class ThaumaEnergistics {

    public static final String MOD_ID = "thaumaenergistics";

    public ThaumaEnergistics(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, TEConfig.SPEC);
        container.registerConfig(ModConfig.Type.COMMON, TECompatConfig.SPEC, MOD_ID + "-compat.toml");

        TEWtlib.init();

        TEConditions.register(bus);
        TEKeyTypes.register(bus);
        TEDataComponents.register(bus);
        TEItems.register(bus);
        TEProcessors.register(bus);
        TECompat.register(bus);
        TEParts.register(bus);
        TEBlocks.register(bus);
        TEBlockEntities.register(bus);
        TEMenus.register(bus);
        TECreativeTabs.register(bus);

        bus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(TESetup::init));
    }
}
