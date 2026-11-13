package velrondev.thaumaenergistics.blockentity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.IAspectSource;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.features.IPlayerRegistry;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.util.AECableType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.api.key.EssentiaKey;
import velrondev.thaumaenergistics.config.TEConfig;
import velrondev.thaumaenergistics.registry.TEBlockEntities;
import velrondev.thaumaenergistics.registry.TEBlocks;

public class InfusionProviderBlockEntity extends BlockEntity implements IInWorldGridNodeHost, IActionHost, IAspectSource {

    private static final String SNAPSHOT_TAG = "snapshot";
    private static final String GRID_TAG = "proxy";

    private static final IGridNodeListener<InfusionProviderBlockEntity> LISTENER = new IGridNodeListener<>() {
        @Override
        public void onSaveChanges(InfusionProviderBlockEntity owner, IGridNode node) {
            owner.setChanged();
        }
    };

    private final IManagedGridNode mainNode = GridHelper.createManagedNode(this, LISTENER)
            .setVisualRepresentation(TEBlocks.INFUSION_PROVIDER_ITEM.get())
            .setInWorldNode(true)
            .setTagName(GRID_TAG)
            .setIdlePowerUsage(TEConfig.INFUSION_PROVIDER_IDLE_POWER.get())
            .setExposedOnSides(EnumSet.allOf(Direction.class));

    private final IActionSource source = IActionSource.ofMachine(this);

    private AspectList snapshot = AspectList.EMPTY;
    private int syncTicks;

    public InfusionProviderBlockEntity(BlockPos pos, BlockState state) {
        super(TEBlockEntities.INFUSION_PROVIDER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, InfusionProviderBlockEntity provider) {
        provider.tickServer(level);
    }

    private void tickServer(Level level) {
        if (++syncTicks < TEConfig.INFUSION_PROVIDER_SYNC_INTERVAL.get()) {
            return;
        }
        syncTicks = 0;
        AspectList current = readNetwork(level);
        if (!current.equals(snapshot)) {
            snapshot = current;
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void setOwner(LivingEntity placer) {
        if (placer instanceof ServerPlayer player) {
            mainNode.setOwningPlayerId(IPlayerRegistry.getPlayerId(player));
            setChanged();
        }
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        GridHelper.onFirstTick(this, provider -> provider.mainNode.create(provider.getLevel(), provider.getBlockPos()));
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        mainNode.destroy();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        mainNode.destroy();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        mainNode.deserialize(input);
        snapshot = input.read(SNAPSHOT_TAG, AspectList.CODEC).orElse(AspectList.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        mainNode.serialize(output);
        writeSnapshot(output);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        writeSnapshot(output);
        return output.buildResult();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void writeSnapshot(ValueOutput output) {
        if (!snapshot.isEmpty()) {
            output.store(SNAPSHOT_TAG, AspectList.CODEC, snapshot);
        }
    }

    @Nullable
    @Override
    public IGridNode getGridNode(Direction dir) {
        return mainNode.getNode();
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    @Nullable
    @Override
    public IGridNode getActionableNode() {
        return mainNode.getNode();
    }

    @Override
    public int getSourcePriority() {
        return TEConfig.INFUSION_PROVIDER_PRIORITY.get();
    }

    @Override
    public boolean isBlocked() {
        return !TEConfig.INFUSION_PROVIDER_ENABLED.get() || !mainNode.isActive();
    }

    @Override
    public AspectList getAspects() {
        Level level = getLevel();
        if (level == null) {
            return AspectList.EMPTY;
        }
        if (level.isClientSide()) {
            return snapshot;
        }
        return readNetwork(level);
    }

    @Override
    public void setAspects(AspectList aspects) {
    }

    @Override
    public boolean accepts(Holder<IAspect> aspect) {
        return false;
    }

    @Override
    public int fill(Holder<IAspect> aspect, int amount) {
        return amount;
    }

    @Override
    public int amountOf(Holder<IAspect> aspect) {
        Level level = getLevel();
        if (level == null) {
            return 0;
        }
        if (level.isClientSide()) {
            return snapshot.amountOf(aspect);
        }
        IGrid grid = mainNode.getGrid();
        EssentiaKey key = EssentiaKey.of(aspect);
        if (grid == null || key == null || !mainNode.isActive()) {
            return 0;
        }
        return (int) Math.min(Integer.MAX_VALUE, grid.getStorageService().getCachedInventory().get(key));
    }

    @Override
    public boolean holds(Holder<IAspect> aspect, int amount) {
        return amountOf(aspect) >= amount;
    }

    @Override
    public boolean drain(Holder<IAspect> aspect, int amount) {
        Level level = getLevel();
        if (level == null || level.isClientSide() || amount <= 0 || isBlocked()) {
            return false;
        }
        IGrid grid = mainNode.getGrid();
        EssentiaKey key = EssentiaKey.of(aspect);
        if (grid == null || key == null) {
            return false;
        }
        var storage = grid.getStorageService().getInventory();
        if (storage.extract(key, amount, Actionable.SIMULATE, source) < amount) {
            return false;
        }
        double cost = amount * TEConfig.INFUSION_PROVIDER_POWER_PER_ESSENTIA.get();
        var energy = grid.getEnergyService();
        if (cost > 0 && energy.extractAEPower(cost, Actionable.SIMULATE, PowerMultiplier.CONFIG) < cost - 0.01) {
            return false;
        }
        if (storage.extract(key, amount, Actionable.MODULATE, source) < amount) {
            return false;
        }
        if (cost > 0) {
            energy.extractAEPower(cost, Actionable.MODULATE, PowerMultiplier.CONFIG);
        }
        return true;
    }

    public List<Component> describeStored() {
        List<Component> lines = new ArrayList<>();
        for (AspectInstance instance : getAspects().sortedByAmount()) {
            EssentiaKey key = EssentiaKey.of(instance.aspect());
            if (key != null) {
                lines.add(Component.translatable("message.thaumaenergistics.infusion_provider.entry", key.getDisplayName(), instance.amount()));
            }
        }
        return lines;
    }

    private AspectList readNetwork(Level level) {
        IGrid grid = mainNode.getGrid();
        if (grid == null || !mainNode.isActive()) {
            return AspectList.EMPTY;
        }
        List<AspectInstance> entries = new ArrayList<>();
        for (var entry : grid.getStorageService().getCachedInventory()) {
            AEKey what = entry.getKey();
            if (what instanceof EssentiaKey essentia && entry.getLongValue() > 0) {
                Holder<IAspect> holder = essentia.holder(level.registryAccess());
                if (holder != null) {
                    entries.add(new AspectInstance(holder, (int) Math.min(Integer.MAX_VALUE, entry.getLongValue())));
                }
            }
        }
        return AspectList.ofEntries(entries);
    }
}
