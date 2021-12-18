package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.capablilities.CapabilityHandler;
import com.mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import com.mrbysco.forcecraft.entities.projectile.ForceArrowEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class LootingHandler {

	@SubscribeEvent
	public void onLooting(LootingLevelEvent event) {
		final DamageSource source = event.getDamageSource();
		if (source == null || source.getEntity() == null) {
			return;
		}

		int level = event.getLootingLevel();

		IPlayerModifier playerModifier = source.getEntity().getCapability(CAPABILITY_PLAYERMOD).orElse(null);
		int customLevel = 0;
		if(playerModifier != null) {
			customLevel += playerModifier.getLuckLevel();
		}

		if(source.getDirectEntity() instanceof ForceArrowEntity) {
			ForceArrowEntity forceArrow = (ForceArrowEntity) source.getDirectEntity();
			customLevel += forceArrow.getLuck();
		}

		if(customLevel > 4) {
			customLevel = 4;
		}
		level += customLevel;

		event.setLootingLevel(level);
	}

	@SubscribeEvent
	public void onTreasureDrop(LivingDropsEvent event) {
		if (event.getSource() == null || event.getSource().getEntity() == null) {
			return;
		}

		Entity source = event.getSource().getEntity();
		if(source instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) source;
			player.getMainHandItem().getCapability(CapabilityHandler.CAPABILITY_TOOLMOD).ifPresent(cap -> {
				if(cap.hasTreasure()) {
					Random rand = player.getRandom();
					int looting = event.getLootingLevel();
					LivingEntity entity = event.getEntityLiving();
					BlockPos entityPos = entity.blockPosition();

					int chanceMax = 20;
					if(looting > 0) {
						chanceMax = chanceMax / looting;
						if(chanceMax < 0) {
							chanceMax = 1;
						}
					}
					float dropChance = rand.nextInt(chanceMax);
					if(dropChance == 0) {
						if(entity.isInvertedHealAndHarm()) {
							event.getDrops().add(new ItemEntity(entity.level, entityPos.getX(), entityPos.getY(), entityPos.getZ(), new ItemStack(ForceRegistry.UNDEATH_CARD.get(), rand.nextInt(Math.max(1, looting)) + 1)));
						} else {
							if(entity instanceof MonsterEntity) {
								event.getDrops().add(new ItemEntity(entity.level, entityPos.getX(), entityPos.getY(), entityPos.getZ(), new ItemStack(ForceRegistry.DARKNESS_CARD.get(), rand.nextInt(Math.max(1, looting)) + 1)));
							} else {
								event.getDrops().add(new ItemEntity(entity.level, entityPos.getX(), entityPos.getY(), entityPos.getZ(), new ItemStack(ForceRegistry.LIFE_CARD.get(), rand.nextInt(Math.max(1, looting)) + 1)));
							}
						}
					}
				}
			});
		}
	}
}
