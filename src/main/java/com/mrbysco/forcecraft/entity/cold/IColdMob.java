package com.mrbysco.forcecraft.entity.cold;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface IColdMob {
	EntityType<?> originalEntityType();

	default void tranformMob(LivingEntity livingEntity, Level level) {
		if (level instanceof IColdMob coldMob) {
			Entity replacement = coldMob.originalEntityType().create(level, EntitySpawnReason.CONVERSION);
			if (replacement != null) {
				replacement.copyPosition(livingEntity);
				UUID mobUUID = replacement.getUUID();
				replacement.restoreFrom(livingEntity);
				replacement.setUUID(mobUUID);

				level.addFreshEntity(replacement);
				livingEntity.discard();
			}
		}
	}

	int getEatAnimationTick();

	default float getHeadEatPositionScale(float a) {
		int eatAnimationTick = getEatAnimationTick();
		if (eatAnimationTick <= 0) {
			return 0.0F;
		} else if (eatAnimationTick >= 4 && eatAnimationTick <= 36) {
			return 1.0F;
		} else {
			return eatAnimationTick < 4 ? (eatAnimationTick - a) / 4.0F : -(eatAnimationTick - 40 - a) / 4.0F;
		}
	}

	default float getHeadEatAngleScale(float xRot, float a) {
		int eatAnimationTick = getEatAnimationTick();
		if (eatAnimationTick > 4 && eatAnimationTick <= 36) {
			float scale = (eatAnimationTick - 4 - a) / 32.0F;
			return (float) (Math.PI / 5) + 0.21991149F * Mth.sin(scale * 28.7F);
		} else {
			return eatAnimationTick > 0 ? (float) (Math.PI / 5) : xRot * (float) (Math.PI / 180.0);
		}
	}
}
