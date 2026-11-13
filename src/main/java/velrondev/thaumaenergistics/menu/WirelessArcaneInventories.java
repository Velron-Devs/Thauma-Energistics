package velrondev.thaumaenergistics.menu;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;

import appeng.api.inventories.InternalInventory;
import appeng.items.contents.StackDependentSupplier;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;
import appeng.util.inv.SupplierInternalInventory;
import appeng.util.inv.filter.IAEItemFilter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import velrondev.thaumaenergistics.crafting.TerminalArcaneInput;
import velrondev.thaumaenergistics.part.ArcaneTerminalPart;
import velrondev.thaumaenergistics.registry.TEDataComponents;

public final class WirelessArcaneInventories {

    private final SupplierInternalInventory<InternalInventory> craftingGrid;
    private final SupplierInternalInventory<InternalInventory> wand;
    private final SupplierInternalInventory<InternalInventory> crystals;

    public WirelessArcaneInventories(Player player, Supplier<ItemStack> terminal) {
        this.craftingGrid = inventory(player, terminal, TEDataComponents.ARCANE_CRAFTING_INV, TerminalArcaneInput.GRID_SLOTS, 64, null);
        this.wand = inventory(player, terminal, TEDataComponents.ARCANE_WAND_INV, 1, 1, ArcaneTerminalFilters.wand());
        this.crystals = inventory(player, terminal, TEDataComponents.ARCANE_CRYSTALS_INV, ArcaneCrystals.PRIMALS.size(), 64, ArcaneTerminalFilters.crystals());
    }

    private static SupplierInternalInventory<InternalInventory> inventory(Player player, Supplier<ItemStack> terminal,
            DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> component, int size, int maxStack, @Nullable IAEItemFilter filter) {
        return new SupplierInternalInventory<>(new StackDependentSupplier<>(terminal, stack -> create(player, stack, component.get(), size, maxStack, filter)));
    }

    private static InternalInventory create(Player player, ItemStack stack, DataComponentType<ItemContainerContents> component, int size, int maxStack, @Nullable IAEItemFilter filter) {
        InternalInventoryHost host = new InternalInventoryHost() {
            @Override
            public void saveChangedInventory(AppEngInternalInventory inv) {
                stack.set(component, inv.toItemContainerContents());
            }

            @Override
            public boolean isClientSide() {
                return player.level().isClientSide();
            }
        };
        AppEngInternalInventory inventory = filter == null ? new AppEngInternalInventory(host, size, maxStack) : new AppEngInternalInventory(host, size, maxStack, filter);
        inventory.fromItemContainerContents(stack.getOrDefault(component, ItemContainerContents.EMPTY));
        return inventory;
    }

    @Nullable
    public InternalInventory get(Identifier id) {
        if (id.equals(ArcaneTerminalPart.INV_CRAFTING)) {
            return craftingGrid;
        }
        if (id.equals(ArcaneTerminalPart.INV_WAND)) {
            return wand;
        }
        if (id.equals(ArcaneTerminalPart.INV_CRYSTALS)) {
            return crystals;
        }
        return null;
    }

    public static ArcaneWorkbenchContext context(ServerPlayer player) {
        UUID host = UUID.nameUUIDFromBytes(("wireless_arcane_terminal/" + player.getUUID()).getBytes(StandardCharsets.UTF_8));
        return ArcaneWorkbenchContext.virtual(player, host, player.getUUID());
    }
}
