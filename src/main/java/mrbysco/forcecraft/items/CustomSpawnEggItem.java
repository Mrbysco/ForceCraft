package mrbysco.forcecraft.items;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class CustomSpawnEggItem extends SpawnEggItem {
	private static final Map<Supplier<EntityType<?>>, CustomSpawnEggItem> EGGS = Maps.newIdentityHashMap();
	private final Supplier<EntityType<?>> typeIn;
	private final int primaryColor;
	private final int secondaryColor;

	public CustomSpawnEggItem(Supplier<EntityType<?>> type, int primary, int secondary, Properties properties) {
		super(null, primary, secondary, properties);
		this.typeIn = type;
		this.primaryColor = primary;
		this.secondaryColor = secondary;
		EGGS.put(type, this);
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		if (worldIn.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			ItemStack stack = context.getItem();
			BlockPos pos = context.getPos();
			Direction dir = context.getFace();
			BlockState state = worldIn.getBlockState(pos);
			Block block = state.getBlock();
			if (block == Blocks.SPAWNER) {
				TileEntity tile = worldIn.getTileEntity(pos);
				if (tile instanceof MobSpawnerTileEntity) {
					AbstractSpawner spawner = ((MobSpawnerTileEntity)tile).getSpawnerBaseLogic();
					EntityType<?> type = this.getType(stack.getTag());
					spawner.setEntityType(type);
					tile.markDirty();
					worldIn.notifyBlockUpdate(pos, state, state, 3);
					stack.shrink(1);
					return ActionResultType.SUCCESS;
				}
			}

			BlockPos pos2;
			if (state.getCollisionShape(worldIn, pos).isEmpty()) {
				pos2 = pos;
			} else {
				pos2 = pos.offset(dir);
			}

			EntityType<?> type = this.getType(stack.getTag());
			if (!worldIn.isRemote && type.spawn((ServerWorld)worldIn, stack, context.getPlayer(), pos2, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, pos2) && dir == Direction.UP) != null) {
				stack.shrink(1);
			}

			return ActionResultType.SUCCESS;
		}
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
			return ActionResult.resultPass(itemstack);
		} else if (worldIn.isRemote) {
			return ActionResult.resultSuccess(itemstack);
		} else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
			BlockPos blockpos = blockraytraceresult.getPos();
			if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
				return ActionResult.resultPass(itemstack);
			} else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
				EntityType<?> entitytype = this.getType(itemstack.getTag());
				if (!worldIn.isRemote && entitytype.spawn((ServerWorld)worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
					return ActionResult.resultPass(itemstack);
				} else {
					if (!playerIn.abilities.isCreativeMode) {
						itemstack.shrink(1);
					}

					playerIn.addStat(Stats.ITEM_USED.get(this));
					return ActionResult.resultSuccess(itemstack);
				}
			} else {
				return ActionResult.resultFail(itemstack);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public int getColor(int p_195983_1_) {
		return p_195983_1_ == 0 ? this.primaryColor : this.secondaryColor;
	}

	public static Iterable<CustomSpawnEggItem> getCustomEggs() {
		return Iterables.unmodifiableIterable(EGGS.values());
	}

	public EntityType<?> getType(@Nullable CompoundNBT compound) {
		if (compound != null && compound.contains("EntityTag", 10)) {
			CompoundNBT lvt_2_1_ = compound.getCompound("EntityTag");
			if (lvt_2_1_.contains("id", 8)) {
				return (EntityType)EntityType.byKey(lvt_2_1_.getString("id")).orElse(this.typeIn.get());
			}
		}

		return this.typeIn.get();
	}
}