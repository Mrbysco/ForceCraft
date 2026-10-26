package com.mrbysco.forcecraft.block;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.blockentity.furnace.ForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

import java.util.function.ToIntFunction;

public class ForceFurnaceBlock extends AbstractFurnaceBlock {
	public static final MapCodec<ForceFurnaceBlock> CODEC = simpleCodec(ForceFurnaceBlock::new);

	public ForceFurnaceBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends AbstractFurnaceBlock> codec() {
		return CODEC;
	}

	public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
		return (state) -> {
			return state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		};
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player) {
			BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof ForceFurnaceBlockEntity furnaceBE) {
			if (!level.isClientSide()) {
				player.openMenu(furnaceBE, pos);
			}
			player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			double x = pos.getX() + 0.5;
			double y = pos.getY();
			double z = pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1) {
				level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis axis = direction.getAxis();
			double r = 0.52;
			double ss = random.nextDouble() * 0.6 - 0.3;
			double dx = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : ss;
			double dy = random.nextDouble() * 6.0 / 16.0;
			double dz = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : ss;
			level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
			level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
		}
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ForceFurnaceBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createForceFurnaceTicker(level, type, ForceRegistry.FURNACE_BLOCK_ENTITY.get());
	}

	protected static <T extends BlockEntity> @Nullable BlockEntityTicker<T> createForceFurnaceTicker(
			Level level, BlockEntityType<T> actualType, BlockEntityType<? extends ForceFurnaceBlockEntity> expectedType
	) {
		return level instanceof ServerLevel serverLevel
				? createTickerHelper(
				actualType, expectedType, (innerLevel, pos, state, entity) ->
						ForceFurnaceBlockEntity.forceServerTick(serverLevel, pos, state, entity)
		)
				: null;
	}
}
