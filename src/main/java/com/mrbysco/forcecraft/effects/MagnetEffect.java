package com.mrbysco.forcecraft.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.List;
import java.util.function.Consumer;

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
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		//Inspired by Botania Code
		double x = entity.getX();
		double y = entity.getY() + 0.75;
		double z = entity.getZ();
		double range = 10.0d;

		range += amplifier * 0.3f;

		List<ItemEntity> items = entity.getCommandSenderWorld().getEntitiesOfClass(ItemEntity.class, new AABB(x - range, y - range, z - range, x + range, y + range, z + range));
		for (ItemEntity item : items) {
			if (item.getItem().isEmpty() || !item.isAlive()) {
				continue;
			}

			// constant force!
			float strength = 0.14F;

			Vec3 entityVector = new Vec3(item.getX(), item.getY() - item.getMyRidingOffset(entity) + item.getBbHeight() / 2, item.getZ());
			Vec3 finalVector = new Vec3(x, y, z).subtract(entityVector);

			if (Math.sqrt(finalVector.x * finalVector.x + finalVector.y * finalVector.y + finalVector.z * finalVector.z) > 1) {
				finalVector = finalVector.normalize();
			}

			item.setDeltaMovement(finalVector.multiply(strength, strength, strength));
		}
	}

	@Override
	public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInInventory(MobEffectInstance effect) {
				return false;
			}

			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
}
