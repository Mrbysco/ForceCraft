package com.mrbysco.forcecraft.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TimeEffect extends MobEffect {
	private final boolean speedUp;
	private boolean isTickingExtra = false;

	public TimeEffect(boolean speedUp) {
		super(MobEffectCategory.HARMFUL, 0xE8C547);
		this.speedUp = speedUp;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplification) {
		if (speedUp && !isTickingExtra) {
			isTickingExtra = true;
			try {
				for (int i = 0; i < amplification + 1; i++) livingEntity.tick();
			} finally {
				isTickingExtra = false;
			}
		}

		return true;
	}
}
