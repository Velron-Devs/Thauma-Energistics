package velrondev.thaumaenergistics.behavior;

import com.google.common.primitives.Ints;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;

import appeng.api.behaviors.StackExportStrategy;
import appeng.api.behaviors.StackTransferContext;
import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import appeng.api.storage.StorageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import velrondev.thaumaenergistics.api.key.EssentiaKey;

public class EssentiaExportStrategy implements StackExportStrategy {

    private final ServerLevel level;
    private final EssentiaTarget target;

    public EssentiaExportStrategy(ServerLevel level, BlockPos pos, Direction side) {
        this.level = level;
        this.target = new EssentiaTarget(level, pos, side);
    }

    private Holder<IAspect> resolve(AEKey what) {
        return what instanceof EssentiaKey key ? key.holder(level.registryAccess()) : null;
    }

    @Override
    public long transfer(StackTransferContext context, AEKey what, long maxAmount) {
        Holder<IAspect> aspect = resolve(what);
        if (aspect == null || !target.available()) {
            return 0;
        }
        var inventory = context.getInternalStorage().getInventory();
        long extracted = StorageHelper.poweredExtraction(context.getEnergySource(), inventory, what, maxAmount, context.getActionSource(), Actionable.SIMULATE);
        int accepted = target.insert(aspect, Ints.saturatedCast(extracted), true);
        if (accepted <= 0) {
            return 0;
        }
        extracted = StorageHelper.poweredExtraction(context.getEnergySource(), inventory, what, accepted, context.getActionSource(), Actionable.MODULATE);
        int inserted = target.insert(aspect, Ints.saturatedCast(extracted), false);
        if (inserted < extracted) {
            inventory.insert(what, extracted - inserted, Actionable.MODULATE, context.getActionSource());
        }
        return inserted;
    }

    @Override
    public long push(AEKey what, long maxAmount, Actionable mode) {
        Holder<IAspect> aspect = resolve(what);
        if (aspect == null || !target.available()) {
            return 0;
        }
        return target.insert(aspect, Ints.saturatedCast(maxAmount), mode.isSimulate());
    }
}
