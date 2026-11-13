package velrondev.thaumaenergistics.part;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import appeng.api.behaviors.StackExportStrategy;
import appeng.api.config.SchedulingMode;
import appeng.api.config.Settings;
import appeng.api.networking.IGrid;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEKey;
import appeng.api.util.IConfigManagerBuilder;
import appeng.core.settings.TickRates;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.parts.automation.IOBusPart;
import appeng.util.prioritylist.DefaultPriorityList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.behavior.EssentiaExportStrategy;
import velrondev.thaumaenergistics.behavior.EssentiaTransferContext;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;
import velrondev.thaumaenergistics.registry.TUUMenus;

public class EssentiaExportBusPart extends IOBusPart {

    public static final ResourceLocation ESSENTIA_BASE = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_export_bus_base");

    public static final ResourceLocation ESSENTIA_OFF = ResourceLocation.fromNamespaceAndPath("ae2", "part/export_bus_off");
    public static final ResourceLocation ESSENTIA_ON = ResourceLocation.fromNamespaceAndPath("ae2", "part/export_bus_on");
    public static final ResourceLocation ESSENTIA_HAS_CHANNEL = ResourceLocation.fromNamespaceAndPath("ae2", "part/export_bus_has_channel");

    @PartModels
    public static final IPartModel ESSENTIA_MODELS_OFF = new PartModel(ESSENTIA_BASE, ESSENTIA_OFF);
    @PartModels
    public static final IPartModel ESSENTIA_MODELS_ON = new PartModel(ESSENTIA_BASE, ESSENTIA_ON);
    @PartModels
    public static final IPartModel ESSENTIA_MODELS_HAS_CHANNEL = new PartModel(ESSENTIA_BASE, ESSENTIA_HAS_CHANNEL);

    private static final String NEXT_SLOT_TAG = "nextSlot";

    @Nullable
    private StackExportStrategy strategy;
    private int nextSlot;

    public EssentiaExportBusPart(IPartItem<?> partItem) {
        super(TickRates.ExportBus, Set.of(TUUKeyTypes.essentia()), partItem);
    }

    @Override
    protected void registerSettings(IConfigManagerBuilder builder) {
        super.registerSettings(builder);
        builder.registerSetting(Settings.SCHEDULING_MODE, SchedulingMode.DEFAULT);
    }

    @Override
    public void readFromNBT(CompoundTag data, HolderLookup.Provider registries) {
        super.readFromNBT(data, registries);
        nextSlot = data.getInt(NEXT_SLOT_TAG);
    }

    @Override
    public void writeToNBT(CompoundTag data, HolderLookup.Provider registries) {
        super.writeToNBT(data, registries);
        data.putInt(NEXT_SLOT_TAG, nextSlot);
    }

    private StackExportStrategy strategy() {
        if (strategy == null) {
            BlockPos target = getHost().getBlockEntity().getBlockPos().relative(getSide());
            strategy = new EssentiaExportStrategy((ServerLevel) getLevel(), target, getSide().getOpposite());
        }
        return strategy;
    }

    @Override
    protected boolean doBusWork(IGrid grid) {
        SchedulingMode mode = getConfigManager().getSetting(Settings.SCHEDULING_MODE);
        EssentiaTransferContext context = new EssentiaTransferContext(
                grid.getStorageService(),
                grid.getEnergyService(),
                source,
                getOperationsPerTick(),
                DefaultPriorityList.INSTANCE);

        int slots = availableSlots();
        int visited = 0;
        while (visited < slots && context.hasOperationsLeft()) {
            AEKey what = getConfig().getKey(startingSlot(mode, visited, slots));
            visited++;
            if (what == null) {
                continue;
            }
            long factor = what.getAmountPerOperation();
            long moved = strategy().transfer(context, what, context.getOperationsRemaining() * factor);
            if (moved > 0) {
                context.reduceOperationsRemaining(Math.max(1, moved / factor));
            }
        }

        if (context.hasDoneWork() && mode == SchedulingMode.ROUNDROBIN) {
            nextSlot = (nextSlot + visited) % slots;
        }
        return context.hasDoneWork();
    }

    private int startingSlot(SchedulingMode mode, int offset, int slots) {
        if (mode == SchedulingMode.RANDOM) {
            return getLevel().getRandom().nextInt(slots);
        }
        if (mode == SchedulingMode.ROUNDROBIN) {
            return (nextSlot + offset) % slots;
        }
        return offset;
    }

    @Override
    protected MenuType<?> getMenuType() {
        return TUUMenus.ESSENTIA_EXPORT_BUS.get();
    }

    @Override
    public void getBoxes(IPartCollisionHelper helper) {
        helper.addBox(4, 4, 12, 12, 12, 14);
        helper.addBox(5, 5, 14, 11, 11, 15);
        helper.addBox(6, 6, 15, 10, 10, 16);
        helper.addBox(6, 6, 11, 10, 10, 12);
    }

    @Override
    public IPartModel getStaticModels() {
        if (isActive() && isPowered()) {
            return ESSENTIA_MODELS_HAS_CHANNEL;
        }
        return isPowered() ? ESSENTIA_MODELS_ON : ESSENTIA_MODELS_OFF;
    }
}
