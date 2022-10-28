package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForceDeathHandler {
	private static final String SPAWNED_TAG = "LiquidForceSpawned";

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		World world = event.getEntity().level;
		if (world.isClientSide) return;

		if (event.getSource() != null && event.getSource() == ForceCraft.LIQUID_FORCE_DAMAGE) {
			// Killed by Liquid Force
			if (livingEntity instanceof MobEntity) {
				MobEntity mob = (MobEntity) livingEntity;
				CompoundNBT mobData = mob.getPersistentData();
				if (!mobData.contains(SPAWNED_TAG)) {
					if (livingEntity.getType().getRegistryName().equals(EntityType.CREEPER.getRegistryName())) {
						CreeperTotEntity totEntity = ForceEntities.CREEPER_TOT.get().create(world);
						if (totEntity != null) {
							totEntity.absMoveTo(mob.getX(), mob.getY(), mob.getZ(), mob.yRot, mob.xRot);
							CompoundNBT persistentData = totEntity.getPersistentData();
							persistentData.putBoolean(SPAWNED_TAG, true);
							world.addFreshEntity(totEntity);
						}
					} else {
						if (!mob.isBaby()) {
							mob.getArmorSlots().forEach((stack) -> {
								if (!stack.isEmpty()) {
									mob.spawnAtLocation(stack.copy());
									stack.shrink(64);
								}
							});
							mob.getHandSlots().forEach((stack) -> {
								if (!stack.isEmpty()) {
									mob.spawnAtLocation(stack.copy());
									stack.shrink(64);
								}
							});
							MobEntity childMob = (MobEntity) mob.getType().create(world);
							if (childMob != null) {
								childMob.setBaby(true);
								childMob.absMoveTo(mob.getX(), mob.getY(), mob.getZ(), mob.yRot, mob.xRot);

								CompoundNBT persistentData = childMob.getPersistentData();
								persistentData.putBoolean(SPAWNED_TAG, true);
								world.addFreshEntity(childMob);
							}
						}
					}
				}
			}
		}
	}
}
