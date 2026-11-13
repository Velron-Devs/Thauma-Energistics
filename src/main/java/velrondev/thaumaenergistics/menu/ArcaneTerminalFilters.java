package velrondev.thaumaenergistics.menu;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.taint.item.ItemEssentiaCrystal;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;

import appeng.api.inventories.InternalInventory;
import appeng.util.inv.filter.IAEItemFilter;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

public final class ArcaneTerminalFilters {

    private ArcaneTerminalFilters() {
    }

    public static IAEItemFilter wand() {
        return new IAEItemFilter() {
            @Override
            public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
                return stack.getItem() instanceof ItemWand;
            }
        };
    }

    public static IAEItemFilter crystals() {
        return new IAEItemFilter() {
            @Override
            public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
                if (!(stack.getItem() instanceof ItemEssentiaCrystal) || slot >= ArcaneCrystals.PRIMALS.size()) {
                    return false;
                }
                Holder<IAspect> aspect = ItemEssentiaCrystal.aspectOf(stack);
                return aspect != null && aspect.is(ArcaneCrystals.PRIMALS.get(slot));
            }
        };
    }
}
