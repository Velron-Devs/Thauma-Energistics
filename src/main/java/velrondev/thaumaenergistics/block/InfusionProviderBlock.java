package velrondev.thaumaenergistics.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.blockentity.InfusionProviderBlockEntity;
import velrondev.thaumaenergistics.registry.TEBlockEntities;

public class InfusionProviderBlock extends Block implements EntityBlock {

    public InfusionProviderBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusionProviderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || type != TEBlockEntities.INFUSION_PROVIDER.get()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<InfusionProviderBlockEntity>) InfusionProviderBlockEntity::serverTick;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (placer != null && level.getBlockEntity(pos) instanceof InfusionProviderBlockEntity provider) {
            provider.setOwner(placer);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.isShiftKeyDown() || !(level.getBlockEntity(pos) instanceof InfusionProviderBlockEntity provider)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            var lines = provider.describeStored();
            if (lines.isEmpty()) {
                player.sendSystemMessage(Component.translatable("message.thaumaenergistics.infusion_provider.empty"));
            } else {
                player.sendSystemMessage(Component.translatable("message.thaumaenergistics.infusion_provider.stored"));
                lines.forEach(player::sendSystemMessage);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
