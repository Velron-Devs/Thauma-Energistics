package velrondev.thaumaenergistics.part;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;

import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractTerminalPart;
import appeng.util.inv.AppEngInternalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.config.TUUConfig;
import velrondev.thaumaenergistics.crafting.TerminalArcaneInput;
import velrondev.thaumaenergistics.crafting.TerminalWandCharger;
import velrondev.thaumaenergistics.menu.ArcaneCrystals;
import velrondev.thaumaenergistics.menu.ArcaneTerminalFilters;
import velrondev.thaumaenergistics.menu.ArcaneTerminalHost;
import velrondev.thaumaenergistics.registry.TUUItems;
import velrondev.thaumaenergistics.registry.TUUMenus;

public class ArcaneTerminalPart extends AbstractTerminalPart implements ArcaneTerminalHost, IGridTickable {

    public static final ResourceLocation INV_CRAFTING = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "arcane_terminal_crafting");

    public static final ResourceLocation INV_WAND = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "arcane_terminal_wand");

    public static final ResourceLocation INV_CRYSTALS = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "arcane_terminal_crystals");

    @PartModels
    public static final ResourceLocation MODEL_OFF = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/arcane_terminal_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/arcane_terminal_on");

    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

    private static final String CRAFTING_TAG = "arcaneCraftingGrid";

    private static final String WAND_TAG = "arcaneWand";

    private static final String CRYSTALS_TAG = "arcaneCrystals";

    private static final String UPGRADES_TAG = "upgrades";

    private static final int UPGRADE_SLOTS = 1;

    private final AppEngInternalInventory craftingGrid = new AppEngInternalInventory(this, TerminalArcaneInput.GRID_SLOTS);
    private final AppEngInternalInventory wand = new AppEngInternalInventory(this, 1, 1, ArcaneTerminalFilters.wand());
    private final AppEngInternalInventory crystals = new AppEngInternalInventory(this, ArcaneCrystals.PRIMALS.size(), 64, ArcaneTerminalFilters.crystals());
    private final IUpgradeInventory upgrades;

    public ArcaneTerminalPart(IPartItem<?> partItem) {
        super(partItem);
        this.upgrades = UpgradeInventories.forMachine(partItem.asItem(), UPGRADE_SLOTS, this::onUpgradesChanged);
        getMainNode().addService(IGridTickable.class, this);
    }

    private void onUpgradesChanged() {
        saveChanges();
        getMainNode().ifPresent((grid, node) -> grid.getTickManager().wakeDevice(node));
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return upgrades;
    }

    @Override
    public boolean chargesWandOnTick() {
        return true;
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        int interval = TUUConfig.ARCANE_CHARGE_INTERVAL_TICKS.get();
        return new TickingRequest(interval, interval, !isCharging());
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (!isCharging()) {
            return TickRateModulation.SLEEP;
        }
        if (getMainNode().isActive() && getLevel() != null) {
            ItemStack stack = wand.getStackInSlot(0);
            if (TerminalWandCharger.charge(getLevel(), getAuraPos(), stack) > 0) {
                wand.setItemDirect(0, stack);
            }
        }
        return TickRateModulation.SAME;
    }

    private boolean isCharging() {
        return TUUConfig.ARCANE_CHARGE_ENABLED.get() && upgrades.isInstalled(TUUItems.UPGRADE_ARCANE.get());
    }

    @Override
    public ArcaneWorkbenchContext context(ServerPlayer player) {
        BlockPos pos = getBlockEntity().getBlockPos();
        ServerLevel level = (ServerLevel) getLevel();
        String identity = level.dimension().location() + "/" + pos.asLong() + "/" + getSide();
        UUID host = UUID.nameUUIDFromBytes(identity.getBytes(StandardCharsets.UTF_8));
        return new ArcaneWorkbenchContext(level, host, null, player.getUUID(), player.getUUID(), ArcaneWorkbenchContext.Kind.VIRTUAL);
    }

    @Override
    public Level getAuraLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getAuraPos() {
        return getBlockEntity().getBlockPos();
    }

    @Override
    public void addAdditionalDrops(List<ItemStack> drops, boolean wrenched) {
        super.addAdditionalDrops(drops, wrenched);
        for (ItemStack stack : craftingGrid) {
            if (!stack.isEmpty()) {
                drops.add(stack);
            }
        }
        for (ItemStack stack : wand) {
            if (!stack.isEmpty()) {
                drops.add(stack);
            }
        }
        for (ItemStack stack : crystals) {
            if (!stack.isEmpty()) {
                drops.add(stack);
            }
        }
        for (ItemStack stack : upgrades) {
            if (!stack.isEmpty()) {
                drops.add(stack);
            }
        }
    }

    @Override
    public void clearContent() {
        super.clearContent();
        craftingGrid.clear();
        wand.clear();
        crystals.clear();
        upgrades.clear();
    }

    @Override
    public void readFromNBT(CompoundTag data, HolderLookup.Provider registries) {
        super.readFromNBT(data, registries);
        craftingGrid.readFromNBT(data, CRAFTING_TAG, registries);
        wand.readFromNBT(data, WAND_TAG, registries);
        crystals.readFromNBT(data, CRYSTALS_TAG, registries);
        upgrades.readFromNBT(data, UPGRADES_TAG, registries);
    }

    @Override
    public void writeToNBT(CompoundTag data, HolderLookup.Provider registries) {
        super.writeToNBT(data, registries);
        craftingGrid.writeToNBT(data, CRAFTING_TAG, registries);
        wand.writeToNBT(data, WAND_TAG, registries);
        crystals.writeToNBT(data, CRYSTALS_TAG, registries);
        upgrades.writeToNBT(data, UPGRADES_TAG, registries);
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return TUUMenus.ARCANE_TERMINAL.get();
    }

    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        if (id.equals(INV_CRAFTING)) {
            return craftingGrid;
        }
        if (id.equals(INV_WAND)) {
            return wand;
        }
        if (id.equals(INV_CRYSTALS)) {
            return crystals;
        }
        if (id.equals(UPGRADES)) {
            return upgrades;
        }
        return super.getSubInventory(id);
    }

    @Override
    public IPartModel getStaticModels() {
        return selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
    }
}
