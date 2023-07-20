package com.mrbysco.forcecraft;

import com.mrbysco.forcecraft.registry.ForceDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class Reference {

	public enum MODIFIERS {
		MOD_SPEED,
		MOD_EXP,
		MOD_CHARGE,
		MOD_CHARGEII,
		MOD_LUMBERJACK,
		MOD_GRAFTING,
		MOD_HOLDING,
		MOD_HEALING,
		MOD_ENDER,
		MOD_CAMO,
		MOD_SIGHT,
		MOD_RAINBOW,
		MOD_SOUL,
		MOD_LIGHT,
		MOD_BANE,
		MOD_BLEED,
		MOD_FORCE,
		MOD_DAMAGE,
		MOD_STURDY,
		MOD_TOUCH,
		MOD_HEAT,
		MOD_LUCK,
		MOD_CRAFT,
		MOD_GRINDING,
		MOD_REPAIR,
		MOD_WING,
		MOD_FREEZING,
		MOD_TREASURE,
		MOD_IMPERVIOUS
	}

	public static final String MOD_ID = "forcecraft";
	public static final int numTools = 11;

	public static DamageSource causeBleedingDamage(Entity entity) {
		return entity.damageSources().source(ForceDamageTypes.BLEEDING, entity);
	}

	public static DamageSource causeLiquidForceDamage(Entity entity) {
		return entity.damageSources().source(ForceDamageTypes.LIQUID_FORCE, entity);
	}
}
