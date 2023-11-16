package com.mrbysco.forcecraft.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class MagnetEffect extends Effect {
	public MagnetEffect() {
		super(EffectType.BENEFICIAL, 0);
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
	public boolean shouldRenderHUD(EffectInstance effect) {
		return false;
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean shouldRender(EffectInstance effect) {
		return false;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		//Inspired by Botania Code
		double x = entity.getX();
		double y = entity.getY() + 0.75;
		double z = entity.getZ();
		double range = 10.0d;

		range += amplifier * 0.3f;

		List<ItemEntity> items = entity.getCommandSenderWorld().getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
		for (ItemEntity item : items) {
			if (item.getItem().isEmpty() || !item.isAlive()) {
				continue;
			}

			// constant force!
			float strength = 0.14F;

			Vector3d entityVector = new Vector3d(item.getX(), item.getY() - item.getMyRidingOffset() + item.getBbHeight() / 2, item.getZ());
			Vector3d finalVector = new Vector3d(x, y, z).subtract(entityVector);

			if (Math.sqrt(finalVector.x * finalVector.x + finalVector.y * finalVector.y + finalVector.z * finalVector.z) > 1) {
				finalVector = finalVector.normalize();
			}

			item.setDeltaMovement(finalVector.multiply(strength, strength, strength));
		}
	}
}
