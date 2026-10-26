package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class ForceDamageTypes {
	public static final ResourceKey<DamageType> BLEEDING = register("bleeding");
	public static final ResourceKey<DamageType> LIQUID_FORCE = register("liquid_force");

	private static ResourceKey<DamageType> register(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Reference.modLoc(name));
	}

	public static void bootstrap(BootstrapContext<DamageType> context) {
		context.register(ForceDamageTypes.BLEEDING, new DamageType("forcecraft.bleeding", 0.0F));
		context.register(ForceDamageTypes.LIQUID_FORCE, new DamageType("forcecraft.liquid_force", 0.0F, DamageEffects.DROWNING));
	}
}
