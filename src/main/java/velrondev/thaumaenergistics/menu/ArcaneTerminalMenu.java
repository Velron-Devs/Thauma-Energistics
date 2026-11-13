package velrondev.thaumaenergistics.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.leclowndu93150.thaumaturge.api.aspect.AspectComponents;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftCost;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftingTransaction;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;

import appeng.api.inventories.InternalInventory;
import appeng.api.storage.MEStorage;
import appeng.core.network.serverbound.InventoryActionPacket;
import appeng.helpers.ICraftingGridMenu;
import appeng.helpers.InventoryAction;
import appeng.me.storage.LinkStatusRespectingInventory;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.ClientActionKey;
import appeng.menu.guisync.GuiSync;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.slot.AppEngSlot;
import appeng.menu.slot.CraftingMatrixSlot;
import appeng.util.inv.PlayerInternalInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import velrondev.thaumaenergistics.config.TEConfig;
import velrondev.thaumaenergistics.crafting.TerminalArcaneInput;
import velrondev.thaumaenergistics.crafting.TerminalArcaneStore;
import velrondev.thaumaenergistics.crafting.TerminalCrystals;
import velrondev.thaumaenergistics.crafting.TerminalWandCharger;
import velrondev.thaumaenergistics.part.ArcaneTerminalPart;
import velrondev.thaumaenergistics.registry.TEItems;

public class ArcaneTerminalMenu extends MEStorageMenu implements ICraftingGridMenu {

    private static final ClientActionKey<Void> ACTION_CLEAR_TO_PLAYER = new ClientActionKey<>("clearToPlayer");
    private static final int HOTBAR_SIZE = 9;
    private static final List<EquipmentSlot> ARMOR_ORDER = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    @GuiSync(20)
    public Component visLine = Component.empty();
    @GuiSync(21)
    public Component detailLine = Component.empty();
    @GuiSync(22)
    public String crystalState = "";

    private final ArcaneTerminalHost terminal;
    private final InternalInventory craftingGrid;
    private final CraftingMatrixSlot[] gridSlots = new CraftingMatrixSlot[TerminalArcaneInput.GRID_SLOTS];
    private final ArcaneResultSlot resultSlot;
    private final InternalInventory wandInventory;
    private final InternalInventory crystalInventory;
    private final MEStorage network;
    private int refreshCooldown;
    private int chargeCooldown;

    public ArcaneTerminalMenu(MenuType<?> type, int id, Inventory playerInventory, ArcaneTerminalHost host) {
        super(type, id, playerInventory, host, true);
        this.terminal = host;
        this.craftingGrid = host.getSubInventory(ArcaneTerminalPart.INV_CRAFTING);
        this.wandInventory = host.getSubInventory(ArcaneTerminalPart.INV_WAND);
        this.crystalInventory = host.getSubInventory(ArcaneTerminalPart.INV_CRYSTALS);
        for (int slot = 0; slot < gridSlots.length; slot++) {
            addSlot(gridSlots[slot] = new CraftingMatrixSlot(this, craftingGrid, slot), SlotSemantics.CRAFTING_GRID);
        }
        this.network = new LinkStatusRespectingInventory(host.getInventory(), this::getLinkStatus);
        addSlot(this.resultSlot = new ArcaneResultSlot(this, energySource, network, craftingGrid), SlotSemantics.CRAFTING_RESULT);

        for (EquipmentSlot equipment : ARMOR_ORDER) {
            addSlot(new ArcaneArmorSlot(playerInventory, equipment), ArcaneSlots.ARMOR);
        }
        addSlot(new AppEngSlot(wandInventory, 0), ArcaneSlots.WAND);
        for (int slot = 0; slot < crystalInventory.size(); slot++) {
            addSlot(new AppEngSlot(crystalInventory, slot), ArcaneSlots.CRYSTALS);
        }

        registerClientAction(ACTION_CLEAR_TO_PLAYER, this::clearToPlayerInventory);
        refreshResult();
    }

    @Override
    public InternalInventory getCraftingMatrix() {
        return craftingGrid;
    }

    @Override
    public void slotsChanged(Container container) {
        refreshResult();
    }

    @Override
    public void broadcastChanges() {
        if (isServerSide() && --refreshCooldown <= 0) {
            refreshCooldown = TEConfig.ARCANE_TERMINAL_REFRESH_TICKS.get();
            refreshResult();
        }
        if (isServerSide() && !terminal.chargesWandOnTick() && --chargeCooldown <= 0) {
            chargeCooldown = TEConfig.ARCANE_CHARGE_INTERVAL_TICKS.get();
            chargeWand();
        }
        super.broadcastChanges();
    }

    private void chargeWand() {
        if (!TEConfig.ARCANE_CHARGE_ENABLED.get() || !terminal.getUpgrades().isInstalled(TEItems.UPGRADE_ARCANE.get())) {
            return;
        }
        ItemStack stack = wandInventory.getStackInSlot(0);
        if (TerminalWandCharger.charge(terminal.getAuraLevel(), terminal.getAuraPos(), stack) > 0) {
            wandInventory.setItemDirect(0, stack);
        }
    }

