package velrondev.thaumaenergistics.crafting;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.WandEconomy;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.config.TEConfig;

public final class TerminalWandCharger {

    private TerminalWandCharger() {
    }

    public static int charge(Level level, BlockPos pos, ItemStack wand) {
        if (level.isClientSide() || wand.isEmpty() || !(wand.getItem() instanceof ItemWand) || !TEConfig.ARCANE_CHARGE_ENABLED.get()) {
            return 0;
        }
        int max = WandVisHelper.capacityOf(wand);
        int budget = TEConfig.ARCANE_CHARGE_VIS_PER_PULSE.get();
        int added = 0;
        for (ResourceKey<IAspect> primal : TTAspects.PRIMALS) {
            if (budget <= 0) {
                break;
            }
            int room = (max - WandVisHelper.storedIn(wand, primal)) / WandEconomy.CENTIVIS_PER_VIS;
            int wanted = Math.min(budget, room);
            if (wanted <= 0) {
                continue;
            }
            int available = Mth.floor(AuraHelper.drainVis(level, pos, wanted, true));
            if (available <= 0) {
                continue;
            }
            int taken = Mth.floor(AuraHelper.drainVis(level, pos, available, false));
            if (taken <= 0) {
                continue;
            }
            int overflow = WandVisHelper.topUp(wand, primal, taken, true);
            if (overflow > 0) {
                AuraHelper.addVis(level, pos, overflow);
            }
            budget -= taken - overflow;
            added += taken - overflow;
        }
        return added;
    }
}
