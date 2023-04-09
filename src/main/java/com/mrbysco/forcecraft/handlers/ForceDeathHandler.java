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
import net.minecraftforge.registries.ForgeRegistries;

public class ForceDeathHandler {
	private static final String SPAWNED_TAG = "LiquidForceSpawned";

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		final LivingEntity livingEntity = event.getEntity();
		final Level level = event.getEntity().level;
		if (level.isClientSide) return;

		if (event.getSource() != null && event.getSource() == ForceCraft.LIQUID_FORCE_DAMAGE) {
			// Killed by Liquid Force
			if (livingEntity instanceof Mob mob) {
				CompoundTag mobData = mob.getPersistentData();
				if (!mobData.contains(SPAWNED_TAG)) {
					if (ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType()).equals(ForgeRegistries.ENTITY_TYPES.getKey(EntityType.CREEPER))) {
						CreeperTotEntity totEntity = ForceEntities.CREEPER_TOT.get().create(level);
						if (totEntity != null) {
							totEntity.absMoveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
							CompoundTag persistentData = totEntity.getPersistentData();
							persistentData.putBoolean(SPAWNED_TAG, true);
							level.addFreshEntity(totEntity);
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
							Mob childMob = (Mob) mob.getType().create(level);
							if (childMob != null) {
								childMob.setBaby(true);
								childMob.absMoveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());

								CompoundTag persistentData = childMob.getPersistentData();
								persistentData.putBoolean(SPAWNED_TAG, true);
								level.addFreshEntity(childMob);
							}
						}
					}
				}
			}
		}
	}
}
