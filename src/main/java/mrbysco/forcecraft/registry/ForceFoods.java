package mrbysco.forcecraft.registry;

import net.minecraft.item.Food;

public class ForceFoods {
	public static final Food FORTUNE_COOKIE = (new Food.Builder()).hunger(2).saturation(0.1F).setAlwaysEdible().build();
	public static final Food SOUL_WAFER = (new Food.Builder()).hunger(2).saturation(1.0F).build();
	public static final Food BACON = (new Food.Builder()).hunger(2).saturation(0.4F).meat().build();
	public static final Food COOKED_BACON = (new Food.Builder()).hunger(4).saturation(0.8F).meat().build();
}
