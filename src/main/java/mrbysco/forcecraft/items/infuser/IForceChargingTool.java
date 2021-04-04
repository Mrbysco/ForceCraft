package mrbysco.forcecraft.items.infuser;

import net.minecraft.item.ItemStack;

public interface IForceChargingTool {

	static final int FORCE_DMG_RATIO = 25;

	default int damageItem(ItemStack stack, int amount) {
		int forceDrain = amount * FORCE_DMG_RATIO;

		ForceToolData fd = new ForceToolData(stack);
		int actual = Math.min(fd.getForce(), forceDrain);
		fd.setForce(fd.getForce() - actual);
		fd.write(stack);

		int diff = forceDrain - actual;

		return diff / FORCE_DMG_RATIO;
	}

}
