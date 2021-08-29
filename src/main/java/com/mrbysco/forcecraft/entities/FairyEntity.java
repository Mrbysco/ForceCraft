package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

public class FairyEntity extends CreatureEntity implements IFlyingAnimal {
	public FairyEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new FlyingMovementController(this, 20, true);
		this.lookController = new FairyEntity.LookHelperController(this);
		this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathPriority(PathNodeType.COCOA, -1.0F);
		this.setPathPriority(PathNodeType.FENCE, -1.0F);
	}

	@SuppressWarnings("deprecation")
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 0.5F));
		this.goalSelector.addGoal(8, new FairyEntity.WanderGoal());
		this.goalSelector.addGoal(9, new SwimGoal(this));
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 1.0D)
				.createMutableAttribute(Attributes.FLYING_SPEED, (double)0.6F)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.3F);
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity playerEntity) {
		playSound(collideSound(playerEntity), 1.0F, 1.0F);
		playerEntity.heal(Float.MAX_VALUE);

		this.remove();
	}

	public static boolean canSpawnOn(EntityType<? extends MobEntity> typeIn, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random) {
		BlockPos blockpos = pos.down();
		return reason == SpawnReason.SPAWNER || (worldIn.getBlockState(blockpos).canEntitySpawn(worldIn, blockpos, typeIn) && worldIn.getLightSubtracted(pos, 0) > 8);
	}

	public SoundEvent collideSound(PlayerEntity playerEntity) {
		int randomInt = world.rand.nextInt(100);
		if(UUID.fromString("7135da42-d327-47bb-bb04-5ba4e212fb32").equals(playerEntity.getUniqueID())) {
			ForceCraft.LOGGER.info("");
			return ForceSounds.FAIRY_PICKUP.get();
		}
		if(randomInt <= 1 && UUID.fromString("10755ea6-9721-467a-8b5c-92adf689072c").equals(playerEntity.getUniqueID())) {
			return ForceSounds.FAIRY_LISTEN_SPECIAL.get();
		} else if(randomInt <= 10) {
			return ForceSounds.FAIRY_LISTEN.get();
		} else {
			return ForceSounds.FAIRY_PICKUP.get();
		}
	}

	public void tick() {
		super.tick();
		if (this.rand.nextFloat() < 0.05F && getMotion() != Vector3d.ZERO) {
			for(int i = 0; i < this.rand.nextInt(2) + 1; ++i) {
				this.addParticle(this.world, this.getPosX() - (double)0.3F, this.getPosX() + (double)0.3F,
						this.getPosZ() - (double)0.3F, this.getPosZ() + (double)0.3F, this.getPosYHeight(0.5D),
						ParticleTypes.POOF);
			}
		}
	}

	private void addParticle(World worldIn, double posX, double posX2, double posZ, double posZ2, double posY, IParticleData particleData) {
		worldIn.addParticle(particleData, MathHelper.lerp(worldIn.rand.nextDouble(), posX, posX2), posY - 0.2F, MathHelper.lerp(worldIn.rand.nextDouble(), posZ, posZ2), 0.0D, 0.0D, 0.0D);
	}

	class LookHelperController extends LookController {
		public LookHelperController(MobEntity entityIn) {
			super(entityIn);
		}

		/**
		 * Updates look
		 */
		public void tick() {
		}
	}

	class WanderGoal extends Goal {
		WanderGoal() {
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			return FairyEntity.this.navigator.noPath() && FairyEntity.this.rand.nextInt(10) == 0;
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting() {
			return FairyEntity.this.navigator.hasPath();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			Vector3d vector3d = this.getRandomLocation();
			if (vector3d != null) {
				FairyEntity.this.navigator.setPath(FairyEntity.this.navigator.getPathToPos(new BlockPos(vector3d), 1), 1.0D);
			}

		}

		@Nullable
		private Vector3d getRandomLocation() {
			Vector3d vector3d = FairyEntity.this.getLook(0.0F);

			Vector3d vector3d2 = RandomPositionGenerator.findAirTarget(FairyEntity.this, 8, 7, vector3d, ((float)Math.PI / 2F), 2, 1);
			return vector3d2 != null ? vector3d2 : RandomPositionGenerator.findGroundTarget(FairyEntity.this, 8, 4, -2, vector3d, (double)((float)Math.PI / 2F));
		}
	}
}
