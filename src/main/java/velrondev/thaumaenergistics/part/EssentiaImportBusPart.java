package velrondev.thaumaenergistics.part;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import appeng.api.behaviors.StackImportStrategy;
import appeng.api.networking.IGrid;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.core.definitions.AEItems;
import appeng.core.settings.TickRates;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.parts.automation.IOBusPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.MenuType;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.behavior.EssentiaImportStrategy;
import velrondev.thaumaenergistics.behavior.EssentiaTransferContext;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;
import velrondev.thaumaenergistics.registry.TUUMenus;

public class EssentiaImportBusPart extends IOBusPart {

    public static final ResourceLocation ESSENTIA_BASE = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "part/essentia_import_bus_base");

    public static final ResourceLocation ESSENTIA_OFF = ResourceLocation.fromNamespaceAndPath("ae2", "part/import_bus_off");
    public static final ResourceLocation ESSENTIA_ON = ResourceLocation.fromNamespaceAndPath("ae2", "part/import_bus_on");
    public static final ResourceLocation ESSENTIA_HAS_CHANNEL = ResourceLocation.fromNamespaceAndPath("ae2", "part/import_bus_has_channel");

    @PartModels
    public static final IPartModel ESSENTIA_MODELS_OFF = new PartModel(ESSENTIA_BASE, ESSENTIA_OFF);
    @PartModels
    public static final IPartModel ESSENTIA_MODELS_ON = new PartModel(ESSENTIA_BASE, ESSENTIA_ON);
    @PartModels
    public static final IPartModel ESSENTIA_MODELS_HAS_CHANNEL = new PartModel(ESSENTIA_BASE, ESSENTIA_HAS_CHANNEL);

    @Nullable
    private StackImportStrategy strategy;

    public EssentiaImportBusPart(IPartItem<?> partItem) {
        super(TickRates.ImportBus, Set.of(TUUKeyTypes.essentia()), partItem);
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
        return TUUMenus.ESSENTIA_IMPORT_BUS.get();
    }

    @Override
    public void getBoxes(IPartCollisionHelper helper) {
        helper.addBox(6, 6, 11, 10, 10, 13);
        helper.addBox(5, 5, 13, 11, 11, 14);
        helper.addBox(4, 4, 14, 12, 12, 16);
    }

    @Override
    public IPartModel getStaticModels() {
        if (isActive() && isPowered()) {
            return ESSENTIA_MODELS_HAS_CHANNEL;
        }
        return isPowered() ? ESSENTIA_MODELS_ON : ESSENTIA_MODELS_OFF;
    }
}
