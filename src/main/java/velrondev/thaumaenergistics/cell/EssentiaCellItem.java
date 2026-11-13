package velrondev.thaumaenergistics.cell;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import appeng.api.config.FuzzyMode;
import appeng.api.ids.AEComponents;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.IBasicCellItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.core.localization.PlayerMessages;
import appeng.items.contents.CellConfig;
import appeng.recipes.game.StorageCellDisassemblyRecipe;
import appeng.util.ConfigInventory;
import appeng.util.InteractionUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.config.TEConfig;
import velrondev.thaumaenergistics.registry.TEKeyTypes;

public class EssentiaCellItem extends Item implements IBasicCellItem {

    private final int totalBytes;
    private final Supplier<Double> idleDrain;

    public EssentiaCellItem(Properties properties, int kilobytes, Supplier<Double> idleDrain) {
        super(properties);
        this.totalBytes = kilobytes * 1024;
        this.idleDrain = idleDrain;
    }

    @Override
    public AEKeyType getKeyType() {
        return TEKeyTypes.essentia();
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        return totalBytes;
    }

    @Override
    public int getBytesPerType(ItemStack cellItem) {
        return TEConfig.CELL_BYTES_PER_TYPE.get();
    }

    @Override
    public int getTotalTypes(ItemStack cellItem) {
        return TEConfig.CELL_TYPES.get();
    }

    @Override
    public double getIdleDrain() {
        return idleDrain.get();
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, 3);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack stack) {
        return CellConfig.create(Set.of(getKeyType()), stack);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack stack) {
        return stack.getOrDefault(AEComponents.STORAGE_CELL_FUZZY_MODE, FuzzyMode.IGNORE_ALL);
    }

    @Override
    public void setFuzzyMode(ItemStack stack, FuzzyMode mode) {
        stack.set(AEComponents.STORAGE_CELL_FUZZY_MODE, mode);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> lines, TooltipFlag flag) {
        addCellInformationToTooltip(stack, lines);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return getCellTooltipImage(stack);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            disassemble(player.getItemInHand(hand), serverLevel, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel serverLevel && disassemble(stack, serverLevel, context.getPlayer())) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean disassemble(ItemStack stack, ServerLevel level, Player player) {
        if (!InteractionUtil.isInAlternateUseMode(player)) {
            return false;
        }
        List<ItemStack> results = StorageCellDisassemblyRecipe.getDisassemblyResult(level, stack.getItem());
        Inventory inventory = player.getInventory();
        if (results.isEmpty() || inventory.getSelectedItem() != stack) {
            return false;
        }
        var cellInventory = StorageCells.getCellInventory(stack, null);
        if (cellInventory != null && !cellInventory.getAvailableStacks().isEmpty()) {
            player.sendOverlayMessage(PlayerMessages.OnlyEmptyCellsCanBeDisassembled.text());
            return false;
        }
        inventory.setItem(inventory.getSelectedSlot(), ItemStack.EMPTY);
        for (ItemStack result : results) {
            inventory.placeItemBackInInventory(result.copy());
        }
        getUpgrades(stack).forEach(inventory::placeItemBackInInventory);
        return true;
    }
}
