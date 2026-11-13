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
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.parts.reporting.AbstractTerminalPart;
import appeng.util.inv.AppEngInternalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.config.TEConfig;
import velrondev.thaumaenergistics.crafting.TerminalArcaneInput;
import velrondev.thaumaenergistics.crafting.TerminalWandCharger;
import velrondev.thaumaenergistics.menu.ArcaneCrystals;
import velrondev.thaumaenergistics.menu.ArcaneTerminalFilters;
import velrondev.thaumaenergistics.menu.ArcaneTerminalHost;
import velrondev.thaumaenergistics.registry.TEItems;
import velrondev.thaumaenergistics.registry.TEMenus;

public class ArcaneTerminalPart extends AbstractTerminalPart implements ArcaneTerminalHost, IGridTickable {

    public static final Identifier INV_CRAFTING = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "arcane_terminal_crafting");

    public static final Identifier INV_WAND = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "arcane_terminal_wand");

    public static final Identifier INV_CRYSTALS = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "arcane_terminal_crystals");

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
        int interval = TEConfig.ARCANE_CHARGE_INTERVAL_TICKS.get();
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
        return TEConfig.ARCANE_CHARGE_ENABLED.get() && upgrades.isInstalled(TEItems.UPGRADE_ARCANE.get());
    }

    @Override
    public ArcaneWorkbenchContext context(ServerPlayer player) {
        BlockPos pos = getBlockEntity().getBlockPos();
        ServerLevel level = (ServerLevel) getLevel();
        String identity = level.dimension().identifier() + "/" + pos.asLong() + "/" + getSide();
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
    public void readFromNBT(ValueInput data) {
        super.readFromNBT(data);
        craftingGrid.readFromNBT(data, CRAFTING_TAG);
        wand.readFromNBT(data, WAND_TAG);
        crystals.readFromNBT(data, CRYSTALS_TAG);
        upgrades.readFromNBT(data, UPGRADES_TAG);
    }

    @Override
    public void writeToNBT(ValueOutput data) {
        super.writeToNBT(data);
        craftingGrid.writeToNBT(data, CRAFTING_TAG);
        wand.writeToNBT(data, WAND_TAG);
        crystals.writeToNBT(data, CRYSTALS_TAG);
        upgrades.writeToNBT(data, UPGRADES_TAG);
    }

    @Override
    public MenuType<?> getMenuType(Player player) {
        return TEMenus.ARCANE_TERMINAL.get();
    }

    @Override
    public InternalInventory getSubInventory(Identifier id) {
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
}
