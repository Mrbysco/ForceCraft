package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.effects.BleedingEffect;
import com.mrbysco.forcecraft.effects.MagnetEffect;
import com.mrbysco.forcecraft.effects.ShakeEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForceEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Reference.MOD_ID);

	public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding", () -> new BleedingEffect());
	public static final RegistryObject<MobEffect> MAGNET = EFFECTS.register("magnet", () -> new MagnetEffect());
	public static final RegistryObject<MobEffect> SHAKING = EFFECTS.register("shaking", () -> new ShakeEffect());
}
