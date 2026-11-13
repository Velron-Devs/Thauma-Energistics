package velrondev.thaumaenergistics.compat.extendedae;

import com.glodblock.github.extendedae.common.EAESingletons;
import com.glodblock.github.extendedae.common.me.matrix.ClusterAssemblerMatrix;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixSpeed;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import velrondev.thaumaenergistics.config.TECompatConfig;
import velrondev.thaumaenergistics.processor.ProcessorMetal;

public class TierMatrixSpeedTile extends TileAssemblerMatrixSpeed {

    private final ProcessorMetal metal;

    public TierMatrixSpeedTile(BlockPos pos, BlockState state) {
        super(EAESingletons.ASSEMBLER_MATRIX_SPEED.get().getBlockEntityType(), pos, state);
        this.metal = ((MatrixTier) state.getBlock()).metal();
        this.getMainNode().setIdlePowerUsage(TECompatConfig.MATRIX_IDLE_POWER.get());
    }

    @Override
    public void add(ClusterAssemblerMatrix cluster) {
        int cores = TECompatConfig.MATRIX_SPEED_CORES.get(metal).get();
        for (int i = 0; i < cores; i++) {
            cluster.addSpeedCore();
        }
    }
}
