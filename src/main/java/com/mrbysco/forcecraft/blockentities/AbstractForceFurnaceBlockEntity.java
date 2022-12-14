package com.mrbysco.forcecraft.blockentities;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractForceFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
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

		@Override
		public void setStackInSlot(int slot, @NotNull ItemStack stack) {
			super.setStackInSlot(slot, stack);

			if (slot == 0) {
				cookingTotalTime = getCookingProgress();
				cookingProgress = 0;
				setChanged();
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

	private int litTime;
	private int litDuration;
	private int cookingProgress;
	private int cookingTotalTime;
	private int burnSpeed;
	private int cookingSpeed;
	protected final ContainerData furnaceData = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> AbstractForceFurnaceBlockEntity.this.litTime;
				case 1 -> AbstractForceFurnaceBlockEntity.this.litDuration;
				case 2 -> AbstractForceFurnaceBlockEntity.this.cookingProgress;
				case 3 -> AbstractForceFurnaceBlockEntity.this.cookingTotalTime;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case 0 -> AbstractForceFurnaceBlockEntity.this.litTime = value;
				case 1 -> AbstractForceFurnaceBlockEntity.this.litDuration = value;
				case 2 -> AbstractForceFurnaceBlockEntity.this.cookingProgress = value;
				case 3 -> AbstractForceFurnaceBlockEntity.this.cookingTotalTime = value;
			}
		}

		public int getCount() {
			return 4;
		}
	};
	protected AbstractCookingRecipe currentRecipe;
	protected ItemStack failedMatch = ItemStack.EMPTY;

	private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

	protected AbstractForceFurnaceBlockEntity(BlockEntityType<?> tileTypeIn, BlockPos pos, BlockState state) {
		super(tileTypeIn, pos, state);
	}

	protected RecipeType<? extends AbstractCookingRecipe> getRecipeType() {
		ItemStack upgrade = getUpgrade();
		if (!upgrade.isEmpty()) {
			if (upgrade.getItem() == ForceRegistry.FREEZING_CORE.get()) {
				return ForceRecipes.FREEZING.get();
			} else if (upgrade.getItem() == ForceRegistry.GRINDING_CORE.get()) {
				return ForceRecipes.GRINDING.get();
			}
		}
		return RecipeType.SMELTING;
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
			AbstractCookingRecipe rec = level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.getRecipeType(), this, this.level).orElse(null);
			if (rec == null) failedMatch = input;
			else failedMatch = ItemStack.EMPTY;
			return currentRecipe = rec;
		}
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	public void load(CompoundTag nbt) {
		super.load(nbt);

		this.upgradeHandler.deserializeNBT(nbt.getCompound("UpgradeHandler"));
		this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));

		this.litTime = nbt.getInt("BurnTime");
		this.burnSpeed = nbt.getInt("BurnSpeed");
		this.cookingProgress = nbt.getInt("CookTime");
		this.cookingTotalTime = nbt.getInt("CookTimeTotal");
		this.litDuration = nbt.contains("BurnTimeTotal") ? nbt.getInt("BurnTimeTotal") : this.getBurnDuration(this.handler.getStackInSlot(FUEL_SLOT));
		this.cookingSpeed = nbt.getInt("CookSpeed");
		CompoundTag recipesUsed = nbt.getCompound("RecipesUsed");

		for (String s : recipesUsed.getAllKeys()) {
			this.recipes.put(new ResourceLocation(s), recipesUsed.getInt(s));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("BurnTime", this.litTime);
		compound.putInt("BurnSpeed", this.burnSpeed);
		compound.putInt("CookTime", this.cookingProgress);
		compound.putInt("CookTimeTotal", this.cookingTotalTime);
		compound.putInt("BurnTimeTotal", this.litDuration);
		compound.putInt("CookSpeed", this.cookingSpeed);

		compound.put("UpgradeHandler", upgradeHandler.serializeNBT());
		compound.put("ItemStackHandler", handler.serializeNBT());
		CompoundTag recipesUsed = new CompoundTag();
		this.recipes.forEach((recipeId, craftedAmount) -> recipesUsed.putInt(recipeId.toString(), craftedAmount));
		compound.put("RecipesUsed", recipesUsed);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractForceFurnaceBlockEntity furnace) {
		boolean wasBurning = furnace.isLit();
		if (furnace.isLit() && furnace.canBurn(furnace.currentRecipe)) {
			int speed = furnace.getSpeed();
			if (furnace.burnSpeed != speed) {
				furnace.burnSpeed = speed;
			}
			furnace.litTime -= furnace.burnSpeed;
		}

		if (level != null) {
			boolean dirty = false;
			ItemStack fuel = furnace.handler.getStackInSlot(FUEL_SLOT);
			if (furnace.isLit() || !fuel.isEmpty() && !furnace.handler.getStackInSlot(INPUT_SLOT).isEmpty()) {
				AbstractCookingRecipe cookingRecipe = furnace.getRecipe();
				if (!furnace.isLit() && furnace.canBurn(cookingRecipe)) {
					furnace.litTime = furnace.getBurnDuration(fuel);
					furnace.litDuration = furnace.litTime;
					if (furnace.isLit()) {
						dirty = true;
						if (fuel.hasCraftingRemainingItem())
							furnace.handler.setStackInSlot(FUEL_SLOT, fuel.getCraftingRemainingItem());
						else if (!fuel.isEmpty()) {
							fuel.shrink(1);
							if (fuel.isEmpty()) {
								furnace.handler.setStackInSlot(FUEL_SLOT, fuel.getCraftingRemainingItem());
							}
						}
					}
				}

				if (furnace.isLit() && furnace.canBurn(cookingRecipe)) {
					int speed = furnace.isEfficient() ? 4 : furnace.getSpeed();
					if (furnace.cookingSpeed != speed) {
						furnace.cookingSpeed = speed;
					}

					furnace.cookingProgress += furnace.cookingSpeed;
					if (furnace.cookingProgress >= furnace.cookingTotalTime) {
						furnace.cookingProgress = 0;
						furnace.cookingTotalTime = furnace.getCookingProgress();
						if (furnace.burn(cookingRecipe)) {
							furnace.setRecipeUsed(cookingRecipe);
						}
						dirty = true;
					}
				} else {
					furnace.cookingProgress = 0;
				}
			} else if (!furnace.isLit() && furnace.cookingProgress > 0) {
				furnace.cookingProgress = Mth.clamp(furnace.cookingProgress - 2, 0, furnace.cookingTotalTime);
			}

			if (wasBurning != furnace.isLit()) {
				dirty = true;
				level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(furnace.isLit())), 3);
			}

			if (dirty) {
				setChanged(level, pos, state);
			}
		}
	}

	protected boolean canBurn(@Nullable Recipe<?> recipeIn) {
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

	private boolean burn(@Nullable Recipe<?> recipe) {
		if (recipe != null && this.canBurn(recipe)) {
			ItemStack itemstack = this.handler.getStackInSlot(INPUT_SLOT);
			List<? extends String> additionalBlacklist = new ArrayList<>();
			if (ConfigHandler.COMMON.furnaceOutputBlacklist.get() != null) {
				if (!ConfigHandler.COMMON.furnaceOutputBlacklist.get().isEmpty() && !ConfigHandler.COMMON.furnaceOutputBlacklist.get().get(0).isEmpty()) {
					additionalBlacklist = ConfigHandler.COMMON.furnaceOutputBlacklist.get();
				}
			}

			if (recipe instanceof MultipleOutputFurnaceRecipe multipleRecipe) {
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
							BlockEntity foundTile = this.level.getBlockEntity(offPos);
							if (foundTile != null) {
								ResourceLocation typeLocation = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(foundTile.getType());
								boolean flag = foundTile instanceof Hopper || foundTile instanceof AbstractFurnaceBlockEntity || foundTile instanceof AbstractForceFurnaceBlockEntity;
								boolean flag2 = typeLocation != null && (!hopperBlacklist.contains(typeLocation) && (additionalBlacklist.isEmpty() || !additionalBlacklist.contains(typeLocation.toString())));
								if (!flag && flag2 && !foundTile.isRemoved() && foundTile.hasLevel() && foundTile.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
									IItemHandler itemHandler = foundTile.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).orElse(null);
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
						((ServerLevel) level).sendParticles(ParticleTypes.POOF, (double) worldPosition.getX(), (double) worldPosition.getY() + 1, (double) worldPosition.getZ(), 1, 0, 0, 0, 0.0D);
						ItemEntity itemEntity = new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 1, getBlockPos().getZ(), outputStack);
						level.addFreshEntity(itemEntity);
					} else {
						ItemStack currentOutputStack = this.handler.getStackInSlot(OUTPUT_SLOT);
						if (currentOutputStack.isEmpty() && !outputStack.isEmpty()) {
							this.handler.setStackInSlot(OUTPUT_SLOT, outputStack);
						} else if (currentOutputStack.getItem() == outputStacks.get(i).getItem()) {
							currentOutputStack.grow(outputStack.getCount());
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
						BlockEntity foundTile = this.level.getBlockEntity(offPos);
						if (foundTile != null) {
							ResourceLocation typeLocation = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(foundTile.getType());
							boolean flag = foundTile instanceof Hopper || foundTile instanceof AbstractFurnaceBlockEntity || foundTile instanceof AbstractForceFurnaceBlockEntity;
							boolean flag2 = typeLocation != null && (!hopperBlacklist.contains(typeLocation) && (additionalBlacklist.isEmpty() || !additionalBlacklist.contains(typeLocation.toString())));
							if (!flag && flag2 && !foundTile.isRemoved() && foundTile.hasLevel() && foundTile.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
								IItemHandler itemHandler = foundTile.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).orElse(null);
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
			return true;
		}
		return false;
	}

	protected int getBurnDuration(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		} else {
			return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel, null);
		}
	}

	protected int getCookingProgress() {
		AbstractCookingRecipe rec = getRecipe();
		if (rec == null) return 200;
		return this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.getRecipeType(), this, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(100);
	}

	public static boolean isFuel(ItemStack stack) {
		return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null) > 0;
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
			this.cookingTotalTime = this.getCookingProgress();
			this.cookingProgress = 0;
			this.setChanged();
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean stillValid(Player player) {
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

	public void setRecipeUsed(@Nullable Recipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.getId();
			this.recipes.addTo(resourcelocation, 1);
		}
	}

	@Nullable
	public Recipe<?> getRecipeUsed() {
		return null;
	}

	public void awardUsedRecipes(Player player) {
	}

	public void unlockRecipes(Player player) {
		List<Recipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
		player.awardRecipes(list);
		this.recipes.clear();
	}

	public List<Recipe<?>> grantStoredRecipeExperience(Level world, Vec3 pos) {
		List<Recipe<?>> list = Lists.newArrayList();

		for (Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
			world.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				splitAndSpawnExperience(world, pos, entry.getIntValue(), (((AbstractCookingRecipe) recipe).getExperience() * getXPMultiplier()));
			});
		}

		return list;
	}

	private static void splitAndSpawnExperience(Level world, Vec3 pos, int craftedAmount, float experience) {
		int i = Mth.floor((float) craftedAmount * experience);
		float f = Mth.frac((float) craftedAmount * experience);
		if (f != 0.0F && Math.random() < (double) f) {
			++i;
		}

		while (i > 0) {
			int j = ExperienceOrb.getExperienceValue(i);
			i -= j;
			world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, j));
		}

	}

	public void fillStackedContents(StackedContents helper) {
		for (int i = 0; i < handler.getSlots(); i++) {
			helper.accountStack(handler.getStackInSlot(i));
		}
	}

	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
			net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
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
	public void invalidateCaps() {
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

		protected IItemHandler getIItemHandler(Level world) {
			if (world.isAreaLoaded(worldPosition, 1)) {
				BlockEntity tileEntity = world.getBlockEntity(tilePos);
				if (!tileEntity.isRemoved() && tileEntity.hasLevel() && tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
					return tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).orElse(null);
				}
			}
			return null;
		}

		@Override
		public int compareTo(BiggestInventory otherInventory) {
			return Integer.compare(this.inventorySize, otherInventory.inventorySize);
		}
	}

	public ContainerData getFurnaceData() {
		return furnaceData;
	}
}