package com.mrbysco.forcecraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.config.ForceConfig;

import java.util.ArrayList;
import java.util.List;

public record TimeData(long expiresAt, float rate, List<TimeSource> sources) {
	public record TimeSource(long pos, long expiresAt, float rate) {
		public static final Codec<TimeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.LONG.fieldOf("pos").forGetter(TimeSource::pos),
				Codec.LONG.fieldOf("expires_at").forGetter(TimeSource::expiresAt),
				Codec.FLOAT.fieldOf("rate").forGetter(TimeSource::rate)
		).apply(instance, TimeSource::new));
	}

	public static final Codec<TimeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.LONG.fieldOf("expires_at").forGetter(TimeData::expiresAt),
			Codec.FLOAT.fieldOf("rate").forGetter(TimeData::rate),
			TimeSource.CODEC.listOf().optionalFieldOf("speedSources", List.of()).forGetter(TimeData::sources)
	).apply(instance, TimeData::new));

	public TimeData(long expiresAt, float rate) {
		this(expiresAt, rate, List.of());
	}

	public static TimeData stack(TimeData existing, long gameTime, long expiresAt, float rate, long sourceId) {
		if (rate <= 1.0F) {
			return new TimeData(expiresAt, rate);
		}

		List<TimeSource> sources = new ArrayList<>();
		if (existing != null) {
			for (TimeSource source : existing.sources()) {
				if (source.pos() != sourceId && source.expiresAt() > gameTime) {
					sources.add(source);
				}
			}
		}

		if (sources.size() >= ForceConfig.COMMON.timeMaxStacks.get()) {
			return existing;
		}

		sources.add(new TimeSource(sourceId, expiresAt, rate));
		return fromSources(sources);
	}

	private static TimeData fromSources(List<TimeSource> sources) {
		long latestExpiry = 0L;
		float totalRate = 1.0F;
		for (TimeSource source : sources) {
			latestExpiry = Math.max(latestExpiry, source.expiresAt());
			totalRate += source.rate() - 1.0F;
		}
		return new TimeData(latestExpiry, totalRate, List.copyOf(sources));
	}

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