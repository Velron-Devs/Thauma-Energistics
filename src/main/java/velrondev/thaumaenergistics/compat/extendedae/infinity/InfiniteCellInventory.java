package velrondev.thaumaenergistics.compat.extendedae.infinity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class InfiniteCellInventory implements StorageCell {

    private final InfiniteCellItem item;
    private final ItemStack stack;

    public InfiniteCellInventory(InfiniteCellItem item, ItemStack stack) {
        this.item = item;
        this.stack = stack;
    }

    @Override
    public CellState getStatus() {
        return CellState.NOT_EMPTY;
    }

    @Override
    public double getIdleDrain() {
        return item.idleDrain();
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        return item.target().matches().test(what) ? amount : 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        return item.target().matches().test(what) ? amount : 0;
    }

    @Override
    public void persist() {
    }

    @Override
    public Component getDescription() {
        return stack.getHoverName();
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        AEKey key = item.resolve();
        if (key != null) {
            out.add(key, InfiniteCellItem.infiniteAmount(key));
        }
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return item.target().matches().test(what);
    }
}
