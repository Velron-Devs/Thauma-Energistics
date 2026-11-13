package velrondev.thaumaenergistics.crafting;

import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;
import com.leclowndu93150.thaumaturge.api.recipe.IArcaneWorkbench;
import com.leclowndu93150.thaumaturge.api.recipe.IWorkbenchAuraSource;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class TerminalArcaneAuraSource implements IWorkbenchAuraSource {

    private static final float ROUNDING_MARGIN = 0.0001F;

    @Override
    public int supply(ArcaneWorkbenchContext context, ServerPlayer player, IArcaneWorkbench workbench, int amount, TransactionContext transaction) {
        if (amount <= 0 || !(workbench instanceof TerminalArcaneInput input)) {
            return 0;
        }
        float drained = AuraHelper.drainVis(context.level(), input.auraPos(), amount, transaction);
        return Mth.floor(drained + ROUNDING_MARGIN);
    }
}
