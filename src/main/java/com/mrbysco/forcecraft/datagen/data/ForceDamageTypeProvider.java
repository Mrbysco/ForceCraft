package com.mrbysco.forcecraft.datagen.data;

import com.mrbysco.forcecraft.registry.ForceDamageTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class ForceDamageTypeProvider {
	public static void bootstrap(BootstapContext<DamageType> context) {
		context.register(ForceDamageTypes.BLEEDING, new DamageType("instrumentalmobs.bleeding", 0.0F));
		context.register(ForceDamageTypes.LIQUID_FORCE, new DamageType("instrumentalmobs.liquid_force", 0.0F, DamageEffects.DROWNING));
	}
}
