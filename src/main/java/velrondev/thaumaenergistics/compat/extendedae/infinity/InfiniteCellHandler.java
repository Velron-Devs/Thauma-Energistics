package velrondev.thaumaenergistics.compat.extendedae.infinity;

import org.jetbrains.annotations.Nullable;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.world.item.ItemStack;

public final class InfiniteCellHandler implements ICellHandler {

    public static final InfiniteCellHandler INSTANCE = new InfiniteCellHandler();

    private InfiniteCellHandler() {
    }

    @Override
    public boolean isCell(ItemStack stack) {
        return stack.getItem() instanceof InfiniteCellItem;
    }

    @Nullable
    @Override
    public StorageCell getCellInventory(ItemStack stack, @Nullable ISaveProvider container) {
        return stack.getItem() instanceof InfiniteCellItem item ? new InfiniteCellInventory(item, stack) : null;
    }
}
