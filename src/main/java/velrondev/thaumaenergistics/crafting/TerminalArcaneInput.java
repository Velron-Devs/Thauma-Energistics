package velrondev.thaumaenergistics.crafting;

import java.util.List;
import java.util.function.Supplier;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class TerminalArcaneInput extends ArcaneCraftingInput {

    public static final int GRID_SIZE = 3;
    public static final int GRID_SLOTS = GRID_SIZE * GRID_SIZE;

    private final Supplier<AspectList> crystalSource;
    private final Supplier<ItemStack> wandSource;
    private final Supplier<BlockPos> auraPosSource;
    private AspectList crystals;

    public TerminalArcaneInput(List<ItemStack> grid, Player player, Supplier<AspectList> crystalSource, Supplier<ItemStack> wandSource, Supplier<BlockPos> auraPosSource) {
        super(GRID_SIZE, GRID_SIZE, grid);
        this.crystalSource = crystalSource;
        this.wandSource = wandSource;
        this.auraPosSource = auraPosSource;
        withPlayer(player);
    }

    public BlockPos auraPos() {
        return auraPosSource.get();
    }

    @Override
    public ItemStack wandStack() {
        return wandSource.get();
    }

    @Override
    public AspectList availableCrystals() {
        if (crystals == null) {
            crystals = crystalSource.get();
        }
        return crystals;
    }
}
