package velrondev.thaumaenergistics.compat.ae2things;

import org.jetbrains.annotations.Nullable;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import io.github.projectet.ae2things.AE2Things;
import net.minecraft.world.item.ItemStack;

public final class EssentiaDiskCellHandler implements ICellHandler {

    public static final EssentiaDiskCellHandler INSTANCE = new EssentiaDiskCellHandler();

    private EssentiaDiskCellHandler() {
    }

    @Override
    public boolean isCell(ItemStack stack) {
        return stack.getItem() instanceof EssentiaDiskItem;
    }

    @Nullable
    @Override
    public StorageCell getCellInventory(ItemStack stack, @Nullable ISaveProvider container) {
        return EssentiaDiskCellInventory.create(stack, container, AE2Things.currentStorageManager());
    }
}
