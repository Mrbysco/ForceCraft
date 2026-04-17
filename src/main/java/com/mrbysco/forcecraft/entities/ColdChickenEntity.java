package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.entities.goal.EatGrassToRestoreGoal;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ColdChickenEntity extends Chicken implements IColdMob {
	private int grassTimer;
	private EatGrassToRestoreGoal eatGrassGoal;
	private Identifier originalTypeLocation;

	public ColdChickenEntity(EntityType<? extends Chicken> type, Level level) {
		super(type, level);
		this.originalTypeLocation = Identifier.withDefaultNamespace("chicken");
	}

	public ColdChickenEntity(Level level, Identifier typeLocation) {
		super(ForceEntities.COLD_CHICKEN.get(), level);
		if (typeLocation != null) {
			this.originalTypeLocation = typeLocation;
		}
	}

	@Override
	public boolean canMate(Animal otherAnimal) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);

		if (input.getString("OriginalMob").isEmpty()) {
			this.originalTypeLocation = Identifier.withDefaultNamespace("pig");
		} else {
			this.originalTypeLocation = Identifier.tryParse(input.getStringOr("OriginalMob", ""));
		}
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);

		output.putString("OriginalMob", this.originalTypeLocation.toString());
	}

	@Override
	protected void registerGoals() {
		this.eatGrassGoal = new EatGrassToRestoreGoal(this);
		super.registerGoals();
		this.goalSelector.addGoal(5, this.eatGrassGoal);
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Chicken.createAttributes();
	}

	@Override
	protected void customServerAiStep(ServerLevel serverLevel) {
		this.grassTimer = this.eatGrassGoal.getEatingGrassTimer();
		super.customServerAiStep(serverLevel);
	}

	public void aiStep() {
		if (this.level().isClientSide()) {
			this.grassTimer = Math.max(0, this.grassTimer - 1);
		}

		super.aiStep();
	}

	public void handleEntityEvent(byte id) {
		if (id == 10) {
			this.grassTimer = 40;
		} else {
			super.handleEntityEvent(id);
		}

	}

	public float getHeadRotationPointY(float p_70894_1_) {
		if (this.grassTimer <= 0) {
			return 0.0F;
		} else if (this.grassTimer >= 4 && this.grassTimer <= 36) {
			return 1.0F;
		} else {
			return this.grassTimer < 4 ? ((float) this.grassTimer - p_70894_1_) / 4.0F : -((float) (this.grassTimer - 40) - p_70894_1_) / 4.0F;
		}
	}

	public float getHeadRotationAngleX(float p_70890_1_) {
		if (this.grassTimer > 4 && this.grassTimer <= 36) {
			float f = ((float) (this.grassTimer - 4) - p_70890_1_) / 32.0F;
			return ((float) Math.PI / 5F) + 0.21991149F * Mth.sin(f * 28.7F);
		} else {
			return this.grassTimer > 0 ? ((float) Math.PI / 5F) : this.getXRot() * ((float) Math.PI / 180F);
		}
	}

	@Override
	public Identifier getOriginal() {
		return this.originalTypeLocation;
	}
}
