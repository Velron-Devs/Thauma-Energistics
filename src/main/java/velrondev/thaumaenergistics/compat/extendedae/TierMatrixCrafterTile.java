package velrondev.thaumaenergistics.compat.extendedae;

import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixCrafter;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import velrondev.thaumaenergistics.config.TUUCompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class TierMatrixCrafterTile extends TileAssemblerMatrixCrafter {

    private final ProcessorMetal metal;

    public TierMatrixCrafterTile(BlockPos pos, BlockState state) {
        super(pos, state);
        this.metal = ((MatrixTier) state.getBlock()).metal();
        this.getMainNode().setIdlePowerUsage(TUUCompatConfig.MATRIX_IDLE_POWER.get());
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        int boost = TUUCompatConfig.MATRIX_CRAFTER_BOOST.get(metal).get();
        return super.tickingRequest(node, ticksSinceLastCall * boost);
    }
}
