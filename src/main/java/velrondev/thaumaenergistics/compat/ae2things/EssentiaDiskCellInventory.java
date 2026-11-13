package velrondev.thaumaenergistics.compat.ae2things;

import org.jetbrains.annotations.Nullable;

import appeng.api.storage.cells.ISaveProvider;
import io.github.projectet.ae2things.storage.DISKCellInventory;
import io.github.projectet.ae2things.storage.IDISKCellItem;
import io.github.projectet.ae2things.util.StorageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EssentiaDiskCellInventory extends DISKCellInventory {

    private final ItemStack stack;

    public EssentiaDiskCellInventory(IDISKCellItem cellType, ItemStack stack, @Nullable ISaveProvider container, @Nullable StorageManager storageManager) {
        super(cellType, stack, container, storageManager);
        this.stack = stack;
    }

    @Override
    public Component getDescription() {
        return stack.getHoverName();
    }

    @Nullable
    public static EssentiaDiskCellInventory create(ItemStack stack, @Nullable ISaveProvider container, @Nullable StorageManager storageManager) {
        if (!(stack.getItem() instanceof EssentiaDiskItem disk)) {
            return null;
        }
        return new EssentiaDiskCellInventory(disk, stack, container, storageManager);
    }
}
