package mrbysco.forcecraft.entities;

import mrbysco.forcecraft.entities.goal.EatGrassToRestoreGoal;
import mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColdPigEntity extends PigEntity implements IColdMob {
	private int grassTimer;
	private EatGrassToRestoreGoal eatGrassGoal;
	private ResourceLocation originalTypeLocation;

	public ColdPigEntity(EntityType<? extends PigEntity> type, World worldIn) {
		super(type, worldIn);
		this.originalTypeLocation = new ResourceLocation("minecraft", "pig");
	}

	public ColdPigEntity(World worldIn, ResourceLocation typeLocation) {
		super(ForceEntities.COLD_PIG.get(), worldIn);
		if(typeLocation != null) {
			this.originalTypeLocation = typeLocation;
		}
	}

	@Override
	public boolean canMateWith(AnimalEntity otherAnimal) {
		return false;
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		if(compound.getString("OriginalMob").isEmpty()) {
			this.originalTypeLocation = new ResourceLocation("minecraft", "pig");
		} else {
			this.originalTypeLocation = new ResourceLocation(compound.getString("OriginalMob"));
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putString("OriginalMob", this.originalTypeLocation.toString());
	}

	@Override
	protected void registerGoals() {
		this.eatGrassGoal = new EatGrassToRestoreGoal(this);
		super.registerGoals();
		this.goalSelector.addGoal(5, this.eatGrassGoal);
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return PigEntity.func_234215_eI_();
	}

	@Override
	protected void updateAITasks() {
		this.grassTimer = this.eatGrassGoal.getEatingGrassTimer();
		super.updateAITasks();
	}

	public void livingTick() {
		if (this.world.isRemote) {
			this.grassTimer = Math.max(0, this.grassTimer - 1);
		}

		super.livingTick();
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 10) {
			this.grassTimer = 40;
		} else {
			super.handleStatusUpdate(id);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_) {
		if (this.grassTimer <= 0) {
			return 0.0F;
		} else if (this.grassTimer >= 4 && this.grassTimer <= 36) {
			return 1.0F;
		} else {
			return this.grassTimer < 4 ? ((float)this.grassTimer - p_70894_1_) / 4.0F : -((float)(this.grassTimer - 40) - p_70894_1_) / 4.0F;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationAngleX(float p_70890_1_) {
		if (this.grassTimer > 4 && this.grassTimer <= 36) {
			float f = ((float)(this.grassTimer - 4) - p_70890_1_) / 32.0F;
			return ((float)Math.PI / 5F) + 0.21991149F * MathHelper.sin(f * 28.7F);
		} else {
			return this.grassTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch * ((float)Math.PI / 180F);
		}
	}

	@Override
	public ResourceLocation getOriginal() {
		return this.originalTypeLocation;
	}
}
