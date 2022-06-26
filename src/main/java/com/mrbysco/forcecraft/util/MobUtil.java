package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.capabilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class MobUtil {
	private static final int BLEEDING_SECONDS = 20;

	public static void addBleedingEffect(int level, LivingEntity target, Entity trueSource) {
		int adjustedLevel = level;
		if (trueSource instanceof Player player) {
			IPlayerModifier playerCap = player.getCapability(CAPABILITY_PLAYERMOD).orElse(null);
			if (playerCap != null) {
				if (playerCap.hasBleeding()) {
					adjustedLevel += playerCap.getBleedingLevel();
				}
			}
		}
		if (adjustedLevel > 15) {
			adjustedLevel = 15;
		}

		if (adjustedLevel > 0) {
//            ForceCraft.LOGGER.info("Added BLEEDING to " + target.getName());
			target.addEffect(new MobEffectInstance(ForceEffects.BLEEDING.get(), BLEEDING_SECONDS * adjustedLevel, 0, false, true));
		}
	}
}
