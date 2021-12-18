package com.mrbysco.forcecraft.entities.projectile;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.util.ForceUtils;
import com.mrbysco.forcecraft.util.MobUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_BANE;

public class ForceArrowEntity extends ArrowEntity {
	private static final DataParameter<Boolean> ENDER = EntityDataManager.defineId(ForceArrowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BANE = EntityDataManager.defineId(ForceArrowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SPEED = EntityDataManager.defineId(ForceArrowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> GLOWING = EntityDataManager.defineId(ForceArrowEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> LUCK = EntityDataManager.defineId(ForceArrowEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> BLEEDING = EntityDataManager.defineId(ForceArrowEntity.class, DataSerializers.INT);

	public ForceArrowEntity(EntityType<? extends ArrowEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public ForceArrowEntity(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.setOwner(shooter);
	}

	@Override
	public void shootFromRotation(Entity projectile, float x, float y, float z, float velocity, float inaccuracy) {
		float newVelocity = isSpeedy() ? velocity + 1.0F : velocity;
		super.shootFromRotation(projectile, x, y, z, newVelocity, inaccuracy);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ENDER, false);
		this.entityData.define(BANE, false);
		this.entityData.define(LUCK, 0);
		this.entityData.define(BLEEDING, 0);
		this.entityData.define(SPEED, false);
		this.entityData.define(GLOWING, false);
	}

	public boolean isBane() {
		return this.entityData.get(BANE);
	}

	public void setBane() {
		this.entityData.set(BANE, true);
	}

	public boolean isSpeedy() {
		return this.entityData.get(SPEED);
	}

	public void setSpeedy() {
		this.entityData.set(SPEED, true);
	}

	public boolean isEnder() {
		return this.entityData.get(ENDER);
	}

	public void setEnder() {
		this.entityData.set(ENDER, true);
	}

	public boolean appliesGlowing() {
		return this.entityData.get(GLOWING);
	}

	public void setAppliesGlowing() {
		this.entityData.set(GLOWING, true);
	}

	public int getLuck() {
		return this.entityData.get(LUCK);
	}

	public void setLuck(int luck) {
		this.entityData.set(LUCK, luck);
	}

	public int getBleeding() {
		return this.entityData.get(BLEEDING);
	}

	public void setBleeding(int bleeding) {
		this.entityData.set(BLEEDING, bleeding);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);

		if(compound.getBoolean("Bane")) {
			setBane();
		}
		if(compound.getBoolean("Ender")) {
			setEnder();
		}
		if(compound.getBoolean("AppliesGlowing")) {
			setAppliesGlowing();
		}
		if(compound.getBoolean("Speedy")) {
			setSpeedy();
		}
		this.setLuck(compound.getInt("Luck"));
		this.setBleeding(compound.getInt("Bleeding"));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);

		if(isBane()) {
			compound.putBoolean("Bane", true);
		}
		if(isEnder()) {
			compound.putBoolean("Ender", true);
		}
		if(appliesGlowing()) {
			compound.putBoolean("AppliesGlowing", true);
		}
		if(isSpeedy()) {
			compound.putBoolean("Speedy", true);
		}
		compound.putInt("Luck", getLuck());
		compound.putInt("Bleeding", getBleeding());
	}

	@Override
	public void setEffectsFromItem(ItemStack stack) {
		if (stack.getItem() == Items.ARROW) {
			this.potion = Potions.EMPTY;
			this.effects.clear();
			this.entityData.set(ID_EFFECT_COLOR, -1);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);

		if(isEnder()) {
			ForceUtils.teleportRandomly(living);
		}

		if(appliesGlowing()) {
			living.addEffect(new EffectInstance(Effects.GLOWING, 200, 0));
		}

		if(getBleeding() > 0) {
			MobUtil.addBleedingEffect(getBleeding(), living, getOwner());
		}

		if(isBane()) {
			if(living instanceof CreeperEntity){
				CreeperEntity creeper = ((CreeperEntity) living);
				creeper.getCapability(CAPABILITY_BANE).ifPresent((entityCap) -> {
					if(entityCap.canExplode()){
						creeper.setSwellDir(-1);
						creeper.getEntityData().set(CreeperEntity.DATA_IS_IGNITED, false);
						entityCap.setExplodeAbility(false);
						creeper.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof CreeperSwellGoal);
						ForceCraft.LOGGER.debug("Added Bane to " + living.getName());
					}
				});
			}
		}
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(ForceRegistry.FORCE_ARROW.get());
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.FORCE_ARROW.get();
	}

	@Nonnull
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
