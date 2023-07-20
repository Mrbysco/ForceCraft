package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ForceDamageTypes {
	public static final ResourceKey<DamageType> BLEEDING = register("bleeding");
	public static final ResourceKey<DamageType> LIQUID_FORCE = register("liquid_force");

	private static ResourceKey<DamageType> register(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Reference.MOD_ID, name));
	}
}
