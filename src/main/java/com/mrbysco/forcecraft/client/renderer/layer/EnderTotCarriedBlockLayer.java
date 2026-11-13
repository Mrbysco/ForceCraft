package com.mrbysco.forcecraft.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.client.state.EnderTotRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class EnderTotCarriedBlockLayer extends RenderLayer<EnderTotRenderState, EnderTotModel> {
	public EnderTotCarriedBlockLayer(RenderLayerParent<EnderTotRenderState, EnderTotModel> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, EnderTotRenderState state, float yRot, float xRot) {
		BlockModelRenderState carriedBlock = state.carriedBlock;
		if (!carriedBlock.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0.0F, 0.6875F, -0.75F);
			poseStack.rotate(Axis.XP, 20.0F);
			poseStack.rotate(Axis.YP, 45.0F);
			poseStack.translate(0.25F, 0.1875F, 0.25F);
			float s = 0.5F;
			poseStack.scale(-0.5F, -0.5F, 0.5F);
			poseStack.rotate(Axis.YP, 90.0F);
			carriedBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
			poseStack.popPose();
		}
	}
}
