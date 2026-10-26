package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.enchanting.EnchantedBlockLootEvent;
import net.neoforged.neoforge.event.enchanting.EnchantedEntityLootEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class HeatHandler {
	@SubscribeEvent
	public static void onBlockLoot(EnchantedBlockLootEvent event) {
		if (!event.getEnchantment().is(Enchantments.FIRE_ASPECT)) return; // Only run once
		if (!event.getTool().has(ForceComponents.HEAT)) return;

		event.setEnchantmentLevel(event.getEnchantmentLevel() + 1);
	}

	@SubscribeEvent
	public static void onEntityLoot(EnchantedEntityLootEvent event) {
		if (!event.getEnchantment().is(Enchantments.FIRE_ASPECT)) return; // Only run once

		DamageSource source = event.getDamageSource();
		if (source == null) return;

		ItemStack weapon = source.getWeaponItem();
		if (weapon == null) return;
		if (!weapon.has(ForceComponents.HEAT)) return;

		event.setEnchantmentLevel(event.getEnchantmentLevel() + 1);
	}

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		ItemStack weapon = event.getSource().getWeaponItem();
		if (weapon == null || weapon.isEmpty()) return;
		if (!weapon.getOrDefault(ForceComponents.HEAT, false)) return;

		event.getEntity().igniteForSeconds(LevelBasedValue.perLevel(4.0F).calculate(1));
	}
}
