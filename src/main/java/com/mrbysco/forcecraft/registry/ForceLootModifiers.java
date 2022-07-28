package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.lootmodifiers.SmeltingModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

public class ForceLootModifiers {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	private static final RegistryObject<Codec<? extends IGlobalLootModifier>> SMELTING = GLM.register("smelting", SmeltingModifier.CODEC);
}
