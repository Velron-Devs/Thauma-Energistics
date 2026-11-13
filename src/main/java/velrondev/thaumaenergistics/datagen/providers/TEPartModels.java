package velrondev.thaumaenergistics.datagen.providers;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.google.gson.JsonElement;

import appeng.api.parts.IPartItem;
import appeng.api.util.AEColor;
import appeng.api.util.AEColorVariant;
import appeng.client.api.model.parts.CompositePartModel;
import appeng.client.api.model.parts.PartModel;
import appeng.client.api.model.parts.StaticPartModel;
import appeng.client.model.StatusIndicatorPartModel;
import appeng.client.render.AEColorItemTintSource;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.registry.TEParts;

public final class TEPartModels {

    private static final String AE2 = "ae2";
    private static final int FULL_LIGHT = 15;
    private static final int FIXED = -1;

    private static final TextureSlot SIDES = TextureSlot.create("sides");
    private static final TextureSlot FRONT_BASE = TextureSlot.create("front_base");
    private static final TextureSlot PANEL = TextureSlot.create("panel");
    private static final TextureSlot FRAME = TextureSlot.create("frame");
    private static final TextureSlot DOTS = TextureSlot.create("dots");
    private static final TextureSlot DARK = TextureSlot.create("dark");
    private static final TextureSlot PLATE = TextureSlot.create("plate");
    private static final TextureSlot FLASK = TextureSlot.create("flask");
    private static final TextureSlot MEDIUM = TextureSlot.create("medium");

    private static final Identifier PART_BASE_ITEM = ae2("item/part_base");
    private static final Identifier DISPLAY_BASE = ae2("part/display_base");

    private record Layer(TextureSlot slot, String texture, int tint) {
    }

    private static final List<Layer> ARCANE_LAYERS = List.of(
            new Layer(PANEL, "arcane_terminal_panel", AEColor.TINTINDEX_MEDIUM),
            new Layer(FRAME, "arcane_terminal_frame", FIXED),
            new Layer(DOTS, "arcane_terminal_dots", AEColor.TINTINDEX_BRIGHT),
            new Layer(DARK, "arcane_terminal_dark", AEColor.TINTINDEX_DARK));

    private static final List<Layer> ESSENTIA_LAYERS_OFF = List.of(
            new Layer(FLASK, "essentia_terminal_flask", AEColor.TINTINDEX_MEDIUM),
            new Layer(PLATE, "essentia_terminal_plate", AEColor.TINTINDEX_BRIGHT),
            new Layer(FRAME, "essentia_terminal_frame", FIXED),
            new Layer(MEDIUM, "essentia_terminal_medium", AEColor.TINTINDEX_MEDIUM),
            new Layer(DARK, "essentia_terminal_dark", AEColor.TINTINDEX_DARK));

    private static final List<Layer> ESSENTIA_LAYERS_ON = List.of(
            new Layer(PLATE, "essentia_terminal_plate", AEColor.TINTINDEX_BRIGHT),
            new Layer(FLASK, "essentia_terminal_flask", AEColor.TINTINDEX_MEDIUM),
            new Layer(FRAME, "essentia_terminal_frame", FIXED),
            new Layer(MEDIUM, "essentia_terminal_medium", AEColor.TINTINDEX_MEDIUM),
            new Layer(DARK, "essentia_terminal_dark", AEColor.TINTINDEX_DARK));

    private final ItemModelGenerators itemModels;
    private final BiConsumer<Identifier, ModelInstance> modelOutput;
    private final BiConsumer<IPartItem<?>, PartModel.Unbaked> partModels;

