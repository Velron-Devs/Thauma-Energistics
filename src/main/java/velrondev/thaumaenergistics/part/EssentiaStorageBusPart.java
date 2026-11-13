package velrondev.thaumaenergistics.part;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.config.AccessRestriction;
import appeng.api.config.IncludeExclude;
import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.api.config.StorageFilter;
import appeng.api.config.YesNo;
import appeng.api.ids.AEComponents;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import appeng.api.util.AECableType;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigManagerBuilder;
import appeng.core.definitions.AEItems;
import appeng.core.settings.TickRates;
import appeng.helpers.IConfigInvHost;
import appeng.helpers.IPriorityHost;
import appeng.items.parts.PartModels;
import appeng.me.storage.CompositeStorage;
import appeng.me.storage.ITickingMonitor;
import appeng.me.storage.MEInventoryHandler;
import appeng.me.storage.NullInventory;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.PartAdjacentApi;
import appeng.parts.PartModel;
import appeng.parts.automation.StackWorldBehaviors;
import appeng.parts.automation.UpgradeablePart;
import appeng.util.ConfigInventory;
import appeng.util.Platform;
import appeng.util.SettingsFrom;
import appeng.util.prioritylist.IPartitionList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.ICapabilityInvalidationListener;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.config.TUUConfig;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;
import velrondev.thaumaenergistics.registry.TUUMenus;

public class EssentiaStorageBusPart extends UpgradeablePart implements IGridTickable, IStorageProvider, IPriorityHost, IConfigInvHost {

    public static final int FILTER_SLOTS = 63;
    public static final int BASE_FILTER_SLOTS = 18;
    public static final int SLOTS_PER_CAPACITY_CARD = 9;

    public static final ResourceLocation MODEL_BASE = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_storage_bus_base");

    @PartModels
    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_storage_bus_off"));
    @PartModels
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_storage_bus_on"));
    @PartModels
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_storage_bus_has_channel"));

    private static final String PRIORITY_TAG = "priority";
    private static final String CONFIG_TAG = "config";

    private final ConfigInventory config = ConfigInventory.configTypes(FILTER_SLOTS)
            .supportedType(TUUKeyTypes.essentia())
            .changeListener(this::onConfigurationChanged)
            .build();
    private final BusInventory handler = new BusInventory(NullInventory.of());
    private Map<AEKeyType, ExternalStorageStrategy> strategies;
    private Component handlerDescription;
    private boolean wasOnline;
    private int priority;
    private UpdateStatus updateStatus = UpdateStatus.FAST;
    private ITickingMonitor monitor;

    private final ICapabilityInvalidationListener capabilityListener = () -> {
        if (!PartAdjacentApi.isPartValid(this)) {
            return false;
        }
        onCapabilityInvalidation();
        return true;
    };

    public EssentiaStorageBusPart(IPartItem<?> partItem) {
        super(partItem);
        getMainNode()
                .addService(IStorageProvider.class, this)
                .addService(IGridTickable.class, this);
    }

    @Override
    protected void registerSettings(IConfigManagerBuilder builder) {
        super.registerSettings(builder);
        builder.registerSetting(Settings.ACCESS, AccessRestriction.READ_WRITE);
        builder.registerSetting(Settings.STORAGE_FILTER, StorageFilter.EXTRACTABLE_ONLY);
        builder.registerSetting(Settings.FILTER_ON_EXTRACT, YesNo.YES);
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        if (getLevel() instanceof ServerLevel serverLevel) {
            BlockPos target = getBlockEntity().getBlockPos().relative(getSide());
            serverLevel.registerCapabilityListener(target, capabilityListener);
        }
    }

    @Override
    protected final void onMainNodeStateChanged(IGridNodeListener.State reason) {
        boolean online = getMainNode().isOnline();
        if (wasOnline != online) {
            wasOnline = online;
            getHost().markForUpdate();
            remountStorage();
        }
    }

    private void remountStorage() {
        IStorageProvider.requestUpdate(getMainNode());
    }

    @Override
    public void onSettingChanged(IConfigManager manager, Setting<?> setting) {
        onConfigurationChanged();
        getHost().markForSave();
    }

    @Override
    public final void upgradesChanged() {
        super.upgradesChanged();
        onConfigurationChanged();
    }

