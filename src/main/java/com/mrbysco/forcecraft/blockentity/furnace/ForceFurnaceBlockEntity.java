package com.mrbysco.forcecraft.blockentity.furnace;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.menu.ForceFurnaceMenu;
import com.mrbysco.forcecraft.recipe.MultiOutputFurnaceRecipe;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ForceFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
	private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> frozenCheck;
	private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> grindingCheck;

	private final ItemStacksResourceHandler upgradeHandler = new ItemStacksResourceHandler(1) {
		@Override
		public boolean isValid(int index, ItemResource resource) {
			return resource.is(ForceTags.Items.UPGRADE_CORES);
		}

		@Override
		protected int getCapacity(int index, ItemResource resource) {
			return 1;
		}

		@Override
		public int extract(ItemResource resource, int amount, TransactionContext transaction) {
			// You can not extract cores without breaking them
			return 0;
		}

		@Override
		public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
			return 0;
		}
	};

	private static final List<Identifier> HOPPER_BLACKLIST = List.of(
			Identifier.withDefaultNamespace("hopper"),
			Identifier.fromNamespaceAndPath("cyclic", "hopper"),
			Identifier.fromNamespaceAndPath("cyclic", "hopper_gold"),
			Identifier.fromNamespaceAndPath("cyclic", "hopper_fluid"),
			Identifier.fromNamespaceAndPath("uppers", "upper"),
			Identifier.fromNamespaceAndPath("goldenhopper", "golden_hopper"),
			Identifier.fromNamespaceAndPath("woodenhopper", "wooden_hopper")
	);

	public ForceFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.SMELTING);
		this.frozenCheck = RecipeManager.createCheck(ForceRecipes.FREEZING.get());
		this.grindingCheck = RecipeManager.createCheck(ForceRecipes.GRINDING.get());
	}

	public static void forceServerTick(ServerLevel level, BlockPos pos, BlockState state, ForceFurnaceBlockEntity entity) {
		boolean changed = false;
		boolean isLit;
		boolean wasLit;
		if (entity.litTimeRemaining > 0) {
			wasLit = true;
			entity.litTimeRemaining = Math.max(0, entity.litTimeRemaining - entity.getBurnSpeed());
			isLit = entity.litTimeRemaining > 0;
		} else {
			wasLit = false;
			isLit = false;
		}

		ItemStack fuel = entity.items.get(1);
		ItemStack ingredient = entity.items.get(0);
		boolean hasIngredient = !ingredient.isEmpty();
		boolean hasFuel = !fuel.isEmpty();

		if (isLit || hasFuel && hasIngredient) {
			if (hasIngredient) {
				SingleRecipeInput input = new SingleRecipeInput(ingredient);
				RecipeHolder<? extends AbstractCookingRecipe> recipe = entity.getRecipeFor(input, level);
				if (recipe != null) {
					ItemStack burnResult = recipe.value().assemble(input);
					if (!burnResult.isEmpty() && canBurn(entity.items, entity.getMaxStackSize(), burnResult)) {
						if (!isLit) {
							int newLitTime = entity.getBurnDuration(level.fuelValues(), fuel);
							entity.litTimeRemaining = newLitTime;
							entity.litTotalTime = newLitTime;
							if (newLitTime > 0) {
								consumeFuel(entity.items, fuel);
								isLit = true;
								changed = true;
							}
						}

						if (isLit) {
							entity.cookingTotalTime = recipe.value().cookingTime();
							entity.cookingTimer += entity.getCookSpeed();
							if (entity.cookingTimer >= entity.cookingTotalTime) {
								entity.cookingTimer = 0;
								entity.burnRecipe(recipe.value(), ingredient, burnResult, level);
								entity.setRecipeUsed(recipe);
								changed = true;
							}
						} else {
							entity.cookingTimer = 0;
						}
					} else {
						entity.cookingTimer = 0;
					}
				} else {
					entity.cookingTimer = 0;
				}
			} else {
				entity.cookingTimer = 0;
			}
		} else if (entity.cookingTimer > 0) {
			entity.cookingTimer = Mth.clamp(entity.cookingTimer - 2, 0, entity.cookingTotalTime);
		}

		if (wasLit != isLit) {
			changed = true;
			state = state.setValue(AbstractFurnaceBlock.LIT, isLit);
			level.setBlock(pos, state, 3);
		}
		if (changed) {
			setChanged(level, pos, state);
		}
	}

	private void burnRecipe(AbstractCookingRecipe recipe, ItemStack ingredient, ItemStack burnResult, ServerLevel level) {
		List<ResourceHandler<ItemResource>> targets = findOutputTargets(level);

		setOutput(insertIntoTargets(targets, burnResult.copy()));

		if (recipe instanceof MultiOutputFurnaceRecipe multi && level.getRandom().nextFloat() < multi.secondaryChance()) {
			if (multi.result2().isPresent()) {
				ItemStack remainder = insertIntoTargets(targets, multi.result2().get().create());
				if (!remainder.isEmpty()) {
					BlockPos pos = this.worldPosition;
					level.sendParticles(ParticleTypes.POOF, pos.getX(), pos.getY() + 1, pos.getZ(), 1, 0, 0, 0, 0.0D);
					level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), remainder));
				}
			}
		}

		if (ingredient.is(Items.WET_SPONGE) && this.items.get(1).is(Items.BUCKET)) {
			this.items.set(1, new ItemStack(Items.WATER_BUCKET));
		}
		ingredient.shrink(1);
	}

	public boolean isEfficient() {
		return upgradeHandler.getResource(0).is(ForceRegistry.HEAT_CORE.get());
	}

	public boolean isFast() {
		return upgradeHandler.getResource(0).is(ForceRegistry.SPEED_CORE.get());
	}

	public boolean hasXPMultiplied() {
		return upgradeHandler.getResource(0).is(ForceRegistry.EXPERIENCE_CORE.get());
	}

	public int getBurnSpeed() {
		return isFast() ? 10 : 2;
	}

	public int getCookSpeed() {
		return isEfficient() ? 4 : getBurnSpeed();
	}

	public int getXPMultiplier() {
		return hasXPMultiplied() ? 2 : 1;
	}

	private @Nullable RecipeHolder<? extends AbstractCookingRecipe> getRecipeFor(SingleRecipeInput input, ServerLevel serverLevel) {
		ItemResource upgrade = upgradeHandler.getResource(0);
		if (!upgrade.isEmpty()) {
			if (upgrade.is(ForceRegistry.FROZEN_CORE.get())) {
				return frozenCheck.getRecipeFor(input, serverLevel).orElse(null);
			} else if (upgrade.is(ForceRegistry.GRINDING_CORE.get())) {
				return grindingCheck.getRecipeFor(input, serverLevel).orElse(null);
			}
		}
		return quickCheck.getRecipeFor(input, serverLevel).orElse(null);
	}

	@Override
	public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 position) {
		int multiplier = getXPMultiplier();
		if (multiplier > 1) {
			this.recipesUsed.replaceAll((key, count) -> count * multiplier);
		}
		return super.getRecipesToAwardAndPopExperience(level, position);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable(Reference.MOD_ID + ".container.force_furnace");
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new ForceFurnaceMenu(containerId, inventory, this);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.upgradeHandler.deserialize(input.childOrEmpty("Upgrade"));
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		this.upgradeHandler.serialize(output.child("Upgrade"));
	}

	private List<ResourceHandler<ItemResource>> findOutputTargets(ServerLevel level) {
		List<? extends String> configBlacklist = ForceConfig.COMMON.furnaceOutputBlacklist.get();
		List<ResourceHandler<ItemResource>> found = new ArrayList<>();

		for (Direction dir : Direction.values()) {
			BlockPos offPos = this.worldPosition.relative(dir);
			if (!level.isLoaded(offPos)) continue;

			BlockEntity be = level.getBlockEntity(offPos);
			if (be == null || be.isRemoved()) continue;
			if (be instanceof Hopper || be instanceof AbstractFurnaceBlockEntity) continue;

			Identifier typeId = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType());
			if (typeId == null || HOPPER_BLACKLIST.contains(typeId) || configBlacklist.contains(typeId.toString()))
				continue;

			ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, offPos, dir.getOpposite());
			if (handler != null) found.add(handler);
		}

		found.sort(Comparator.comparingInt((ResourceHandler<ItemResource> h) -> h.size()).reversed());
		return found;
	}

	private static ItemStack insertIntoTargets(List<ResourceHandler<ItemResource>> targets, ItemStack stack) {
		ItemResource resource = ItemResource.of(stack);
		int remaining = stack.getCount();
		for (ResourceHandler<ItemResource> target : targets) {
			try (Transaction tx = Transaction.openRoot()) {
				remaining -= target.insert(resource, remaining, tx);
				tx.commit();
			}
			if (remaining <= 0) return ItemStack.EMPTY;
		}
		return resource.toStack(remaining);
	}

	private void setOutput(ItemStack stack) {
		if (stack.isEmpty()) return;
		ItemStack current = this.items.get(2);
		if (current.isEmpty()) {
			this.items.set(2, stack);
		} else if (ItemStack.isSameItemSameComponents(current, stack)) {
			current.grow(stack.getCount());
		}
	}

	public ItemStacksResourceHandler getUpgradeHandler() {
		return upgradeHandler;
	}

	public ContainerData getDataAccess() {
		return this.dataAccess;
	}

	public boolean hasRecipeFor(ItemStack stack, ServerLevel level) {
		return getRecipeFor(new SingleRecipeInput(stack), level) != null;
	}
}
