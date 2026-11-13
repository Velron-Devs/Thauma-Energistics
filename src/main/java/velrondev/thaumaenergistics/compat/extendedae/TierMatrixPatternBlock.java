package velrondev.thaumaenergistics.compat.extendedae;

import java.util.function.Supplier;

import com.glodblock.github.extendedae.common.blocks.matrix.BlockAssemblerMatrixPattern;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class TierMatrixPatternBlock extends BlockAssemblerMatrixPattern implements MatrixTier {

    private final ProcessorMetal metal;
    private final Supplier<Item> item;

    public TierMatrixPatternBlock(Properties properties, ProcessorMetal metal, Supplier<Item> item) {
        super(properties);
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
        return new TierMatrixPatternTile(pos, state);
    }
}
