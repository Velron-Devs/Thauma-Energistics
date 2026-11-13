package velrondev.thaumaenergistics.cell;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import velrondev.thaumaenergistics.config.TEConfig;

public class InfinityEssentiaCellItem extends EssentiaCellItem {

    public static final String TOOLTIP_KEY = "item.thaumaenergistics.essentia_cell_infinity.tooltip";

    public InfinityEssentiaCellItem(Properties properties, Supplier<Double> idleDrain) {
        super(properties, 0, idleDrain);
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        return TEConfig.INFINITY_CELL_BYTES.get();
    }

    @Override
    public int getTotalTypes(ItemStack cellItem) {
        return TEConfig.INFINITY_CELL_TYPES.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> lines, TooltipFlag flag) {
        lines.accept(Component.translatable(TOOLTIP_KEY).withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, context, tooltipDisplay, lines, flag);
    }
}
