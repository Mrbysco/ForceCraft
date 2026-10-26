package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.ItemStack;

public class ForceUtil {
	public static final int FORCE_DMG_RATIO = 25;

	public static int damageForceItem(ItemStack stack, int amount) {
		if (amount <= 0 || !stack.isDamageableItem() || !stack.has(ForceComponents.FORCE_STORED)) return amount;
		int forceDrain = amount * FORCE_DMG_RATIO;

		int force = stack.getOrDefault(ForceComponents.FORCE_STORED, 0);
		int actual = Math.min(force, forceDrain);
		if (actual > 0) {
			stack.set(ForceComponents.FORCE_STORED, force - actual);
		}

		int remaining = forceDrain - actual;
		return remaining / FORCE_DMG_RATIO;
	}

	public static void chargeForce(ItemStack stack, int toCharge) {
		if (stack.is(ForceTags.Items.CAN_STORE_FORCE)) {
			int newForce = stack.getOrDefault(ForceComponents.FORCE_STORED, 0) + toCharge;
			stack.set(ForceComponents.FORCE_STORED, newForce);
		}
	}
}
