package mrbysco.forcecraft.handlers;

import mrbysco.forcecraft.capablilities.CapabilityHandler;
import mrbysco.forcecraft.capablilities.playermodifier.IPlayerModifier;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_PLAYERMOD;

public class LootingHandler {

	@SubscribeEvent
	public void onLooting(LootingLevelEvent event) {
		if (event.getDamageSource() == null || event.getDamageSource().getTrueSource() == null) {
			return;
		}

		int level = event.getLootingLevel();

		IPlayerModifier playerModifier = event.getDamageSource().getTrueSource().getCapability(CAPABILITY_PLAYERMOD).orElse(null);
		if(playerModifier != null) {
			level += playerModifier.getLuckLevel();
		}

		event.setLootingLevel(level);
	}

	@SubscribeEvent
	public void onTreasureDrop(LivingDropsEvent event) {
		if (event.getSource() == null || event.getSource().getTrueSource() == null || !event.isRecentlyHit()) {
			return;
		}

		Entity source = event.getSource().getTrueSource();
		if(source instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) source;
			player.getActiveItemStack().getCapability(CapabilityHandler.CAPABILITY_TOOLMOD).ifPresent(cap -> {
				if(cap.hasTreasure()) {
					Random rand = player.getRNG();
					int looting = event.getLootingLevel();
					LivingEntity entity = event.getEntityLiving();
					BlockPos entityPos = entity.getPosition();

					float dropChance = rand.nextInt(20 - (Math.max(looting, 10)));
					if(dropChance == 0) {
						if(entity.isEntityUndead()) {
							event.getDrops().add(new ItemEntity(entity.world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), new ItemStack(ForceRegistry.UNDEATH_CARD.get(), rand.nextInt(Math.max(1, looting)) + 1)));
						} else {
							if(entity instanceof MonsterEntity) {
								event.getDrops().add(new ItemEntity(entity.world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), new ItemStack(ForceRegistry.DARKNESS_CARD.get(), rand.nextInt(Math.max(1, looting)) + 1)));
							} else {
								event.getDrops().add(new ItemEntity(entity.world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), new ItemStack(ForceRegistry.LIFE_CARD.get(), rand.nextInt(Math.max(1, looting)) + 1)));
							}
						}
					}
				}
			});
		}
	}
}
