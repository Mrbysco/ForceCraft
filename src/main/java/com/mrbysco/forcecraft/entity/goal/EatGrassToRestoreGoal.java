package com.mrbysco.forcecraft.entity.goal;

import com.mrbysco.forcecraft.entity.cold.IColdMob;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

import java.util.EnumSet;
import java.util.function.Predicate;

public class EatGrassToRestoreGoal extends Goal {
	private static final Predicate<BlockState> IS_GRASS = (state) -> state.is(ForceTags.Blocks.COLD_EDIBLE_GRASS_BLOCKS);
	private final Mob grassEater;
	private int eatingGrassTimer;

	public EatGrassToRestoreGoal(Mob grassEater) {
		this.grassEater = grassEater;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		if (this.grassEater.getRandom().nextInt(this.grassEater.isBaby() ? 50 : 1000) != 0) {
			return false;
		} else {
			BlockPos blockpos = this.grassEater.blockPosition();
			return IS_GRASS.test(getServerLevel(grassEater).getBlockState(blockpos));
		}
	}

	@Override
	public void start() {
		this.eatingGrassTimer = 40;
		getServerLevel(this.grassEater).broadcastEntityEvent(this.grassEater, (byte) 10);
		this.grassEater.getNavigation().stop();
	}

	@Override
	public void stop() {
		this.eatingGrassTimer = 0;
	}

	@Override
	public boolean canContinueToUse() {
		return this.eatingGrassTimer > 0;
	}

	public int getEatAnimationTick() {
		return this.eatingGrassTimer;
	}

	@Override
	public void tick() {
		this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
		if (this.getEatAnimationTick() == 4) {
			ServerLevel serverLevel = getServerLevel(this.grassEater);
			BlockPos blockpos = this.grassEater.blockPosition();
			if (IS_GRASS.test(serverLevel.getBlockState(blockpos))) {
				if (EventHooks.canEntityGrief(serverLevel, this.grassEater)) {
					serverLevel.destroyBlock(blockpos, false);
				}

				transformMob();
			}
		}
	}

	public void transformMob() {
		if (this.grassEater instanceof IColdMob coldMob) {
			coldMob.tranformMob(this.grassEater, getServerLevel(this.grassEater));
		}
	}
}
