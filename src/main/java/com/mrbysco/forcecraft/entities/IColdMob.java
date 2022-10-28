package com.mrbysco.forcecraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public interface IColdMob {
	ResourceLocation getOriginal();

	default void transformMob(LivingEntity livingEntity, World entityWorld) {
		if (livingEntity instanceof IColdMob) {
			ResourceLocation originalLocation = ((IColdMob) livingEntity).getOriginal();
			Entity replacementMob = ForgeRegistries.ENTITIES.getValue(originalLocation).create(entityWorld);
			if (replacementMob != null) {
				replacementMob.copyPosition(livingEntity);
				UUID mobUUID = replacementMob.getUUID();
				replacementMob.restoreFrom(livingEntity);
				replacementMob.setUUID(mobUUID);

				entityWorld.addFreshEntity(replacementMob);
				livingEntity.remove(false);
			}
		}
	}

	;
}
