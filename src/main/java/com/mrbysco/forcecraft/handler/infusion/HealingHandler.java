package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class HealingHandler {
	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		ItemStack weapon = event.getSource().getWeaponItem();
		if (weapon == null || weapon.isEmpty()) return;
		int healLevel = weapon.getOrDefault(ForceComponents.HEALING, 0);
		if (healLevel <= 0) return;

		LivingEntity livingEntity = event.getEntity();

		if (livingEntity.is(EntityTypeTags.UNDEAD)) {
			livingEntity.hurt(livingEntity.level().damageSources().magic(), healLevel);
		} else {
			livingEntity.heal(healLevel);
		}
	}
}
