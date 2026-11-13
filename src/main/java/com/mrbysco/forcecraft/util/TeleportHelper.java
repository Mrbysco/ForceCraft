package com.mrbysco.forcecraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

public class TeleportHelper {
	public static void teleportRandomly(LivingEntity livingEntity) {
		if (!livingEntity.level().isClientSide() && livingEntity.isAlive() && !livingEntity.isInWaterOrRain()) {
			double xx = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
			double yy = livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(32) - 16);
			double zz = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
			teleportTo(livingEntity, xx, yy, zz);
		}
	}

	public static void teleportTo(LivingEntity livingEntity, double x, double y, double z) {
		Level level = livingEntity.level();
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(x, y, z);

		while (blockpos$mutable.getY() > 0 && !level.getBlockState(blockpos$mutable).is(BlockTags.BLOCKS_MOTION)) {
			blockpos$mutable.move(Direction.DOWN);
		}

		BlockState blockstate = level.getBlockState(blockpos$mutable);
		boolean couldStandOn = blockstate.is(BlockTags.BLOCKS_MOTION);
		boolean isWet = blockstate.getFluidState().is(FluidTags.WATER);
		if (couldStandOn && !isWet) {
			EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(livingEntity, x, y, z);
			if (event.isCanceled()) return;
			boolean flag2 = livingEntity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true, BlockTags.ENDERMAN_DOES_NOT_TELEPORT_TO);
			if (flag2 && !livingEntity.isSilent()) {
				level.playSound((Player) null, livingEntity.xo, livingEntity.yo, livingEntity.zo, SoundEvents.ENDERMAN_TELEPORT, livingEntity.getSoundSource(), 1.0F, 1.0F);
				livingEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

}
