package com.mrbysco.forcecraft.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MagnetEffect extends MobEffect {
	public MagnetEffect() {
		super(MobEffectCategory.BENEFICIAL, 0);
	}

	@Override
	public boolean isInstantenous() {
		return false;
	}

	@Override
	public boolean isBeneficial() {
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
		//Inspired by Botania Code
		double x = mob.getX();
		double y = mob.getY() + 0.75;
		double z = mob.getZ();
		double range = 10.0d;

		range += amplification * 0.3f;

		List<ItemEntity> items = mob.level().getEntitiesOfClass(ItemEntity.class, new AABB(x - range, y - range, z - range, x + range, y + range, z + range));
		for (ItemEntity item : items) {
			if (item.getItem().isEmpty() || !item.isAlive()) {
				continue;
			}

			// constant force!
			float strength = 0.14F;

			Vec3 entityVector = new Vec3(item.getX(), item.getY() - item.getPassengerRidingPosition(mob).y() + item.getBbHeight() / 2, item.getZ());
			Vec3 finalVector = new Vec3(x, y, z).subtract(entityVector);

			if (Math.sqrt(finalVector.x * finalVector.x + finalVector.y * finalVector.y + finalVector.z * finalVector.z) > 1) {
				finalVector = finalVector.normalize();
			}

			item.setDeltaMovement(finalVector.multiply(strength, strength, strength));
		}
		return true;
	}
}
