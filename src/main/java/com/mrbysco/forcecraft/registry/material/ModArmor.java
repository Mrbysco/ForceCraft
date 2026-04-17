package com.mrbysco.forcecraft.registry.material;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

public class ModArmor {
	public static final ArmorMaterial FORCE = new ArmorMaterial(6, Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 4);
		map.put(ArmorType.CHESTPLATE, 6);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 6);
	}),
			0,
			SoundEvents.ARMOR_EQUIP_IRON,
			4.0F,
			0.1F,
			ForceTags.FORCE_REPAIR_INGREDIENTS,
			createAsset("sylvan"));

	private static ResourceKey<EquipmentAsset> createAsset(String name) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, Reference.modLoc(name));
	}
}
