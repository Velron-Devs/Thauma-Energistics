package velrondev.thaumaenergistics.menu;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;

import appeng.api.inventories.InternalInventory;
import appeng.helpers.WirelessTerminalMenuHost;
import appeng.menu.ISubMenu;
import appeng.menu.locator.ItemMenuHostLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.item.WirelessArcaneTerminalItem;

public class WirelessArcaneTerminalMenuHost extends WirelessTerminalMenuHost<WirelessArcaneTerminalItem> implements ArcaneTerminalHost {

    private final WirelessArcaneInventories inventories;

    public WirelessArcaneTerminalMenuHost(WirelessArcaneTerminalItem item, Player player, ItemMenuHostLocator locator, BiConsumer<Player, ISubMenu> returnToMainMenu) {
        super(item, player, locator, returnToMainMenu);
        this.inventories = new WirelessArcaneInventories(player, this::getItemStack);
    }

    @Nullable
    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        return inventories.get(id);
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