    private void scheduleUpdate() {
        if (isClientSide()) {
            return;
        }
        updateStatus = UpdateStatus.FAST;
        getMainNode().ifPresent((grid, node) -> grid.getTickManager().alertDevice(node));
    }

    @Override
    public void readFromNBT(CompoundTag data, HolderLookup.Provider registries) {
        super.readFromNBT(data, registries);
        priority = data.getInt(PRIORITY_TAG);
        config.readFromChildTag(data, CONFIG_TAG, registries);
    }

    @Override
    public void writeToNBT(CompoundTag data, HolderLookup.Provider registries) {
        super.writeToNBT(data, registries);
        data.putInt(PRIORITY_TAG, priority);
        config.writeToChildTag(data, CONFIG_TAG, registries);
    }

    @Override
    public final boolean onUseWithoutItem(Player player, Vec3 pos) {
        if (!isClientSide()) {
            MenuOpener.open(getMenuType(), player, MenuLocators.forPart(this));
        }
        return true;
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.returnTo(getMenuType(), player, MenuLocators.forPart(this));
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(getPartItem());
    }

    public MenuType<?> getMenuType() {
        return TUUMenus.ESSENTIA_STORAGE_BUS.get();
    }

    @Override
    public final void getBoxes(IPartCollisionHelper helper) {
        helper.addBox(3, 3, 15, 13, 13, 16);
        helper.addBox(2, 2, 14, 14, 14, 15);
        helper.addBox(5, 5, 12, 11, 11, 14);
    }

    @Override
    protected final int getUpgradeSlots() {
        return 5;
    }

    @Override
    public final float getCableConnectionLength(AECableType cable) {
        return 4;
    }

    @Override
    public final void onNeighborChanged(BlockGetter level, BlockPos pos, BlockPos neighbor) {
        if (pos.relative(getSide()).equals(neighbor) && !isClientSide()) {
            getMainNode().ifPresent((grid, node) -> grid.getTickManager().alertDevice(node));
        }
    }

