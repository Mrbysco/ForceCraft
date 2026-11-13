package com.mrbysco.forcecraft.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.forcecraft.client.ClientHandler;
import com.mrbysco.forcecraft.client.ForceRenderStateHolder;
import net.minecraft.client.gui.render.GuiItemAtlas;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GuiItemAtlas.class)
public class GuiItemAtlasMixin {

	@WrapOperation(
			method = "drawToSlot",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableScissorForRenderTypeDraws()V")
	)
	private void forcecraft$wrapEndBatchWithModifier(Operation<Void> original, @Local(argsOnly = true) ItemStackRenderState item) {
		boolean isSilhouette = ((ForceRenderStateHolder) item).forcecraft$isSilhouette();
		if (!isSilhouette) {
			original.call();
			return;
		}

		RenderSystem.pushPipelineModifier(ClientHandler.SILHOUTTE);
		try {
			original.call();
		} finally {
			RenderSystem.popPipelineModifier();
		}
	}
}
