package com.mrbysco.forcecraft.blocks.infuser;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.blockentities.InfuserBlockEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jetbrains.annotations.Nullable;

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
	public static final MapCodec<InfuserBlock> CODEC = simpleCodec(InfuserBlock::new);

	public InfuserBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
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
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInfuserTicker(level, blockEntityType, ForceRegistry.INFUSER_BLOCK_ENTITY.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createInfuserTicker(Level level, BlockEntityType<T> p_151989_, BlockEntityType<? extends InfuserBlockEntity> infuserBlockEntityType) {
		return level.isClientSide() ? null : createTickerHelper(p_151989_, infuserBlockEntityType, InfuserBlockEntity::serverTick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
	                                      Player player, InteractionHand hand, BlockHitResult hitResult) {
		ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, pos, hitResult.getDirection());
		if (handler != null) {
			if (ItemAccess.forPlayerInteraction(player, hand).getCapability(Capabilities.Fluid.ITEM) != null) {
				if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection()))
					return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof InfuserBlockEntity infuserBE) {
				player.openMenu(infuserBE, pos);
			}

			return InteractionResult.CONSUME;
		}
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @org.jspecify.annotations.Nullable Orientation orientation, boolean movedByPiston) {

		if (level.isClientSide()) {
			boolean flag = level.hasNeighborSignal(pos);
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (flag && blockentity instanceof InfuserBlockEntity infuserBE) {
				if (infuserBE.hasValidRecipe() && !infuserBE.canWork) {
					infuserBE.startWork();
					infuserBE.setChanged();
				}
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
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

