package com.mrbysco.forcecraft.modifier;

import com.mrbysco.forcecraft.config.ConfigHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum ConfigEnum {
	FALSE("invalid", () -> false),
	FORCE_ORE("force_ore", ConfigHandler.COMMON.generateForceOre),
	FORCE_TREE("force_tree", ConfigHandler.COMMON.generateForceTree);


	public final String name;
	public final Supplier<Boolean> configValue;

	ConfigEnum(String name, Supplier<Boolean> configValue) {
		this.name = name;
		this.configValue = configValue;
	}

	public boolean getValue() {
		return configValue.get();
	}

	@Nullable
	public static ConfigEnum getByName(@Nullable String value) {
		for (ConfigEnum captcha : values()) {
			if (captcha.name.equals(value)) {
				return captcha;
			}
		}
		return FALSE;
	}
}
