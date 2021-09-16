package com.mrbysco.forcecraft.compat.patchouli;

import net.minecraft.util.ResourceLocation;

public class PatchouliCompat {
	public static void openBook() {
		vazkii.patchouli.api.PatchouliAPI.get().openBookGUI(new ResourceLocation("forcecraft:force_and_you"));
	}
}
