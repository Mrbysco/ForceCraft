package com.mrbysco.forcecraft.recipe.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.config.ConfigHandler;
import net.neoforged.neoforge.common.conditions.ICondition;

public class TorchEnabledCondition implements ICondition {
	public static final TorchEnabledCondition INSTANCE = new TorchEnabledCondition();
	public static final Codec<TorchEnabledCondition> CODEC = Codec.unit(INSTANCE).stable();

	@Override
	public Codec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(IContext context) {
		return ConfigHandler.COMMON.timeTorchEnabled.get();
	}
}