package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.lootmodifiers.SmeltingModifier;
import com.mrbysco.forcecraft.lootmodifiers.SmeltingModifier.Serializer;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceLootModifiers {
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	private static final RegistryObject<Serializer> SMELTING = GLM.register("smelting", SmeltingModifier.Serializer::new);
}
