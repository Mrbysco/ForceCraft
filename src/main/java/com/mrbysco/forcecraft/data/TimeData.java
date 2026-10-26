package com.mrbysco.forcecraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.config.ForceConfig;

public record TimeData(long expiresAt, float rate) {
	public static final Codec<TimeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.LONG.fieldOf("expires_at").forGetter(TimeData::expiresAt),
			Codec.FLOAT.fieldOf("rate").forGetter(TimeData::rate)
	).apply(instance, TimeData::new));

	public boolean isActive(long gameTime) {
		return gameTime < expiresAt;
	}

	public int extraTicks() {
		return rate > 1.0F ? Math.round(rate) - 1 : 0;
	}

	public boolean shouldSkip(long gameTime) {
		if (rate <= 0.0F) return true;
		if (rate >= 1.0F) return false;
		int factor = Math.max(2, Math.round(1.0F / rate));
		return gameTime % factor != 0;
	}

	public static float rateFor(TimeMode mode) {
		return switch (mode) {
			case STOP -> 0.0F;
			case SLOW -> ForceConfig.COMMON.slowSpeed.get().floatValue();
			case FAST -> ForceConfig.COMMON.fastSpeed.get().floatValue();
			case HYPER -> ForceConfig.COMMON.hyperSpeed.get().floatValue();
		};
	}
}