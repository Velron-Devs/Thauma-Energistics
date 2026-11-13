package velrondev.thaumaenergistics.behavior;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaItemStorage;
import com.leclowndu93150.thaumaturge.api.essentia.ItemEssentiaTransferResult;

import appeng.api.behaviors.ContainerItemStrategy;
import appeng.api.config.Actionable;
import appeng.api.stacks.GenericStack;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.api.key.EssentiaKey;

public class EssentiaContainerItemStrategy implements ContainerItemStrategy<EssentiaKey, EssentiaContainerItemStrategy.Context> {

    @Nullable
    private static IEssentiaItemStorage storage(ItemStack stack) {
        return stack.isEmpty() ? null : stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
    }

    @Nullable
    @Override
    public GenericStack getContainedStack(ItemStack stack) {
        IEssentiaItemStorage storage = storage(stack);
        if (storage == null) {
            return null;
        }
        for (AspectInstance entry : storage.contents().entries()) {
            if (entry.amount() > 0) {
                EssentiaKey key = EssentiaKey.of(entry.aspect());
                if (key != null) {
                    return new GenericStack(key, entry.amount());
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Context findCarriedContext(Player player, AbstractContainerMenu menu) {
        return storage(menu.getCarried()) == null ? null : new CarriedContext(player, menu);
    }

    @Nullable
    @Override
    public Context findPlayerSlotContext(Player player, int slot) {
        return storage(player.getInventory().getItem(slot)) == null ? null : new PlayerInventoryContext(player, slot);
    }

    @Override
    public long extract(Context context, EssentiaKey what, long amount, Actionable mode) {
        return transfer(context, what, amount, mode, false);
    }

    @Override
    public long insert(Context context, EssentiaKey what, long amount, Actionable mode) {
        return transfer(context, what, amount, mode, true);
    }

    @Override
    public void playFillSound(Player player, EssentiaKey what) {
        feedback(player, IEssentiaItemStorage.TransferDirection.FILL);
    }

    @Override
    public void playEmptySound(Player player, EssentiaKey what) {
        feedback(player, IEssentiaItemStorage.TransferDirection.DRAIN);
    }

    @Nullable
    @Override
    public GenericStack getExtractableContent(Context context) {
        return getContainedStack(context.getStack());
    }

    private static long transfer(Context context, EssentiaKey what, long amount, Actionable mode, boolean fill) {
        if (amount <= 0) {
            return 0;
        }
        ItemStack held = context.getStack();
        ItemStack single = held.copyWithCount(1);
        IEssentiaItemStorage storage = storage(single);
        Holder<IAspect> aspect = what.holder(context.player().level().registryAccess());
        if (storage == null || aspect == null) {
            return 0;
        }
        int requested = (int) Math.min(amount, Integer.MAX_VALUE);
        ItemEssentiaTransferResult result = fill ? storage.insert(aspect, requested) : storage.extract(aspect, requested);
        int moved = result.amountMoved();
        if (moved > 0 && mode == Actionable.MODULATE) {
            held.shrink(1);
            context.addOverflow(result.resultingStack());
        }
        return Math.max(moved, 0);
    }

    private static void feedback(Player player, IEssentiaItemStorage.TransferDirection direction) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu == null) {
            return;
        }
        IEssentiaItemStorage storage = storage(menu.getCarried());
        if (storage != null) {
            storage.playTransferFeedback(player, direction);
        }
    }

    public interface Context {

        Player player();

        ItemStack getStack();

        void addOverflow(ItemStack stack);
    }

    private record CarriedContext(Player player, AbstractContainerMenu menu) implements Context {

        @Override
        public ItemStack getStack() {
            return menu.getCarried();
        }

        @Override
        public void addOverflow(ItemStack stack) {
            if (menu.getCarried().isEmpty()) {
                menu.setCarried(stack);
            } else {
                player.getInventory().placeItemBackInInventory(stack);
            }
        }
    }

    private record PlayerInventoryContext(Player player, int slot) implements Context {

        @Override
        public ItemStack getStack() {
            return player.getInventory().getItem(slot);
        }

        @Override
        public void addOverflow(ItemStack stack) {
            player.getInventory().placeItemBackInInventory(stack);
        }
    }
}
