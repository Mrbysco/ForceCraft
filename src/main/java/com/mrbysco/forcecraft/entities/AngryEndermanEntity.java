package com.mrbysco.forcecraft.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class AngryEndermanEntity extends EnderMan {
	public AngryEndermanEntity(EntityType<? extends EnderMan> type, Level level) {
		super(type, level);
		this.setPathfindingMalus(PathType.WATER, 8.0F); //Reset to default as like Ender Tots, Angry Enderman aren't afraid of water
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AngryEndermanEntity.StareGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new AngryEndermanEntity.FindPlayerGoal(this, this::isAngryAt));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, true, false));
		this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	private boolean isBeingStaredBy(Player player) {
		return !LivingEntity.PLAYER_NOT_WEARING_DISGUISE_ITEM_FOR_TARGET.test(player, this) ? false :
				this.isLookingAtMe(player, 0.025, true, false, this.getEyeY()) &&
				!net.neoforged.neoforge.common.CommonHooks.shouldSuppressEnderManAnger(this, player);
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	public boolean teleport() {
		if (!this.level().isClientSide() && this.isAlive() && !this.isInWater()) {
			double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
			double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
			double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
			return this.teleport(d0, d1, d2);
		} else {
			return false;
		}
	}

	static class StareGoal extends Goal {
		private final AngryEndermanEntity enderman;
		private LivingEntity target;

		public StareGoal(AngryEndermanEntity enderman) {
			this.enderman = enderman;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			this.target = this.enderman.getTarget();
			if (this.target instanceof Player playerTarget) {
				double dist = this.target.distanceToSqr(this.enderman);
				return dist > 256.0 ? false : this.enderman.isBeingStaredBy(playerTarget);
			} else {
				return false;
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.enderman.getNavigation().stop();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
		}
	}

	static class FindPlayerGoal extends NearestAttackableTargetGoal<Player> {
		private final AngryEndermanEntity enderman;
		/**
		 * The player
		 */
		private Player player;
		private int aggroTime;
		private int teleportTime;
		private final TargetingConditions startAggroTargetConditions;
		private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();

		public FindPlayerGoal(AngryEndermanEntity enderman, TargetingConditions.@Nullable Selector selector) {
			super(enderman, Player.class, 10, false, false, selector);
			this.enderman = enderman;
			this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector((livingEntity, level) ->
					this.enderman.isBeingStaredBy((Player) livingEntity));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			this.player = getServerLevel(this.enderman).getNearestPlayer(this.startAggroTargetConditions, this.enderman);
			return this.player != null;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.aggroTime = 5;
			this.teleportTime = 0;
			this.enderman.setBeingStaredAt();
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by another one
		 */
		public void stop() {
			this.player = null;
			super.stop();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			if (this.player != null) {
				if (!this.enderman.isBeingStaredBy(this.player)) {
					return false;
				} else {
					this.enderman.lookAt(this.player, 10.0F, 10.0F);
					return true;
				}
			} else {
				return this.target != null && this.continueAggroTargetConditions.test(getServerLevel(this.enderman), this.enderman, this.target) ||
						super.canContinueToUse();
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.enderman.getTarget() == null) {
				super.setTarget((LivingEntity) null);
			}

			if (this.player != null) {
				if (--this.aggroTime <= 0) {
					this.target = this.player;
					this.player = null;
					super.start();
				}
			} else {
				if (this.target != null && !this.enderman.isPassenger()) {
					if (this.enderman.isBeingStaredBy((Player) this.target)) {
						if (this.target.distanceToSqr(this.enderman) < 16.0D) {
							this.enderman.teleport();
						}

						this.teleportTime = 0;
					} else if (this.target.distanceToSqr(this.enderman) > 128.0D && this.teleportTime++ >= 30 && this.enderman.teleportTowards(this.target)) {
						this.teleportTime = 0;
					}
				}

				super.tick();
			}
		}
	}
}
