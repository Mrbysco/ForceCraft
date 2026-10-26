package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceAttachments;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

@EventBusSubscriber
public class BaneHandler {
	@SubscribeEvent
	public static void onEnderTeleportEvent(EntityTeleportEvent.EnderEntity event) {
		if (event.getEntity() instanceof EnderMan enderman) {
			if (enderman.hasData(ForceAttachments.BANE)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityCreation(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide() && event.loadedFromDisk()) {
			if (event.getEntity() instanceof Creeper creeper) {
				if (creeper.hasData(ForceAttachments.BANE)) {
					creeper.setSwellDir(-1);
					creeper.getEntityData().set(Creeper.DATA_IS_IGNITED, false);
					creeper.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof SwellGoal);
				}
			}
		}
	}
}
