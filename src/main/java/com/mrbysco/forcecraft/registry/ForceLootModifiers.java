package com.mrbysco.forcecraft.registry;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.loot.MobChunkModifier;
import com.mrbysco.forcecraft.loot.SmeltModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ForceLootModifiers {
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> SMELTING = GLM.register("smelting", SmeltModifier.CODEC);
	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> MOB_CHUNKS = GLM.register("mob_chunks", MobChunkModifier.CODEC);
}
