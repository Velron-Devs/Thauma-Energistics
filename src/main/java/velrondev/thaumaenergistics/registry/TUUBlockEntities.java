package velrondev.thaumaenergistics.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.blockentity.InfusionProviderBlockEntity;

public final class TUUBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TUU.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusionProviderBlockEntity>> INFUSION_PROVIDER = BLOCK_ENTITIES.register("infusion_provider",
            () -> BlockEntityType.Builder.of(InfusionProviderBlockEntity::new, TUUBlocks.INFUSION_PROVIDER.get()).build(null));

    private TUUBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
