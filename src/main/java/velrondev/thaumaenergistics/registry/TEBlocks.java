package velrondev.thaumaenergistics.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.block.InfusionProviderBlock;

public final class TEBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaEnergistics.MOD_ID);
    private static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(ThaumaEnergistics.MOD_ID);

    public static final DeferredBlock<InfusionProviderBlock> INFUSION_PROVIDER = BLOCKS.registerBlock("infusion_provider", InfusionProviderBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.2F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops());
    public static final DeferredItem<BlockItem> INFUSION_PROVIDER_ITEM = BLOCK_ITEMS.registerSimpleBlockItem("infusion_provider", INFUSION_PROVIDER);

    private TEBlocks() {
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ITEMS.register(bus);
    }
}
