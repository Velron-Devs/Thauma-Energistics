package velrondev.thaumaenergistics.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.blockentity.InfusionProviderBlockEntity;

public final class TEBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ThaumaEnergistics.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusionProviderBlockEntity>> INFUSION_PROVIDER = BLOCK_ENTITIES.register("infusion_provider",
            () -> new BlockEntityType<>(InfusionProviderBlockEntity::new, TEBlocks.INFUSION_PROVIDER.get()));

    private TEBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
