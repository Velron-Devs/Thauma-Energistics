package velrondev.thaumaenergistics.compat.extendedae;

import java.util.function.Supplier;

import com.glodblock.github.extendedae.common.blocks.matrix.BlockAssemblerMatrixCrafter;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class TierMatrixCrafterBlock extends BlockAssemblerMatrixCrafter implements MatrixTier {

    private final ProcessorMetal metal;
    private final Supplier<Item> item;

    public TierMatrixCrafterBlock(ProcessorMetal metal, Supplier<Item> item) {
        this.metal = metal;
        this.item = item;
    }

    @Override
    public ProcessorMetal metal() {
        return metal;
    }

    @Override
    public Item getPresentItem() {
        return item.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TierMatrixCrafterTile(pos, state);
    }
}
