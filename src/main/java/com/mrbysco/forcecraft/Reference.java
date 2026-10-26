package com.mrbysco.forcecraft;

import com.mrbysco.forcecraft.registry.ForceDamageTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class Reference {
	public static final String MOD_ID = "forcecraft";

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static DamageSource causeBleedingDamage(Entity entity) {
		return entity.damageSources().source(ForceDamageTypes.BLEEDING, entity);
	}

	public static DamageSource causeLiquidForceDamage(Entity entity) {
		return entity.damageSources().source(ForceDamageTypes.LIQUID_FORCE, entity);
	}
}
