package velrondev.thaumaenergistics.crafting;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.IArcaneCraftingStore;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import velrondev.thaumaenergistics.config.TEConfig;

public final class TerminalArcaneStore implements IArcaneCraftingStore {

    private static final double POWER_TOLERANCE = 0.9;

    private final ServerPlayer player;
    private final InternalInventory grid;
    private final InternalInventory crystalSlots;
    private final MEStorage network;
    private final IEnergySource energy;
    private final IActionSource source;

    public TerminalArcaneStore(ServerPlayer player, InternalInventory grid, InternalInventory crystalSlots, MEStorage network, IEnergySource energy, IActionSource source) {
        this.player = player;
        this.grid = grid;
        this.crystalSlots = crystalSlots;
        this.network = network;
        this.energy = energy;
        this.source = source;
    }

    @Override
    public boolean consume(Consumption consumption, TransactionContext transaction) {
        return apply(consumption.grid(), consumption.remainders(), consumption.crystals());
    }

    public boolean apply(List<ItemStack> expected, List<ItemStack> remainders, AspectList crystals) {
        for (int slot = 0; slot < grid.size(); slot++) {
            ItemStack wanted = slot < expected.size() ? expected.get(slot) : ItemStack.EMPTY;
            if (!wanted.isEmpty() && !ItemStack.isSameItemSameComponents(wanted, grid.getStackInSlot(slot))) {
                return false;
            }
        }

        List<TerminalCrystals.Take> takes = List.of();
        if (!crystals.isEmpty()) {
            AspectList fromNetwork = TerminalCrystals.drawFromSlots(crystalSlots, crystals, false);
            if (!fromNetwork.isEmpty()) {
                takes = TerminalCrystals.plan(network, source, fromNetwork);
                if (takes == null) {
                    return false;
                }
            }
            TerminalCrystals.drawFromSlots(crystalSlots, crystals, true);
        }
        for (TerminalCrystals.Take take : takes) {
            network.extract(take.key(), take.amount(), Actionable.MODULATE, source);
        }

        for (int slot = 0; slot < grid.size(); slot++) {
            ItemStack stack = grid.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack remainder = slot < remainders.size() ? remainders.get(slot).copy() : ItemStack.EMPTY;
            if (stack.getCount() > 1) {
                grid.extractItem(slot, 1, false);
            } else if (!refill(stack)) {
                grid.setItemDirect(slot, ItemStack.EMPTY);
            }
            if (!remainder.isEmpty()) {
                if (grid.getStackInSlot(slot).isEmpty()) {
                    grid.setItemDirect(slot, remainder);
                } else {
                    store(remainder);
                }
            }
        }
        return true;
    }

    private boolean refill(ItemStack stack) {
        double power = TEConfig.ARCANE_TERMINAL_POWER_PER_INGREDIENT.get();
        if (power > 0 && energy.extractAEPower(power, Actionable.SIMULATE, PowerMultiplier.CONFIG) < power * POWER_TOLERANCE) {
            return false;
        }
        long extracted = network.extract(AEItemKey.of(stack), 1, Actionable.MODULATE, source);
        if (extracted <= 0) {
            return false;
        }
        if (power > 0) {
            energy.extractAEPower(power, Actionable.MODULATE, PowerMultiplier.CONFIG);
        }
        return true;
    }

    private void store(ItemStack stack) {
        long inserted = network.insert(AEItemKey.of(stack), stack.getCount(), Actionable.MODULATE, source);
        int left = (int) (stack.getCount() - inserted);
        if (left > 0) {
            ItemStack rest = stack.copyWithCount(left);
            if (!player.getInventory().add(rest)) {
                player.drop(rest, false);
            }
        }
    }
}
