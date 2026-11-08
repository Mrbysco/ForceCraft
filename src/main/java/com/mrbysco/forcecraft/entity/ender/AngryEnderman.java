package com.mrbysco.forcecraft.entity.ender;

import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class AngryEnderman extends AbstractForceEnderman{
	public AngryEnderman(EntityType<? extends EnderMan> type, Level level) {
		super(type, level);
	}

	@Override
	protected int getTeleportRange() {
		return 64;
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.ANGRY_ENDERMAN.get();
	}
}
