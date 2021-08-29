package com.mrbysco.forcecraft.entities;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class AngryEndermanEntity extends EndermanEntity {
	public AngryEndermanEntity(EntityType<? extends EndermanEntity> type, World worldIn) {
		super(type, worldIn);
		this.setPathPriority(PathNodeType.WATER, 8.0F); //Reset to default as like Ender Tots, Angry Enderman aren't afraid of water
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new AngryEndermanEntity.StareGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new AngryEndermanEntity.FindPlayerGoal(this, this::func_233680_b_));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, 10, true, false, field_213627_bA));
		this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.3F)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	public boolean teleportRandomly() {
		if (!this.world.isRemote() && this.isAlive() && !this.isInWaterOrBubbleColumn()) {
			double d0 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 64.0D;
			double d1 = this.getPosY() + (double)(this.rand.nextInt(64) - 32);
			double d2 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 64.0D;
			return this.teleportTo(d0, d1, d2);
		} else {
			return false;
		}
	}

	static class StareGoal extends Goal {
		private final EndermanEntity enderman;
		private LivingEntity targetPlayer;

		public StareGoal(EndermanEntity endermanIn) {
			this.enderman = endermanIn;
			this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			this.targetPlayer = this.enderman.getAttackTarget();
			if (!(this.targetPlayer instanceof PlayerEntity)) {
				return false;
			} else {
				double d0 = this.targetPlayer.getDistanceSq(this.enderman);
				return d0 > 256.0D ? false : this.enderman.shouldAttackPlayer((PlayerEntity)this.targetPlayer);
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.enderman.getNavigator().clearPath();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			this.enderman.getLookController().setLookPosition(this.targetPlayer.getPosX(), this.targetPlayer.getPosYEye(), this.targetPlayer.getPosZ());
		}
	}

	static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
		private final AngryEndermanEntity enderman;
		/** The player */
		private PlayerEntity player;
		private int aggroTime;
		private int teleportTime;
		private final EntityPredicate field_220791_m;
		private final EntityPredicate field_220792_n = (new EntityPredicate()).setIgnoresLineOfSight();

		public FindPlayerGoal(AngryEndermanEntity endermantIn, @Nullable Predicate<LivingEntity> p_i241912_2_) {
			super(endermantIn, PlayerEntity.class, 10, false, false, p_i241912_2_);
			this.enderman = endermantIn;
			this.field_220791_m = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate((p_220790_1_) -> {
				return endermantIn.shouldAttackPlayer((PlayerEntity)p_220790_1_);
			});
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			this.player = this.enderman.world.getClosestPlayer(this.field_220791_m, this.enderman);
			return this.player != null;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.aggroTime = 5;
			this.teleportTime = 0;
			this.enderman.func_226538_eu_();
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by another one
		 */
		public void resetTask() {
			this.player = null;
			super.resetTask();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting() {
			if (this.player != null) {
				if (!this.enderman.shouldAttackPlayer(this.player)) {
					return false;
				} else {
					this.enderman.faceEntity(this.player, 10.0F, 10.0F);
					return true;
				}
			} else {
				return this.nearestTarget != null && this.field_220792_n.canTarget(this.enderman, this.nearestTarget) ? true : super.shouldContinueExecuting();
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.enderman.getAttackTarget() == null) {
				super.setNearestTarget((LivingEntity)null);
			}

			if (this.player != null) {
				if (--this.aggroTime <= 0) {
					this.nearestTarget = this.player;
					this.player = null;
					super.startExecuting();
				}
			} else {
				if (this.nearestTarget != null && !this.enderman.isPassenger()) {
					if (this.enderman.shouldAttackPlayer((PlayerEntity)this.nearestTarget)) {
						if (this.nearestTarget.getDistanceSq(this.enderman) < 16.0D) {
							this.enderman.teleportRandomly();
						}

						this.teleportTime = 0;
					} else if (this.nearestTarget.getDistanceSq(this.enderman) > 128.0D && this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.nearestTarget)) {
						this.teleportTime = 0;
					}
				}

				super.tick();
			}
		}
	}
}
