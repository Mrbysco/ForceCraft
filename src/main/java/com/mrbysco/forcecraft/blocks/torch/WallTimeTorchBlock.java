package com.mrbysco.forcecraft.blocks.torch;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.blockentities.TimeTorchBlockEntity;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WallTimeTorchBlock extends WallTorchBlock implements EntityBlock {

	public WallTimeTorchBlock(Properties properties, SimpleParticleType particleData) {
		super(particleData, properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ConfigHandler.COMMON.timeTorchEnabled.get() ? new TimeTorchBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createTimeTorchTicker(level, blockEntityType, ForceRegistry.TIME_TORCH_BLOCK_ENTITY.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createTimeTorchTicker(Level level, BlockEntityType<T> entityType, BlockEntityType<? extends TimeTorchBlockEntity> timeTorchBlockEntityType) {
		return level.isClientSide ? null : createTickerHelper(entityType, timeTorchBlockEntityType, TimeTorchBlockEntity::serverTick);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType,
																											BlockEntityType<E> entityType,
																											BlockEntityTicker<? super E> entityTicker) {
		return entityType == blockEntityType ? (BlockEntityTicker<A>) entityTicker : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (ConfigHandler.COMMON.timeTorchLogging.get()) {
			if (placer != null) {
				ForceCraft.LOGGER.info("A Time Torch has been placed at {} by {}", pos, placer.getDisplayName().getString());
			} else {
				ForceCraft.LOGGER.info("A Time Torch has been placed at {} by a non entity", pos);
			}
		}
	}
}