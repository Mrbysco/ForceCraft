package com.mrbysco.forcecraft.modifier;

import com.mrbysco.forcecraft.config.ForceConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum ConfigEnum {
	FALSE("invalid", () -> false),
	FORCE_ORE("force_ore", ForceConfig.COMMON.generateForceOre),
	NETHER_FORCE_ORE("force_ore_nether", ForceConfig.COMMON.generateNetherForceOre),
	FORCE_TREE("force_tree", ForceConfig.COMMON.generateForceTree);


	public final String name;
	public final Supplier<Boolean> configValue;

	ConfigEnum(String name, Supplier<Boolean> configValue) {
		this.name = name;
		this.configValue = configValue;
	}

	public boolean getValue() {
		return configValue.get();
	}

	@NotNull
	public static ConfigEnum getByName(@Nullable String value) {
		for (ConfigEnum captcha : values()) {
			if (captcha.name.equals(value)) {
				return captcha;
			}
		}
		return FALSE;
	}
}
