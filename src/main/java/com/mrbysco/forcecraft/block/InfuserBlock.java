package com.mrbysco.forcecraft.block;

import com.mojang.serialization.MapCodec;
import com.mrbysco.forcecraft.blockentity.infuser.InfuserBlockEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;

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

	public InfuserBlock(Properties properties) {
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

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InfuserBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createTickerHelper(type, ForceRegistry.INFUSER_BLOCK_ENTITY.get(), level.isClientSide() ?
				InfuserBlockEntity::clientTick :
				InfuserBlockEntity::serverTick
		);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		var itemHandler = ItemAccess.forPlayerInteraction(player, hand).oneByOne();
		var fluidCap = itemHandler.getCapability(Capabilities.Fluid.ITEM);
		if (fluidCap != null) {
			if (!level.isClientSide()) {
				FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection(), null);
				return InteractionResult.SUCCESS_SERVER;
			}

			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
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
					infuserBE.startWork(false);
					infuserBE.setChanged();
				}
			}
		}
	}
}
