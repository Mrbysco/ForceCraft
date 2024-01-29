package com.mrbysco.forcecraft.entities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface IColdMob {
	ResourceLocation getOriginal();

	default void transformMob(LivingEntity livingEntity, Level entityWorld) {
		if (livingEntity instanceof IColdMob) {
			ResourceLocation originalLocation = ((IColdMob) livingEntity).getOriginal();
			Entity replacementMob = BuiltInRegistries.ENTITY_TYPE.get(originalLocation).create(entityWorld);
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

	;
}
