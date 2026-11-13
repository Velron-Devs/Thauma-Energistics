package velrondev.thaumaenergistics.compat.extendedae;

import java.util.List;

import com.glodblock.github.extendedae.common.EAESingletons;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixPattern;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEItemKey;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.CombinedInternalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import velrondev.thaumaenergistics.config.TECompatConfig;

public class TierMatrixPatternTile extends TileAssemblerMatrixPattern {

    private static final String EXTRA_TAG = "pattern_extra";

    private final MatrixTier tier;
    private final AppEngInternalInventory extra;
    private final InternalInventory combined;

    public TierMatrixPatternTile(BlockPos pos, BlockState state) {
        super(EAESingletons.ASSEMBLER_MATRIX_PATTERN.get().getBlockEntityType(), pos, state);
        this.tier = (MatrixTier) state.getBlock();
        this.getMainNode().setIdlePowerUsage(TECompatConfig.MATRIX_IDLE_POWER.get());
        this.extra = new AppEngInternalInventory(this, TECompatConfig.MATRIX_EXTRA_PATTERN_SLOTS.get(tier.metal()).get(), 1);
        this.extra.setFilter(new TileAssemblerMatrixPattern.Filter(this::getLevel));
        this.combined = new CombinedInternalInventory(super.getPatternInventory(), this.extra);
    }

    @Override
    public void saveAdditional(ValueOutput data) {
        super.saveAdditional(data);
        this.extra.writeToNBT(data, EXTRA_TAG);
    }

    @Override
    public void loadTag(ValueInput data) {
        super.loadTag(data);
        this.extra.readFromNBT(data, EXTRA_TAG);
    }

    @Override
    public void updatePatterns() {
        super.updatePatterns();
        List<IPatternDetails> patterns = this.getAvailablePatterns();
        for (ItemStack stack : this.extra) {
            IPatternDetails details = PatternDetailsHelper.decodePattern(stack, this.getLevel());
            if (details != null) {
                patterns.add(details);
            }
        }
        ICraftingProvider.requestUpdate(this.getMainNode());
    }

    @Override
    public InternalInventory getTerminalPatternInventory() {
        return this.combined;
    }

    @Override
    public PatternContainerGroup getTerminalGroup() {
        AEItemKey icon = AEItemKey.of(this.getMatrixBlock().asItem());
        Component name = this.hasCustomName() ? this.getCustomName() : icon.getDisplayName();
        return new PatternContainerGroup(icon, name, List.of(Component.translatable("gui.extendedae.assembler_matrix.pattern")));
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        super.addAdditionalDrops(level, pos, drops);
        for (ItemStack stack : this.extra) {
            drops.add(stack);
        }
    }

    @Override
    public void clearContent() {
        super.clearContent();
        this.extra.clear();
    }
}
