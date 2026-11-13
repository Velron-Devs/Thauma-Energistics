package velrondev.thaumaenergistics.behavior;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;

import appeng.api.behaviors.StackImportStrategy;
import appeng.api.behaviors.StackTransferContext;
import appeng.api.config.Actionable;
import appeng.api.stacks.AEKeyType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import velrondev.thaumaenergistics.api.key.EssentiaKey;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public class EssentiaImportStrategy implements StackImportStrategy {

    private final EssentiaTarget target;

    public EssentiaImportStrategy(ServerLevel level, BlockPos pos, Direction side) {
        this.target = new EssentiaTarget(level, pos, side);
    }

    @Override
    public boolean transfer(StackTransferContext context) {
        AEKeyType type = TUUKeyTypes.essentia();
        if (!context.isKeyTypeEnabled(type) || !target.available()) {
            return false;
        }
        long perOperation = type.getAmountPerOperation();
        long remaining = context.getOperationsRemaining() * perOperation;
        var inventory = context.getInternalStorage().getInventory();

        for (AspectInstance entry : target.contents().entries()) {
            if (remaining <= 0) {
                break;
            }
            Holder<IAspect> aspect = entry.aspect();
            EssentiaKey key = EssentiaKey.of(aspect);
            if (key == null || context.isInFilter(key) == context.isInverted()) {
                continue;
            }
            long fits = inventory.insert(key, Math.min(remaining, entry.amount()), Actionable.SIMULATE, context.getActionSource());
            if (fits <= 0) {
                continue;
            }
            int extracted = target.extract(aspect, (int) fits, false);
            if (extracted <= 0) {
                continue;
            }
            long inserted = inventory.insert(key, extracted, Actionable.MODULATE, context.getActionSource());
            if (inserted < extracted) {
                target.insert(aspect, (int) (extracted - inserted), false);
            }
            context.reduceOperationsRemaining(Math.max(1, inserted / perOperation));
            remaining -= inserted;
        }
        return false;
    }
}
