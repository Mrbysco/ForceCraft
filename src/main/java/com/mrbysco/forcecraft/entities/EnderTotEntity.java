package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagContainer;
import net.minecraft.tags.SerializationTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class EnderTotEntity extends EnderMan {
	private static final ResourceLocation TOT_HOLDABLE = new ResourceLocation(Reference.MOD_ID, "endertot_holdable");

	public EnderTotEntity(EntityType<? extends EnderMan> type, Level worldIn) {
		super(type, worldIn);
		this.setPathfindingMalus(BlockPathTypes.WATER, 8.0F); //Reset to default as Ender Tots aren't afraid of water
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new EnderTotEntity.StareGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(10, new EnderTotEntity.PlaceBlockGoal(this));
		this.goalSelector.addGoal(11, new EnderTotEntity.TakeBlockGoal(this));
		this.targetSelector.addGoal(1, new EnderTotEntity.FindPlayerGoal(this, this::isAngryAt));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, 10, true, false, ENDERMITE_SELECTOR));
		this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 1.35F;
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public boolean teleport() {
		if (!this.level.isClientSide() && this.isAlive() && !this.isInWaterOrBubble()) {
			double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 32.0D;
			double d1 = this.getY() + (double)(this.random.nextInt(32) - 16);
			double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 32;
			return this.teleport(d0, d1, d2);
		} else {
			return false;
		}
	}

	@Override
	public void die(DamageSource cause) {
		Entity entitySource = cause.getDirectEntity();
		if(entitySource instanceof LivingEntity livingEntity && !this.level.isClientSide) {
			int total = getRandom().nextInt(2) + 1;
			for(int i = 0; i < total; i++) {
				AngryEndermanEntity endermanEntity = ForceEntities.ANGRY_ENDERMAN.get().create(level);
				endermanEntity.moveTo(getX(), getY() + 0.5D, getZ(), 0.0F, 0.0F);
				endermanEntity.setTarget(livingEntity);
				level.addFreshEntity(endermanEntity);
			}
		}
		super.die(cause);
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, (double)0.35F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	static class StareGoal extends Goal {
		private final EnderTotEntity endertot;
		private LivingEntity targetPlayer;

		public StareGoal(EnderTotEntity endertotIn) {
			this.endertot = endertotIn;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			this.targetPlayer = this.endertot.getTarget();
			if (!(this.targetPlayer instanceof Player)) {
				return false;
			} else {
				double d0 = this.targetPlayer.distanceToSqr(this.endertot);
				return !(d0 > 256.0D) && this.endertot.isLookingAtMe((Player) this.targetPlayer);
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.endertot.getNavigation().stop();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			this.endertot.getLookControl().setLookAt(this.targetPlayer.getX(), this.targetPlayer.getEyeY(), this.targetPlayer.getZ());
		}
	}

	static class FindPlayerGoal extends NearestAttackableTargetGoal<Player> {
		private final EnderTotEntity endertot;
		/** The player */
		private Player player;
		private int aggroTime;
		private int teleportTime;
		private final TargetingConditions startAggroTargetConditions;
		private final TargetingConditions continueAggroTargetConditions = (new TargetingConditions()).allowUnseeable();

		public FindPlayerGoal(EnderTotEntity enderTotIn, @Nullable Predicate<LivingEntity> p_i241912_2_) {
			super(enderTotIn, Player.class, 10, false, false, p_i241912_2_);
			this.endertot = enderTotIn;
			this.startAggroTargetConditions = (new TargetingConditions()).range(this.getFollowDistance()).selector((p_220790_1_) -> enderTotIn.isLookingAtMe((Player)p_220790_1_));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			this.player = this.endertot.level.getNearestPlayer(this.startAggroTargetConditions, this.endertot);
			return this.player != null;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.aggroTime = 5;
			this.teleportTime = 0;
			this.endertot.setBeingStaredAt();
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
				if (!this.endertot.isLookingAtMe(this.player)) {
					return false;
				} else {
					this.endertot.lookAt(this.player, 10.0F, 10.0F);
					return true;
				}
			} else {
				return this.target != null && this.continueAggroTargetConditions.test(this.endertot, this.target) || super.canContinueToUse();
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.endertot.getTarget() == null) {
				super.setTarget((LivingEntity)null);
			}

			if (this.player != null) {
				if (--this.aggroTime <= 0) {
					this.target = this.player;
					this.player = null;
					super.start();
				}
			} else {
				if (this.target != null && !this.endertot.isPassenger()) {
					if (this.endertot.isLookingAtMe((Player)this.target)) {
						if (this.target.distanceToSqr(this.endertot) < 16.0D) {
							this.endertot.teleport();
						}

						this.teleportTime = 0;
					} else if (this.target.distanceToSqr(this.endertot) > 128.0D && this.teleportTime++ >= 30 && this.endertot.teleportTowards(this.target)) {
						this.teleportTime = 0;
					}
				}

				super.tick();
			}
		}
	}

	static class TakeBlockGoal extends Goal {
		private final EnderTotEntity endertot;

		public TakeBlockGoal(EnderTotEntity endertotIn) {
			this.endertot = endertotIn;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			if (this.endertot.getCarriedBlock() != null) {
				return false;
			} else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.endertot.level, this.endertot)) {
				return false;
			} else {
				return this.endertot.getRandom().nextInt(20) == 0;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			Random random = this.endertot.getRandom();
			Level world = this.endertot.level;
			int i = Mth.floor(this.endertot.getX() - 2.0D + random.nextDouble() * 4.0D);
			int j = Mth.floor(this.endertot.getY() + random.nextDouble() * 3.0D);
			int k = Mth.floor(this.endertot.getZ() - 2.0D + random.nextDouble() * 4.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = world.getBlockState(blockpos);
			Block block = blockstate.getBlock();
			Vec3 vector3d = new Vec3((double)Mth.floor(this.endertot.getX()) + 0.5D, (double)j + 0.5D, (double)Mth.floor(this.endertot.getZ()) + 0.5D);
			Vec3 vector3d1 = new Vec3((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D);
			BlockHitResult blockraytraceresult = world.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.endertot));
			boolean flag = blockraytraceresult.getBlockPos().equals(blockpos);
			TagContainer tagCollection = SerializationTags.getInstance();
			Tag<Block> holdableTag = tagCollection.getBlocks().getTag(TOT_HOLDABLE);
			if (holdableTag != null && block.is(holdableTag) && flag) {
				world.removeBlock(blockpos, false);
				this.endertot.setCarriedBlock(blockstate.getBlock().defaultBlockState());
			}

		}
	}

	static class PlaceBlockGoal extends Goal {
		private final EnderTotEntity endertot;

		public PlaceBlockGoal(EnderTotEntity totEntity) {
			this.endertot = totEntity;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			if (this.endertot.getCarriedBlock() == null) {
				return false;
			} else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.endertot.level, this.endertot)) {
				return false;
			} else {
				return this.endertot.getRandom().nextInt(2000) == 0;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			Random random = this.endertot.getRandom();
			Level world = this.endertot.level;
			int i = Mth.floor(this.endertot.getX() - 1.0D + random.nextDouble() * 2.0D);
			int j = Mth.floor(this.endertot.getY() + random.nextDouble() * 2.0D);
			int k = Mth.floor(this.endertot.getZ() - 1.0D + random.nextDouble() * 2.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = world.getBlockState(blockpos);
			BlockPos blockpos1 = blockpos.below();
			BlockState blockstate1 = world.getBlockState(blockpos1);
			BlockState blockstate2 = this.endertot.getCarriedBlock();
			if (blockstate2 != null) {
				blockstate2 = Block.updateFromNeighbourShapes(blockstate2, this.endertot.level, blockpos);
				if (this.canPlaceBlock(world, blockpos, blockstate2, blockstate, blockstate1, blockpos1) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(endertot, net.minecraftforge.common.util.BlockSnapshot.create(world.dimension(), world, blockpos1), net.minecraft.core.Direction.UP)) {
					world.setBlock(blockpos, blockstate2, 3);
					this.endertot.setCarriedBlock((BlockState)null);
				}

			}
		}

		@SuppressWarnings("deprecation")
		private boolean canPlaceBlock(Level p_220836_1_, BlockPos p_220836_2_, BlockState p_220836_3_, BlockState p_220836_4_, BlockState p_220836_5_, BlockPos p_220836_6_) {
			return p_220836_4_.isAir(p_220836_1_, p_220836_2_) && !p_220836_5_.isAir(p_220836_1_, p_220836_6_) && !p_220836_5_.is(Blocks.BEDROCK) && !p_220836_5_.is(net.minecraftforge.common.Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST) && p_220836_5_.isCollisionShapeFullBlock(p_220836_1_, p_220836_6_) && p_220836_3_.canSurvive(p_220836_1_, p_220836_2_) && p_220836_1_.getEntities(this.endertot, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(p_220836_2_))).isEmpty();
		}
	}
}
