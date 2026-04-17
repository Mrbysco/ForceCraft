package com.mrbysco.forcecraft.client.extension;

import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

public class MagnetEffectExtension implements IClientMobEffectExtensions {
	@Override
	public boolean isVisibleInInventory(MobEffectInstance effect) {
		return false;
	}

	@Override
	public boolean isVisibleInGui(MobEffectInstance effect) {
		return false;
	}
}
