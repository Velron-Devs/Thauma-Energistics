package velrondev.thaumaenergistics.datagen.providers;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;

public final class TUUTextures {

    private final ExistingFileHelper helper;

    public TUUTextures(ExistingFileHelper helper) {
        this.helper = helper;
    }

    public ResourceLocation item(String path, ResourceLocation placeholder) {
        return pick("item/" + path, placeholder);
    }

    public ResourceLocation block(String path, ResourceLocation placeholder) {
        return pick("block/" + path, placeholder);
    }

    public static ResourceLocation of(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private ResourceLocation pick(String path, ResourceLocation placeholder) {
        ResourceLocation own = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, path);
        return helper.exists(own, ModelProvider.TEXTURE) ? own : placeholder;
    }
}
