package com.mrbysco.forcecraft.blocks.engine;

import com.mrbysco.forcecraft.tiles.ForceEngineTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import net.minecraft.block.AbstractBlock.Properties;

public class ForceEngineBlock extends DirectionalBlock {

	public static final VoxelShape SHAPE_UP = Stream.of(
			Block.box(4, 8, 4, 12, 16, 12),
			Block.box(11, 4, 3, 15, 11, 13),
			Block.box(1, 4, 3, 5, 11, 13),
			Block.box(3, 4, 12, 13, 10, 14),
			Block.box(3, 4, 2, 13, 10, 4),
			Block.box(0, 0, 0, 16, 4, 16)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_DOWN = Stream.of(
			Block.box(4, 0, 4, 12, 8, 12),
			Block.box(11, 5, 3, 15, 12, 13),
			Block.box(1, 5, 3, 5, 12, 13),
			Block.box(3, 6, 2, 13, 12, 4),
			Block.box(3, 6, 12, 13, 12, 14),
			Block.box(0, 12, 0, 16, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_NORTH = Stream.of(
			Block.box(4, 4, 0, 12, 12, 8),
			Block.box(11, 3, 5, 15, 13, 12),
			Block.box(1, 3, 5, 5, 13, 12),
			Block.box(3, 12, 6, 13, 14, 12),
			Block.box(3, 2, 6, 13, 4, 12),
			Block.box(0, 0, 12, 16, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_EAST = Stream.of(
			Block.box(8, 4, 4, 16, 12, 12),
			Block.box(4, 3, 11, 11, 13, 15),
			Block.box(4, 3, 1, 11, 13, 5),
			Block.box(4, 12, 3, 10, 14, 13),
			Block.box(4, 2, 3, 10, 4, 13),
			Block.box(0, 0, 0, 4, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_SOUTH = Stream.of(
			Block.box(4, 4, 8, 12, 12, 16),
			Block.box(11, 3, 4, 15, 13, 11),
			Block.box(1, 3, 4, 5, 13, 11),
			Block.box(3, 2, 4, 13, 4, 10),
			Block.box(3, 12, 4, 13, 14, 10),
			Block.box(0, 0, 0, 16, 16, 4)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_WEST = Stream.of(
			Block.box(0, 4, 4, 8, 12, 12),
			Block.box(5, 3, 11, 12, 13, 15),
			Block.box(5, 3, 1, 12, 13, 5),
			Block.box(6, 2, 3, 12, 4, 13),
			Block.box(6, 12, 3, 12, 14, 13),
			Block.box(12, 0, 0, 16, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public ForceEngineBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(ACTIVE, false));
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof ForceEngineTile) {
			LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (player.getItemInHand(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
					FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.getDirection());
				} else {
					if (!worldIn.isClientSide) {
						NetworkHooks.openGui((ServerPlayerEntity) player, (ForceEngineTile) tileentity, pos);
					}
				}
			});

			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.getValue(FACING)) {
			case UP:
				return SHAPE_UP;
			case DOWN:
				return SHAPE_DOWN;
			case NORTH:
				return SHAPE_NORTH;
			case EAST:
				return SHAPE_EAST;
			case SOUTH:
				return SHAPE_SOUTH;
			case WEST:
				return SHAPE_WEST;
			default:
				return super.getShape(state, worldIn, pos, context);
		}
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getClickedFace();
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
		return blockstate.is(this) && blockstate.getValue(FACING) == direction ? this.defaultBlockState().setValue(FACING, direction).setValue(ACTIVE, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos()))) :
				this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(ACTIVE, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide) {
			boolean flag = state.getValue(ACTIVE);
			if (flag != worldIn.hasNeighborSignal(pos)) {
				if (flag) {
					worldIn.getBlockTicks().scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlock(pos, state.cycle(ACTIVE), 2);
				}
			}
		}
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.getValue(ACTIVE) && !worldIn.hasNeighborSignal(pos)) {
			worldIn.setBlock(pos, state.cycle(ACTIVE), 2);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ForceEngineTile();
	}

	public static ToIntFunction<BlockState> getLightValueActive(int lightValue) {
		return (state) -> state.getValue(ACTIVE) ? lightValue : 0;
	}

	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(ACTIVE)) {
			Direction direction = stateIn.getValue(FACING);
			double d0 = (double) pos.getX() + 0.55D - (double) (rand.nextFloat() * 0.1F);
			double d1 = (double) pos.getY() + 0.55D - (double) (rand.nextFloat() * 0.1F);
			double d2 = (double) pos.getZ() + 0.55D - (double) (rand.nextFloat() * 0.1F);
			double d3 = (double) (0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
			if (rand.nextInt(5) == 0) {
				worldIn.addParticle(ParticleTypes.SMOKE, d0 + (double) direction.getStepX() * d3, d1 + (double) direction.getStepY() * d3, d2 + (double) direction.getStepZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof ForceEngineTile) {
				ForceEngineTile engineTile = (ForceEngineTile) tileentity;
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), engineTile.inputHandler.getStackInSlot(0));
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), engineTile.inputHandler.getStackInSlot(1));
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), engineTile.outputHandler.getStackInSlot(0));
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), engineTile.outputHandler.getStackInSlot(1));
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}
