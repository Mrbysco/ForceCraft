package com.mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.block.ForceEngineBlock;
import com.mrbysco.forcecraft.blockentity.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.state.ForceEngineRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EngineBER implements BlockEntityRenderer<ForceEngineBlockEntity, ForceEngineRenderState> {
	private static final Identifier PISTON_TEXTURE = Reference.modLoc("textures/block/engine/piston.png");
	private static final Identifier EXTENSION_TEXTURE = Reference.modLoc("textures/block/engine/extension.png");

	private static final int PISTON_PERIOD_TICKS = 20;
	private static final float PISTON_STROKE = 6F;
	private static final float PIXEL = 1F / 16F;

	private final ModelPart piston;
	private final ModelPart extension;

	public EngineBER(BlockEntityRendererProvider.Context context) {
		ModelPart root = context.bakeLayer(ClientHandler.ENGINE);

		this.piston = root.getChild("piston");
		this.extension = root.getChild("extension");
	}

	@Override
	public ForceEngineRenderState createRenderState() {
		return new ForceEngineRenderState();
	}

	@Override
	public void extractRenderState(ForceEngineBlockEntity blockEntity, ForceEngineRenderState state,
	                               float partialTicks, Vec3 cameraPosition,
	                               ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.orientation = blockEntity.getFacing();
		state.isGenerating = blockEntity.getBlockState().getValue(ForceEngineBlock.GENERATING);
		state.animationTime = blockEntity.getLevel() != null
				? (blockEntity.getLevel().getGameTime() % PISTON_PERIOD_TICKS) + partialTicks
				: 0F;
	}

	@Override
	public void submit(ForceEngineRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();

		poseStack.translate(0.5, 0.5, 0.5);
		applyOrientation(poseStack, state.orientation);
		poseStack.translate(-0.5, -0.5, -0.5);

		float step = getPistonStep(state);

		// piston head
		poseStack.pushPose();
		poseStack.translate(0, step * PIXEL, 0);
		submitNodeCollector.submitModelPart(
				this.piston, poseStack, RenderTypes.entityCutout(PISTON_TEXTURE),
				state.lightCoords, OverlayTexture.NO_OVERLAY, null
		);
		poseStack.popPose();

		// piston sleeve, one segment per 2 pixels of extension
		if (step > 0) {
			for (int offset = 0; offset <= step + 2; offset += 2) {
				submitNodeCollector.submitModelPart(
						this.extension, poseStack, RenderTypes.entityCutout(EXTENSION_TEXTURE),
						state.lightCoords, OverlayTexture.NO_OVERLAY, null
				);
				poseStack.translate(0, 2 * PIXEL, 0);
			}
		}

		poseStack.popPose();
	}

	private static void applyOrientation(PoseStack poseStack, Direction direction) {
		switch (direction) {
			case EAST -> poseStack.mulPose(Axis.ZP.rotation(-Mth.HALF_PI));
			case WEST -> poseStack.mulPose(Axis.ZP.rotation(Mth.HALF_PI));
			case DOWN -> poseStack.mulPose(Axis.ZP.rotation(Mth.PI));
			case NORTH -> poseStack.mulPose(Axis.XP.rotation(-Mth.HALF_PI));
			case SOUTH -> poseStack.mulPose(Axis.XP.rotation(Mth.HALF_PI));
			default -> {
			} // UP: no rotation
		}
	}


	private static float getPistonStep(ForceEngineRenderState state) {
		if (!state.isGenerating) {
			return 0F;
		}

		float progress = state.animationTime / PISTON_PERIOD_TICKS;
		float wave = progress < 0.5F ? progress * 2F : 2F - progress * 2F;
		return wave * PISTON_STROKE;
	}
}
