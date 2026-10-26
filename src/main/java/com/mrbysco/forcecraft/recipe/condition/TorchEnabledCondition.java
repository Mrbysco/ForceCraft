package com.mrbysco.forcecraft.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.config.ForceConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class TorchEnabledCondition implements ICondition {
	public static final TorchEnabledCondition INSTANCE = new TorchEnabledCondition();
	public static final MapCodec<TorchEnabledCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	@Override
	public MapCodec<? extends ICondition> codec() {
		return ForceConditions.TIME_TORCH_ENABLED.get();
	}

	@Override
	public boolean test(IContext context) {
		return ForceConfig.COMMON.timeTorchEnabled.get();
	}
}