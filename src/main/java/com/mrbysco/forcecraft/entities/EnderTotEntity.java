package com.mrbysco.forcecraft.entities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
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
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class EnderTotEntity extends EndermanEntity {
	private static final ResourceLocation TOT_HOLDABLE = new ResourceLocation(Reference.MOD_ID, "endertot_holdable");

	public EnderTotEntity(EntityType<? extends EndermanEntity> type, World worldIn) {
		super(type, worldIn);
		this.setPathfindingMalus(PathNodeType.WATER, 8.0F); //Reset to default as Ender Tots aren't afraid of water
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EnderTotEntity.StareGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(10, new EnderTotEntity.PlaceBlockGoal(this));
		this.goalSelector.addGoal(11, new EnderTotEntity.TakeBlockGoal(this));
		this.targetSelector.addGoal(1, new EnderTotEntity.FindPlayerGoal(this, this::isAngryAt));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, 10, true, false, ENDERMITE_SELECTOR));
		this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
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
		if(entitySource instanceof LivingEntity && !this.level.isClientSide) {
			LivingEntity livingEntity = (LivingEntity) entitySource;
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

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return MonsterEntity.createMonsterAttributes()
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
			if (!(this.targetPlayer instanceof PlayerEntity)) {
				return false;
			} else {
				double d0 = this.targetPlayer.distanceToSqr(this.endertot);
				return !(d0 > 256.0D) && this.endertot.isLookingAtMe((PlayerEntity) this.targetPlayer);
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

	static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
		private final EnderTotEntity endertot;
		/** The player */
		private PlayerEntity player;
		private int aggroTime;
		private int teleportTime;
		private final EntityPredicate startAggroTargetConditions;
		private final EntityPredicate continueAggroTargetConditions = (new EntityPredicate()).allowUnseeable();

		public FindPlayerGoal(EnderTotEntity enderTotIn, @Nullable Predicate<LivingEntity> p_i241912_2_) {
			super(enderTotIn, PlayerEntity.class, 10, false, false, p_i241912_2_);
			this.endertot = enderTotIn;
			this.startAggroTargetConditions = (new EntityPredicate()).range(this.getFollowDistance()).selector((p_220790_1_) -> enderTotIn.isLookingAtMe((PlayerEntity)p_220790_1_));
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
					if (this.endertot.isLookingAtMe((PlayerEntity)this.target)) {
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
			World world = this.endertot.level;
			int i = MathHelper.floor(this.endertot.getX() - 2.0D + random.nextDouble() * 4.0D);
			int j = MathHelper.floor(this.endertot.getY() + random.nextDouble() * 3.0D);
			int k = MathHelper.floor(this.endertot.getZ() - 2.0D + random.nextDouble() * 4.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = world.getBlockState(blockpos);
			Block block = blockstate.getBlock();
			Vector3d vector3d = new Vector3d((double)MathHelper.floor(this.endertot.getX()) + 0.5D, (double)j + 0.5D, (double)MathHelper.floor(this.endertot.getZ()) + 0.5D);
			Vector3d vector3d1 = new Vector3d((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D);
			BlockRayTraceResult blockraytraceresult = world.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this.endertot));
			boolean flag = blockraytraceresult.getBlockPos().equals(blockpos);
			ITagCollectionSupplier tagCollection = TagCollectionManager.getInstance();
			ITag<Block> holdableTag = tagCollection.getBlocks().getTag(TOT_HOLDABLE);
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
			World world = this.endertot.level;
			int i = MathHelper.floor(this.endertot.getX() - 1.0D + random.nextDouble() * 2.0D);
			int j = MathHelper.floor(this.endertot.getY() + random.nextDouble() * 2.0D);
			int k = MathHelper.floor(this.endertot.getZ() - 1.0D + random.nextDouble() * 2.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = world.getBlockState(blockpos);
			BlockPos blockpos1 = blockpos.below();
			BlockState blockstate1 = world.getBlockState(blockpos1);
			BlockState blockstate2 = this.endertot.getCarriedBlock();
			if (blockstate2 != null) {
				blockstate2 = Block.updateFromNeighbourShapes(blockstate2, this.endertot.level, blockpos);
				if (this.canPlaceBlock(world, blockpos, blockstate2, blockstate, blockstate1, blockpos1) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(endertot, net.minecraftforge.common.util.BlockSnapshot.create(world.dimension(), world, blockpos1), net.minecraft.util.Direction.UP)) {
					world.setBlock(blockpos, blockstate2, 3);
					this.endertot.setCarriedBlock((BlockState)null);
				}

			}
		}

		@SuppressWarnings("deprecation")
		private boolean canPlaceBlock(World p_220836_1_, BlockPos p_220836_2_, BlockState p_220836_3_, BlockState p_220836_4_, BlockState p_220836_5_, BlockPos p_220836_6_) {
			return p_220836_4_.isAir(p_220836_1_, p_220836_2_) && !p_220836_5_.isAir(p_220836_1_, p_220836_6_) && !p_220836_5_.is(Blocks.BEDROCK) && !p_220836_5_.is(net.minecraftforge.common.Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST) && p_220836_5_.isCollisionShapeFullBlock(p_220836_1_, p_220836_6_) && p_220836_3_.canSurvive(p_220836_1_, p_220836_2_) && p_220836_1_.getEntities(this.endertot, AxisAlignedBB.unitCubeFromLowerCorner(Vector3d.atLowerCornerOf(p_220836_2_))).isEmpty();
		}
	}
}
