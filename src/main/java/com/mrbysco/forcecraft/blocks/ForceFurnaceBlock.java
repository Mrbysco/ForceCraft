package com.mrbysco.forcecraft.blocks;

import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.tiles.AbstractForceFurnaceTile;
import com.mrbysco.forcecraft.tiles.ForceFurnaceTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.ToIntFunction;

public class ForceFurnaceBlock extends AbstractFurnaceBlock {

	private static final String NBT_UPGRADE = "upgrade";

	public ForceFurnaceBlock(AbstractBlock.Properties builder) {
		super(builder);
	}

	public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
		return (state) -> {
			return state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		};
	}

	protected void openContainer(World worldIn, BlockPos pos, PlayerEntity player) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof ForceFurnaceTileEntity) {
			if (!worldIn.isClientSide) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (ForceFurnaceTileEntity) tileentity, pos);
			}
			player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				worldIn.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * d3 : d4;
			double d6 = rand.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * d3 : d4;
			worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new ForceFurnaceTileEntity();
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof AbstractForceFurnaceTile) {
				AbstractForceFurnaceTile furnaceTile = ((AbstractForceFurnaceTile) tileentity);
				for (int i = 0; i < furnaceTile.getContainerSize(); ++i) {
					if (!(furnaceTile.getItem(i).getItem() instanceof UpgradeCoreItem)) {
						spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), furnaceTile.getItem(i));
					}
				}
				((AbstractForceFurnaceTile) tileentity).grantStoredRecipeExperience(worldIn, Vector3d.atCenterOf(pos));
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack) {
		double d0 = (double) EntityType.ITEM.getWidth();
		double d1 = 1.0D - d0;
		double d2 = d0 / 2.0D;
		double d3 = Math.floor(x) + worldIn.random.nextDouble() * d1 + d2;
		double d4 = Math.floor(y) + worldIn.random.nextDouble() * d1;
		double d5 = Math.floor(z) + worldIn.random.nextDouble() * d1 + d2;

		while (!stack.isEmpty()) {
			ItemEntity itementity = new ItemEntity(worldIn, d3, d4, d5, stack.split(worldIn.random.nextInt(21) + 10));
			float f = 0.05F;
			itementity.setDeltaMovement(worldIn.random.nextGaussian() * (double) 0.05F, worldIn.random.nextGaussian() * (double) f + (double) 0.2F, worldIn.random.nextGaussian() * (double) f);
			worldIn.addFreshEntity(itementity);
		}
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof AbstractForceFurnaceTile) {

			AbstractForceFurnaceTile furnaceTile = (AbstractForceFurnaceTile) tileentity;
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
	public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.playerDestroy(worldIn, player, pos, state, te, stack);
		if (te instanceof ForceFurnaceTileEntity) {
			ForceFurnaceTileEntity tile = (ForceFurnaceTileEntity) te;
			if (!tile.getUpgrade().isEmpty()) {
				worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 0.5F, 1.0F);
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}
}