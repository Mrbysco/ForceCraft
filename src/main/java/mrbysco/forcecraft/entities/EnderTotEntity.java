package mrbysco.forcecraft.entities;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.registry.ForceEntities;
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
		this.setPathPriority(PathNodeType.WATER, 8.0F); //Reset to default as Ender Tots aren't afraid of water
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
		this.targetSelector.addGoal(1, new EnderTotEntity.FindPlayerGoal(this, this::func_233680_b_));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, 10, true, false, field_213627_bA));
		this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.35F;
	}

	@Override
	public boolean isChild() {
		return true;
	}

	@Override
	public boolean teleportRandomly() {
		if (!this.world.isRemote() && this.isAlive() && !this.isInWaterOrBubbleColumn()) {
			double d0 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 32.0D;
			double d1 = this.getPosY() + (double)(this.rand.nextInt(32) - 16);
			double d2 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 32;
			return this.teleportTo(d0, d1, d2);
		} else {
			return false;
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		Entity entitySource = cause.getImmediateSource();
		if(entitySource instanceof LivingEntity && !this.world.isRemote) {
			LivingEntity livingEntity = (LivingEntity) entitySource;
			int total = getRNG().nextInt(2) + 1;
			for(int i = 0; i < total; i++) {
				AngryEndermanEntity endermanEntity = ForceEntities.ANGRY_ENDERMAN.get().create(world);
				endermanEntity.setLocationAndAngles(getPosX(), getPosY() + 0.5D, getPosZ(), 0.0F, 0.0F);
				endermanEntity.setAttackTarget(livingEntity);
				world.addEntity(endermanEntity);
			}
		}
		super.onDeath(cause);
	}

	public static AttributeModifierMap.MutableAttribute generateAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.35F)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 64.0D);
	}

	static class StareGoal extends Goal {
		private final EnderTotEntity endertot;
		private LivingEntity targetPlayer;

		public StareGoal(EnderTotEntity endertotIn) {
			this.endertot = endertotIn;
			this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			this.targetPlayer = this.endertot.getAttackTarget();
			if (!(this.targetPlayer instanceof PlayerEntity)) {
				return false;
			} else {
				double d0 = this.targetPlayer.getDistanceSq(this.endertot);
				return !(d0 > 256.0D) && this.endertot.shouldAttackPlayer((PlayerEntity) this.targetPlayer);
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.endertot.getNavigator().clearPath();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			this.endertot.getLookController().setLookPosition(this.targetPlayer.getPosX(), this.targetPlayer.getPosYEye(), this.targetPlayer.getPosZ());
		}
	}

	static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
		private final EnderTotEntity endertot;
		/** The player */
		private PlayerEntity player;
		private int aggroTime;
		private int teleportTime;
		private final EntityPredicate field_220791_m;
		private final EntityPredicate field_220792_n = (new EntityPredicate()).setIgnoresLineOfSight();

		public FindPlayerGoal(EnderTotEntity enderTotIn, @Nullable Predicate<LivingEntity> p_i241912_2_) {
			super(enderTotIn, PlayerEntity.class, 10, false, false, p_i241912_2_);
			this.endertot = enderTotIn;
			this.field_220791_m = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate((p_220790_1_) -> enderTotIn.shouldAttackPlayer((PlayerEntity)p_220790_1_));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			this.player = this.endertot.world.getClosestPlayer(this.field_220791_m, this.endertot);
			return this.player != null;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			this.aggroTime = 5;
			this.teleportTime = 0;
			this.endertot.func_226538_eu_();
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
				if (!this.endertot.shouldAttackPlayer(this.player)) {
					return false;
				} else {
					this.endertot.faceEntity(this.player, 10.0F, 10.0F);
					return true;
				}
			} else {
				return this.nearestTarget != null && this.field_220792_n.canTarget(this.endertot, this.nearestTarget) || super.shouldContinueExecuting();
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			if (this.endertot.getAttackTarget() == null) {
				super.setNearestTarget((LivingEntity)null);
			}

			if (this.player != null) {
				if (--this.aggroTime <= 0) {
					this.nearestTarget = this.player;
					this.player = null;
					super.startExecuting();
				}
			} else {
				if (this.nearestTarget != null && !this.endertot.isPassenger()) {
					if (this.endertot.shouldAttackPlayer((PlayerEntity)this.nearestTarget)) {
						if (this.nearestTarget.getDistanceSq(this.endertot) < 16.0D) {
							this.endertot.teleportRandomly();
						}

						this.teleportTime = 0;
					} else if (this.nearestTarget.getDistanceSq(this.endertot) > 128.0D && this.teleportTime++ >= 30 && this.endertot.teleportToEntity(this.nearestTarget)) {
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
		public boolean shouldExecute() {
			if (this.endertot.getHeldBlockState() != null) {
				return false;
			} else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.endertot.world, this.endertot)) {
				return false;
			} else {
				return this.endertot.getRNG().nextInt(20) == 0;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			Random random = this.endertot.getRNG();
			World world = this.endertot.world;
			int i = MathHelper.floor(this.endertot.getPosX() - 2.0D + random.nextDouble() * 4.0D);
			int j = MathHelper.floor(this.endertot.getPosY() + random.nextDouble() * 3.0D);
			int k = MathHelper.floor(this.endertot.getPosZ() - 2.0D + random.nextDouble() * 4.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = world.getBlockState(blockpos);
			Block block = blockstate.getBlock();
			Vector3d vector3d = new Vector3d((double)MathHelper.floor(this.endertot.getPosX()) + 0.5D, (double)j + 0.5D, (double)MathHelper.floor(this.endertot.getPosZ()) + 0.5D);
			Vector3d vector3d1 = new Vector3d((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D);
			BlockRayTraceResult blockraytraceresult = world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this.endertot));
			boolean flag = blockraytraceresult.getPos().equals(blockpos);
			ITagCollectionSupplier tagCollection = TagCollectionManager.getManager();
			ITag<Block> holdableTag = tagCollection.getBlockTags().get(TOT_HOLDABLE);
			if (holdableTag != null && block.isIn(holdableTag) && flag) {
				world.removeBlock(blockpos, false);
				this.endertot.setHeldBlockState(blockstate.getBlock().getDefaultState());
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
		public boolean shouldExecute() {
			if (this.endertot.getHeldBlockState() == null) {
				return false;
			} else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.endertot.world, this.endertot)) {
				return false;
			} else {
				return this.endertot.getRNG().nextInt(2000) == 0;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			Random random = this.endertot.getRNG();
			World world = this.endertot.world;
			int i = MathHelper.floor(this.endertot.getPosX() - 1.0D + random.nextDouble() * 2.0D);
			int j = MathHelper.floor(this.endertot.getPosY() + random.nextDouble() * 2.0D);
			int k = MathHelper.floor(this.endertot.getPosZ() - 1.0D + random.nextDouble() * 2.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = world.getBlockState(blockpos);
			BlockPos blockpos1 = blockpos.down();
			BlockState blockstate1 = world.getBlockState(blockpos1);
			BlockState blockstate2 = this.endertot.getHeldBlockState();
			if (blockstate2 != null) {
				blockstate2 = Block.getValidBlockForPosition(blockstate2, this.endertot.world, blockpos);
				if (this.func_220836_a(world, blockpos, blockstate2, blockstate, blockstate1, blockpos1) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(endertot, net.minecraftforge.common.util.BlockSnapshot.create(world.getDimensionKey(), world, blockpos1), net.minecraft.util.Direction.UP)) {
					world.setBlockState(blockpos, blockstate2, 3);
					this.endertot.setHeldBlockState((BlockState)null);
				}

			}
		}

		@SuppressWarnings("deprecation")
		private boolean func_220836_a(World p_220836_1_, BlockPos p_220836_2_, BlockState p_220836_3_, BlockState p_220836_4_, BlockState p_220836_5_, BlockPos p_220836_6_) {
			return p_220836_4_.isAir(p_220836_1_, p_220836_2_) && !p_220836_5_.isAir(p_220836_1_, p_220836_6_) && !p_220836_5_.matchesBlock(Blocks.BEDROCK) && !p_220836_5_.isIn(net.minecraftforge.common.Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST) && p_220836_5_.hasOpaqueCollisionShape(p_220836_1_, p_220836_6_) && p_220836_3_.isValidPosition(p_220836_1_, p_220836_2_) && p_220836_1_.getEntitiesWithinAABBExcludingEntity(this.endertot, AxisAlignedBB.fromVector(Vector3d.copy(p_220836_2_))).isEmpty();
		}
	}
}
