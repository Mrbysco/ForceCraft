package mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.client.model.EnderTotModel;
import mrbysco.forcecraft.client.renderer.layer.EnderTotHeldBlockLayer;
import mrbysco.forcecraft.client.renderer.layer.EndertotEyesLayer;
import mrbysco.forcecraft.entities.EnderTotEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

public class EnderTotRenderer extends MobRenderer<EnderTotEntity, EnderTotModel<EnderTotEntity>> {
	private static final ResourceLocation ENDERTOT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/ender_tot.png");
	private final Random rnd = new Random();

	public EnderTotRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new EnderTotModel<>(0.0F), 0.5F);
		this.addLayer(new EndertotEyesLayer<>(this));
		this.addLayer(new EnderTotHeldBlockLayer(this));
	}

	@Override
	public void render(EnderTotEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		BlockState blockstate = entityIn.getHeldBlockState();
		EnderTotModel<EnderTotEntity> endertotModel = this.getEntityModel();
		endertotModel.isCarrying = blockstate != null;
		endertotModel.isAttacking = entityIn.isScreaming();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public Vector3d getRenderOffset(EnderTotEntity entityIn, float partialTicks) {
		if (entityIn.isScreaming()) {
			double d0 = 0.02D;
			return new Vector3d(this.rnd.nextGaussian() * d0, 0.0D, this.rnd.nextGaussian() * d0);
		} else {
			return super.getRenderOffset(entityIn, partialTicks);
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(EnderTotEntity entity) {
		return ENDERTOT_TEXTURES;
	}
}