    @Override
    public final TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(TickRates.StorageBus, false);
    }

    @Override
    public final TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (updateStatus != UpdateStatus.NONE) {
            updateTarget(false);
        }
        if (monitor != null) {
            return monitor.onTick();
        }
        return updateStatus == UpdateStatus.SLOW ? TickRateModulation.IDLE : TickRateModulation.SLEEP;
    }

    public MEStorage getInternalHandler() {
        return handler.delegate();
    }

    public Component getConnectedToDescription() {
        return handlerDescription;
    }

    private boolean isMounted() {
        return TUUConfig.ESSENTIA_STORAGE_BUS_ENABLED.get() && getMainNode().isOnline() && !(handler.delegate() instanceof NullInventory);
    }

    private void onConfigurationChanged() {
        if (getMainNode().isReady()) {
            updateTarget(true);
        }
    }

    private void onCapabilityInvalidation() {
        handler.replaceDelegate(NullInventory.of());
        scheduleUpdate();
    }

    private void updateTarget(boolean forceFullUpdate) {
        if (isClientSide()) {
            return;
        }

        Map<AEKeyType, MEStorage> found = Collections.emptyMap();
        if (Platform.areBlockEntitiesTicking(getLevel(), getBlockEntity().getBlockPos().relative(getSide()))) {
            updateStatus = UpdateStatus.NONE;
            found = new IdentityHashMap<>(1);
            findStorages(found);
        } else {
            updateStatus = UpdateStatus.SLOW;
        }

        if (!forceFullUpdate && handler.delegate() instanceof CompositeStorage composite && !found.isEmpty()) {
            composite.setStorages(found);
            handlerDescription = composite.getDescription();
            return;
        }

        boolean wasSleeping = monitor == null;
        boolean wasMounted = isMounted();

        MEStorage inventory;
        if (found.isEmpty()) {
            inventory = NullInventory.of();
            handlerDescription = null;
        } else {
            inventory = new CompositeStorage(found);
            handlerDescription = inventory.getDescription();
        }
        handler.replaceDelegate(inventory);

        AccessRestriction access = getConfigManager().getSetting(Settings.ACCESS);
        handler.setAllowExtraction(access.isAllowExtraction());
        handler.setAllowInsertion(access.isAllowInsertion());
        handler.setWhitelist(isUpgradedWith(AEItems.INVERTER_CARD) ? IncludeExclude.BLACKLIST : IncludeExclude.WHITELIST);
        handler.setPartitionList(createFilter());
        handler.setVoidOverflow(isUpgradedWith(AEItems.VOID_CARD));

        boolean filterOnExtract = getConfigManager().getSetting(Settings.FILTER_ON_EXTRACT) == YesNo.YES;
        handler.setExtractFiltering(filterOnExtract, isExtractableOnly() && filterOnExtract);

        monitor = inventory instanceof ITickingMonitor ticking ? ticking : null;

        if (wasSleeping != (monitor == null)) {
            getMainNode().ifPresent((grid, node) -> {
                if (monitor == null) {
                    grid.getTickManager().sleepDevice(node);
                } else {
                    grid.getTickManager().wakeDevice(node);
                }
            });
        }

        if (wasMounted != isMounted()) {
            remountStorage();
        }
    }

    private boolean isExtractableOnly() {
        return getConfigManager().getSetting(Settings.STORAGE_FILTER) == StorageFilter.EXTRACTABLE_ONLY;
    }

    private IPartitionList createFilter() {
        IPartitionList.Builder builder = IPartitionList.builder();
        int slots = BASE_FILTER_SLOTS + getInstalledUpgrades(AEItems.CAPACITY_CARD) * SLOTS_PER_CAPACITY_CARD;
        for (int i = 0; i < config.size() && i < slots; i++) {
            builder.add(config.getKey(i));
        }
        return builder.build();
    }

    private void findStorages(Map<AEKeyType, MEStorage> storages) {
        boolean extractableOnly = isExtractableOnly();
        for (Map.Entry<AEKeyType, ExternalStorageStrategy> entry : strategies().entrySet()) {
            if (entry.getKey() != TUUKeyTypes.essentia()) {
                continue;
            }
            MEStorage wrapper = entry.getValue().createWrapper(extractableOnly, this::alertOnExternalChange);
            if (wrapper != null) {
                storages.put(entry.getKey(), wrapper);
            }
        }
    }

    private void alertOnExternalChange() {
        getMainNode().ifPresent((grid, node) -> grid.getTickManager().alertDevice(node));
    }

    private Map<AEKeyType, ExternalStorageStrategy> strategies() {
        if (strategies == null) {
            var host = getHost().getBlockEntity();
            strategies = StackWorldBehaviors.createExternalStorageStrategies(
                    (ServerLevel) host.getLevel(),
                    host.getBlockPos().relative(getSide()),
                    getSide().getOpposite());
        }
        return strategies;
    }

    @Override
    public void mountInventories(IStorageMounts mounts) {
        if (isMounted()) {
            mounts.mount(handler, priority);
        }
    }

    @Override
    public final int getPriority() {
        return priority;
    }

    @Override
    public final void setPriority(int newValue) {
        priority = newValue;
        getHost().markForSave();
        remountStorage();
    }

    @Override
    public ConfigInventory getConfig() {
        return config;
    }

    @Override
    public void importSettings(SettingsFrom mode, DataComponentMap input, @Nullable Player player) {
        super.importSettings(mode, input, player);
        var exported = input.get(AEComponents.EXPORTED_CONFIG_INV);
        if (exported != null) {
            config.readFromList(exported);
        }
    }

    @Override
    public void exportSettings(SettingsFrom mode, DataComponentMap.Builder builder) {
        super.exportSettings(mode, builder);
        if (mode == SettingsFrom.MEMORY_CARD) {
            builder.set(AEComponents.EXPORTED_CONFIG_INV, config.toList());
        }
    }

    @Override
    public IPartModel getStaticModels() {
        if (isActive() && isPowered()) {
            return MODELS_HAS_CHANNEL;
        } else if (isPowered()) {
            return MODELS_ON;
        }
        return MODELS_OFF;
    }

    private static final class BusInventory extends MEInventoryHandler {

        private BusInventory(MEStorage inventory) {
            super(inventory);
        }

        private MEStorage delegate() {
            return getDelegate();
        }

        private void replaceDelegate(MEStorage storage) {
            setDelegate(storage);
        }
    }

    private enum UpdateStatus {
        FAST,
        SLOW,
        NONE
    }
}
