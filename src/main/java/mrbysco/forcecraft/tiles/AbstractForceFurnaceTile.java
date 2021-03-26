package mrbysco.forcecraft.tiles;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mrbysco.forcecraft.config.ConfigHandler;
import mrbysco.forcecraft.recipe.ForceRecipes;
import mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractForceFurnaceTile extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
	public static final int INPUT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int OUTPUT_SLOT = 2;
	public static final int UPGRADE_SLOT = 3;

	private static final int[] SLOTS_UP = new int[]{INPUT_SLOT};
	private static final int[] SLOTS_DOWN = new int[]{OUTPUT_SLOT, FUEL_SLOT};
	private static final int[] SLOTS_HORIZONTAL = new int[]{FUEL_SLOT};

	protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
	private int burnTime;
	private int burnSpeed;
	private int burnTimeTotal;
	private int cookTime;
	private int cookTimeTotal;
	private int cookSpeed;
	protected final IIntArray furnaceData = new IIntArray() {
		public int get(int index) {
			switch(index) {
				case 0:
					return AbstractForceFurnaceTile.this.burnTime;
				case 1:
					return AbstractForceFurnaceTile.this.burnTimeTotal;
				case 2:
					return AbstractForceFurnaceTile.this.cookTime;
				case 3:
					return AbstractForceFurnaceTile.this.cookTimeTotal;
				default:
					return 0;
			}
		}

		public void set(int index, int value) {
			switch(index) {
				case 0:
					AbstractForceFurnaceTile.this.burnTime = value;
					break;
				case 1:
					AbstractForceFurnaceTile.this.burnTimeTotal = value;
					break;
				case 2:
					AbstractForceFurnaceTile.this.cookTime = value;
					break;
				case 3:
					AbstractForceFurnaceTile.this.cookTimeTotal = value;
			}

		}

		public int size() {
			return 4;
		}
	};
	protected AbstractCookingRecipe currentRecipe;
	protected ItemStack failedMatch = ItemStack.EMPTY;

	private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

	protected AbstractForceFurnaceTile(TileEntityType<?> tileTypeIn) {
		super(tileTypeIn);
	}

	protected IRecipeType<? extends AbstractCookingRecipe> getRecipeType() {
		ItemStack upgrade = getUpgrade();
		if(!upgrade.isEmpty()) {
			if(upgrade.getItem() == ForceRegistry.FREEZING_CORE.get()) {
				return ForceRecipes.FREEZING;
			} else if(upgrade.getItem() == ForceRegistry.GRINDING_CORE.get()) {
				return ForceRecipes.GRINDING;
			}
		}
		return IRecipeType.SMELTING;
	}

	public ItemStack getUpgrade() {
		return this.items.get(UPGRADE_SLOT);
	}

	public boolean isEfficient() {
		ItemStack upgrade = getUpgrade();
		return !getUpgrade().isEmpty() && upgrade.getItem() == ForceRegistry.HEAT_CORE.get();
	}

	public boolean isFast() {
		ItemStack upgrade = getUpgrade();
		return !getUpgrade().isEmpty() && upgrade.getItem() == ForceRegistry.SPEED_CORE.get();
	}

	public boolean hasXPMultiplied() {
		ItemStack upgrade = getUpgrade();
		return !getUpgrade().isEmpty() && upgrade.getItem() == ForceRegistry.EXPERIENCE_CORE.get();
	}

	public int getSpeed() {
		return isFast() ? 10 : 2;
	}

	public int getXPMultiplier() {
		return hasXPMultiplied() ? 2 : 1;
	}

	protected AbstractCookingRecipe getRecipe() {
		ItemStack input = this.getStackInSlot(INPUT_SLOT);
		if (input.isEmpty() || input == failedMatch) return null;
		if (currentRecipe != null && currentRecipe.matches(this, world) && currentRecipe.getType() == getRecipeType()) return currentRecipe;
		else {
			AbstractCookingRecipe rec = world.getRecipeManager().getRecipe(this.getRecipeType(), this, this.world).orElse(null);
			if (rec == null) failedMatch = input;
			else failedMatch = ItemStack.EMPTY;
			return currentRecipe = rec;
		}
	}

	private boolean isBurning() {
		return this.burnTime > 0;
	}

	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.items);
		this.burnTime = nbt.getInt("BurnTime");
		this.burnSpeed = nbt.getInt("BurnSpeed");
		this.cookTime = nbt.getInt("CookTime");
		this.cookTimeTotal = nbt.getInt("CookTimeTotal");
		this.burnTimeTotal = nbt.contains("BurnTimeTotal") ? nbt.getInt("BurnTimeTotal") : this.getBurnTime(this.items.get(FUEL_SLOT));
		this.cookSpeed = nbt.getInt("CookSpeed");
		CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");

		for(String s : compoundnbt.keySet()) {
			this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
		}
	}

	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("BurnTime", this.burnTime);
		compound.putInt("BurnSpeed", this.burnSpeed);
		compound.putInt("CookTime", this.cookTime);
		compound.putInt("CookTimeTotal", this.cookTimeTotal);
		compound.putInt("BurnTimeTotal", this.burnTimeTotal);
		compound.putInt("CookSpeed", this.cookSpeed);
		ItemStackHelper.saveAllItems(compound, this.items);
		CompoundNBT compoundnbt = new CompoundNBT();
		this.recipes.forEach((recipeId, craftedAmount) -> {
			compoundnbt.putInt(recipeId.toString(), craftedAmount);
		});
		compound.put("RecipesUsed", compoundnbt);
		return compound;
	}

	public void tick() {
		boolean wasBurning = this.isBurning();
		boolean dirty = false;
		if (this.isBurning() && !this.items.get(INPUT_SLOT).isEmpty()) {
			int speed = getSpeed();
			if(this.burnSpeed != speed) {
				this.burnSpeed = speed;
			}
			this.burnTime -= this.burnSpeed;
		}

		if (this.world != null && !this.world.isRemote) {
			ItemStack fuel = this.items.get(FUEL_SLOT);
			if (this.isBurning() || !fuel.isEmpty() && !this.items.get(INPUT_SLOT).isEmpty()) {
				AbstractCookingRecipe irecipe = getRecipe();
				boolean valid = this.canSmelt(irecipe);
				if (!this.isBurning() && valid) {
					this.burnTime = this.getBurnTime(fuel);
					this.burnTimeTotal = this.burnTime;
					if (this.isBurning()) {
						dirty = true;
						if (fuel.hasContainerItem())
							this.items.set(FUEL_SLOT, fuel.getContainerItem());
						else
						if (!fuel.isEmpty()) {
							fuel.shrink(1);
							if (fuel.isEmpty()) {
								this.items.set(FUEL_SLOT, fuel.getContainerItem());
							}
						}
					}
				}

				if (this.isBurning() && valid) {
					int speed = isEfficient() ? 4 : getSpeed();
					if(this.cookSpeed != speed) {
						this.cookSpeed = speed;
					}

					this.cookTime += this.cookSpeed;

					if (this.cookTime >= this.cookTimeTotal) {
						this.cookTime = 0;
						this.cookTimeTotal = this.getCookTime();
						this.smelt(irecipe);
						dirty = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
			}

			if (wasBurning != this.isBurning()) {
				dirty = true;
				this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
			}
		}

		if (dirty) {
			this.markDirty();
		}
	}

	protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
		if (!this.items.get(INPUT_SLOT).isEmpty() && recipeIn != null) {
			ItemStack recipeOutput = recipeIn.getRecipeOutput();
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				ItemStack output = this.items.get(OUTPUT_SLOT);
				if (output.isEmpty()) {
					return true;
				} else if (!output.isItemEqual(recipeOutput)) {
					return false;
				} else if (output.getCount() + recipeOutput.getCount() <= this.getInventoryStackLimit() && output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
					return true;
				} else {
					return output.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
				}
			}
		} else {
			return false;
		}
	}

	private void smelt(@Nullable IRecipe<?> recipe) {
		if (recipe != null && this.canSmelt(recipe)) {
			ItemStack itemstack = this.items.get(INPUT_SLOT);
			List<? extends String> additionalBlacklist = new ArrayList<>();
			if(ConfigHandler.COMMON.furnaceOutputBlacklist.get() != null) {
				if(!ConfigHandler.COMMON.furnaceOutputBlacklist.get().isEmpty() && !ConfigHandler.COMMON.furnaceOutputBlacklist.get().get(0).isEmpty()) {
					additionalBlacklist = ConfigHandler.COMMON.furnaceOutputBlacklist.get();
				}
			}

			if(recipe instanceof MultipleOutputFurnaceRecipe) {
				MultipleOutputFurnaceRecipe multipleRecipe = (MultipleOutputFurnaceRecipe) recipe;
				NonNullList<ItemStack> outputStacks = multipleRecipe.getRecipeOutputs();
				for(int i = 0; i < outputStacks.size(); i++) {
					ItemStack outputStack = outputStacks.get(i).copy();

					if(i > 0) {
						if(multipleRecipe.getSecondaryChance() != 1.0F || world.rand.nextFloat() > multipleRecipe.getSecondaryChance()) {
							//Early break if change didn't work out on second output
							break;
						}
					}

					for(Direction dir : Direction.values()) {
						BlockPos offPos = pos.offset(dir);
						if(this.world.isAreaLoaded(pos, 1)) {
							TileEntity foundTile = this.world.getTileEntity(offPos);
							boolean flag = foundTile instanceof HopperTileEntity || foundTile instanceof AbstractFurnaceTileEntity || foundTile instanceof AbstractForceFurnaceTile;
							boolean flag2 = foundTile != null && (additionalBlacklist.isEmpty() || additionalBlacklist.contains(foundTile.getType().toString()));
							if(!flag && flag2 && !foundTile.isRemoved() && foundTile.hasWorld() && foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
								IItemHandler itemHandler = foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
								ItemStack rest = ItemHandlerHelper.insertItem(itemHandler, outputStack, false);
								outputStack = rest;
								if(outputStack.isEmpty()) {
									break;
								}
							}
						} else {
							break;
						}
					}
					if(i > 0 && !outputStack.isEmpty()) {
						((ServerWorld)world).spawnParticle(ParticleTypes.POOF, (double)pos.getX(), (double)pos.getY() + 1, (double)pos.getZ(), 1, 0, 0, 0, 0.0D);
						ItemEntity itemEntity = new ItemEntity(world, getPos().getX(), getPos().getY() + 1, getPos().getZ(), outputStack);
						world.addEntity(itemEntity);
					} else {
						ItemStack itemstack2 = this.items.get(OUTPUT_SLOT);
						if (itemstack2.isEmpty() && !outputStack.isEmpty()) {
							this.items.set(OUTPUT_SLOT, outputStack);
						} else if (itemstack2.getItem() == outputStacks.get(i).getItem()) {
							itemstack2.grow(outputStack.getCount());
						}
					}
				}
			} else {
				ItemStack itemstack1 = recipe.getRecipeOutput();
				ItemStack outputStack = itemstack1.copy();
				for(Direction dir : Direction.values()) {
					BlockPos offPos = pos.offset(dir);
					if(this.world.isAreaLoaded(pos, 1)) {
						TileEntity foundTile = this.world.getTileEntity(offPos);
						boolean flag = foundTile instanceof HopperTileEntity || foundTile instanceof AbstractFurnaceTileEntity || foundTile instanceof AbstractForceFurnaceTile;
						boolean flag2 = foundTile != null && (additionalBlacklist.isEmpty() || additionalBlacklist.contains(foundTile.getType().toString()));
						if(!flag && flag2 && !foundTile.isRemoved() && foundTile.hasWorld() && foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
							IItemHandler itemHandler = foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
							ItemStack rest = ItemHandlerHelper.insertItem(itemHandler, outputStack, false);
							outputStack = rest;
							if(outputStack.isEmpty()) {
								break;
							}
						}
					} else {
						break;
					}
				}

				ItemStack itemstack2 = this.items.get(OUTPUT_SLOT);
				if (itemstack2.isEmpty() && !outputStack.isEmpty()) {
					this.items.set(OUTPUT_SLOT, outputStack);
				} else if (itemstack2.getItem() == itemstack1.getItem()) {
					itemstack2.grow(outputStack.getCount());
				}
			}

			if (!this.world.isRemote) {
				this.setRecipeUsed(recipe);
			}

			if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(FUEL_SLOT).isEmpty() && this.items.get(FUEL_SLOT).getItem() == Items.BUCKET) {
				this.items.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			itemstack.shrink(1);
		}
	}

	protected int getBurnTime(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		} else {
			return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel);
		}
	}

	protected int getCookTime() {
		AbstractCookingRecipe rec = getRecipe();
		if (rec == null) return 200;
		return this.world.getRecipeManager().getRecipe(this.getRecipeType(), this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(100);
	}

	public static boolean isFuel(ItemStack stack) {
		return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
	}

	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
			return SLOTS_DOWN;
		} else {
			return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
		}
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from the given side.
	 */
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 */
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (direction == Direction.DOWN && index == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.items.size();
	}

	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.cookTimeTotal = this.getCookTime();
			this.cookTime = 0;
			this.markDirty();
		}

	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			ItemStack itemstack = this.items.get(FUEL_SLOT);
			return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
		}
	}

	public void clear() {
		this.items.clear();
	}

	public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.getId();
			this.recipes.addTo(resourcelocation, 1);
		}

	}

	@Nullable
	public IRecipe<?> getRecipeUsed() {
		return null;
	}

	public void onCrafting(PlayerEntity player) {
	}

	public void unlockRecipes(PlayerEntity player) {
		List<IRecipe<?>> list = this.grantStoredRecipeExperience(player.world, player.getPositionVec());
		player.unlockRecipes(list);
		this.recipes.clear();
	}

	public List<IRecipe<?>> grantStoredRecipeExperience(World world, Vector3d pos) {
		List<IRecipe<?>> list = Lists.newArrayList();

		for(Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
			world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				splitAndSpawnExperience(world, pos, entry.getIntValue(), (((AbstractCookingRecipe)recipe).getExperience() * getXPMultiplier()));
			});
		}

		return list;
	}

	private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
		int i = MathHelper.floor((float)craftedAmount * experience);
		float f = MathHelper.frac((float)craftedAmount * experience);
		if (f != 0.0F && Math.random() < (double)f) {
			++i;
		}

		while(i > 0) {
			int j = ExperienceOrbEntity.getXPSplit(i);
			i -= j;
			world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
		}

	}

	public void fillStackedContents(RecipeItemHelper helper) {
		for(ItemStack itemstack : this.items) {
			helper.accountStack(itemstack);
		}

	}

	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
			net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == Direction.UP)
				return handlers[0].cast();
			else if (facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[2].cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		for (int x = 0; x < handlers.length; x++)
			handlers[x].invalidate();
	}
}