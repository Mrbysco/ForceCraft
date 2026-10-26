package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.enchanting.EnchantedBlockLootEvent;
import net.neoforged.neoforge.event.enchanting.EnchantedEntityLootEvent;

@EventBusSubscriber
public class LuckHandler {
	@SubscribeEvent
	public static void onBlockLoot(EnchantedBlockLootEvent event) {
		if (!event.getEnchantment().is(Enchantments.FORTUNE)) return; // Only run once

		int luck = event.getTool().getOrDefault(ForceComponents.LUCK, 0);
		if (luck == 0) return;

		if (event.getTool().is(ItemTags.SWORDS)) return;

		event.setEnchantmentLevel(Math.max(event.getEnchantmentLevel(), luck));
	}

	@SubscribeEvent
	public static void onEntityLoot(EnchantedEntityLootEvent event) {
		if (!event.getEnchantment().is(Enchantments.FORTUNE)) return; // Only run once

		DamageSource source = event.getDamageSource();
		if (source == null) return;

		ItemStack weapon = source.getWeaponItem();
		if (weapon == null) return;

		int luck = weapon.getOrDefault(ForceComponents.LUCK, 0);
		if (luck == 0 || !weapon.is(ItemTags.SWORDS)) return;

		event.setEnchantmentLevel(Math.max(event.getEnchantmentLevel(), luck));
	}
}
