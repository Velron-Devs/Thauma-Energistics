package velrondev.thaumaenergistics.compat.extendedae;

import java.util.function.Supplier;

import com.glodblock.github.extendedae.common.blocks.matrix.BlockAssemblerMatrixSpeed;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class TierMatrixSpeedBlock extends BlockAssemblerMatrixSpeed implements MatrixTier {

    private final ProcessorMetal metal;
    private final Supplier<Item> item;

    public TierMatrixSpeedBlock(ProcessorMetal metal, Supplier<Item> item) {
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
        return new TierMatrixSpeedTile(pos, state);
    }
}
