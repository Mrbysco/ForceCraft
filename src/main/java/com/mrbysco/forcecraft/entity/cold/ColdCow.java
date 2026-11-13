package com.mrbysco.forcecraft.entity.cold;

import com.mrbysco.forcecraft.entity.goal.EatGrassToRestoreGoal;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ColdCow extends Cow implements IColdMob {
	private int eatAnimationTick;
	private EatGrassToRestoreGoal eatGrassGoal;
	private EntityType<?> originalType = EntityTypes.COW;

	public ColdCow(EntityType<? extends Cow> type, Level level) {
		super(type, level);
	}

	public ColdCow(Level level, EntityType<?> typeLocation) {
		super(ForceEntities.COLD_COW.get(), level);
		if (typeLocation != null) {
			this.originalType = typeLocation;
		}
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.COLD_COW.get();
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Cow.createAttributes();
	}

	@Override
	public EntityType<?> originalEntityType() {
		return this.originalType;
	}

	@Override
	public int getEatAnimationTick() {
		return this.eatAnimationTick;
	}

	@Override
	public boolean canMate(Animal partner) {
		return false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.eatGrassGoal = new EatGrassToRestoreGoal(this);
		this.goalSelector.addGoal(5, this.eatGrassGoal);
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		this.eatAnimationTick = this.eatGrassGoal.getEatAnimationTick();
		super.customServerAiStep(level);
	}

	@Override
	public void aiStep() {
		if (this.level().isClientSide()) {
			this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
		}

		super.aiStep();
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 10) {
			this.eatAnimationTick = 40;
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.store("OriginalType", EntityType.CODEC, this.originalType);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		input.read("OriginalType", EntityType.CODEC)
				.map(type -> this.originalType = type);
	}
}
