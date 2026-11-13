package velrondev.thaumaenergistics.compat.ae2wtlib;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;

import de.mari_023.ae2wtlib.api.terminal.ItemWT;
import de.mari_023.ae2wtlib.api.terminal.WTMenuHost;

import appeng.api.inventories.InternalInventory;
import appeng.menu.ISubMenu;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.menu.ArcaneTerminalHost;
import velrondev.thaumaenergistics.menu.WirelessArcaneInventories;

public class WtlibArcaneTerminalHost extends WTMenuHost implements ArcaneTerminalHost {

    private final WirelessArcaneInventories inventories;

    public WtlibArcaneTerminalHost(ItemWT item, Player player, ItemMenuHostLocator locator, BiConsumer<Player, ISubMenu> returnToMainMenu) {
        super(item, player, locator, returnToMainMenu);
        this.inventories = new WirelessArcaneInventories(player, this::getItemStack);
    }

    @Nullable
    @Override
    public InternalInventory getSubInventory(Identifier id) {
        InternalInventory arcane = inventories.get(id);
        return arcane != null ? arcane : super.getSubInventory(id);
    }

    @Override
    public ArcaneWorkbenchContext context(ServerPlayer player) {
        return WirelessArcaneInventories.context(player);
    }

    @Override
    public Level getAuraLevel() {
        return getPlayer().level();
    }

    @Override
    public BlockPos getAuraPos() {
        return getPlayer().blockPosition();
    }
}
