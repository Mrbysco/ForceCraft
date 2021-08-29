package com.mrbysco.forcecraft.registry;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.effects.BleedingEffect;
import com.mrbysco.forcecraft.effects.MagnetEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceEffects {
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MOD_ID);

	public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding", () -> new BleedingEffect());
	public static final RegistryObject<Effect> MAGNET = EFFECTS.register("magnet", () -> new MagnetEffect());
}
