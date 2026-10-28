package com.mrbysco.forcecraft.registry;

import net.minecraft.world.food.FoodProperties;

public class ForceFoods {
	public static final FoodProperties FORTUNE_COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).alwaysEdible().build();
	public static final FoodProperties SNOW_COOKIE = (new FoodProperties.Builder()).nutrition(-2).saturationModifier(0.1F).alwaysEdible().build();
}
