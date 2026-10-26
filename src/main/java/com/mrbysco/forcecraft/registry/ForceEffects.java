package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.effects.BleedingEffect;
import com.mrbysco.forcecraft.effects.FrozenEffect;
import com.mrbysco.forcecraft.effects.TimeEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ForceEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Reference.MOD_ID);

	public static final DeferredHolder<MobEffect, MobEffect> FROZEN = EFFECTS.register("frozen", FrozenEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> BLEEDING = EFFECTS.register("bleeding", BleedingEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> TIME_SLOWDOWN = EFFECTS.register("time_slowdown", () -> new TimeEffect(false));
	public static final DeferredHolder<MobEffect, MobEffect> TIME_SPEEDUP = EFFECTS.register("time_speedup", () -> new TimeEffect(true));
}
