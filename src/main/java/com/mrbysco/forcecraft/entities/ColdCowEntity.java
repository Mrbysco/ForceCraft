package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.entities.goal.EatGrassToRestoreGoal;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColdCowEntity extends CowEntity implements IColdMob {
	private int grassTimer;
	private EatGrassToRestoreGoal eatGrassGoal;
	private ResourceLocation originalTypeLocation;

	public ColdCowEntity(EntityType<? extends CowEntity> type, World worldIn) {
		super(type, worldIn);
		this.originalTypeLocation = new ResourceLocation("minecraft", "cow");
	}

	public ColdCowEntity(World worldIn, ResourceLocation typeLocation) {
		super(ForceEntities.COLD_COW.get(), worldIn);
		if (typeLocation != null) {
			this.originalTypeLocation = typeLocation;
		}
	}

	@Override
	public boolean canMate(AnimalEntity otherAnimal) {
		return false;
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);

		if (compound.getString("OriginalMob").isEmpty()) {
			this.originalTypeLocation = new ResourceLocation("minecraft", "cow");
		} else {
			this.originalTypeLocation = new ResourceLocation(compound.getString("OriginalMob"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("OriginalMob", this.originalTypeLocation.toString());
	}

	@Override
	protected void registerGoals() {
		this.eatGrassGoal = new EatGrassToRestoreGoal(this);
		super.registerGoals();
		this.goalSelector.addGoal(5, this.eatGrassGoal);
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return CowEntity.createAttributes();
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
			return ((float) Math.PI / 5F) + 0.21991149F * MathHelper.sin(f * 28.7F);
		} else {
			return this.grassTimer > 0 ? ((float) Math.PI / 5F) : this.xRot * ((float) Math.PI / 180F);
		}
	}

	@Override
	public ResourceLocation getOriginal() {
		return this.originalTypeLocation;
	}
}
