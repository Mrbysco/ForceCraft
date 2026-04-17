package com.mrbysco.forcecraft.entities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface IColdMob {
	Identifier getOriginal();

	default void transformMob(LivingEntity livingEntity, Level entityWorld) {
		if (livingEntity instanceof IColdMob) {
			Identifier originalLocation = ((IColdMob) livingEntity).getOriginal();
			Entity replacementMob = BuiltInRegistries.ENTITY_TYPE.getValue(originalLocation).create(entityWorld, EntitySpawnReason.CONVERSION);
			if (replacementMob != null) {
				replacementMob.copyPosition(livingEntity);
				UUID mobUUID = replacementMob.getUUID();
				replacementMob.restoreFrom(livingEntity);
				replacementMob.setUUID(mobUUID);

				entityWorld.addFreshEntity(replacementMob);
				livingEntity.discard();
			}
		}
	}

}
