package com.mrbysco.forcecraft.blocks.infuser;

import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class InfuserBlock extends BaseEntityBlock {

	private static final VoxelShape SHAPE = Stream.of(
			Block.box(3, 10, 3, 13, 11, 13),
			Block.box(2, 0, 2, 4, 5, 4),
			Block.box(12, 0, 2, 14, 5, 4),
			Block.box(12, 0, 12, 14, 5, 14),
			Block.box(2, 0, 12, 4, 5, 14),
			Block.box(2, 5, 2, 14, 10, 14)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	public InfuserBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InfuserBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInfuserTicker(level, blockEntityType, ForceRegistry.INFUSER_BLOCK_ENTITY.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createInfuserTicker(Level level, BlockEntityType<T> p_151989_, BlockEntityType<? extends InfuserBlockEntity> infuserBlockEntityType) {
		return level.isClientSide ? null : createTickerHelper(p_151989_, infuserBlockEntityType, InfuserBlockEntity::serverTick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof InfuserBlockEntity) {
			LazyOptional<IFluidHandler> fluidHandler = blockentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (playerIn.getItemInHand(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
					FluidUtil.interactWithFluidHandler(playerIn, handIn, level, pos, hit.getDirection());
				} else {
					if (!level.isClientSide) {
						NetworkHooks.openScreen((ServerPlayer) playerIn, (InfuserBlockEntity) blockentity, pos);
					}
				}
			});

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof InfuserBlockEntity) {
				blockentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					for (int i = 0; i < handler.getSlots(); ++i) {
						Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
					}
				});
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof InfuserBlockEntity infuserTile) {
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
					level.addParticle(ParticleTypes.REVERSE_PORTAL, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
}

