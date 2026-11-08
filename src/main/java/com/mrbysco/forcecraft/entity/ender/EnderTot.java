package com.mrbysco.forcecraft.entity.ender;

import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.EventHooks;

public class EnderTot extends AbstractForceEnderman {
	public EnderTot(EntityType<? extends EnderMan> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder generateAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(10, new PlaceBlockGoal(this));
		this.goalSelector.addGoal(11, new TakeBlockGoal(this));
	}

	@Override
	protected int getTeleportRange() {
		return 32;
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void die(DamageSource cause) {
		if (cause.getDirectEntity() instanceof LivingEntity livingEntity && !this.level().isClientSide()) {
			int total = getRandom().nextInt(2) + 1;
			for (int i = 0; i < total; i++) {
				AngryEnderman enderman = ForceEntities.ANGRY_ENDERMAN.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
				if (enderman != null) {
					enderman.snapTo(getX(), getY() + 0.5D, getZ(), 0.0F, 0.0F);
					enderman.setTarget(livingEntity);
					this.level().addFreshEntity(enderman);
				}
			}
		}
		super.die(cause);
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.ENDER_TOT.get();
	}

	static class TakeBlockGoal extends Goal {
		private final EnderTot endertot;

		public TakeBlockGoal(EnderTot endertot) {
			this.endertot = endertot;
		}

		@Override
		public boolean canUse() {
			return this.endertot.getCarriedBlock() == null
					&& EventHooks.canEntityGrief(getServerLevel(this.endertot.level()), this.endertot)
					&& this.endertot.getRandom().nextInt(reducedTickDelay(20)) == 0;
		}

		@Override
		public void tick() {
			RandomSource random = this.endertot.getRandom();
			Level level = this.endertot.level();
			int x = Mth.floor(this.endertot.getX() - 2.0D + random.nextDouble() * 4.0D);
			int y = Mth.floor(this.endertot.getY() + random.nextDouble() * 3.0D);
			int z = Mth.floor(this.endertot.getZ() - 2.0D + random.nextDouble() * 4.0D);
			BlockPos pos = new BlockPos(x, y, z);
			BlockState state = level.getBlockState(pos);
			Vec3 from = new Vec3(this.endertot.getBlockX() + 0.5D, y + 0.5D, this.endertot.getBlockZ() + 0.5D);
			Vec3 to = new Vec3(x + 0.5D, y + 0.5D, z + 0.5D);
			BlockHitResult hit = level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.endertot));
			if (state.is(ForceTags.Blocks.ENDERTOT_HOLDABLE) && hit.getBlockPos().equals(pos)) {
				level.removeBlock(pos, false);
				this.endertot.setCarriedBlock(state.getBlock().defaultBlockState());
			}
		}
	}

	static class PlaceBlockGoal extends Goal {
		private final EnderTot endertot;

		public PlaceBlockGoal(EnderTot endertot) {
			this.endertot = endertot;
		}

		@Override
		public boolean canUse() {
			return this.endertot.getCarriedBlock() != null
					&& EventHooks.canEntityGrief(getServerLevel(this.endertot.level()), this.endertot)
					&& this.endertot.getRandom().nextInt(reducedTickDelay(2000)) == 0;
		}

		@Override
		public void tick() {
			RandomSource random = this.endertot.getRandom();
			Level level = this.endertot.level();
			int x = Mth.floor(this.endertot.getX() - 1.0D + random.nextDouble() * 2.0D);
			int y = Mth.floor(this.endertot.getY() + random.nextDouble() * 2.0D);
			int z = Mth.floor(this.endertot.getZ() - 1.0D + random.nextDouble() * 2.0D);
			BlockPos pos = new BlockPos(x, y, z);
			BlockPos below = pos.below();
			BlockState carried = this.endertot.getCarriedBlock();
			if (carried != null) {
				carried = Block.updateFromNeighbourShapes(carried, level, pos);
				if (canPlaceBlock(level, pos, carried, level.getBlockState(pos), level.getBlockState(below), below)
						&& !EventHooks.onBlockPlace(endertot, BlockSnapshot.create(level.dimension(), level, below), net.minecraft.core.Direction.UP)) {
					level.setBlock(pos, carried, 3);
					this.endertot.setCarriedBlock(null);
				}
			}
		}

		private boolean canPlaceBlock(Level level, BlockPos pos, BlockState carried, BlockState targetState, BlockState belowState, BlockPos below) {
			return targetState.isAir()
					&& !belowState.isAir()
					&& !belowState.is(Blocks.BEDROCK)
					&& !belowState.is(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST)
					&& belowState.isCollisionShapeFullBlock(level, below)
					&& carried.canSurvive(level, pos)
					&& level.getEntities(this.endertot, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos))).isEmpty();
		}
	}
}
