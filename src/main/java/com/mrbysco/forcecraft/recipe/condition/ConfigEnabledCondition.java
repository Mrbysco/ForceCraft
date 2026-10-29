package com.mrbysco.forcecraft.recipe.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.config.ForceConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ConfigEnabledCondition(String option) implements ICondition {
	public static final MapCodec<ConfigEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance
					.group(Codec.STRING.fieldOf("option").forGetter(ConfigEnabledCondition::option))
					.apply(instance, ConfigEnabledCondition::new)
	);

	@Override
	public MapCodec<? extends ICondition> codec() {
		return ForceConditions.TIME_TORCH_ENABLED.get();
	}

	@Override
	public boolean test(IContext context) {
		return switch(option) {
			case "timeTorchEnabled" -> ForceConfig.COMMON.timeTorchEnabled.get();
			case "timeRodEnabled" -> ForceConfig.COMMON.timeRodEnabled.get();
			case "timeWeaponEnabled" -> ForceConfig.COMMON.timeWeaponEnabled.get();
			default -> false;
		};
	}
}