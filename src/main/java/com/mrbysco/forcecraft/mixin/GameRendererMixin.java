package com.mrbysco.forcecraft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.forcecraft.client.ShakeUtil;
import net.minecraft.client.DeltaTracker;
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
	Minecraft minecraft;

	@Inject(method = "renderLevel(Lnet/minecraft/client/DeltaTracker;)V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Options;screenEffectScale()Lnet/minecraft/client/OptionInstance;",
			shift = At.Shift.AFTER,
			ordinal = 0))
	public void renderLevel(DeltaTracker deltaTracker, CallbackInfo ci, @Local PoseStack poseStack) {
		ShakeUtil.shakeScreen(minecraft, deltaTracker, poseStack);
	}
}