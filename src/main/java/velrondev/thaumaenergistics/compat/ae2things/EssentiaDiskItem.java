package velrondev.thaumaenergistics.compat.ae2things;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.CellState;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.hooks.AEToolItem;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import appeng.util.InteractionUtil;
import io.github.projectet.ae2things.AE2Things;
import io.github.projectet.ae2things.storage.DISKCellInventory;
import io.github.projectet.ae2things.storage.IDISKCellItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public class EssentiaDiskItem extends Item implements IDISKCellItem, AEToolItem {

    private final IntSupplier kilobytes;
    private final Supplier<Double> idleDrain;
    private final Supplier<Item> component;
    private final Supplier<Item> housing;

    public EssentiaDiskItem(IntSupplier kilobytes, Supplier<Double> idleDrain, Supplier<Item> component, Supplier<Item> housing) {
        super(new Properties().stacksTo(1).fireResistant()
                .component(AE2Things.DATA_DISK_ITEM_COUNT, 0L)
                .component(AE2Things.DATA_FUZZY_MODE, FuzzyMode.IGNORE_ALL));
        this.kilobytes = kilobytes;
        this.idleDrain = idleDrain;
        this.component = component;
        this.housing = housing;
    }

    @Override
    public AEKeyType getKeyType() {
        return TUUKeyTypes.essentia();
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        long bytes = kilobytes.getAsInt() * 1000L * TUUKeyTypes.essentia().getAmountPerByte();
        return (int) Math.min(Integer.MAX_VALUE, bytes);
    }

    @Override
    public boolean isBlackListed(ItemStack cellItem, AEKey requestedAddition) {
        return false;
    }

    @Override
    public double getIdleDrain() {
        return idleDrain.get();
    }

    @Override
    public boolean isEditable(ItemStack stack) {
        return true;
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, 1);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack stack) {
        return CellConfig.create(Set.of(getKeyType()), stack);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack stack) {
        return stack.getOrDefault(AE2Things.DATA_FUZZY_MODE, FuzzyMode.IGNORE_ALL);
    }

    @Override
    public void setFuzzyMode(ItemStack stack, FuzzyMode mode) {
        stack.set(AE2Things.DATA_FUZZY_MODE, mode);
    }

    @Override
    public ItemStack clone(ItemStack item) {
        UUID diskId = item.get(AE2Things.DATA_DISK_ID);
        if (diskId == null) {
            return item.copy();
        }
        UUID id = UUID.randomUUID();
        ItemStack copy = item.copy();
        copy.set(AE2Things.DATA_DISK_ID, id);
        copy.setCount(copy.getMaxStackSize());
        var manager = AE2Things.currentStorageManager();
        if (manager == null) {
            copy.remove(AE2Things.DATA_DISK_ITEM_COUNT);
            return copy;
        }
        var storage = manager.getOrCreateDisk(diskId);
        copy.set(AE2Things.DATA_DISK_ITEM_COUNT, storage.itemCount);
        manager.updateDisk(id, storage);
        return copy;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        addCellInformationToTooltip(stack, lines);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        disassemble(player.getItemInHand(hand), level, player);
        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()), player.getItemInHand(hand));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && disassemble(stack, context.getLevel(), player)) {
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
        }
        return InteractionResult.PASS;
    }

    private boolean disassemble(ItemStack stack, Level level, Player player) {
        if (level.isClientSide() || !InteractionUtil.isInAlternateUseMode(player)) {
            return false;
        }
        Inventory inventory = player.getInventory();
        var cell = StorageCells.getCellInventory(stack, null);
        if (cell == null || inventory.getSelected() != stack || !cell.getAvailableStacks().isEmpty()) {
            return false;
        }
        inventory.setItem(inventory.selected, ItemStack.EMPTY);
        inventory.placeItemBackInInventory(new ItemStack(component.get()));
        for (ItemStack upgrade : getUpgrades(stack)) {
            inventory.placeItemBackInInventory(upgrade);
        }
        inventory.placeItemBackInInventory(new ItemStack(housing.get()));
        return true;
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 1) {
            DISKCellInventory inventory = DISKCellInventory.createInventory(stack, null, null);
            CellState state = inventory != null ? inventory.getClientStatus() : CellState.EMPTY;
            return 0xFF000000 | state.getStateColor();
        }
        return 0xFFFFFFFF;
    }
}
