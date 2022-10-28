package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.registry.ForceEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class MobUtil {
	private static final int BLEEDING_SECONDS = 20;

	public static void addBleedingEffect(int level, LivingEntity target, Entity trueSource) {
		int adjustedLevel = level;
		if (trueSource != null && trueSource instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;
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
			target.addEffect(new EffectInstance(ForceEffects.BLEEDING.get(), BLEEDING_SECONDS * adjustedLevel, 0, false, true));
		}
	}
}
