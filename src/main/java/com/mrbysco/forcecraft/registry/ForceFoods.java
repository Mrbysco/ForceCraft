package com.mrbysco.forcecraft.registry;

import net.minecraft.item.Food;

public class ForceFoods {
	public static final Food FORTUNE_COOKIE = (new Food.Builder()).nutrition(2).saturationMod(0.1F).alwaysEat().build();
	public static final Food SOUL_WAFER = (new Food.Builder()).nutrition(2).saturationMod(1.0F).build();
	public static final Food BACON = (new Food.Builder()).nutrition(2).saturationMod(0.4F).meat().build();
	public static final Food COOKED_BACON = (new Food.Builder()).nutrition(4).saturationMod(0.8F).meat().build();
}
