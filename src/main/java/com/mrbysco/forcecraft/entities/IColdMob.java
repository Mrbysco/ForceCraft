package com.mrbysco.forcecraft.entities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public interface IColdMob {
	ResourceLocation getOriginal();

	default void transformMob(LivingEntity livingEntity, Level entityWorld) {
		if (livingEntity instanceof IColdMob) {
			ResourceLocation originalLocation = ((IColdMob) livingEntity).getOriginal();
			Entity replacementMob = ForgeRegistries.ENTITY_TYPES.getValue(originalLocation).create(entityWorld);
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
