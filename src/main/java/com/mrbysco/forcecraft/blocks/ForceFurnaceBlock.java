package com.mrbysco.forcecraft.blocks;

import com.mrbysco.forcecraft.blockentities.AbstractForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.blockentities.ForceFurnaceBlockEntity;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.ToIntFunction;

public class ForceFurnaceBlock extends AbstractFurnaceBlock implements EntityBlock {

	private static final String NBT_UPGRADE = "upgrade";

	public ForceFurnaceBlock(BlockBehaviour.Properties builder) {
		super(builder);
	}

	public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
		return (state) -> {
			return state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		};
	}

	protected void openContainer(Level level, BlockPos pos, Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof ForceFurnaceBlockEntity) {
			if (!level.isClientSide) {
				NetworkHooks.openGui((ServerPlayer) player, (ForceFurnaceBlockEntity) blockentity, pos);
			}
			player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * d3 : d4;
			double d6 = rand.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * d3 : d4;
			level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ForceFurnaceBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createForceFurnaceTicker(level, blockEntityType, ForceRegistry.FURNACE_BLOCK_ENTITY.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createForceFurnaceTicker(Level level, BlockEntityType<T> p_151989_, BlockEntityType<? extends AbstractForceFurnaceBlockEntity> abstractForceFurnaceType) {
		return level.isClientSide ? null : createTickerHelper(p_151989_, abstractForceFurnaceType, AbstractForceFurnaceBlockEntity::serverTick);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof AbstractForceFurnaceBlockEntity furnaceTile) {
				for (int i = 0; i < furnaceTile.getContainerSize(); ++i) {
					if (!(furnaceTile.getItem(i).getItem() instanceof UpgradeCoreItem)) {
						spawnItemStack(level, pos.getX(), pos.getY(), pos.getZ(), furnaceTile.getItem(i));
					}
				}
				((AbstractForceFurnaceBlockEntity) blockentity).grantStoredRecipeExperience(level, Vec3.atCenterOf(pos));
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	public static void spawnItemStack(Level level, double x, double y, double z, ItemStack stack) {
		double d0 = (double) EntityType.ITEM.getWidth();
		double d1 = 1.0D - d0;
		double d2 = d0 / 2.0D;
		double d3 = Math.floor(x) + level.random.nextDouble() * d1 + d2;
		double d4 = Math.floor(y) + level.random.nextDouble() * d1;
		double d5 = Math.floor(z) + level.random.nextDouble() * d1 + d2;

		while (!stack.isEmpty()) {
			ItemEntity itementity = new ItemEntity(level, d3, d4, d5, stack.split(level.random.nextInt(21) + 10));
			float f = 0.05F;
			itementity.setDeltaMovement(level.random.nextGaussian() * (double) 0.05F, level.random.nextGaussian() * (double) f + (double) 0.2F, level.random.nextGaussian() * (double) f);
			level.addFreshEntity(itementity);
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof AbstractForceFurnaceBlockEntity furnaceTile) {

			if (stack.hasCustomHoverName()) {
				furnaceTile.setCustomName(stack.getHoverName());
			}
			// inventory step

			if (stack.getTag() != null && stack.getTag().contains(NBT_UPGRADE)) {
				ItemStack upgrade = ItemStack.of(stack.getTag().getCompound(NBT_UPGRADE));
				furnaceTile.setUpgrade(upgrade);
			}
		}
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, te, stack);
		if (te instanceof ForceFurnaceBlockEntity tile) {
			if (!tile.getUpgrade().isEmpty()) {
				level.playSound((Player) null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F);
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}
}