package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.item.tool.ForceBowItem;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class BleedHandler {
	@SubscribeEvent
	public static void onDamage(LivingDamageEvent.Post event) {
		ItemStack weapon = event.getSource().getWeaponItem();
		if (weapon == null || weapon.isEmpty()) return;
		if (weapon.getItem() instanceof ForceBowItem) return;
		int bleedLevel = weapon.getOrDefault(ForceComponents.BLEED, 0);
		if (bleedLevel == 0) return;

		LivingEntity target = event.getEntity();
		if (!target.isAlive()) return;

		target.addEffect(new MobEffectInstance(ForceEffects.BLEEDING, 100, bleedLevel), event.getSource().getEntity());
	}
}
