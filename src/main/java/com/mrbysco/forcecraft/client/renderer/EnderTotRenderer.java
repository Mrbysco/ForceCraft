package com.mrbysco.forcecraft.client.renderer;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.EnderTotModel;
import com.mrbysco.forcecraft.client.renderer.layer.EnderTotCarriedBlockLayer;
import com.mrbysco.forcecraft.client.renderer.layer.EnderTotEyesLayer;
import com.mrbysco.forcecraft.client.state.EnderTotRenderState;
import com.mrbysco.forcecraft.entity.ender.EnderTot;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EnderTotRenderer extends MobRenderer<EnderTot, EnderTotRenderState, EnderTotModel> {
	public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
	private static final Identifier ENDERTOT_TEXTURES = Reference.modLoc("textures/entity/ender_tot.png");
	private final RandomSource random = RandomSource.create();
	private final BlockModelResolver blockModelResolver;

	public EnderTotRenderer(EntityRendererProvider.Context context) {
		super(context, new EnderTotModel(context.bakeLayer(ClientHandler.ENDERTOT)), 0.5F);
		this.blockModelResolver = context.getBlockModelResolver();
		this.addLayer(new EnderTotEyesLayer(this));
		this.addLayer(new EnderTotCarriedBlockLayer(this));
	}

	@Override
	public Vec3 getRenderOffset(EnderTotRenderState state) {
		Vec3 offset = super.getRenderOffset(state);
		if (state.isCreepy) {
			double d = 0.02 * state.scale;
			return offset.add(this.random.nextGaussian() * d, 0.0, this.random.nextGaussian() * d);
		} else {
			return offset;
		}
	}

	@Override
	public Identifier getTextureLocation(EnderTotRenderState state) {
		return ENDERTOT_TEXTURES;
	}

	public EnderTotRenderState createRenderState() {
		return new EnderTotRenderState();
	}

	public void extractRenderState(EnderTot entity, EnderTotRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
		state.isCreepy = entity.isCreepy();
		BlockState carriedBlock = entity.getCarriedBlock();
		if (carriedBlock != null) {
			this.blockModelResolver.update(state.carriedBlock, carriedBlock, BLOCK_DISPLAY_CONTEXT);
		} else {
			state.carriedBlock.clear();
		}
	}
}