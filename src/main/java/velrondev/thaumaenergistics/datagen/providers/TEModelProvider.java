package velrondev.thaumaenergistics.datagen.providers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import appeng.api.parts.IPartItem;
import appeng.client.api.model.parts.ClientPart;
import appeng.client.api.model.parts.PartModel;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.core.TECompat;
import velrondev.thaumaenergistics.core.TECompatLoader;
import velrondev.thaumaenergistics.registry.TEBlocks;

public final class TEModelProvider extends ModelProvider {

    private final PackOutput.PathProvider partModelOutput;
    private final Map<IPartItem<?>, ClientPart> parts = new LinkedHashMap<>();

    public TEModelProvider(PackOutput output) {
        super(output, ThaumaEnergistics.MOD_ID);
        this.partModelOutput = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "ae2/parts");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        parts.clear();
        CompletableFuture<?> models = super.run(output);
        CompletableFuture<?> clientParts = DataProvider.saveAll(
                output,
                ClientPart.CODEC,
                part -> partModelOutput.json(BuiltInRegistries.ITEM.getKey(part.asItem())),
                parts);
        return CompletableFuture.allOf(models, clientParts);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(TEBlocks.INFUSION_PROVIDER.get());
        new TEItemModels(itemModels).register();
        new TEProcessorModels(itemModels).register();
        if (TECompat.extendedAeActive()) {
            new TEExtendedAeModels(blockModels, itemModels).register();
        }
        if (TECompat.thingsActive()) {
            TECompatLoader.models(TECompat.THINGS_MODELS, blockModels, itemModels);
        }
        if (TECompat.megaThingsActive()) {
            TECompatLoader.models(TECompat.MEGA_THINGS_MODELS, blockModels, itemModels);
        }
        if (TECompat.megaCellsActive()) {
            TECompatLoader.models(TECompat.MEGA_CELLS_MODELS, blockModels, itemModels);
        }
        new TEPartModels(blockModels, itemModels, this::registerPart).register();
    }

    private void registerPart(IPartItem<?> part, PartModel.Unbaked model) {
        ClientPart previous = parts.put(part, new ClientPart(model, ClientPart.Properties.DEFAULT));
        if (previous != null) {
            throw new IllegalStateException("Duplicate part model definition for " + IPartItem.getId(part));
        }
    }

    @Override
    public String getName() {
        return "Model Definitions and Part Models - " + modId;
    }
}
