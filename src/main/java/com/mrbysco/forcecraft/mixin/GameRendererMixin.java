package com.mrbysco.forcecraft.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.client.ShakeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Options;screenEffectScale()Lnet/minecraft/client/OptionInstance;",
			shift = At.Shift.BEFORE,
			ordinal = 0))
	public void renderLevel(float partialTicks, long finishTimeNano, PoseStack poseStack, CallbackInfo ci) {
		ShakeUtil.shakeScreen(minecraft, partialTicks, finishTimeNano, poseStack);
	}
}