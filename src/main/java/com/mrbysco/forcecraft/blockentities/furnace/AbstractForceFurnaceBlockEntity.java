package com.mrbysco.forcecraft.blockentities.furnace;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.UpgradeCoreItem;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractForceFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible {
	public static final int INPUT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int OUTPUT_SLOT = 2;

	public static final int UPGRADE_SLOT = 0;

	protected static final int[] SLOTS_UP = new int[]{INPUT_SLOT};
	protected static final int[] SLOTS_DOWN = new int[]{OUTPUT_SLOT, FUEL_SLOT};
	protected static final int[] SLOTS_HORIZONTAL = new int[]{FUEL_SLOT};

	public final ItemStackHandler handler = new ItemStackHandler(3) {
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			if (slot == FUEL_SLOT) {
				ItemStack itemstack = getStackInSlot(FUEL_SLOT);
				return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
			} else return slot != OUTPUT_SLOT;
		}

		@Override
		public void setStackInSlot(int slot, @NotNull ItemStack stack) {
			super.setStackInSlot(slot, stack);
			if (slot == 0) { // TODO: Should be checking if the stack was changed or not, currently isn't
//				cookingTotalTime = getCookingProgress();
//				cookingProgress = 0;
				setChanged();
			}
		}
	};
	public final ItemStackHandler upgradeHandler = new ItemStackHandler(1) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return 1;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return getStackInSlot(slot).isEmpty() && stack.getItem() instanceof UpgradeCoreItem && stack.getCount() == 1;
		}

		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}
	};

	protected static final List<Identifier> hopperBlacklist = List.of(
			Identifier.withDefaultNamespace("hopper"),
			Identifier.fromNamespaceAndPath("cyclic", "hopper"),
			Identifier.fromNamespaceAndPath("cyclic", "hopper_gold"),
			Identifier.fromNamespaceAndPath("cyclic", "hopper_fluid"),
			Identifier.fromNamespaceAndPath("uppers", "upper"),
			Identifier.fromNamespaceAndPath("goldenhopper", "golden_hopper"),
			Identifier.fromNamespaceAndPath("woodenhopper", "wooden_hopper")
	);

	protected int litTime;
	protected int litDuration;
	protected int cookingProgress;
	protected int cookingTotalTime;
	protected int burnSpeed;
	protected int cookingSpeed;
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
	protected RecipeHolder<? extends AbstractCookingRecipe> currentRecipe;
	protected ItemStack failedMatch = ItemStack.EMPTY;

	protected final Object2IntOpenHashMap<Identifier> recipes = new Object2IntOpenHashMap<>();

	protected AbstractForceFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
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

	protected RecipeHolder<? extends AbstractCookingRecipe> getRecipe() {
		ItemStack input = this.getItem(INPUT_SLOT);
		if (input.isEmpty() || input == failedMatch || level == null) return null;
		if (currentRecipe != null && currentRecipe.value().matches(new SingleRecipeInput(this.handler.getStackInSlot(INPUT_SLOT)), level) && currentRecipe.value().getType() == getRecipeType())
			return currentRecipe;
		else {
			RecipeHolder<? extends AbstractCookingRecipe> rec = level.getRecipeManager().getRecipeFor(this.getRecipeType(),
					new SingleRecipeInput(this.handler.getStackInSlot(INPUT_SLOT)), this.level).orElse(null);
			if (rec == null) failedMatch = input;
			else failedMatch = ItemStack.EMPTY;
			return currentRecipe = rec;
		}
	}

	protected boolean isLit() {
		return this.litTime > 0;
	}

	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		this.upgradeHandler.deserializeNBT(registries, tag.getCompound("UpgradeHandler"));
		this.handler.deserializeNBT(registries, tag.getCompound("ItemStackHandler"));

		this.litTime = tag.getInt("BurnTime");
		this.burnSpeed = tag.getInt("BurnSpeed");
		this.cookingProgress = tag.getInt("CookTime");
		this.cookingTotalTime = tag.getInt("CookTimeTotal");
		this.litDuration = tag.contains("BurnTimeTotal") ? tag.getInt("BurnTimeTotal") : this.getBurnDuration(this.handler.getStackInSlot(FUEL_SLOT));
		this.cookingSpeed = tag.getInt("CookSpeed");
		CompoundTag recipesUsed = tag.getCompound("RecipesUsed");

		for (String s : recipesUsed.getAllKeys()) {
			this.recipes.put(Identifier.parse(s), recipesUsed.getInt(s));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putInt("BurnTime", this.litTime);
		compound.putInt("BurnSpeed", this.burnSpeed);
		compound.putInt("CookTime", this.cookingProgress);
		compound.putInt("CookTimeTotal", this.cookingTotalTime);
		compound.putInt("BurnTimeTotal", this.litDuration);
		compound.putInt("CookSpeed", this.cookingSpeed);

		compound.put("UpgradeHandler", upgradeHandler.serializeNBT(registries));
		compound.put("ItemStackHandler", handler.serializeNBT(registries));
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
				RecipeHolder<? extends AbstractCookingRecipe> recipeHolder = furnace.getRecipe();
				AbstractCookingRecipe cookingRecipe = recipeHolder != null ? recipeHolder.value() : null;
				if (!furnace.isLit() && furnace.canBurn(recipeHolder)) {
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

				if (furnace.isLit() && furnace.canBurn(recipeHolder)) {
					int speed = furnace.isEfficient() ? 4 : furnace.getSpeed();
					if (furnace.cookingSpeed != speed) {
						furnace.cookingSpeed = speed;
					}

					if (cookingRecipe != null && furnace.cookingTotalTime != cookingRecipe.getCookingTime())
						furnace.cookingTotalTime = cookingRecipe.getCookingTime();

					furnace.cookingProgress += furnace.cookingSpeed;
					if (furnace.cookingProgress >= furnace.cookingTotalTime) {
						furnace.cookingProgress = 0;
						furnace.cookingTotalTime = furnace.getCookingProgress();
						if (furnace.burn(recipeHolder)) {
							furnace.setRecipeUsed(recipeHolder);
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
				level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, furnace.isLit()), 3);
			}

			if (dirty) {
				setChanged(level, pos, state);
			}
		}
	}

	protected boolean canBurn(@Nullable RecipeHolder<?> recipeHolder) {
		Recipe<?> recipeIn = recipeHolder != null ? recipeHolder.value() : null;
		if (!this.handler.getStackInSlot(INPUT_SLOT).isEmpty() && recipeIn != null) {
			ItemStack recipeOutput = recipeIn.getResultItem(level.registryAccess());
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				ItemStack output = this.handler.getStackInSlot(OUTPUT_SLOT);
				if (output.isEmpty()) {
					return true;
				} else if (!ItemStack.isSameItem(output, recipeOutput)) {
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

	private boolean burn(@Nullable RecipeHolder<?> holder) {
		if (holder != null && this.canBurn(holder) && level != null) {
			Recipe<?> recipe = holder.value();
			ItemStack itemstack = this.handler.getStackInSlot(INPUT_SLOT);
			List<? extends String> additionalBlacklist = new ArrayList<>();
			ConfigHandler.COMMON.furnaceOutputBlacklist.get();
			if (!ConfigHandler.COMMON.furnaceOutputBlacklist.get().isEmpty() && !ConfigHandler.COMMON.furnaceOutputBlacklist.get().getFirst().isEmpty()) {
				additionalBlacklist = ConfigHandler.COMMON.furnaceOutputBlacklist.get();
			}

			if (recipe instanceof MultipleOutputFurnaceRecipe multipleRecipe) {
				NonNullList<ItemStack> outputStacks = multipleRecipe.getRecipeOutputs();
				for (int i = 0; i < outputStacks.size(); i++) {
					ItemStack outputStack = outputStacks.get(i).copy();

					if (i > 0) {
						if (multipleRecipe.getSecondaryChance() != 1.0F || level.getRandom().nextFloat() > multipleRecipe.getSecondaryChance()) {
							//Early break if change didn't work out on second output
							break;
						}
					}

					List<BiggestInventory> inventoryList = new ArrayList<>();
					for (Direction dir : Direction.values()) {
						BlockPos offPos = worldPosition.relative(dir);
						if (this.level.isAreaLoaded(worldPosition, 1)) {
							BlockEntity foundTile = this.level.getBlockEntity(offPos);
							IItemHandler itemHandler = this.level.getCapability(Capabilities.ItemHandler.BLOCK, offPos, dir.getOpposite());
							if (foundTile != null && itemHandler != null) {
								Identifier typeLocation = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(foundTile.getType());
								boolean flag = foundTile instanceof Hopper || foundTile instanceof AbstractFurnaceBlockEntity || foundTile instanceof AbstractForceFurnaceBlockEntity;
								boolean flag2 = typeLocation != null && (!hopperBlacklist.contains(typeLocation) && (additionalBlacklist.isEmpty() || !additionalBlacklist.contains(typeLocation.toString())));
								if (!flag && flag2 && !foundTile.isRemoved() && foundTile.hasLevel() && itemHandler != null) {
									inventoryList.add(new BiggestInventory(offPos, itemHandler.getSlots(), dir.getOpposite()));
								}
							}
						}
					}
					inventoryList.sort(Collections.reverseOrder());
					for (BiggestInventory inventory : inventoryList) {
						IItemHandler itemHandler = inventory.getIItemHandler(this.level, this.worldPosition);
						outputStack = ItemHandlerHelper.insertItem(itemHandler, outputStack, false);
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
				ItemStack itemstack1 = recipe.getResultItem(level.registryAccess());
				ItemStack outputStack = itemstack1.copy();

				List<BiggestInventory> inventoryList = new ArrayList<>();
				for (Direction dir : Direction.values()) {
					BlockPos offPos = worldPosition.relative(dir);
					if (this.level.isAreaLoaded(worldPosition, 1)) {
						BlockEntity foundTile = this.level.getBlockEntity(offPos);
						IItemHandler itemHandler = this.level.getCapability(Capabilities.ItemHandler.BLOCK, offPos, dir.getOpposite());
						if (foundTile != null && itemHandler != null) {
							Identifier typeLocation = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(foundTile.getType());
							boolean flag = foundTile instanceof Hopper || foundTile instanceof AbstractFurnaceBlockEntity || foundTile instanceof AbstractForceFurnaceBlockEntity;
							boolean flag2 = typeLocation != null && (!hopperBlacklist.contains(typeLocation) && (additionalBlacklist.isEmpty() || !additionalBlacklist.contains(typeLocation.toString())));
							if (!flag && flag2 && !foundTile.isRemoved() && foundTile.hasLevel() && foundTile != null) {
								inventoryList.add(new BiggestInventory(offPos, itemHandler.getSlots(), dir.getOpposite()));
							}
						}
					}
				}
				inventoryList.sort(Collections.reverseOrder());
				for (BiggestInventory inventory : inventoryList) {
					IItemHandler itemHandler = inventory.getIItemHandler(this.level, this.worldPosition);
					outputStack = ItemHandlerHelper.insertItem(itemHandler, outputStack, false);
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
				this.setRecipeUsed(holder);
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
			return fuel.getBurnTime(null);
		}
	}

	protected int getCookingProgress() {
		if (level == null) return 200;

		RecipeHolder<? extends AbstractCookingRecipe> rec = getRecipe();
		if (rec == null)
			return this.level.getRecipeManager().getRecipeFor(this.getRecipeType(), new SingleRecipeInput(this.handler.getStackInSlot(INPUT_SLOT)), this.level).map(RecipeHolder::value).map(AbstractCookingRecipe::getCookingTime).orElse(100);
		return rec.value().getCookingTime();
	}

	public static boolean isFuel(ItemStack stack) {
		return stack.getBurnTime(null) > 0;
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
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 */
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		if (direction == Direction.DOWN && index == 1) {
			Item item = stack.getItem();
			return item == Items.WATER_BUCKET || item == Items.BUCKET;
		}

		return true;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getContainerSize() {
		return this.handler.getSlots();
	}

	protected NonNullList<ItemStack> getItems() {
		NonNullList<ItemStack> stacks = NonNullList.withSize(this.handler.getSlots(), ItemStack.EMPTY);
		for (int i = 0; i < this.handler.getSlots(); i++) {
			stacks.set(i, this.handler.getStackInSlot(i));
		}
		return stacks;
	}

	protected void setItems(NonNullList<ItemStack> items) {
		for (int i = 0; i < this.handler.getSlots(); i++) {
			this.handler.setStackInSlot(i, items.get(i));
		}
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
		boolean flag = handler.isItemValid(index, stack) && !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemstack);
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
		if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
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

	public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
		if (recipe != null) {
			Identifier resourcelocation = recipe.id();
			this.recipes.addTo(resourcelocation, 1);
		}
	}

	@Nullable
	public RecipeHolder<?> getRecipeUsed() {
		return null;
	}


	@Override
	public void awardUsedRecipes(Player player, List<ItemStack> stacks) {
	}

	public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
		List<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience(player.serverLevel(), player.position());
		player.awardRecipes(list);
		this.recipes.clear();
	}

	public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 pos) {
		List<RecipeHolder<?>> list = Lists.newArrayList();

		for (Object2IntMap.Entry<Identifier> entry : this.recipes.object2IntEntrySet()) {
			serverLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				createExperience(serverLevel, pos, entry.getIntValue(), (((AbstractCookingRecipe) recipe.value()).getExperience() * getXPMultiplier()));
			});
		}

		return list;
	}

	private static void createExperience(ServerLevel serverLevel, Vec3 pos, int craftedAmount, float experience) {
		int i = Mth.floor((float) craftedAmount * experience);
		float f = Mth.frac((float) craftedAmount * experience);
		if (f != 0.0F && Math.random() < (double) f) {
			++i;
		}

		ExperienceOrb.award(serverLevel, pos, i);
	}

	public void fillStackedContents(StackedContents helper) {
		for (int i = 0; i < handler.getSlots(); i++) {
			helper.accountStack(handler.getStackInSlot(i));
		}
	}

	public ContainerData getFurnaceData() {
		return furnaceData;
	}
}