package velrondev.thaumaenergistics.cell;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.config.TUUConfig;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public class EssentiaCellItem extends Item implements IBasicCellItem {

    private final IntSupplier kilobytes;
    private final Supplier<Double> idleDrain;

    public EssentiaCellItem(Properties properties, int kilobytes, Supplier<Double> idleDrain) {
        this(properties, () -> kilobytes, idleDrain);
    }

    public EssentiaCellItem(Properties properties, IntSupplier kilobytes, Supplier<Double> idleDrain) {
        super(properties);
        this.kilobytes = kilobytes;
        this.idleDrain = idleDrain;
    }

    @Override
    public AEKeyType getKeyType() {
        return TUUKeyTypes.essentia();
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        return (int) Math.min(Integer.MAX_VALUE, kilobytes.getAsInt() * 1024L);
    }

    @Override
    public int getBytesPerType(ItemStack cellItem) {
        return TUUConfig.CELL_BYTES_PER_TYPE.get();
    }

    @Override
    public int getTotalTypes(ItemStack cellItem) {
        return TUUConfig.CELL_TYPES.get();
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        addCellInformationToTooltip(stack, lines);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return getCellTooltipImage(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        disassemble(stack, level, player);
        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()), stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return disassemble(stack, context.getLevel(), context.getPlayer())
                ? InteractionResult.sidedSuccess(context.getLevel().isClientSide())
                : InteractionResult.PASS;
    }

    private boolean disassemble(ItemStack stack, Level level, Player player) {
        if (!InteractionUtil.isInAlternateUseMode(player)) {
            return false;
        }
        List<ItemStack> results = StorageCellDisassemblyRecipe.getDisassemblyResult(level, stack.getItem());
        if (results.isEmpty() || player.getInventory().getSelected() != stack) {
            return false;
        }
        var inventory = StorageCells.getCellInventory(stack, null);
        if (inventory != null && !inventory.getAvailableStacks().isEmpty()) {
            player.displayClientMessage(PlayerMessages.OnlyEmptyCellsCanBeDisassembled.text(), true);
            return false;
        }
        player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
        for (ItemStack result : results) {
            player.getInventory().placeItemBackInInventory(result.copy());
        }
        getUpgrades(stack).forEach(player.getInventory()::placeItemBackInInventory);
        return true;
    }
}
