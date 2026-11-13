package velrondev.thaumaenergistics.menu;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Iterators;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Settings;
import appeng.api.config.StorageFilter;
import appeng.api.config.YesNo;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import appeng.api.util.IConfigManager;
import appeng.core.definitions.AEItems;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.UpgradeableMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import appeng.util.ConfigInventory;
import velrondev.thaumaenergistics.part.EssentiaStorageBusPart;

public class EssentiaStorageBusMenu extends UpgradeableMenu<EssentiaStorageBusPart> {

    private static final String ACTION_CLEAR = "clear";
    private static final String ACTION_PARTITION = "partition";
    private static final int CONFIG_COLUMNS = 9;
    private static final int BASE_ROWS = 2;
    private static final int OPTIONAL_ROWS = 5;

    @GuiSync(3)
    public AccessRestriction rwMode = AccessRestriction.READ_WRITE;

    @GuiSync(4)
    public StorageFilter storageFilter = StorageFilter.EXTRACTABLE_ONLY;

    @GuiSync(7)
    public YesNo filterOnExtract = YesNo.YES;

    @GuiSync(8)
    @Nullable
    public Component connectedTo;

    public EssentiaStorageBusMenu(MenuType<EssentiaStorageBusMenu> menuType, int id, Inventory playerInventory, EssentiaStorageBusPart host) {
        super(menuType, id, playerInventory, host);
        registerClientAction(ACTION_CLEAR, this::clear);
        registerClientAction(ACTION_PARTITION, this::partition);
        this.connectedTo = host.getConnectedToDescription();
    }

    @Override
    protected void setupConfig() {
        addExpandableConfigSlots(getHost().getConfig(), BASE_ROWS, CONFIG_COLUMNS, OPTIONAL_ROWS);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.connectedTo = getHost().getConnectedToDescription();
    }

    @Override
    protected void loadSettingsFromHost(IConfigManager manager) {
        this.rwMode = manager.getSetting(Settings.ACCESS);
        this.storageFilter = manager.getSetting(Settings.STORAGE_FILTER);
        this.filterOnExtract = manager.getSetting(Settings.FILTER_ON_EXTRACT);
    }

    @Override
    public boolean isSlotEnabled(int idx) {
        return getUpgrades().getInstalledUpgrades(AEItems.CAPACITY_CARD) > idx;
    }

    public void clear() {
        if (isClientSide()) {
            sendClientAction(ACTION_CLEAR);
            return;
        }
        getHost().getConfig().clear();
        broadcastChanges();
    }

    public void partition() {
        if (isClientSide()) {
            sendClientAction(ACTION_PARTITION);
            return;
        }

        ConfigInventory config = getHost().getConfig();
        MEStorage contents = getHost().getInternalHandler();

        Iterator<AEKey> keys = Collections.emptyIterator();
        if (contents != null) {
            keys = Iterators.transform(contents.getAvailableStacks().iterator(), Map.Entry::getKey);
        }

        config.beginBatch();
        try {
            for (int slot = 0; slot < config.size(); slot++) {
                if (keys.hasNext() && isSlotEnabled(slot / CONFIG_COLUMNS - BASE_ROWS)) {
                    config.setStack(slot, new GenericStack(keys.next(), 1));
                } else {
                    config.setStack(slot, null);
                }
            }
        } finally {
            config.endBatch();
        }
        broadcastChanges();
    }

    public AccessRestriction getReadWriteMode() {
        return rwMode;
    }

    public StorageFilter getStorageFilter() {
        return storageFilter;
    }

    public YesNo getFilterOnExtract() {
        return filterOnExtract;
    }

    @Nullable
    public Component getConnectedTo() {
        return connectedTo;
    }
}
