package velrondev.thaumaenergistics.cell;

import java.util.Set;

import appeng.items.contents.CellConfig;
import appeng.items.storage.CreativeCellItem;
import appeng.util.ConfigInventory;
import net.minecraft.world.item.ItemStack;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public class EssentiaCreativeCellItem extends CreativeCellItem {

    public EssentiaCreativeCellItem(Properties properties) {
        super(properties);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack stack) {
        return CellConfig.create(Set.of(TUUKeyTypes.essentia()), stack);
    }
}
