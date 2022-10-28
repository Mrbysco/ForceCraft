package com.mrbysco.forcecraft.tiles;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.IHopper;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractForceFurnaceTile extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
	public static final int INPUT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int OUTPUT_SLOT = 2;

	public static final int UPGRADE_SLOT = 0;

	private static final int[] SLOTS_UP = new int[]{INPUT_SLOT};
	private static final int[] SLOTS_DOWN = new int[]{OUTPUT_SLOT, FUEL_SLOT};
	private static final int[] SLOTS_HORIZONTAL = new int[]{FUEL_SLOT};

	public final ItemStackHandler handler = new ItemStackHandler(3) {
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			if (slot == FUEL_SLOT) {
				ItemStack itemstack = getStackInSlot(FUEL_SLOT);
				return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
			} else if (slot == OUTPUT_SLOT) {
				return false;
			} else {
				return true;
			}
		}
	};
	private LazyOptional<IItemHandler> handlerHolder = LazyOptional.of(() -> handler);

	public final ItemStackHandler upgradeHandler = new ItemStackHandler(1) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return 1;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return getStackInSlot(slot).isEmpty() && stack.getItem() instanceof UpgradeCoreItem && stack.getCount() == 1;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}
	};
	private LazyOptional<IItemHandler> upgradeHandlerHolder = LazyOptional.of(() -> upgradeHandler);

	private static final List<ResourceLocation> hopperBlacklist = Arrays.asList(ResourceLocation.tryParse("hopper"), new ResourceLocation("cyclic", "hopper"),
			new ResourceLocation("cyclic", "hopper_gold"), new ResourceLocation("cyclic", "hopper_fluid"),
			new ResourceLocation("uppers", "upper"), new ResourceLocation("goldenhopper", "golden_hopper"),
			new ResourceLocation("woodenhopper", "wooden_hopper"));

	private int burnTime;
	private int burnSpeed;
	private int burnTimeTotal;
	private int cookTime;
	private int cookTimeTotal;
	private int cookSpeed;
	protected final IIntArray furnaceData = new IIntArray() {
		public int get(int index) {
			switch (index) {
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
			switch (index) {
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

		public int getCount() {
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
		if (!upgrade.isEmpty()) {
			if (upgrade.getItem() == ForceRegistry.FREEZING_CORE.get()) {
				return ForceRecipes.FREEZING;
			} else if (upgrade.getItem() == ForceRegistry.GRINDING_CORE.get()) {
				return ForceRecipes.GRINDING;
			}
		}
		return IRecipeType.SMELTING;
	}

	public void setUpgrade(ItemStack upgrade) {
		this.upgradeHandler.setStackInSlot(UPGRADE_SLOT, upgrade);
	}

	public ItemStack getUpgrade() {
		return this.upgradeHandler.getStackInSlot(UPGRADE_SLOT);
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
		ItemStack input = this.getItem(INPUT_SLOT);
		if (input.isEmpty() || input == failedMatch) return null;
		if (currentRecipe != null && currentRecipe.matches(this, level) && currentRecipe.getType() == getRecipeType())
			return currentRecipe;
		else {
			AbstractCookingRecipe rec = level.getRecipeManager().getRecipeFor((IRecipeType<AbstractCookingRecipe>) this.getRecipeType(), this, this.level).orElse(null);
			if (rec == null) failedMatch = input;
			else failedMatch = ItemStack.EMPTY;
			return currentRecipe = rec;
		}
	}

	private boolean isBurning() {
		return this.burnTime > 0;
	}

	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		if (nbt.contains("UpgradeHandler") && nbt.contains("ItemStackHandler")) {
			upgradeHandler.deserializeNBT(nbt.getCompound("UpgradeHandler"));
			handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
		} else {
			if (nbt.contains("ItemStackHandler")) {
				CompoundNBT handlerTag = nbt.getCompound("ItemStackHandler");
				ListNBT tagList = handlerTag.getList("Items", Constants.NBT.TAG_COMPOUND);
				upgradeHandler.setSize(1);
				int size = handlerTag.getInt("Size");
				if (size == 4) {
					handler.setSize(3);
					for (int i = 0; i < tagList.size(); i++) {
						CompoundNBT itemTags = tagList.getCompound(i);
						int slot = itemTags.getInt("Slot");

						if (slot >= 0 && slot < 4) {
							if (slot == 3) {
								upgradeHandler.setStackInSlot(0, ItemStack.of(itemTags));
							} else {
								handler.setStackInSlot(slot, ItemStack.of(itemTags));
							}
						}
					}
				} else {
					handler.deserializeNBT(handlerTag);
					upgradeHandler.setStackInSlot(0, ItemStack.EMPTY);
				}
			} else {
				ListNBT listnbt = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);

				handler.setSize(3);
				upgradeHandler.setSize(1);
				for (int i = 0; i < listnbt.size(); ++i) {
					CompoundNBT compoundnbt = listnbt.getCompound(i);
					int j = compoundnbt.getByte("Slot") & 255;
					if (j >= 0 && j < this.getContainerSize()) {
						if (j == 3) {
							upgradeHandler.setStackInSlot(0, ItemStack.of(compoundnbt));
						} else {
							handler.setStackInSlot(j, ItemStack.of(compoundnbt));
						}
					}
				}
			}
		}

		this.burnTime = nbt.getInt("BurnTime");
		this.burnSpeed = nbt.getInt("BurnSpeed");
		this.cookTime = nbt.getInt("CookTime");
		this.cookTimeTotal = nbt.getInt("CookTimeTotal");
		this.burnTimeTotal = nbt.contains("BurnTimeTotal") ? nbt.getInt("BurnTimeTotal") : this.getBurnTime(this.handler.getStackInSlot(FUEL_SLOT));
		this.cookSpeed = nbt.getInt("CookSpeed");
		CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");

		for (String s : compoundnbt.getAllKeys()) {
			this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
		}
	}

	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("BurnTime", this.burnTime);
		compound.putInt("BurnSpeed", this.burnSpeed);
		compound.putInt("CookTime", this.cookTime);
		compound.putInt("CookTimeTotal", this.cookTimeTotal);
		compound.putInt("BurnTimeTotal", this.burnTimeTotal);
		compound.putInt("CookSpeed", this.cookSpeed);

		compound.put("UpgradeHandler", upgradeHandler.serializeNBT());
		compound.put("ItemStackHandler", handler.serializeNBT());
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
		if (this.isBurning() && canSmelt(currentRecipe)) {
			int speed = getSpeed();
			if (this.burnSpeed != speed) {
				this.burnSpeed = speed;
			}
			this.burnTime -= this.burnSpeed;
		}

		if (this.level != null && !this.level.isClientSide) {
			ItemStack fuel = this.handler.getStackInSlot(FUEL_SLOT);
			if (this.isBurning() || !fuel.isEmpty() && !this.handler.getStackInSlot(INPUT_SLOT).isEmpty()) {
				AbstractCookingRecipe irecipe = getRecipe();
				boolean valid = this.canSmelt(irecipe);
				if (!this.isBurning() && valid) {
					this.burnTime = this.getBurnTime(fuel);
					this.burnTimeTotal = this.burnTime;
					if (this.isBurning()) {
						dirty = true;
						if (fuel.hasContainerItem())
							this.handler.setStackInSlot(FUEL_SLOT, fuel.getContainerItem());
						else if (!fuel.isEmpty()) {
							fuel.shrink(1);
							if (fuel.isEmpty()) {
								this.handler.setStackInSlot(FUEL_SLOT, fuel.getContainerItem());
							}
						}
					}
				}

				if (this.isBurning() && valid) {
					int speed = isEfficient() ? 4 : getSpeed();
					if (this.cookSpeed != speed) {
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
				this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
			}
		}

		if (dirty) {
			this.setChanged();
		}
	}

	protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
		if (!this.handler.getStackInSlot(INPUT_SLOT).isEmpty() && recipeIn != null) {
			ItemStack recipeOutput = recipeIn.getResultItem();
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				ItemStack output = this.handler.getStackInSlot(OUTPUT_SLOT);
				if (output.isEmpty()) {
					return true;
				} else if (!output.sameItem(recipeOutput)) {
					return false;
				} else if (output.getCount() + recipeOutput.getCount() <= this.getMaxStackSize() && output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
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
			ItemStack itemstack = this.handler.getStackInSlot(INPUT_SLOT);
			List<? extends String> additionalBlacklist = new ArrayList<>();
			if (ConfigHandler.COMMON.furnaceOutputBlacklist.get() != null) {
				if (!ConfigHandler.COMMON.furnaceOutputBlacklist.get().isEmpty() && !ConfigHandler.COMMON.furnaceOutputBlacklist.get().get(0).isEmpty()) {
					additionalBlacklist = ConfigHandler.COMMON.furnaceOutputBlacklist.get();
				}
			}

			if (recipe instanceof MultipleOutputFurnaceRecipe) {
				MultipleOutputFurnaceRecipe multipleRecipe = (MultipleOutputFurnaceRecipe) recipe;
				NonNullList<ItemStack> outputStacks = multipleRecipe.getRecipeOutputs();
				for (int i = 0; i < outputStacks.size(); i++) {
					ItemStack outputStack = outputStacks.get(i).copy();

					if (i > 0) {
						if (multipleRecipe.getSecondaryChance() != 1.0F || level.random.nextFloat() > multipleRecipe.getSecondaryChance()) {
							//Early break if change didn't work out on second output
							break;
						}
					}

					List<BiggestInventory> inventoryList = new ArrayList<>();
					for (Direction dir : Direction.values()) {
						BlockPos offPos = worldPosition.relative(dir);
						if (this.level.isAreaLoaded(worldPosition, 1)) {
							TileEntity foundTile = this.level.getBlockEntity(offPos);
							if (foundTile != null) {
								ResourceLocation typeLocation = foundTile.getType().getRegistryName();
								boolean flag = foundTile instanceof IHopper || foundTile instanceof AbstractFurnaceTileEntity || foundTile instanceof AbstractForceFurnaceTile;
								boolean flag2 = typeLocation != null && (!hopperBlacklist.contains(typeLocation) && (additionalBlacklist.isEmpty() || !additionalBlacklist.contains(typeLocation.toString())));
								if (!flag && flag2 && !foundTile.isRemoved() && foundTile.hasLevel() && foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
									IItemHandler itemHandler = foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()).orElse(null);
									if (itemHandler != null) {
										inventoryList.add(new BiggestInventory(offPos, itemHandler.getSlots(), dir.getOpposite()));
									}
								}
							}
						}
					}
					inventoryList.sort(Collections.reverseOrder());
					for (BiggestInventory inventory : inventoryList) {
						IItemHandler itemHandler = inventory.getIItemHandler(this.level);
						ItemStack rest = ItemHandlerHelper.insertItem(itemHandler, outputStack, false);
						outputStack = rest;
						if (outputStack.isEmpty()) {
							break;
						}
					}

					if (i > 0 && !outputStack.isEmpty()) {
						((ServerWorld) level).sendParticles(ParticleTypes.POOF, (double) worldPosition.getX(), (double) worldPosition.getY() + 1, (double) worldPosition.getZ(), 1, 0, 0, 0, 0.0D);
						ItemEntity itemEntity = new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 1, getBlockPos().getZ(), outputStack);
						level.addFreshEntity(itemEntity);
					} else {
						ItemStack itemstack2 = this.handler.getStackInSlot(OUTPUT_SLOT);
						if (itemstack2.isEmpty() && !outputStack.isEmpty()) {
							this.handler.setStackInSlot(OUTPUT_SLOT, outputStack);
						} else if (itemstack2.getItem() == outputStacks.get(i).getItem()) {
							itemstack2.grow(outputStack.getCount());
						}
					}
				}
			} else {
				ItemStack itemstack1 = recipe.getResultItem();
				ItemStack outputStack = itemstack1.copy();

				List<BiggestInventory> inventoryList = new ArrayList<>();
				for (Direction dir : Direction.values()) {
					BlockPos offPos = worldPosition.relative(dir);
					if (this.level.isAreaLoaded(worldPosition, 1)) {
						TileEntity foundTile = this.level.getBlockEntity(offPos);
						if (foundTile != null) {
							ResourceLocation typeLocation = foundTile.getType().getRegistryName();
							boolean flag = foundTile instanceof IHopper || foundTile instanceof AbstractFurnaceTileEntity || foundTile instanceof AbstractForceFurnaceTile;
							boolean flag2 = typeLocation != null && (!hopperBlacklist.contains(typeLocation) && (additionalBlacklist.isEmpty() || !additionalBlacklist.contains(typeLocation.toString())));
							if (!flag && flag2 && !foundTile.isRemoved() && foundTile.hasLevel() && foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
								IItemHandler itemHandler = foundTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()).orElse(null);
								if (itemHandler != null) {
									inventoryList.add(new BiggestInventory(offPos, itemHandler.getSlots(), dir.getOpposite()));
								}
							}
						}
					}
				}
				inventoryList.sort(Collections.reverseOrder());
				for (BiggestInventory inventory : inventoryList) {
					IItemHandler itemHandler = inventory.getIItemHandler(this.level);
					ItemStack rest = ItemHandlerHelper.insertItem(itemHandler, outputStack, false);
					outputStack = rest;
					if (outputStack.isEmpty()) {
						break;
					}
				}

				ItemStack itemstack2 = this.handler.getStackInSlot(OUTPUT_SLOT);
				if (itemstack2.isEmpty() && !outputStack.isEmpty()) {
					this.handler.setStackInSlot(OUTPUT_SLOT, outputStack);
				} else if (itemstack2.getItem() == itemstack1.getItem()) {
					itemstack2.grow(outputStack.getCount());
				}
			}

			if (!this.level.isClientSide) {
				this.setRecipeUsed(recipe);
			}

			if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.handler.getStackInSlot(FUEL_SLOT).isEmpty() && this.handler.getStackInSlot(FUEL_SLOT).getItem() == Items.BUCKET) {
				this.handler.setStackInSlot(FUEL_SLOT, new ItemStack(Items.WATER_BUCKET));
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
		return this.level.getRecipeManager().getRecipeFor((IRecipeType<AbstractCookingRecipe>) this.getRecipeType(), this, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(100);
	}

	public static boolean isFuel(ItemStack stack) {
		return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
	}

	@Override
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
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStackIn);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 */
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
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
	public int getContainerSize() {
		return this.handler.getSlots();
	}

	public boolean isEmpty() {
		for (int i = 0; i < handler.getSlots(); i++) {
			if (!handler.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	public ItemStack getItem(int index) {
		return this.handler.getStackInSlot(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	public ItemStack removeItem(int index, int count) {
		return ItemHandlerUtils.getAndSplit(this.handler, index, count);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeItemNoUpdate(int index) {
		return ItemHandlerUtils.getAndRemove(this.handler, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setItem(int index, ItemStack stack) {
		ItemStack itemstack = this.handler.getStackInSlot(index);
		boolean flag = handler.isItemValid(index, stack) && !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
		this.handler.setStackInSlot(index, stack);
		if (stack.getCount() > handler.getSlotLimit(index)) {
			stack.setCount(handler.getSlotLimit(index));
		}

		if (index == 0 && !flag) {
			this.cookTimeTotal = this.getCookTime();
			this.cookTime = 0;
			this.setChanged();
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean stillValid(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return handler.isItemValid(index, stack);
	}

	public void clearContent() {
		for (int i = 0; i < handler.getSlots(); i++) {
			handler.setStackInSlot(i, ItemStack.EMPTY);
		}
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

	public void awardUsedRecipes(PlayerEntity player) {
	}

	public void unlockRecipes(PlayerEntity player) {
		List<IRecipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
		player.awardRecipes(list);
		this.recipes.clear();
	}

	public List<IRecipe<?>> grantStoredRecipeExperience(World world, Vector3d pos) {
		List<IRecipe<?>> list = Lists.newArrayList();

		for (Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
			world.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				splitAndSpawnExperience(world, pos, entry.getIntValue(), (((AbstractCookingRecipe) recipe).getExperience() * getXPMultiplier()));
			});
		}

		return list;
	}

	private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
		int i = MathHelper.floor((float) craftedAmount * experience);
		float f = MathHelper.frac((float) craftedAmount * experience);
		if (f != 0.0F && Math.random() < (double) f) {
			++i;
		}

		while (i > 0) {
			int j = ExperienceOrbEntity.getExperienceValue(i);
			i -= j;
			world.addFreshEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
		}

	}

	public void fillStackedContents(RecipeItemHelper helper) {
		for (int i = 0; i < handler.getSlots(); i++) {
			helper.accountStack(handler.getStackInSlot(i));
		}
	}

	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
			net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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

		handlerHolder.invalidate();
		upgradeHandlerHolder.invalidate();
	}

	private class BiggestInventory implements Comparable<BiggestInventory> {
		private final int inventorySize;
		private final BlockPos tilePos;
		private final Direction direction;

		public BiggestInventory(BlockPos pos, int size, Direction dir) {
			this.tilePos = pos;
			this.inventorySize = size;
			this.direction = dir;
		}

		protected IItemHandler getIItemHandler(World world) {
			if (world.isAreaLoaded(worldPosition, 1)) {
				TileEntity tileEntity = world.getBlockEntity(tilePos);
				if (!tileEntity.isRemoved() && tileEntity.hasLevel() && tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
					return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).orElse(null);
				}
			}
			return null;
		}

		@Override
		public int compareTo(BiggestInventory otherInventory) {
			return Integer.compare(this.inventorySize, otherInventory.inventorySize);
		}
	}

	public IIntArray getFurnaceData() {
		return furnaceData;
	}
}