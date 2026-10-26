package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class FalseHandler {

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		ItemStack weapon = event.getSource().getWeaponItem();
		if (weapon == null || weapon.isEmpty()) return;
		if (!weapon.getOrDefault(ForceComponents.FALSE, false)) return;

		float amount = event.getAmount();
		float health = event.getEntity().getHealth();
		if (health - amount <= 0) {
			event.setAmount(health - 1.0F);
		}
	}
}
