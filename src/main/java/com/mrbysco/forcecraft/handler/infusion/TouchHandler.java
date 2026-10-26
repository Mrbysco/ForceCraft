package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.enchanting.EnchantedBlockLootEvent;

@EventBusSubscriber
public class TouchHandler {
	@SubscribeEvent
	public static void onBlockLoot(EnchantedBlockLootEvent event) {
		if (!event.getEnchantment().is(Enchantments.SILK_TOUCH)) return; // Only run once
		if (!event.getTool().has(ForceComponents.TOUCH)) return;

		event.setEnchantmentLevel(event.getEnchantmentLevel() + 1);
	}
}
