package com.mrbysco.forcecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.model.BombModel;
import com.mrbysco.forcecraft.client.state.BombRenderState;
import com.mrbysco.forcecraft.entity.Bomb;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class BombRenderer extends EntityRenderer<Bomb, BombRenderState> {
	private static final Identifier BOMB_TEXTURE = Reference.modLoc("textures/entity/bomb/bomb.png");
	private static final Identifier SUPER_BOMB_TEXTURE = Reference.modLoc("textures/entity/bomb/super_bomb.png");

	private final BombModel model;

	public BombRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new BombModel(context.bakeLayer(ClientHandler.BOMB));
	}

	@Override
	public void submit(BombRenderState state, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);

		if (state.isSuper) {
			poseStack.scale(3.0F, 3.0F, 3.0F);
		} else {
			poseStack.scale(1.5F, 1.5F, 1.5F);
		}

		poseStack.translate(0.0F, -1.5F, 0.0F);
		float fuse = state.fuseRemainingInTicks;
		if (state.fuseRemainingInTicks < 10.0F) {
			float g = 1.0F - state.fuseRemainingInTicks / 10.0F;
			g = Mth.clamp(g, 0.0F, 1.0F);
			g *= g;
			g *= g;
			float s = 1.0F + g * 0.3F;
			poseStack.scale(s, s, s);
		}

		boolean renderWhite = (int) fuse / 5 % 2 == 0;
		int overlay = renderWhite ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10) : OverlayTexture.NO_OVERLAY;

		nodeCollector.submitModel(
				this.model, state, poseStack, RenderTypes.entityCutout(state.isSuper ? SUPER_BOMB_TEXTURE : BOMB_TEXTURE),
				state.lightCoords, overlay, -1, null, state.outlineColor, null
		);
		poseStack.popPose();
		super.submit(state, poseStack, nodeCollector, camera);
	}

	@Override
	public BombRenderState createRenderState() {
		return new BombRenderState();
	}

	@Override
	public void extractRenderState(Bomb bomb, BombRenderState state, float partialTicks) {
		super.extractRenderState(bomb, state, partialTicks);
		state.isSuper = bomb.isSuper();
		state.fuseRemainingInTicks = bomb.getFuse() - partialTicks + 1.0F;
	}
}
