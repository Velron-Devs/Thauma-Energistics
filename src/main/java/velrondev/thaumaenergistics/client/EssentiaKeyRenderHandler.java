package velrondev.thaumaenergistics.client;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.client.AspectRendering;
import com.mojang.blaze3d.vertex.PoseStack;

import appeng.api.client.AEKeyRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import velrondev.thaumaenergistics.api.key.EssentiaKey;

public class EssentiaKeyRenderHandler implements AEKeyRenderHandler<EssentiaKey> {

    @Override
    public void drawInGui(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, EssentiaKey key) {
        Holder<IAspect> aspect = minecraft.level == null ? null : key.holder(minecraft.level.registryAccess());
        if (aspect == null) {
            AspectRendering.renderMissingGui(guiGraphics, x, y);
        } else {
            AspectRendering.renderGui(guiGraphics, minecraft.font, x, y, aspect, 1.0F);
        }
    }

    @Override
    public void drawOnBlockFace(PoseStack poseStack, MultiBufferSource buffers, EssentiaKey key, float scale, int combinedLight, Level level) {
        Holder<IAspect> aspect = key.holder(level.registryAccess());
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        if (aspect == null) {
            AspectRendering.renderMissingBillboard(poseStack, buffers, 1.0F, 1.0F, combinedLight, AspectRendering.BlendMode.ALPHA);
        } else {
            AspectRendering.renderBillboard(poseStack, buffers, aspect, 1.0F, 1.0F, false, combinedLight, AspectRendering.BlendMode.ALPHA);
        }
        poseStack.popPose();
    }

    @Override
    public Component getDisplayName(EssentiaKey key) {
        return key.getDisplayName();
    }
}
