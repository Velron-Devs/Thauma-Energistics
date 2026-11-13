package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.registry.TUUBlocks;

public class TUUBlockStateProvider extends BlockStateProvider {

    public TUUBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TUU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(TUUBlocks.INFUSION_PROVIDER.get(),
                models().cubeAll("infusion_provider", modLoc("block/infusion_provider")));
    }
}
