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
		World world = event.getEntity().world;
		if (world.isRemote) return;

		if(event.getSource() != null && event.getSource() == ForceCraft.LIQUID_FORCE_DAMAGE) {
			// Killed by Liquid Force
			if(livingEntity instanceof MobEntity) {
				MobEntity mob = (MobEntity)livingEntity;
				CompoundNBT mobData = mob.getPersistentData();
				if(!mobData.contains(SPAWNED_TAG)) {
					if(livingEntity.getType().getRegistryName().equals(EntityType.CREEPER.getRegistryName())) {
						CreeperTotEntity totEntity = ForceEntities.CREEPER_TOT.get().create(world);
						if(totEntity != null) {
							totEntity.setPositionAndRotation(mob.getPosX(), mob.getPosY(), mob.getPosZ(), mob.rotationYaw, mob.rotationPitch);
							CompoundNBT persistentData = totEntity.getPersistentData();
							persistentData.putBoolean(SPAWNED_TAG, true);
							world.addEntity(totEntity);
						}
					} else {
						if(!mob.isChild()) {
							mob.getArmorInventoryList().forEach((stack) -> {
								if(!stack.isEmpty()) {
									mob.entityDropItem(stack.copy());
									stack.shrink(64);
								}
							});
							mob.getHeldEquipment().forEach((stack) -> {
								if(!stack.isEmpty()) {
									mob.entityDropItem(stack.copy());
									stack.shrink(64);
								}
							});
							MobEntity childMob = (MobEntity) mob.getType().create(world);
							if(childMob != null) {
								childMob.setChild(true);
								childMob.setPositionAndRotation(mob.getPosX(), mob.getPosY(), mob.getPosZ(), mob.rotationYaw, mob.rotationPitch);

								CompoundNBT persistentData = childMob.getPersistentData();
								persistentData.putBoolean(SPAWNED_TAG, true);
								world.addEntity(childMob);
							}
						}
					}
				}
			}
		}
	}
}
