package mrbysco.forcecraft.blocks;

import mrbysco.forcecraft.items.UpgradeCoreItem;
import mrbysco.forcecraft.tiles.AbstractForceFurnaceTile;
import mrbysco.forcecraft.tiles.ForceFurnaceTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

public class ForceFurnaceBlock extends AbstractFurnaceBlock {

	private static final String NBT_UPGRADE = "upgrade";

	public ForceFurnaceBlock(AbstractBlock.Properties builder) {
		super(builder);
	}

    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.get(BlockStateProperties.LIT) ? lightValue : 0;
        };
    }

    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ForceFurnaceTileEntity) {
            player.openContainer((INamedContainerProvider)tileentity);
            player.addStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = stateIn.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * d3 : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * d3 : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new ForceFurnaceTileEntity();
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof AbstractForceFurnaceTile) {
                IInventory inventory = ((AbstractForceFurnaceTile) tileentity);
                for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                    if(!(inventory.getStackInSlot(i).getItem() instanceof UpgradeCoreItem)) {
                        spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                    }
                }
                ((AbstractForceFurnaceTile)tileentity).grantStoredRecipeExperience(worldIn, Vector3d.copyCentered(pos));
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack) {
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(x) + worldIn.rand.nextDouble() * d1 + d2;
        double d4 = Math.floor(y) + worldIn.rand.nextDouble() * d1;
        double d5 = Math.floor(z) + worldIn.rand.nextDouble() * d1 + d2;

        while(!stack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(worldIn, d3, d4, d5, stack.split(worldIn.rand.nextInt(21) + 10));
            float f = 0.05F;
            itementity.setMotion(worldIn.rand.nextGaussian() * (double)0.05F, worldIn.rand.nextGaussian() * (double)f + (double)0.2F, worldIn.rand.nextGaussian() * (double)f);
            worldIn.addEntity(itementity);
        }
    }
    
    // keep inventory feature  for upgrade only
    // TODO: tooltip?

	@Override
	public List<ItemStack> getDrops(BlockState state, net.minecraft.loot.LootContext.Builder builder) {
		// because harvestBlock manually forces a drop, we must do this to dodge that
		return new ArrayList<>();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof AbstractForceFurnaceTile) {

			AbstractForceFurnaceTile furnaceTile = (AbstractForceFurnaceTile) tileentity;
			if (stack.hasDisplayName()) {
				furnaceTile.setCustomName(stack.getDisplayName());
			}
			// inventory step

			if (stack.getTag() != null && stack.getTag().contains(NBT_UPGRADE)) {

				ItemStack upgrade = ItemStack.read(stack.getTag().getCompound(NBT_UPGRADE));

				furnaceTile.setUpgrade(upgrade);
			}
		}
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity tileentity, ItemStack stackToolUsed) {
		super.harvestBlock(world, player, pos, state, tileentity, stackToolUsed);

	    ItemStack tankStack = new ItemStack(this); 
	    
		if (tileentity != null && tileentity instanceof AbstractForceFurnaceTile) {
			AbstractForceFurnaceTile furnaceTile = (AbstractForceFurnaceTile) tileentity;
			
			CompoundNBT upgrade = new CompoundNBT();
			furnaceTile.getUpgrade().write(upgrade);

			tankStack.getOrCreateTag().put(NBT_UPGRADE, upgrade);
		}
		if (world.isRemote == false) {
			world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tankStack));
		}
	}
}