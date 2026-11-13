package velrondev.thaumaenergistics.client;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.client.AspectRendering;
import com.mojang.blaze3d.vertex.PoseStack;

import appeng.client.api.AEKeyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.api.key.EssentiaKey;

public class EssentiaKeyRenderHandler implements AEKeyRenderer<EssentiaKey, EssentiaKeyRenderHandler.RenderState> {

    @Override
    public void drawInGui(Minecraft minecraft, GuiGraphicsExtractor guiGraphics, int x, int y, EssentiaKey key) {
        Holder<IAspect> aspect = minecraft.level == null ? null : key.holder(minecraft.level.registryAccess());
        if (aspect == null) {
            AspectRendering.renderMissingGui(guiGraphics, x, y);
        } else {
            AspectRendering.renderGui(guiGraphics, minecraft.font, x, y, aspect, 1.0F);
        }
    }

    @Override
    public Class<RenderState> stateClass() {
        return RenderState.class;
    }

    @Override
    public RenderState createState() {
        return new RenderState();
    }

    @Override
    public void extract(RenderState state, EssentiaKey key, @Nullable Level level, int seed) {
        state.aspect = level == null ? null : key.holder(level.registryAccess());
    }

    @Override
    public void submit(PoseStack poseStack, RenderState state, SubmitNodeCollector nodes, int lightCoords) {
        poseStack.pushPose();
        if (state.aspect == null) {
            AspectRendering.submitMissingBillboard(poseStack, nodes, 1.0F, 1.0F, lightCoords, AspectRendering.BlendMode.ALPHA);
        } else {
            AspectRendering.submitBillboard(poseStack, nodes, state.aspect, 1.0F, 1.0F, false, lightCoords, AspectRendering.BlendMode.ALPHA);
        }
        poseStack.popPose();
    }

    @Override
    public List<Component> getTooltip(EssentiaKey key) {
        return List.of(key.getDisplayName());
    }

    public static final class RenderState {
        @Nullable
        public Holder<IAspect> aspect;
    }
}