    public TEPartModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels, BiConsumer<IPartItem<?>, PartModel.Unbaked> partModels) {
        this.itemModels = itemModels;
        this.modelOutput = blockModels.modelOutput;
        this.partModels = partModels;
    }

    public void register() {
        display(TEParts.ARCANE_TERMINAL.get(), "arcane_terminal", ARCANE_LAYERS, ARCANE_LAYERS, ARCANE_LAYERS);
        display(TEParts.ESSENTIA_TERMINAL.get(), "essentia_terminal", ESSENTIA_LAYERS_OFF, ESSENTIA_LAYERS_ON, ESSENTIA_LAYERS_ON);
        bus(TEParts.ESSENTIA_IMPORT_BUS.get(), "essentia_import_bus", "import_bus");
        bus(TEParts.ESSENTIA_EXPORT_BUS.get(), "essentia_export_bus", "export_bus");
        bus(TEParts.ESSENTIA_STORAGE_BUS.get(), "essentia_storage_bus", "storage_bus");
    }

    private void display(IPartItem<?> part, String name, List<Layer> offLayers, List<Layer> onLayers, List<Layer> itemLayers) {
        Identifier off = mod("part/" + name + "_off");
        Identifier on = mod("part/" + name + "_on");
        layeredPart(off, offLayers, false);
        layeredPart(on, onLayers, true);

        partModels.accept(part, new CompositePartModel.Unbaked(List.of(
                new StaticPartModel.Unbaked(DISPLAY_BASE),
                new StatusIndicatorPartModel.Unbaked(on, on, off),
                statusIndicator(ae2("part/display_status")))));

        Item item = part.asItem();
        Identifier itemModel = ModelLocationUtils.getModelLocation(item);
        displayItem(itemModel, itemLayers);
        registerPartItem(item, itemModel);
    }

    private void bus(IPartItem<?> part, String name, String aeName) {
        Identifier face = mod("part/" + name + "_face");
        Identifier base = mod("part/" + name + "_base");

        new ModelTemplate(Optional.of(ae2("part/" + aeName + "_base")), Optional.empty(), TextureSlot.FRONT)
                .create(base, new TextureMapping().put(TextureSlot.FRONT, new Material(face)), modelOutput);

        partModels.accept(part, new CompositePartModel.Unbaked(List.of(
                new StaticPartModel.Unbaked(base),
                statusIndicator(ae2("part/" + aeName)))));

        Item item = part.asItem();
        Identifier itemModel = new ModelTemplate(Optional.of(ae2("item/" + aeName)), Optional.empty(), TextureSlot.FRONT)
                .create(item, new TextureMapping().put(TextureSlot.FRONT, new Material(face)), modelOutput);
        registerPartItem(item, itemModel);
    }

    private void layeredPart(Identifier id, List<Layer> layers, boolean emissive) {
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder();
        TextureMapping mapping = new TextureMapping();
        for (Layer layer : layers) {
            builder.requiredTextureSlot(layer.slot());
            mapping.put(layer.slot(), new Material(mod("part/" + layer.texture())));
        }
        builder.requiredTextureSlot(TextureSlot.PARTICLE);
        mapping.put(TextureSlot.PARTICLE, new Material(mod("part/" + layers.getFirst().texture())));

        for (Layer layer : layers) {
            builder.element(element -> element
                    .from(2, 2, 0)
                    .to(14, 14, 2)
                    .face(Direction.NORTH, face -> {
                        face.texture(layer.slot());
                        if (layer.tint() != FIXED) {
                            face.tintindex(layer.tint());
                        }
                        if (emissive) {
                            face.lightEmission(FULL_LIGHT);
                        }
                    }));
        }

        builder.build().create(id, mapping, cutout());
    }

    private void displayItem(Identifier id, List<Layer> layers) {
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder().parent(PART_BASE_ITEM);
        TextureMapping mapping = new TextureMapping();
        builder.requiredTextureSlot(SIDES);
        mapping.put(SIDES, new Material(ae2("part/monitor_sides")));
        builder.requiredTextureSlot(TextureSlot.BACK);
        mapping.put(TextureSlot.BACK, new Material(ae2("part/monitor_back")));
        builder.requiredTextureSlot(FRONT_BASE);
        mapping.put(FRONT_BASE, new Material(ae2("part/monitor_front")));
        for (Layer layer : layers) {
            builder.requiredTextureSlot(layer.slot());
            mapping.put(layer.slot(), new Material(mod("part/" + layer.texture())));
        }
        builder.requiredTextureSlot(TextureSlot.PARTICLE);
        mapping.put(TextureSlot.PARTICLE, new Material(mod("part/" + layers.getFirst().texture())));

        builder.element(element -> element
                .from(2, 2, 7)
                .to(14, 14, 9)
                .face(Direction.NORTH, face -> face.uvs(2, 2, 14, 14).texture(FRONT_BASE))
                .face(Direction.EAST, face -> face.uvs(14, 2, 16, 14).texture(SIDES))
                .face(Direction.SOUTH, face -> face.uvs(2, 2, 14, 14).texture(TextureSlot.BACK))
                .face(Direction.WEST, face -> face.uvs(0, 2, 2, 14).texture(SIDES))
                .face(Direction.UP, face -> face.uvs(2, 0, 14, 2).texture(SIDES))
                .face(Direction.DOWN, face -> face.uvs(2, 14, 14, 16).texture(SIDES)));

        for (Layer layer : layers) {
            builder.element(element -> element
                    .from(2, 2, 7)
                    .to(14, 14, 8)
                    .face(Direction.NORTH, face -> {
                        face.uvs(2, 2, 14, 14).texture(layer.slot());
                        if (layer.tint() != FIXED) {
                            face.tintindex(layer.tint());
                        }
                    }));
        }

        builder.element(element -> element
                .from(4, 4, 9)
                .to(12, 12, 10)
                .face(Direction.EAST, face -> face.uvs(6, 4, 7, 12).texture(SIDES))
                .face(Direction.SOUTH, face -> face.uvs(4, 4, 12, 12).texture(TextureSlot.BACK))
                .face(Direction.WEST, face -> face.uvs(9, 4, 10, 12).texture(SIDES))
                .face(Direction.UP, face -> face.uvs(4, 9, 12, 10).texture(SIDES))
                .face(Direction.DOWN, face -> face.uvs(4, 6, 12, 7).texture(SIDES)));

        builder.build().create(id, mapping, modelOutput);
    }

    private void registerPartItem(Item item, Identifier model) {
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(
                model,
                new Constant(-1),
                new AEColorItemTintSource(AEColor.TRANSPARENT, AEColorVariant.DARK),
                new AEColorItemTintSource(AEColor.TRANSPARENT, AEColorVariant.MEDIUM),
                new AEColorItemTintSource(AEColor.TRANSPARENT, AEColorVariant.BRIGHT),
                new AEColorItemTintSource(AEColor.TRANSPARENT, AEColorVariant.MEDIUM_BRIGHT)));
    }

    private static StatusIndicatorPartModel.Unbaked statusIndicator(Identifier base) {
        return new StatusIndicatorPartModel.Unbaked(
                base.withSuffix("_has_channel"),
                base.withSuffix("_on"),
                base.withSuffix("_off"));
    }

    private BiConsumer<Identifier, ModelInstance> cutout() {
        return (id, model) -> modelOutput.accept(id, () -> {
            JsonElement json = model.get();
            json.getAsJsonObject().addProperty("render_type", "minecraft:cutout");
            return json;
        });
    }

    private static Identifier mod(String path) {
        return Identifier.fromNamespaceAndPath(ThaumaEnergistics.MOD_ID, path);
    }

    private static Identifier ae2(String path) {
        return Identifier.fromNamespaceAndPath(AE2, path);
    }
}
