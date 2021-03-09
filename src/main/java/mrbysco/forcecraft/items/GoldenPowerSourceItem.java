package mrbysco.forcecraft.items;

import net.minecraft.item.ItemStack;

public class GoldenPowerSourceItem extends BaseItem{
	public GoldenPowerSourceItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 2000;
	}
}
