package com.mrbysco.forcecraft.items.infuser;

import net.minecraft.world.item.ItemStack;

public interface IForceChargingTool {

	static final int FORCE_DMG_RATIO = 25;

	default int damageItem(ItemStack stack, int amount) {
		if (!stack.isDamageableItem()) return 0;
		int forceDrain = amount * FORCE_DMG_RATIO;

		ForceToolData fd = new ForceToolData(stack);
		int actual = Math.min(fd.getForce(), forceDrain);
		fd.setForce(fd.getForce() - actual);

		int diff = forceDrain - actual;

		return diff / FORCE_DMG_RATIO;
	}

}
