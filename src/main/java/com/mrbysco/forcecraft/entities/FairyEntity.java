package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

public class FairyEntity extends PathfinderMob implements FlyingAnimal {
	public FairyEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.moveControl = new FlyingMoveControl(this, 20, true);
		this.lookControl = new FairyEntity.LookHelperController(this);
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
		this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
	}

	@SuppressWarnings("deprecation")
	public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
		return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 0.5F));
		this.goalSelector.addGoal(8, new FairyEntity.WanderGoal());
		this.goalSelector.addGoal(9, new FloatGoal(this));
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.FLYING_SPEED, (double)0.6F)
				.add(Attributes.MOVEMENT_SPEED, (double)0.3F);
	}

	@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}

	@Override
	public void playerTouch(Player playerEntity) {
		playSound(collideSound(playerEntity), 1.0F, 1.0F);
		playerEntity.heal(Float.MAX_VALUE);

		this.remove();
	}

	public static boolean canSpawnOn(EntityType<? extends Mob> typeIn, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random random) {
		BlockPos blockpos = pos.below();
		return reason == MobSpawnType.SPAWNER || (worldIn.getBlockState(blockpos).isValidSpawn(worldIn, blockpos, typeIn) && worldIn.getRawBrightness(pos, 0) > 8);
	}

	public SoundEvent collideSound(Player playerEntity) {
		int randomInt = level.random.nextInt(100);
		if(UUID.fromString("7135da42-d327-47bb-bb04-5ba4e212fb32").equals(playerEntity.getUUID())) {
			return ForceSounds.FAIRY_PICKUP.get();
		}
		if(randomInt <= 1 && UUID.fromString("10755ea6-9721-467a-8b5c-92adf689072c").equals(playerEntity.getUUID())) {
			return ForceSounds.FAIRY_LISTEN_SPECIAL.get();
		} else if(randomInt <= 10) {
			return ForceSounds.FAIRY_LISTEN.get();
		} else {
			return ForceSounds.FAIRY_PICKUP.get();
		}
	}

	public void tick() {
		super.tick();
		if (this.random.nextFloat() < 0.05F && getDeltaMovement() != Vec3.ZERO) {
			for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
				this.addParticle(this.level, this.getX() - (double)0.3F, this.getX() + (double)0.3F,
						this.getZ() - (double)0.3F, this.getZ() + (double)0.3F, this.getY(0.5D),
						ParticleTypes.POOF);
			}
		}
	}

	private void addParticle(Level worldIn, double posX, double posX2, double posZ, double posZ2, double posY, ParticleOptions particleData) {
		worldIn.addParticle(particleData, Mth.lerp(worldIn.random.nextDouble(), posX, posX2), posY - 0.2F, Mth.lerp(worldIn.random.nextDouble(), posZ, posZ2), 0.0D, 0.0D, 0.0D);
	}

	class LookHelperController extends LookControl {
		public LookHelperController(Mob entityIn) {
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
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			return FairyEntity.this.navigation.isDone() && FairyEntity.this.random.nextInt(10) == 0;
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			return FairyEntity.this.navigation.isInProgress();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			Vec3 vector3d = this.getRandomLocation();
			if (vector3d != null) {
				FairyEntity.this.navigation.moveTo(FairyEntity.this.navigation.createPath(new BlockPos(vector3d), 1), 1.0D);
			}

		}

		@Nullable
		private Vec3 getRandomLocation() {
			Vec3 vector3d = FairyEntity.this.getViewVector(0.0F);

			Vec3 vector3d2 = RandomPos.getAboveLandPos(FairyEntity.this, 8, 7, vector3d, ((float)Math.PI / 2F), 2, 1);
			return vector3d2 != null ? vector3d2 : RandomPos.getAirPos(FairyEntity.this, 8, 4, -2, vector3d, (double)((float)Math.PI / 2F));
		}
	}
}
