package com.mrbysco.forcecraft.block.torch;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.blockentity.torch.AbstractTorchBlockEntity;
import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class ForceTorchBlock extends TorchBlock implements EntityBlock {

	private final Supplier<BlockEntityType<AbstractTorchBlockEntity>> blockEntityTypeSupplier;

	public ForceTorchBlock(SimpleParticleType supered, Properties properties, Supplier<BlockEntityType<AbstractTorchBlockEntity>> blockEntityTypeSupplier) {
		super(supered, properties);
		this.blockEntityTypeSupplier = blockEntityTypeSupplier;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof AbstractTorchBlockEntity abstractTorchBlockEntity) {
			abstractTorchBlockEntity.onInteract(player, hitResult);
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return blockEntityTypeSupplier.get().create(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createTorchTicker(level, type, blockEntityTypeSupplier.get());
	}

	protected static <T extends BlockEntity> @Nullable BlockEntityTicker<T> createTorchTicker(
			Level level, BlockEntityType<T> actualType, BlockEntityType<AbstractTorchBlockEntity> expectedType
	) {
		return level instanceof ServerLevel serverLevel
				? createTickerHelper(
				actualType, expectedType, (innerLevel, pos, state, entity) -> AbstractTorchBlockEntity.serverTick(serverLevel, pos, state, entity)
		)
				: null;
	}

	protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
			BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker
	) {
		return expected == actual ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (state.is(ForceRegistry.TIME_TORCH.getTorch()) && ForceConfig.COMMON.timeTorchLogging.get()) {
			if (placer != null) {
				ForceCraft.LOGGER.info("A Time Torch has been placed at {} by {}", pos, placer.getDisplayName().getString());
			} else {
				ForceCraft.LOGGER.info("A Time Torch has been placed at {} by a non entity", pos);
			}
		}
	}
}
