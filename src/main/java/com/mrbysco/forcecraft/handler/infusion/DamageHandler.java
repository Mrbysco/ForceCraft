package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class DamageHandler {
	@SubscribeEvent
	public static void onItemAttributes(ItemAttributeModifierEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.is(ForceTags.Items.FORCE_ARMOR)) return;

		int level = stack.getOrDefault(ForceComponents.DAMAGE, 0);
		if (level == 0) return;

		event.addModifier(
				Attributes.ATTACK_DAMAGE,
				new AttributeModifier(Reference.modLoc("force_damage"), 0.5 + 0.5 * level, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND
		);
	}

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) return;
		if (event.getSource().getDirectEntity() != player) return;
		if (!player.getMainHandItem().isEmpty()) return;

		boolean hasDamage = false;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
			if (player.getItemBySlot(slot).has(ForceComponents.DAMAGE)) {
				hasDamage = true;
				break;
			}
		}
		if (!hasDamage) return;

		event.setAmount(event.getAmount() + 1.0F);
	}
}
