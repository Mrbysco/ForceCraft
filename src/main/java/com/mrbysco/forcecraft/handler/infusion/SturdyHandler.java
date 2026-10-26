package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class SturdyHandler {




	// Reduce incoming damage
	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		if (event.getEntity() instanceof Player player) {
			float reducedAmount = 0.0F;
			boolean damageReduced = false;
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
				if (player.getItemBySlot(slot).has(ForceComponents.STURDY)) {
					reducedAmount += 1.0F;
					damageReduced = true;
					break;
				}
			}
			if (!damageReduced) return;
			event.setAmount(Math.max(0, event.getAmount() - reducedAmount));
		}
	}
}