    public void clearCraftingGrid() {
        Preconditions.checkState(isClientSide());
        ClientPacketDistributor.sendToServer(new InventoryActionPacket(InventoryAction.MOVE_REGION, gridSlots[0].index, 0));
    }

    public void clearToPlayerInventory() {
        if (isClientSide()) {
            sendClientAction(ACTION_CLEAR_TO_PLAYER);
            return;
        }
        PlayerInternalInventory playerInv = new PlayerInternalInventory(getPlayerInventory());
        for (int slot = 0; slot < craftingGrid.size(); slot++) {
            for (int pass = 0; pass < 2; pass++) {
                boolean allowEmpty = pass == 1;
                for (int target = HOTBAR_SIZE; target-- > 0;) {
                    if (playerInv.getStackInSlot(target).isEmpty() == allowEmpty) {
                        craftingGrid.setItemDirect(slot, playerInv.getSlotInv(target).addItems(craftingGrid.getStackInSlot(slot)));
                    }
                }
                for (int target = HOTBAR_SIZE; target < Inventory.INVENTORY_SIZE; target++) {
                    if (playerInv.getStackInSlot(target).isEmpty() == allowEmpty) {
                        craftingGrid.setItemDirect(slot, playerInv.getSlotInv(target).addItems(craftingGrid.getStackInSlot(slot)));
                    }
                }
            }
        }
    }

    public ItemStack craftOnce(ServerPlayer player) {
        if (!TEConfig.ARCANE_TERMINAL_ENABLED.get()) {
            return ItemStack.EMPTY;
        }
        List<ItemStack> items = gridCopy();
        TerminalArcaneInput input = newInput(player, items);
        ArcaneWorkbenchContext context = terminal.context(player);
        TerminalArcaneStore store = new TerminalArcaneStore(player, craftingGrid, crystalInventory, network, energySource, getActionSource());

        ArcaneCraftingTransaction.Failure failure;
        ItemStack crafted = ItemStack.EMPTY;
        try (Transaction transaction = Transaction.openRoot()) {
            ArcaneCraftingTransaction.Result result = ArcaneCraftingTransaction.craft(context, player, input, store, transaction);
            if (result.successful()) {
                transaction.commit();
                wandInventory.setItemDirect(0, wandInventory.getStackInSlot(0));
                crafted = result.output().copy();
            }
            failure = result.failure();
            if (!result.successful() && failure == ArcaneCraftingTransaction.Failure.PAYMENT_UNAVAILABLE) {
                report(player, shortage(result.cost(), input));
                return ItemStack.EMPTY;
            }
        }
        if (!crafted.isEmpty()) {
            return finishCraft(player, crafted);
        }

        if (failure == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
            return craftVanilla(player, items, store);
        }
        if (failure == ArcaneCraftingTransaction.Failure.RESEARCH_LOCKED) {
            report(player, ArcaneTerminalKeys.MESSAGE_LOCKED);
        } else {
            report(player, ArcaneTerminalKeys.MESSAGE_FAILED);
        }
        return ItemStack.EMPTY;
    }

    public void refreshResult() {
        if (isClientSide()) {
            return;
        }
        ServerPlayer player = (ServerPlayer) getPlayer();
        ItemStack output = ItemStack.EMPTY;
        Component vis = Component.empty();
        Component detail = Component.empty();
        String crystals = "";

        List<ItemStack> items = gridCopy();
        boolean filled = items.stream().anyMatch(stack -> !stack.isEmpty());
        if (!TEConfig.ARCANE_TERMINAL_ENABLED.get()) {
            if (filled) {
                detail = Component.translatable(ArcaneTerminalKeys.DISABLED).withStyle(ChatFormatting.RED);
            }
        } else if (filled) {
            TerminalArcaneInput input = newInput(player, items);
            ArcaneWorkbenchContext context = terminal.context(player);
            ArcaneCraftingTransaction.Inspection inspection = ArcaneCraftingTransaction.inspect(context, player, input);
            if (inspection.successful()) {
                output = inspection.output().copy();
                ArcaneCraftingTransaction.Result preview = ArcaneCraftingTransaction.preview(context, player, input);
                ArcaneCraftCost cost = preview.cost();
                if (cost != null) {
                    vis = visLine(inspection.requirements().baseVis(), cost);
                    detail = crystalLine(cost, input);
                    crystals = ArcaneCrystals.encode(cost.crystalsNeeded(), input.availableCrystals(), player.level().registryAccess());
                }
            } else if (inspection.failure() == ArcaneCraftingTransaction.Failure.RESEARCH_LOCKED) {
                detail = Component.translatable(ArcaneTerminalKeys.LOCKED).withStyle(ChatFormatting.RED);
            } else if (inspection.failure() == ArcaneCraftingTransaction.Failure.NO_RECIPE) {
                output = previewVanilla(player, items);
            }
        }

        resultSlot.set(output);
        visLine = vis;
        detailLine = detail;
        crystalState = crystals;
    }

