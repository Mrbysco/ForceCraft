package com.mrbysco.forcecraft.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

public class EnderTotHeldBlockLayer extends RenderLayer<EnderTotEntity, EnderTotModel<EnderTotEntity>> {
	public EnderTotHeldBlockLayer(RenderLayerParent<EnderTotEntity, EnderTotModel<EnderTotEntity>> renderLayerParent) {
		super(renderLayerParent);
	}

	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EnderTotEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		BlockState blockstate = entitylivingbaseIn.getCarriedBlock();
		if (blockstate != null) {
			poseStack.pushPose();
			poseStack.translate(0.0D, 1.0D, -0.625D);
			poseStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
			poseStack.translate(0.25D, 0.1875D, 0.25D);
			poseStack.scale(-0.5F, -0.5F, 0.5F);
			poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
	}
}