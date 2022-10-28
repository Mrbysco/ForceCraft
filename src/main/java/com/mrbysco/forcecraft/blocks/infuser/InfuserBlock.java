package com.mrbysco.forcecraft.blocks.infuser;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class InfuserBlock extends Block {

	private static final VoxelShape SHAPE = Stream.of(
			Block.box(3, 10, 3, 13, 11, 13),
			Block.box(2, 0, 2, 4, 5, 4),
			Block.box(12, 0, 2, 14, 5, 4),
			Block.box(12, 0, 12, 14, 5, 14),
			Block.box(2, 0, 12, 4, 5, 14),
			Block.box(2, 5, 2, 14, 10, 14)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

	public InfuserBlock(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new InfuserTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof InfuserTileEntity) {
			LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (playerIn.getItemInHand(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
					FluidUtil.interactWithFluidHandler(playerIn, handIn, worldIn, pos, hit.getDirection());
				} else {
					if (!worldIn.isClientSide) {
						NetworkHooks.openGui((ServerPlayerEntity) playerIn, (InfuserTileEntity) tileentity, pos);
					}
				}
			});

			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof InfuserTileEntity) {
				tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					for (int i = 0; i < handler.getSlots(); ++i) {
						InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
					}
				});
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof InfuserTileEntity) {
			InfuserTileEntity infuserTile = (InfuserTileEntity) tileentity;
			if (infuserTile.processTime > 0) {
				double d0 = (double) pos.getX() + 0.5D;
				double d1 = (double) pos.getY() + 0.5;
				double d2 = (double) pos.getZ() + 0.5D;

				Direction direction = Direction.UP;
				Direction.Axis direction$axis = direction.getAxis();
				for (int i = 0; i < 3; i++) {
					double d3 = 0.52D;
					double d4 = rand.nextDouble() * 0.6D - 0.3D;
					double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * d3 : d4;
					double d6 = rand.nextDouble() * 6.0D / 16.0D;
					double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * d3 : d4;
					worldIn.addParticle(ParticleTypes.REVERSE_PORTAL, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
}

