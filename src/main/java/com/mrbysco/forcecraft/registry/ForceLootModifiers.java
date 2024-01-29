package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.lootmodifiers.SmeltingModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ForceLootModifiers {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	private static final Supplier<Codec<? extends IGlobalLootModifier>> SMELTING = GLM.register("smelting", SmeltingModifier.CODEC);
}
