package com.mrbysco.forcecraft.data;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;

public enum TimeMode implements StringRepresentable {
	STOP("forcecraft.time_mode.stop"),
	SLOW("forcecraft.time_mode.slow"),
	FAST("forcecraft.time_mode.fast"),
	HYPER("forcecraft.time_mode.hyper");

	public static final Codec<TimeMode> CODEC = StringRepresentable.fromEnum(TimeMode::values);

	private final String langKey;

	TimeMode(String langKey) {
		this.langKey = langKey;
	}

	public MobEffectInstance createEffect(int duration) {
		return switch (this) {
			case STOP -> new MobEffectInstance(ForceEffects.TIME_SLOWDOWN, duration, 0);
			case SLOW -> new MobEffectInstance(ForceEffects.TIME_SLOWDOWN, duration, 1);
			case FAST -> new MobEffectInstance(ForceEffects.TIME_SPEEDUP, duration * 2, 0);
			case HYPER -> new MobEffectInstance(ForceEffects.TIME_SPEEDUP, duration * 5, 3);
		};
	}

	public int getUsageCost() {
		return switch (this) {
			case STOP -> 1;
			case SLOW -> 2;
			case FAST -> 3;
			case HYPER -> 4;
		};
	}

	public boolean isSlowing() {
		return this == STOP || this == SLOW;
	}

	public TimeMode getNext() {
		TimeMode[] modes = values();
		return modes[(ordinal() + 1) % modes.length];
	}

	public Component getName() {
		return Component.translatable(langKey);
	}

	@Override
	public String getSerializedName() {
		return String.valueOf(ordinal());
	}
}
