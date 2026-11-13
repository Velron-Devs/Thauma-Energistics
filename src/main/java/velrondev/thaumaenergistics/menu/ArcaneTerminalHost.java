package velrondev.thaumaenergistics.menu;

import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;

import appeng.api.inventories.ISegmentedInventory;
import appeng.api.storage.ITerminalHost;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface ArcaneTerminalHost extends ITerminalHost, ISegmentedInventory {

    ArcaneWorkbenchContext context(ServerPlayer player);

    Level getAuraLevel();

    BlockPos getAuraPos();

    default boolean chargesWandOnTick() {
        return false;
    }
}
