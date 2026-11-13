package velrondev.thaumaenergistics.part;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import appeng.api.behaviors.StackImportStrategy;
import appeng.api.networking.IGrid;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartItem;
import appeng.core.definitions.AEItems;
import appeng.core.settings.TickRates;
import appeng.parts.automation.IOBusPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.behavior.EssentiaImportStrategy;
import velrondev.thaumaenergistics.behavior.EssentiaTransferContext;
import velrondev.thaumaenergistics.registry.TEKeyTypes;
import velrondev.thaumaenergistics.registry.TEMenus;

public class EssentiaImportBusPart extends IOBusPart {

    public static final Identifier ESSENTIA_BASE = Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, "part/essentia_import_bus_base");

    public static final Identifier ESSENTIA_OFF = Identifier.fromNamespaceAndPath("ae2", "part/import_bus_off");
    public static final Identifier ESSENTIA_ON = Identifier.fromNamespaceAndPath("ae2", "part/import_bus_on");
    public static final Identifier ESSENTIA_HAS_CHANNEL = Identifier.fromNamespaceAndPath("ae2", "part/import_bus_has_channel");

    @Nullable
    private StackImportStrategy strategy;

    public EssentiaImportBusPart(IPartItem<?> partItem) {
        super(TickRates.ImportBus, Set.of(TEKeyTypes.essentia()), partItem);
    }

    @Override
    protected boolean doBusWork(IGrid grid) {
        if (strategy == null) {
            BlockPos target = getHost().getBlockEntity().getBlockPos().relative(getSide());
            strategy = new EssentiaImportStrategy((ServerLevel) getLevel(), target, getSide().getOpposite());
        }

        EssentiaTransferContext context = new EssentiaTransferContext(
                grid.getStorageService(),
                grid.getEnergyService(),
                source,
                getOperationsPerTick(),
                getFilter());
        context.setInverted(isUpgradedWith(AEItems.INVERTER_CARD));
        strategy.transfer(context);
        return context.hasDoneWork();
    }

    @Override
    protected MenuType<?> getMenuType() {
        return TEMenus.ESSENTIA_IMPORT_BUS.get();
    }

    @Override
    public void getBoxes(IPartCollisionHelper helper) {
        helper.addBox(6, 6, 11, 10, 10, 13);
        helper.addBox(5, 5, 13, 11, 11, 14);
        helper.addBox(4, 4, 14, 12, 12, 16);
    }
}
