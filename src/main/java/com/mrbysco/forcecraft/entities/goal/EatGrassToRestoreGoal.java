package com.mrbysco.forcecraft.entities.goal;

import com.mrbysco.forcecraft.entities.IColdMob;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.function.Predicate;

public class EatGrassToRestoreGoal extends Goal {
	private static final Predicate<BlockState> IS_GRASS = BlockStatePredicate.forBlock(Blocks.GRASS);
	/** The entity owner of this AITask */
	private final Mob grassEaterEntity;
	/** The world the grass eater entity is eating from */
	private final Level entityWorld;
	/** Number of ticks since the entity started to eat grass */
	private int eatingGrassTimer;

	public EatGrassToRestoreGoal(Mob grassEaterEntityIn) {
		this.grassEaterEntity = grassEaterEntityIn;
		this.entityWorld = grassEaterEntityIn.level;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		if (this.grassEaterEntity.getRandom().nextInt(this.grassEaterEntity.isBaby() ? 50 : 1000) != 0) {
			return false;
		} else {
			BlockPos blockpos = this.grassEaterEntity.blockPosition();
			if (IS_GRASS.test(this.entityWorld.getBlockState(blockpos))) {
				return true;
			} else {
				return this.entityWorld.getBlockState(blockpos.below()).is(Blocks.GRASS_BLOCK);
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.eatingGrassTimer = 40;
		this.entityWorld.broadcastEntityEvent(this.grassEaterEntity, (byte)10);
		this.grassEaterEntity.getNavigation().stop();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		this.eatingGrassTimer = 0;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return this.eatingGrassTimer > 0;
	}

	/**
	 * Number of ticks since the entity started to eat grass
	 */
	public int getEatingGrassTimer() {
		return this.eatingGrassTimer;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
		if (this.getEatingGrassTimer() == 4) {
			BlockPos blockpos = this.grassEaterEntity.blockPosition();
			if (IS_GRASS.test(this.entityWorld.getBlockState(blockpos))) {
				if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.grassEaterEntity)) {
					this.entityWorld.destroyBlock(blockpos, false);
				}

				transformMob();
			} else {
				BlockPos blockpos1 = blockpos.below();
				if (this.entityWorld.getBlockState(blockpos1).is(Blocks.GRASS_BLOCK)) {
					if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.grassEaterEntity)) {
						this.entityWorld.levelEvent(2001, blockpos1, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
						this.entityWorld.setBlock(blockpos1, Blocks.DIRT.defaultBlockState(), 2);
					}

					transformMob();
				}
			}
		}
	}

	public void transformMob() {
		if(this.grassEaterEntity instanceof IColdMob coldMob) {
			coldMob.transformMob(this.grassEaterEntity, this.entityWorld);
		}
	}
}