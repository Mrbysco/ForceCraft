package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForceDeathHandler {
	private static final String SPAWNED_TAG = "LiquidForceSpawned";

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		Level world = event.getEntity().level;
		if (world.isClientSide) return;

		if (event.getSource() != null && event.getSource() == ForceCraft.LIQUID_FORCE_DAMAGE) {
			// Killed by Liquid Force
			if (livingEntity instanceof Mob mob) {
				CompoundTag mobData = mob.getPersistentData();
				if (!mobData.contains(SPAWNED_TAG)) {
					if (livingEntity.getType().getRegistryName().equals(EntityType.CREEPER.getRegistryName())) {
						CreeperTotEntity totEntity = ForceEntities.CREEPER_TOT.get().create(world);
						if (totEntity != null) {
							totEntity.absMoveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
							CompoundTag persistentData = totEntity.getPersistentData();
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
							Mob childMob = (Mob) mob.getType().create(world);
							if (childMob != null) {
								childMob.setBaby(true);
								childMob.absMoveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());

								CompoundTag persistentData = childMob.getPersistentData();
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
