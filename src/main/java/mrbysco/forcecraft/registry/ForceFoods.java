package mrbysco.forcecraft.registry;

import net.minecraft.item.Food;

public class ForceFoods {
	public static final Food FORTUNE_COOKIE = (new Food.Builder()).hunger(2).saturation(0.1F).setAlwaysEdible().build();
	public static final Food SOUL_WAFER = (new Food.Builder()).hunger(2).saturation(1.0F).build();
}
