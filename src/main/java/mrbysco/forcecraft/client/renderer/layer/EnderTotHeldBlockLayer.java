package mrbysco.forcecraft.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import mrbysco.forcecraft.client.model.EnderTotModel;
import mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Vector3f;

public class EnderTotHeldBlockLayer extends LayerRenderer<EnderTotEntity, EnderTotModel<EnderTotEntity>> {
	public EnderTotHeldBlockLayer(IEntityRenderer<EnderTotEntity, EnderTotModel<EnderTotEntity>> p_i50949_1_) {
		super(p_i50949_1_);
	}

	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EnderTotEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		BlockState blockstate = entitylivingbaseIn.getHeldBlockState();
		if (blockstate != null) {
			matrixStackIn.push();
			matrixStackIn.translate(0.0D, 1.0D, -0.625D);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(20.0F));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45.0F));
			matrixStackIn.translate(0.25D, 0.1875D, 0.25D);
			float f = 0.5F;
			matrixStackIn.scale(-0.5F, -0.5F, 0.5F);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
			matrixStackIn.pop();
		}
	}
}