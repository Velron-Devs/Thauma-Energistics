package velrondev.thaumaenergistics.item;

import java.util.function.Consumer;

import appeng.items.materials.UpgradeCardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class ArcaneChargingUpgradeItem extends UpgradeCardItem {

    public ArcaneChargingUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> lines, TooltipFlag flag) {
        lines.accept(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipDisplay, lines, flag);
    }
}
