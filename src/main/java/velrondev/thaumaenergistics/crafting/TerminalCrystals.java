package velrondev.thaumaenergistics.crafting;

import java.util.ArrayList;
import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.taint.item.ItemEssentiaCrystal;

import appeng.api.config.Actionable;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

public final class TerminalCrystals {

    public record Take(AEItemKey key, long amount) {
    }

    private TerminalCrystals() {
    }

    public static AspectList available(MEStorage storage, InternalInventory slots) {
        AspectList list = available(storage);
        for (int slot = 0; slot < slots.size(); slot++) {
            ItemStack stack = slots.getStackInSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemEssentiaCrystal) {
                Holder<IAspect> aspect = ItemEssentiaCrystal.aspectOf(stack);
                if (aspect != null) {
                    list = list.add(aspect, stack.getCount());
                }
            }
        }
        return list;
    }

    public static AspectList drawFromSlots(InternalInventory slots, AspectList needed, boolean commit) {
        AspectList remaining = AspectList.EMPTY;
        for (AspectInstance need : needed.entries()) {
            int left = need.amount();
            for (int slot = 0; slot < slots.size() && left > 0; slot++) {
                ItemStack stack = slots.getStackInSlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof ItemEssentiaCrystal)) {
                    continue;
                }
                Holder<IAspect> aspect = ItemEssentiaCrystal.aspectOf(stack);
                if (aspect == null || !aspect.unwrapKey().equals(need.aspect().unwrapKey())) {
                    continue;
                }
                int take = Math.min(left, stack.getCount());
                if (commit) {
                    slots.extractItem(slot, take, false);
                }
                left -= take;
            }
            if (left > 0) {
                remaining = remaining.add(need.aspect(), left);
            }
        }
        return remaining;
    }

    public static AspectList available(MEStorage storage) {
        AspectList list = AspectList.EMPTY;
        for (Object2LongMap.Entry<AEKey> entry : storage.getAvailableStacks()) {
            Holder<IAspect> aspect = aspectOf(entry.getKey());
            if (aspect != null) {
                list = list.add(aspect, (int) Math.min(entry.getLongValue(), Integer.MAX_VALUE / 4));
            }
        }
        return list;
    }

    public static List<Take> plan(MEStorage storage, IActionSource source, AspectList needed) {
        List<Take> takes = new ArrayList<>();
        var stacks = storage.getAvailableStacks();
        for (AspectInstance need : needed.entries()) {
            long remaining = need.amount();
            for (Object2LongMap.Entry<AEKey> entry : stacks) {
                if (remaining <= 0) {
                    break;
                }
                Holder<IAspect> aspect = aspectOf(entry.getKey());
                if (aspect == null || !aspect.unwrapKey().equals(need.aspect().unwrapKey())) {
                    continue;
                }
                AEItemKey key = (AEItemKey) entry.getKey();
                long simulated = storage.extract(key, Math.min(remaining, entry.getLongValue()), Actionable.SIMULATE, source);
                if (simulated > 0) {
                    takes.add(new Take(key, simulated));
                    remaining -= simulated;
                }
            }
            if (remaining > 0) {
                return null;
            }
        }
        return takes;
    }

    private static Holder<IAspect> aspectOf(AEKey key) {
        if (key instanceof AEItemKey itemKey && itemKey.getItem() instanceof ItemEssentiaCrystal) {
            return ItemEssentiaCrystal.aspectOf(itemKey.toStack());
        }
        return null;
    }
}
