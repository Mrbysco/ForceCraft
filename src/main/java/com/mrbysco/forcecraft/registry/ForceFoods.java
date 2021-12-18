package com.mrbysco.forcecraft.registry;

import net.minecraft.world.food.FoodProperties;

public class ForceFoods {
	public static final FoodProperties FORTUNE_COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).alwaysEat().build();
	public static final FoodProperties SOUL_WAFER = (new FoodProperties.Builder()).nutrition(2).saturationMod(1.0F).build();
	public static final FoodProperties BACON = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.4F).meat().build();
	public static final FoodProperties COOKED_BACON = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).meat().build();
}
