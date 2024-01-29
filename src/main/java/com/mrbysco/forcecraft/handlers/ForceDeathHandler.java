package com.mrbysco.forcecraft.handlers;

import com.mrbysco.forcecraft.entities.CreeperTotEntity;
import com.mrbysco.forcecraft.registry.ForceDamageTypes;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class ForceDeathHandler {
	private static final String SPAWNED_TAG = "LiquidForceSpawned";

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		final LivingEntity livingEntity = event.getEntity();
		final Level level = event.getEntity().level();
		if (level.isClientSide) return;

		if (event.getSource() != null && event.getSource().is(ForceDamageTypes.LIQUID_FORCE)) {
			// Killed by Liquid Force
			if (livingEntity instanceof Mob mob) {
				CompoundTag mobData = mob.getPersistentData();
				if (!mobData.contains(SPAWNED_TAG)) {
					if (BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CREEPER))) {
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
