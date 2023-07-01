package com.mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.client.renderer.layer.EnderTotHeldBlockLayer;
import com.mrbysco.forcecraft.client.renderer.layer.EndertotEyesLayer;
import com.mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class EnderTotRenderer extends MobRenderer<EnderTotEntity, EnderTotModel<EnderTotEntity>> {
	private static final ResourceLocation ENDERTOT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/ender_tot.png");
	private final Random rnd = new Random();

	public EnderTotRenderer(EntityRendererProvider.Context context) {
		super(context, new EnderTotModel<>(context.bakeLayer(ClientHandler.ENDERTOT)), 0.5F);
		this.addLayer(new EndertotEyesLayer<>(this));
		this.addLayer(new EnderTotHeldBlockLayer(this));
	}

	@Override
	public void render(EnderTotEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		BlockState blockstate = entityIn.getCarriedBlock();
		EnderTotModel<EnderTotEntity> endertotModel = this.getModel();
		endertotModel.isCarrying = blockstate != null;
		endertotModel.isAttacking = entityIn.isCreepy();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public Vec3 getRenderOffset(EnderTotEntity entityIn, float partialTicks) {
		if (entityIn.isCreepy()) {
			double d0 = 0.02D;
			return new Vec3(this.rnd.nextGaussian() * d0, 0.0D, this.rnd.nextGaussian() * d0);
		} else {
			return super.getRenderOffset(entityIn, partialTicks);
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(EnderTotEntity entity) {
		return ENDERTOT_TEXTURES;
	}
}