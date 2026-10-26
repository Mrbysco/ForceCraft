package com.mrbysco.forcecraft.effects;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FrozenEffect extends MobEffect {
	public FrozenEffect() {
		super(MobEffectCategory.HARMFUL, 0x98D9EA);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				Reference.modLoc("effect.frozen_speed"),
				-1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH,
				Reference.modLoc("effect.frozen_jump"),
				-1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplification) {
		livingEntity.setDeltaMovement(0, Math.min(0, livingEntity.getDeltaMovement().y), 0);
		livingEntity.hurtMarked = true;

		if (livingEntity instanceof Mob mob) {
			mob.getNavigation().stop();
		}

		serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
				livingEntity.getX(), livingEntity.getEyeY() - 0.25, livingEntity.getZ(),
				8, 0.25, 0.25, 0.25, 0.0);

		return true;
	}
}
