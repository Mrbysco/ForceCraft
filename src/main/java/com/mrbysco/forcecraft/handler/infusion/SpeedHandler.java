package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

@EventBusSubscriber
public class SpeedHandler {
	private static final Identifier STEP_ID = Reference.modLoc("force_speed_step");

	@SubscribeEvent
	public static void onItemAttributes(ItemAttributeModifierEvent event) {
		ItemStack stack = event.getItemStack();
		int level = stack.getOrDefault(ForceComponents.SPEED, 0);
		if (level == 0) return;
		if (stack.is(ForceRegistry.FORCE_ROD)) return;

		double bonus = new LevelBasedValue.LevelsSquared(1.0F).calculate(level);

		if (stack.is(ForceTags.Items.FORCE_ARMOR)) {
			Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
			if (equippable == null) return;
			EquipmentSlot slot = equippable.slot();

			event.addModifier(
					Attributes.STEP_HEIGHT,
					new AttributeModifier(STEP_ID, 0.4, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.bySlot(slot)
			);

			event.addModifier(
					Attributes.MINING_EFFICIENCY,
					new AttributeModifier(Reference.modLoc("force_speed_" + slot.getName()), bonus, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.bySlot(slot)
			);

			event.addModifier(
					Attributes.MOVEMENT_SPEED,
					new AttributeModifier(Reference.modLoc("force_speed_move_" + slot.getName()), 0.05 * level, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
					EquipmentSlotGroup.bySlot(slot)
			);
			event.addModifier(
					Attributes.FLYING_SPEED,
					new AttributeModifier(Reference.modLoc("force_speed_flying_" + slot.getName()), 0.05 * level, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
					EquipmentSlotGroup.bySlot(slot)
			);
		} else {
			event.addModifier(
					Attributes.MINING_EFFICIENCY,
					new AttributeModifier(Reference.modLoc("force_speed"), bonus, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
			);
		}
	}
}