    private ItemStack craftVanilla(ServerPlayer player, List<ItemStack> items, TerminalArcaneStore store) {
        if (!TEConfig.ARCANE_TERMINAL_VANILLA_RECIPES.get()) {
            return ItemStack.EMPTY;
        }
        ServerLevel level = player.level();
        CraftingInput input = CraftingInput.of(TerminalArcaneInput.GRID_SIZE, TerminalArcaneInput.GRID_SIZE, items);
        Optional<RecipeHolder<CraftingRecipe>> holder = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level);
        if (holder.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack output = holder.get().value().assemble(input);
        if (output.isEmpty()) {
            return ItemStack.EMPTY;
        }
        CommonHooks.setCraftingPlayer(player);
        NonNullList<ItemStack> remaining = holder.get().value().getRemainingItems(input);
        CommonHooks.setCraftingPlayer(null);
        if (!store.apply(items, remaining, AspectList.EMPTY)) {
            return ItemStack.EMPTY;
        }
        player.triggerRecipeCrafted(holder.get(), items);
        return finishCraft(player, output);
    }

    private ItemStack previewVanilla(ServerPlayer player, List<ItemStack> items) {
        if (!TEConfig.ARCANE_TERMINAL_VANILLA_RECIPES.get()) {
            return ItemStack.EMPTY;
        }
        ServerLevel level = player.level();
        CraftingInput input = CraftingInput.of(TerminalArcaneInput.GRID_SIZE, TerminalArcaneInput.GRID_SIZE, items);
        return level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level)
                .map(holder -> holder.value().assemble(input))
                .orElse(ItemStack.EMPTY);
    }

    private ItemStack finishCraft(ServerPlayer player, ItemStack output) {
        output.onCraftedBy(player, output.getCount());
        EventHooks.firePlayerCraftingEvent(player, output, craftingGrid.toContainer());
        refreshResult();
        return output;
    }

    private Component visLine(int baseVis, ArcaneCraftCost cost) {
        int required = cost.auraVis();
        if (required <= 0 && baseVis <= 0) {
            return Component.empty();
        }
        int available = Mth.floor(AuraHelper.getVis(terminal.getAuraLevel(), terminal.getAuraPos()));
        MutableComponent line = Component.translatable(ArcaneTerminalKeys.VIS, available, required);
        if (baseVis > required) {
            line.append(" ").append(Component.translatable(ArcaneTerminalKeys.DISCOUNT, (baseVis - required) * 100 / baseVis));
        }
        if (available < required) {
            line.withStyle(ChatFormatting.RED);
        }
        return line;
    }

    private Component crystalLine(ArcaneCraftCost cost, TerminalArcaneInput input) {
        AspectList needed = cost.crystalsNeeded();
        if (needed.isEmpty()) {
            return Component.empty();
        }
        AspectList available = input.availableCrystals();
        MutableComponent list = Component.empty();
        for (AspectInstance entry : needed.entries()) {
            if (!list.getSiblings().isEmpty()) {
                list.append(", ");
            }
            MutableComponent part = Component.literal(entry.amount() + " ").append(AspectComponents.name(entry.aspect()));
            if (available.amountOf(entry.aspect()) < entry.amount()) {
                part.withStyle(ChatFormatting.RED);
            }
            list.append(part);
        }
        return Component.translatable(ArcaneTerminalKeys.CRYSTALS, list);
    }

    private boolean hasCrystals(ArcaneCraftCost cost, TerminalArcaneInput input) {
        if (cost == null) {
            return true;
        }
        AspectList available = input.availableCrystals();
        for (AspectInstance entry : cost.crystalsNeeded().entries()) {
            if (available.amountOf(entry.aspect()) < entry.amount()) {
                return false;
            }
        }
        return true;
    }

    private String shortage(ArcaneCraftCost cost, TerminalArcaneInput input) {
        return hasCrystals(cost, input) ? ArcaneTerminalKeys.MESSAGE_VIS : ArcaneTerminalKeys.MESSAGE_CRYSTALS;
    }

    private void report(ServerPlayer player, String key) {
        player.sendOverlayMessage(Component.translatable(key));
    }

    private TerminalArcaneInput newInput(ServerPlayer player, List<ItemStack> items) {
        return new TerminalArcaneInput(items, player, () -> TerminalCrystals.available(network, crystalInventory), () -> wandInventory.getStackInSlot(0), terminal::getAuraPos);
    }

    private List<ItemStack> gridCopy() {
        List<ItemStack> items = new ArrayList<>(gridSlots.length);
        for (CraftingMatrixSlot slot : gridSlots) {
            items.add(slot.getItem().copy());
        }
        return items;
    }
}
