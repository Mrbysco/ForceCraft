package mrbysco.forcecraft.blocks.engine;

import mrbysco.forcecraft.tiles.ForceEngineTile;
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

public class ForceEngineBlock extends DirectionalBlock {

	public static final VoxelShape SHAPE_UP = Stream.of(
			Block.makeCuboidShape(4, 8, 4, 12, 16, 12),
			Block.makeCuboidShape(11, 4, 3, 15, 11, 13),
			Block.makeCuboidShape(1, 4, 3, 5, 11, 13),
			Block.makeCuboidShape(3, 4, 12, 13, 10, 14),
			Block.makeCuboidShape(3, 4, 2, 13, 10, 4),
			Block.makeCuboidShape(0, 0, 0, 16, 4, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_DOWN = Stream.of(
			Block.makeCuboidShape(4, 0, 4, 12, 8, 12),
			Block.makeCuboidShape(11, 5, 3, 15, 12, 13),
			Block.makeCuboidShape(1, 5, 3, 5, 12, 13),
			Block.makeCuboidShape(3, 6, 2, 13, 12, 4),
			Block.makeCuboidShape(3, 6, 12, 13, 12, 14),
			Block.makeCuboidShape(0, 12, 0, 16, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_NORTH = Stream.of(
			Block.makeCuboidShape(4, 4, 0, 12, 12, 8),
			Block.makeCuboidShape(11, 3, 5, 15, 13, 12),
			Block.makeCuboidShape(1, 3, 5, 5, 13, 12),
			Block.makeCuboidShape(3, 12, 6, 13, 14, 12),
			Block.makeCuboidShape(3, 2, 6, 13, 4, 12),
			Block.makeCuboidShape(0, 0, 12, 16, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_EAST = Stream.of(
			Block.makeCuboidShape(8, 4, 4, 16, 12, 12),
			Block.makeCuboidShape(4, 3, 11, 11, 13, 15),
			Block.makeCuboidShape(4, 3, 1, 11, 13, 5),
			Block.makeCuboidShape(4, 12, 3, 10, 14, 13),
			Block.makeCuboidShape(4, 2, 3, 10, 4, 13),
			Block.makeCuboidShape(0, 0, 0, 4, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_SOUTH = Stream.of(
			Block.makeCuboidShape(4, 4, 8, 12, 12, 16),
			Block.makeCuboidShape(11, 3, 4, 15, 13, 11),
			Block.makeCuboidShape(1, 3, 4, 5, 13, 11),
			Block.makeCuboidShape(3, 2, 4, 13, 4, 10),
			Block.makeCuboidShape(3, 12, 4, 13, 14, 10),
			Block.makeCuboidShape(0, 0, 0, 16, 16, 4)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public static final VoxelShape SHAPE_WEST = Stream.of(
			Block.makeCuboidShape(0, 4, 4, 8, 12, 12),
			Block.makeCuboidShape(5, 3, 11, 12, 13, 15),
			Block.makeCuboidShape(5, 3, 1, 12, 13, 5),
			Block.makeCuboidShape(6, 2, 3, 12, 4, 13),
			Block.makeCuboidShape(6, 12, 3, 12, 14, 13),
			Block.makeCuboidShape(12, 0, 0, 16, 16, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public ForceEngineBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(ACTIVE, false));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof ForceEngineTile) {
			LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace());
			fluidHandler.ifPresent((handler) -> {
				if(player.getHeldItem(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
					FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.getFace());
				} else {
					if (!worldIn.isRemote) {
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
		switch (state.get(FACING)) {
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
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
		return blockstate.matchesBlock(this) && blockstate.get(FACING) == direction ? this.getDefaultState().with(FACING, direction).with(ACTIVE, Boolean.valueOf(context.getWorld().isBlockPowered(context.getPos()))) :
				this.getDefaultState().with(FACING, direction.getOpposite()).with(ACTIVE, Boolean.valueOf(context.getWorld().isBlockPowered(context.getPos())));
	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isRemote) {
			boolean flag = state.get(ACTIVE);
			if (flag != worldIn.isBlockPowered(pos)) {
				if (flag) {
					worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlockState(pos, state.cycleValue(ACTIVE), 2);
				}
			}
		}
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.get(ACTIVE) && !worldIn.isBlockPowered(pos)) {
			worldIn.setBlockState(pos, state.cycleValue(ACTIVE), 2);
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
		return (state) -> {
			return state.get(ACTIVE) ? lightValue : 0;
		};
	}

	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(ACTIVE)) {
			Direction direction = stateIn.get(FACING);
			double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
			double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
			double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
			double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
			if (rand.nextInt(5) == 0) {
				worldIn.addParticle(ParticleTypes.SMOKE, d0 + (double)direction.getXOffset() * d3, d1 + (double)direction.getYOffset() * d3, d2 + (double)direction.getZOffset() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
			}
		}
	}

	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.matchesBlock(newState.getBlock())) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof ForceEngineTile) {
				ForceEngineTile engineTile = (ForceEngineTile) tileentity;
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), engineTile.handler.getStackInSlot(0));
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), engineTile.throttleHandler.getStackInSlot(0));
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
