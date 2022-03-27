package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.entities.goal.EatGrassToRestoreGoal;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColdChickenEntity extends Chicken implements IColdMob {
	private int grassTimer;
	private EatGrassToRestoreGoal eatGrassGoal;
	private ResourceLocation originalTypeLocation;

	public ColdChickenEntity(EntityType<? extends Chicken> type, Level level) {
		super(type, level);
		this.originalTypeLocation = new ResourceLocation("minecraft", "chicken");
	}

	public ColdChickenEntity(Level level, ResourceLocation typeLocation) {
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
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		if (compound.getString("OriginalMob").isEmpty()) {
			this.originalTypeLocation = new ResourceLocation("minecraft", "chicken");
		} else {
			this.originalTypeLocation = new ResourceLocation(compound.getString("OriginalMob"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("OriginalMob", this.originalTypeLocation.toString());
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
	protected void customServerAiStep() {
		this.grassTimer = this.eatGrassGoal.getEatingGrassTimer();
		super.customServerAiStep();
	}

	public void aiStep() {
		if (this.level.isClientSide) {
			this.grassTimer = Math.max(0, this.grassTimer - 1);
		}

		super.aiStep();
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		if (id == 10) {
			this.grassTimer = 40;
		} else {
			super.handleEntityEvent(id);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_) {
		if (this.grassTimer <= 0) {
			return 0.0F;
		} else if (this.grassTimer >= 4 && this.grassTimer <= 36) {
			return 1.0F;
		} else {
			return this.grassTimer < 4 ? ((float) this.grassTimer - p_70894_1_) / 4.0F : -((float) (this.grassTimer - 40) - p_70894_1_) / 4.0F;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationAngleX(float p_70890_1_) {
		if (this.grassTimer > 4 && this.grassTimer <= 36) {
			float f = ((float) (this.grassTimer - 4) - p_70890_1_) / 32.0F;
			return ((float) Math.PI / 5F) + 0.21991149F * Mth.sin(f * 28.7F);
		} else {
			return this.grassTimer > 0 ? ((float) Math.PI / 5F) : this.getXRot() * ((float) Math.PI / 180F);
		}
	}

	@Override
	public ResourceLocation getOriginal() {
		return this.originalTypeLocation;
	}
}
