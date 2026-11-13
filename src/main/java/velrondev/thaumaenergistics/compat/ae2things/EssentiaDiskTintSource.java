package velrondev.thaumaenergistics.compat.ae2things;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import appeng.api.storage.cells.CellState;
import io.github.projectet.ae2things.storage.DISKCellInventory;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import velrondev.thaumaenergistics.ThaumaEnergistics;

public record EssentiaDiskTintSource() implements ItemTintSource {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "essentia_disk_state");
    public static final MapCodec<EssentiaDiskTintSource> MAP_CODEC = MapCodec.unit(EssentiaDiskTintSource::new);

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        DISKCellInventory inventory = DISKCellInventory.createInventory(stack, null, null);
        CellState state = inventory != null ? inventory.getClientStatus() : CellState.EMPTY;
        return ARGB.opaque(state.getStateColor());
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
