package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.RemoveBinomial;

public class DurabilityUtil {
	public static int processForceDurabilityChanges(ServerLevel level, ItemStack stack, int amount) {
		if (stack.has(ForceComponents.FORCE_STORED)) {
			amount = ForceUtil.damageForceItem(stack, amount);
		}
		if (stack.has(ForceComponents.STURDY)) {
			int sturdyLevel = stack.getOrDefault(ForceComponents.STURDY, 1);
			RemoveBinomial removeBinomial = new RemoveBinomial(
					new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F),
							LevelBasedValue.perLevel(2.0F, 1.0F)
					)
			);
			amount = Mth.floor(removeBinomial.process(sturdyLevel, level.getRandom(), (float) amount));
		}

		return amount;
	}
}