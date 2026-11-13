package velrondev.thaumaenergistics.compat.extendedae.infinity;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.items.storage.StorageCellTooltipComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.extendedae.client.InfiniteCellClientRegistries;

public class InfiniteCellItem extends Item {

    public static final String NAME_KEY = "item." + TUU.MOD_ID + ".infinity_cell_name";
    public static final String TOOLTIP_KEY = "item." + TUU.MOD_ID + ".infinity_cell.tooltip";

    private final InfiniteTarget target;
    private final Supplier<Double> idleDrain;
    private HolderLookup.Provider cachedRegistries;
    private AEKey cachedKey;

    public InfiniteCellItem(InfiniteTarget target, Supplier<Double> idleDrain) {
        super(new Properties().stacksTo(1));
        this.target = target;
        this.idleDrain = idleDrain;
    }

    public InfiniteTarget target() {
        return target;
    }

    public double idleDrain() {
        return idleDrain.get();
    }

    public static long infiniteAmount(AEKey key) {
        return (long) Integer.MAX_VALUE * key.getAmountPerUnit();
    }

    @Nullable
    public synchronized AEKey resolve() {
        HolderLookup.Provider registries = currentRegistries();
        if (registries == null) {
            return null;
        }
        if (registries != cachedRegistries) {
            cachedKey = target.resolver().apply(registries);
            cachedRegistries = registries;
        }
        return cachedKey;
    }

    @Nullable
    private static HolderLookup.Provider currentRegistries() {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            return server.registryAccess();
        }
        return FMLEnvironment.dist == Dist.CLIENT ? InfiniteCellClientRegistries.current() : null;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(NAME_KEY, target.name());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable(TOOLTIP_KEY).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        AEKey key = resolve();
        if (key == null) {
            return Optional.empty();
        }
        return Optional.of(new StorageCellTooltipComponent(List.of(), List.of(new GenericStack(key, infiniteAmount(key))), false, true));
    }
}
