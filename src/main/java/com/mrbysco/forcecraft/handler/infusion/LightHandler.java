package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class LightHandler {

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		ItemStack weapon = event.getSource().getWeaponItem();
		if (weapon == null || weapon.isEmpty()) return;
		int lightLevel = weapon.getOrDefault(ForceComponents.LIGHT, 0);
		if (lightLevel <= 0) return;

		LivingEntity livingEntity = event.getEntity();
		if (livingEntity.is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
			event.setAmount(event.getAmount() * LevelBasedValue.perLevel(2.5F).calculate(lightLevel));
		}
	}
}
