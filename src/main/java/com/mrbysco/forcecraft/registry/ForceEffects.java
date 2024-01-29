package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.effects.BleedingEffect;
import com.mrbysco.forcecraft.effects.MagnetEffect;
import com.mrbysco.forcecraft.effects.ShakeEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForceEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Reference.MOD_ID);

	public static final Supplier<MobEffect> BLEEDING = EFFECTS.register("bleeding", BleedingEffect::new);
	public static final Supplier<MobEffect> MAGNET = EFFECTS.register("magnet", MagnetEffect::new);
	public static final Supplier<MobEffect> SHAKING = EFFECTS.register("shaking", ShakeEffect::new);
}
