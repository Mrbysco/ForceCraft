package com.mrbysco.forcecraft.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ShakeEffect extends MobEffect {
	public ShakeEffect() {
		super(MobEffectCategory.HARMFUL, 0);
	}

	@Override
	public boolean isInstantenous() {
		return false;
	}

	@Override
	public boolean isBeneficial() {
		return false;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
}
