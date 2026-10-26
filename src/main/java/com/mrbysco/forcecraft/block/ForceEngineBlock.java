package com.mrbysco.forcecraft.block;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.blockentity.ForceEngineBlockEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.ToIntFunction;

public class ForceEngineBlock extends DirectionalBlock implements EntityBlock {
	public static final MapCodec<ForceEngineBlock> CODEC = simpleCodec(ForceEngineBlock::new);

	private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(makeShape());

	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty GENERATING = BooleanProperty.create("generating");

	public ForceEngineBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP)
				.setValue(POWERED, false).setValue(GENERATING, false));
	}

	@Override
	protected @NonNull MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, @NonNull BlockState state,
	                                               @NonNull Level level, @NonNull BlockPos pos, Player player,
	                                               @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
		if (!player.isShiftKeyDown()) {
			var itemHandler = ItemAccess.forPlayerInteraction(player, hand).oneByOne();
			var fluidCap = itemHandler.getCapability(Capabilities.Fluid.ITEM);
			if (fluidCap != null) {
				if (!level.isClientSide()) {
					FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection(), null);
					return InteractionResult.SUCCESS_SERVER;
				}

				return InteractionResult.SUCCESS;
			}
		}

		return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected @NonNull RenderShape getRenderShape(@NonNull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos,
	                                                    @NonNull Player player, @NonNull BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof ForceEngineBlockEntity engineBE) {
				player.openMenu(engineBE, pos);
			}

			return InteractionResult.CONSUME;
		}
	}

	private static VoxelShape makeShape() {
		return Shapes.or(
				Block.box(4, 4, 0, 12, 12, 8),
				Block.box(0, 0, 10, 16, 16, 16),
				Block.box(2, 2, 6, 14, 14, 10)
		);
	}

	@Override
	public @NonNull VoxelShape getShape(BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos,
	                                    @NonNull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, GENERATING);
	}

	@Override
	public @NonNull BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public @NonNull BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
		return blockstate.is(this) && blockstate.getValue(FACING) == direction ?
				this.defaultBlockState().setValue(FACING, direction)
						.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos())) :
				this.defaultBlockState().setValue(FACING, direction.getOpposite())
						.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	protected void neighborChanged(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Block block,
	                               @Nullable Orientation orientation, boolean movedByPiston) {
		if (!level.isClientSide()) {
			boolean flag = state.getValue(POWERED);
			if (flag != level.hasNeighborSignal(pos)) {
				if (flag) {
					level.scheduleTick(pos, this, 4);
				} else {
					level.setBlock(pos, state.cycle(POWERED), 2);
				}
			}
		}
	}

	@Override
	public void tick(BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource rand) {
		if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
			level.setBlock(pos, state.cycle(POWERED), 2);
		}
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
		return new ForceEngineBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level,
	                                                                        @NonNull BlockState blockState,
	                                                                        @NonNull BlockEntityType<T> type) {
		return createEngineTicker(level, type, ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get());
	}

	protected static @Nullable <T extends BlockEntity> BlockEntityTicker<T> createEngineTicker(
			Level level, BlockEntityType<T> entityType, BlockEntityType<? extends ForceEngineBlockEntity> entityType1
	) {
		return level.isClientSide() ? null : createTickerHelper(entityType, entityType1, ForceEngineBlockEntity::serverTick);
	}

	@SuppressWarnings("unchecked")
	protected static @Nullable <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
			BlockEntityType<A> entityType, BlockEntityType<E> entityType1, BlockEntityTicker<? super E> entityTicker
	) {
		return entityType1 == entityType ? (BlockEntityTicker<A>) entityTicker : null;
	}

	public static ToIntFunction<BlockState> getLightValueActive(int lightValue) {
		return (state) -> state.getValue(GENERATING) ? lightValue : 0;
	}

}
