package velrondev.thaumaenergistics.compat.megacells;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import velrondev.thaumaenergistics.cell.EssentiaCellItem;

public class MegaEssentiaCellItem extends EssentiaCellItem {

    private final IntSupplier kilobytes;

    public MegaEssentiaCellItem(Properties properties, IntSupplier kilobytes, Supplier<Double> idleDrain) {
        super(properties, 0, idleDrain);
        this.kilobytes = kilobytes;
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        return (int) Math.min(Integer.MAX_VALUE, kilobytes.getAsInt() * 1024L);
    }
}
