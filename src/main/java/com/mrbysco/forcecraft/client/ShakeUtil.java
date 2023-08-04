package com.mrbysco.forcecraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ShakeUtil {

	public static void shakeScreen(Minecraft minecraft, float partialTicks, long finishTimeNano, PoseStack poseStack) {
		float effectScale = minecraft.options.screenEffectScale().get().floatValue();
		Player player = (Player) minecraft.getCameraEntity();
		if (player != null && player.hasEffect(ForceEffects.SHAKING.get()) & effectScale > 0.0F) {
			float f = player.walkDist - player.walkDistO;
			float f1 = -(player.walkDist + f * partialTicks);
			float f2 = Mth.lerp(partialTicks, player.oBob, player.bob);

			// Calculate screen shake offsets based on the provided strength
			float shakeAmount = effectScale * 0.005F; // Adjust this value to control the intensity of the shake
			float time = finishTimeNano / 1000.0f; // Use time for animation

			// Calculate the X-axis offset for rocking back and forth
			float offsetX = (float) Math.sin(time * 0.5f) * shakeAmount;

			// Calculate the rotation angle for smooth rotation back and forth
			float rotationAngle = ((float) Math.cos(time * 0.1F) * 2.5f) * effectScale;

			// Apply the screen shake offsets and rotation to the camera position
			poseStack.translate(offsetX, 0.0D, 0.0D);
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle));

			// Continue with the rest of the original bobView method code
			poseStack.translate(Mth.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(Mth.cos(f1 * (float) Math.PI) * f2), 0.0D);
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f1 * (float) Math.PI) * f2 * 3.0F));
			poseStack.mulPose(Vector3f.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F));
		}
	}
}
