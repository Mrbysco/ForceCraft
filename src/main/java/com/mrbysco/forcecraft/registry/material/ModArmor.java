package com.mrbysco.forcecraft.registry.material;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmor {

	public static final Holder<ArmorMaterial> FORCE = register(
			"force",
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 3);
				map.put(ArmorItem.Type.LEGGINGS, 4);
				map.put(ArmorItem.Type.CHESTPLATE, 6);
				map.put(ArmorItem.Type.HELMET, 3);
				map.put(ArmorItem.Type.BODY, 6);
			}),
			0,
			SoundEvents.ARMOR_EQUIP_IRON,
			4.0F,
			0.1F,
			() -> Ingredient.of(Items.NETHERITE_INGOT)
	);

	private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue,
	                                              Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
	                                              Supplier<Ingredient> repairIngredient) {
		List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Reference.modLoc(name)));
		return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
	}

	private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue,
	                                              Holder<SoundEvent> arequipSound, float toughness, float knockbackResistance,
	                                              Supplier<Ingredient> repairIngredient, List<ArmorMaterial.Layer> armorLayers) {
		EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);
		for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
			enummap.put(armoritem$type, defense.get(armoritem$type));
		}

		return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, Reference.modLoc(name),
				new ArmorMaterial(enummap, enchantmentValue, arequipSound, repairIngredient, armorLayers,
						toughness, knockbackResistance
				)
		);
	}
}
