package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.energy.ForceEnergyStorage;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.storage.PackStackHandler;
import com.mrbysco.forcecraft.components.storage.StorageManager;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookData;
import com.mrbysco.forcecraft.items.infuser.UpgradeTomeItem;
import com.mrbysco.forcecraft.items.tools.ForceAxeItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShearsItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import com.mrbysco.forcecraft.items.tools.ForceSwordItem;
import com.mrbysco.forcecraft.menu.infuser.InfuserMenu;
import com.mrbysco.forcecraft.networking.message.StopInfuserSoundPayload;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.util.EnchantUtils;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InfuserBlockEntity extends BlockEntity implements MenuProvider, Container {
	private static final Set<String> HASHES = new HashSet<>();
	public static final Map<Integer, List<Identifier>> LEVEL_RECIPE_LIST = new HashMap<>();

	private static final int FLUID_CHARGE = 1000;

	public boolean canWork = false;
	public boolean makesSpecialSound = false;

	public int processTime = 0;
	public int maxProcessTime = 20;

	// Slots [0,7] are the surround
	public static final int SLOT_TOOL = 8;
	public static final int SLOT_GEM = 9;
	public static final int SLOT_BOOK = 10;
	// Currently, these costs are fixed PER infusing a thing once
	public static final int ENERGY_COST_PER = 20;
	public static final int FLUID_COST_PER = 1000;
	// Ratio o f gem slot to fluid tank
	private static final int FLUID_PER_GEM = 500;


	protected FluidTank tank = new FluidTank(50000) {
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (!isFluidEqual(resource)) {
				return FluidStack.EMPTY;
			}
			if (action.simulate()) {
				int amount = tank.getFluidAmount() - resource.getAmount() < 0 ? tank.getFluidAmount() : resource.getAmount();
				return new FluidStack(tank.getFluid().getFluid(), amount);
			}
			return super.drain(resource.getAmount(), action);
		}

		@Override
		protected void onContentsChanged() {
			refreshClient();
		}

		@Override
		@NotNull
		public FluidStack drain(int maxDrain, @NotNull FluidAction action) {
			return super.drain(maxDrain, action);
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			return stack.is(ForceTags.FORCE);
		}
	};

	public final ItemStackHandler handler = new ItemStackHandler(11) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			if (slot == SLOT_GEM) {
				return 64;
			}
			return 1;
		}

		@Override
		public int getSlotLimit(int slot) {
			if (slot == SLOT_GEM) {
				return 64;
			}
			return 1;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (slot < SLOT_TOOL) {
				return matchesModifier(stack);
				// is valid modifier
				// non ingredients into the circle
			} else if (slot == SLOT_TOOL) {
				// don't hardcode validation here, check recipe "center" tag or item
				return matchesTool(stack);// stack.getItem().isIn(ForceTags.VALID_INFUSER_TOOLS);
			} else if (slot == SLOT_BOOK) {
				return stack.getItem() == ForceRegistry.UPGRADE_TOME.get();
			} else if (slot == SLOT_GEM) {
				return stack.getItem() == ForceRegistry.FORCE_GEM.get();
			}
			// else tool or something for the around
			return true;
		}
	};

	public ForceEnergyStorage energyStorage = new ForceEnergyStorage(64000, 1000);

	private final NonNullList<ItemStack> infuserContents = NonNullList.create();

	private Int2ObjectOpenHashMap<RecipeHolder<InfuseRecipe>> currentRecipes = new Int2ObjectOpenHashMap<>();

	public InfuserBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public InfuserBlockEntity(BlockPos pos, BlockState state) {
		this(ForceRegistry.INFUSER_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		this.processTime = compound.getInt("processTime");
		this.maxProcessTime = compound.getInt("maxProcessTime");
		//Items
		canWork = compound.getBoolean("canWork");
		handler.deserializeNBT(registries, compound.getCompound("ItemStackHandler"));
		ContainerHelper.loadAllItems(compound, this.infuserContents, registries);
		energyStorage.setEnergy(compound.getInt("EnergyHandler"));
		tank.readFromNBT(registries, compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putInt("processTime", this.processTime);
		compound.putInt("maxProcessTime", this.maxProcessTime);
		//Items
		compound.putBoolean("canWork", canWork);
		compound.put("ItemStackHandler", handler.serializeNBT(registries));
		compound.putInt("EnergyHandler", energyStorage.getEnergyStored());
		ContainerHelper.saveAllItems(compound, this.infuserContents, registries);
		tank.writeToNBT(registries, compound);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, InfuserBlockEntity infuser) {
		if (level.getGameTime() % 20 == 0) {
			if (LEVEL_RECIPE_LIST.isEmpty()) {
				List<RecipeHolder<InfuseRecipe>> holders = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
				for (RecipeHolder<InfuseRecipe> holder : holders) {
					addRecipe(holder);
				}
			}
		}

		if (infuser.handler.getStackInSlot(SLOT_GEM).getItem() == ForceRegistry.FORCE_GEM.get()) {
			infuser.processForceGems();
		}

		if (infuser.canWork) {
			infuser.processTime++;

			if (level.getGameTime() % 60 == 0) {
				infuser.makeWorkSound();
			}

			if (infuser.energyStorage.getEnergyStored() > ENERGY_COST_PER) {
				infuser.energyStorage.consumePower(ENERGY_COST_PER);
			}

			if (infuser.processTime < infuser.maxProcessTime) {
				return;
			}
			infuser.processTime = 0;
			//working with No progress for now, just is valid and did i turn it on

			//once we have a valid tool and have waited. go do the thing
			if (infuser.isWorkAllowed()) {
				if (infuser.areAllModifiersEmpty() && infuser.canCharge()) {
					infuser.processForceCharging();
				} else if (infuser.recipesStillMatch()) {
					infuser.processTool();
				}

				infuser.stopWorkSound();

				if (infuser.makesSpecialSound) {
					infuser.playSound(ForceSounds.INFUSER_SPECIAL_DONE.get(), 1.0F, 1.0F);
				} else {
					infuser.playSound(ForceSounds.INFUSER_DONE.get(), 1.0F, 1.0F);
				}
				infuser.makesSpecialSound = false;
			}
			// auto turn off when done
			//even if tool or book slot become empty, don't auto run next insert
			infuser.canWork = false;
			infuser.processTime = 0;

			infuser.refreshClient();
		} else {
			infuser.processTime = 0;
		}
	}

	public void startWork() {
		canWork = true;
		processTime = 0;
		setMaxTimeFromRecipes();
		if (areAllModifiersEmpty() && canCharge()) {
			maxProcessTime = ConfigHandler.COMMON.forceInfusingTime.get();
		}
		if (maxProcessTime <= 0) {
			//no valid recipes
			canWork = false;
			maxProcessTime = 0;
		}
		if (canWork) {
			stopWorkSound();
			makesSpecialSound = false;
			if (level != null && level.getRandom().nextInt(10) == 0) {
				makesSpecialSound = true;
				playSound(ForceSounds.INFUSER_SPECIAL_BEEP.get(), 1.0F, 1.0F);
			}
			makeWorkSound();
		}
		refreshClient();
	}

	public void makeWorkSound() {
		if (makesSpecialSound) {
			playSound(ForceSounds.INFUSER_SPECIAL.get(), 1.0F, 1.0F);
		} else {
			playSound(ForceSounds.INFUSER_WORKING.get(), 1.0F, 1.0F);
		}
	}

	public void playSound(SoundEvent event, float volume, float pitch) {
		BlockPos pos = getBlockPos();
		if (level != null)
			level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), event, SoundSource.BLOCKS, volume, pitch);
	}

	public void stopWorkSound() {
		if (level != null && !level.isClientSide) {
			BlockPos pos = getBlockPos();
			for (Player player : level.players()) {
				if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 200) {
					((ServerPlayer) player).connection.send(new StopInfuserSoundPayload());
				}
			}
		}
	}

	private void setMaxTimeFromRecipes() {
		maxProcessTime = 0;
		if (!this.getBookInSlot().isEmpty()) { //Make sure it doesn't run if the book is missing
			List<RecipeHolder<InfuseRecipe>> recipes = new ArrayList<>(getMatchingRecipes().values());
			if (!recipes.isEmpty()) {
				for (RecipeHolder<InfuseRecipe> currentHolder : recipes) {
//					ForceCraft.LOGGER.info(recipeCurrent.getId() + " increase  "+ recipeCurrent.getTime());
					maxProcessTime += currentHolder.value().getTime();
				}
			}
		}
	}

	protected Int2ObjectOpenHashMap<RecipeHolder<InfuseRecipe>> getMatchingRecipes() {
		if (getBookInSlot().isEmpty()) return new Int2ObjectOpenHashMap<>();
		if (!currentRecipes.isEmpty() && recipesStillMatch()) return currentRecipes;
		else {
			Int2ObjectOpenHashMap<RecipeHolder<InfuseRecipe>> matchingRecipes = new Int2ObjectOpenHashMap<>();
			for (int i = 0; i < SLOT_TOOL; i++) {
				ItemStack modifier = getModifier(i);
				if (modifier.isEmpty()) continue;

				List<RecipeHolder<InfuseRecipe>> holders = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
				for (RecipeHolder<InfuseRecipe> holder : holders) {
					InfuseRecipe recipe = holder.value();
					if (recipe.matchesModifier(new RecipeWrapper(this.handler), modifier, false)) {
						matchingRecipes.put(i, holder);
						break;
					}
				}
			}
			return currentRecipes = matchingRecipes;
		}
	}

	protected boolean matchesModifier(ItemStack stack) {
		if (level != null) {
			List<RecipeHolder<InfuseRecipe>> holders = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
			for (RecipeHolder<InfuseRecipe> holder : holders) {
				InfuseRecipe recipe = holder.value();
				if (recipe.matchesModifier(this, stack)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean matchesTool(ItemStack toolStack) {
		if (level != null) {
			List<RecipeHolder<InfuseRecipe>> holders = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
			for (RecipeHolder<InfuseRecipe> holder : holders) {
				InfuseRecipe recipe = holder.value();
				if (recipe.matchesTool(toolStack, true)) {
					return true;
				}
			}
		}
		return toolStack.is(ForceTags.VALID_INFUSER_CHARGE);
	}

	protected boolean recipesStillMatch() {
		for (Map.Entry<Integer, RecipeHolder<InfuseRecipe>> entry : currentRecipes.entrySet()) {
			ItemStack modifier = getModifier(entry.getKey());
			if (!entry.getValue().value().matchesModifier(new RecipeWrapper(this.handler), modifier, false)) {
				return false;
			}
		}
		return true;
	}

	private void refreshClient() {
		setChanged();
		BlockState state = level.getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}

	//Processes force Gems in the force infuser slot
	private void processForceGems() {
		FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);
		if (tank.getFluidAmount() + force.getAmount() <= tank.getCapacity()) {
			fill(force, FluidAction.EXECUTE);
			handler.getStackInSlot(SLOT_GEM).shrink(1);

			refreshClient();
		}
	}

	public boolean areAllModifiersEmpty() {
		int emptySlots = 0;
		for (int i = 0; i < SLOT_TOOL; i++) {
			if (handler.getStackInSlot(i).isEmpty()) {
				emptySlots++;
			}
		}

		return emptySlots == 8;
	}

	private void processForceCharging() {
		ItemStack tool = getFromToolSlot();

		ForceToolData force = new ForceToolData(tool);
		int charge = FLUID_CHARGE;
		if (tool.isDamaged()) {
			final int ratio = IForceChargingTool.FORCE_DMG_RATIO;
			int damage = tool.getDamageValue();
			int repaired = 0;
			for (int i = 0; i < damage; i++) {
				if (charge >= ratio) {
					repaired++;
					charge -= ratio;
				}
			}
			tool.setDamageValue(damage - repaired);
		}
		if (charge > 0) {
			force.charge(charge);
		}
		tank.drain(FLUID_CHARGE, FluidAction.EXECUTE);
	}

	private void processTool() {
		for (Map.Entry<Integer, RecipeHolder<InfuseRecipe>> entry : currentRecipes.entrySet()) {
			ItemStack modifier = getModifier(entry.getKey());
			RecipeHolder<InfuseRecipe> recipeHolder = entry.getValue();
			if (recipeHolder.value().matchesModifier(new RecipeWrapper(this.handler), modifier, true)) {
				ItemStack tool = getFromToolSlot();
				boolean success = applyModifier(tool, modifier, recipeHolder);
				ForceCraft.LOGGER.debug("Applying modifier {} on tool {}, success: {}", modifier, tool, success);
				if (success) {

					// for EACH modifier
					modifier.shrink(1);
					tank.drain(FLUID_COST_PER, FluidAction.EXECUTE);
//                    energyStorage.consumePower(ENERGY_COST_PER);

					UpgradeTomeItem.onModifierApplied(this.getBookInSlot(), modifier, tool);
//					ForceCraft.LOGGER.info("process tool success {}, {}", this.getBookInSlot().getTag() , energyStorage.getEnergyStored());
				}
			}
		}

		// TODO: is this notify needed?
		level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
		super.onDataPacket(net, pkt, lookupProvider);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		this.loadAdditional(tag, registries);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag, this.level.registryAccess());
		return tag;
	}

	public ItemStackHandler getItemHandler(@Nullable Direction facing) {
		return handler;
	}

	public FluidTank getFluidTank(@Nullable Direction facing) {
		return tank;
	}

	public ForceEnergyStorage getEnergyStorage(@Nullable Direction facing) {
		return energyStorage;
	}

	public boolean hasTool() {
		ItemStack tool = getFromToolSlot();
		return !tool.isEmpty();
	}

	public boolean hasValidBook() {
		ItemStack tool = getBookInSlot();
		if (!tool.isEmpty()) {
			return tool.getItem() == ForceRegistry.UPGRADE_TOME.get();
		}
		return false;
	}

	public ItemStack getFromToolSlot() {
		return handler.getStackInSlot(SLOT_TOOL);
	}

	public ItemStack getBookInSlot() {
		return handler.getStackInSlot(SLOT_BOOK);
	}

	public ItemStack getModifier(int slot) {
		if (slot >= 0 && slot <= SLOT_TOOL - 1) {
			return handler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	public boolean hasValidModifer(int slot) {
		return !getModifier(slot).isEmpty();
	}

	public boolean canCharge() {
		ItemStack tool = getFromToolSlot();
		return isValidChargeableStack(tool) && tank.getFluidAmount() > FLUID_CHARGE;
	}

	public boolean isValidChargeableStack(ItemStack stack) {
		return stack.is(ForceTags.VALID_INFUSER_CHARGE) && stack.has(ForceComponents.FORCE_INFUSED) && stack.getCount() == 1;
	}

	/**
	 * Loop on all modifiers and apply first one that matches the input test.
	 *
	 * @param tool         the tool to apply the modifier to
	 * @param modifier     the modifier to apply
	 * @param recipeHolder the recipe to apply
	 * @return true if a modifier was applied
	 */
	private boolean applyModifier(ItemStack tool, ItemStack modifier, RecipeHolder<InfuseRecipe> recipeHolder) {
		UpgradeBookData bd = this.getBookInSlot().getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
		//if the recipe level does not exceed what the book has
		//test the ingredient of this recipe, if it matches me

		InfuseRecipe recipe = recipeHolder.value();
		if (recipe.getModifier().apply(tool, modifier, bd, this.level.registryAccess())) {
			bd.onRecipeApply(recipeHolder, getBookInSlot());

			if (recipe.getModifier() == InfuserModifierType.ITEM && recipe.hasOutput()) {
				//overwrite / convert item
				handler.setStackInSlot(SLOT_TOOL, recipe.getResultItem(level.registryAccess()).copy());
			} else {
				//sync item changes
				handler.setStackInSlot(SLOT_TOOL, tool);
			}

			return true;
		}// else {
		//	ForceCraft.LOGGER.info(" apply returned false on {} to the tool {}", recipe.resultModifier, tool);
		//}

		return false;
	}

	// TODO: refactor these static below into another place, possibly InfuserModifier class

	static boolean applyCamo(ItemStack tool, ItemStack mod) {
		PotionContents potionContents = mod.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		for (MobEffectInstance e : potionContents.getAllEffects()) {
			if (e.getEffect() == MobEffects.NIGHT_VISION) {
				return addSightModifier(tool);
			}
			if (e.getEffect() == MobEffects.INVISIBILITY) {
				return addCamoModifier(tool);
			}
		}
		return false;
	}

	static boolean addLightModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceRodItem) {
			if (!stack.has(ForceComponents.ROD_LIGHT)) {
				stack.set(ForceComponents.ROD_LIGHT, true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceBowItem) {
			if (!stack.has(ForceComponents.TOOL_LIGHT)) {
				stack.set(ForceComponents.TOOL_LIGHT, true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	private static boolean addCamoModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceRodItem) {
			if (!stack.has(ForceComponents.ROD_CAMO)) {
				stack.set(ForceComponents.ROD_CAMO, true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			if (!stack.has(ForceComponents.TOOL_CAMO)) {
				stack.set(ForceComponents.TOOL_CAMO, true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	private static boolean addSightModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceRodItem) {
			if (!stack.has(ForceComponents.ROD_SIGHT)) {
				stack.set(ForceComponents.ROD_SIGHT, true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean addWingModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceSwordItem) {
			if (!stack.has(ForceComponents.TOOL_WING)) {
				stack.set(ForceComponents.TOOL_WING, true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			if (!stack.has(ForceComponents.TOOL_WING)) {
				stack.set(ForceComponents.TOOL_WING, true);
				addInfusedTag(stack);
				return true;
			}
		}

		return false;
	}

	static boolean addBaneModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceSwordItem) {
			if (!stack.has(ForceComponents.TOOL_BANE)) {
				stack.set(ForceComponents.TOOL_BANE, 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceBowItem) {
			if (!stack.has(ForceComponents.TOOL_BANE)) {
				stack.set(ForceComponents.TOOL_BANE, 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			if (!stack.has(ForceComponents.TOOL_BANE)) {
				stack.set(ForceComponents.TOOL_BANE, 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addBleedingModifier(ItemStack stack) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.bleedCap.get();
		if (item instanceof ForceSwordItem) {
			int currentBleed = stack.getOrDefault(ForceComponents.TOOL_BLEED, 0);
			if (currentBleed < MAX_CAP) {
				stack.set(ForceComponents.TOOL_BLEED, currentBleed + 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceBowItem) {
			int currentBleed = stack.getOrDefault(ForceComponents.TOOL_BLEED, 0);
			if (currentBleed < MAX_CAP) {
				stack.set(ForceComponents.TOOL_BLEED, currentBleed + 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			int currentBleed = stack.getOrDefault(ForceComponents.TOOL_BLEED, 0);
			if (currentBleed < MAX_CAP) {
				stack.set(ForceComponents.TOOL_BLEED, currentBleed + 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addEnderModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceRodItem) {
			if (!stack.has(ForceComponents.ROD_ENDER)) {
				stack.set(ForceComponents.ROD_ENDER, true);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceSwordItem) {
			if (!stack.has(ForceComponents.TOOL_ENDER)) {
				stack.set(ForceComponents.TOOL_ENDER, true);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceBowItem) {
			if (!stack.has(ForceComponents.TOOL_ENDER)) {
				stack.set(ForceComponents.TOOL_ENDER, true);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addFreezingModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceBowItem) {
			if (!stack.has(ForceComponents.TOOL_FREEZING)) {
				stack.set(ForceComponents.TOOL_FREEZING, true);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addHealingModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceRodItem) {
			int MAX_CAP = ConfigHandler.COMMON.healingCap.get();
			int currentBleed = stack.getOrDefault(ForceComponents.ROD_HEALING, 0);
			if (currentBleed < MAX_CAP) {
				stack.set(ForceComponents.ROD_HEALING, currentBleed + 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addLumberjackModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceAxeItem) {
			if (!stack.has(ForceComponents.TOOL_LUMBERJACK)) {
				stack.set(ForceComponents.TOOL_LUMBERJACK, true);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addRainbowModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceShearsItem) {
			stack.set(ForceComponents.TOOL_RAINBOW, true);
			addInfusedTag(stack);

			return true;
		}
		return false;
	}

	static boolean addTreasureModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceSwordItem || stack.getItem() instanceof ForceAxeItem) {

			if (!stack.has(ForceComponents.TOOL_TREASURE)) {
				stack.set(ForceComponents.TOOL_TREASURE, true);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean upgradeBag(ItemStack stack, UpgradeBookData bd) {
		if (stack.getItem() instanceof ForcePackItem) {
			PackStackHandler handler = StorageManager.getOrCreatePack(stack).getInventory();

			if (handler.canUpgrade(bd)) {
				handler.applyUpgrade();

				stack.set(ForceComponents.SLOTS_USED, ItemHandlerUtils.getUsedSlots(handler));
				stack.set(ForceComponents.SLOTS_TOTAL, handler.getSlotsInUse());
				stack.set(ForceComponents.PACK_TIER, handler.getUpgrades());

				return true;
			}
			return false;
		}
		return false;
	}

	static boolean addSturdyModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		if (item instanceof ForceSwordItem || item instanceof ForceAxeItem || item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceRodItem) {
			int getSturdylevel = stack.getOrDefault(ForceComponents.TOOL_STURDY, 0);
			if (getSturdylevel < ConfigHandler.COMMON.sturdyToolCap.get()) {
				stack.set(ForceComponents.TOOL_STURDY, getSturdylevel + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.UNBREAKING));
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			int getSturdylevel = stack.getOrDefault(ForceComponents.TOOL_STURDY, 0);
			if (getSturdylevel == 0) {
				stack.set(ForceComponents.TOOL_STURDY, getSturdylevel + 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addLuckModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.luckCap.get();
		if (item instanceof ForcePickaxeItem || item instanceof ForceShovelItem || item instanceof ForceAxeItem) {
			int currentLuck = stack.getOrDefault(ForceComponents.TOOL_LUCK, 0);
			if (currentLuck < MAX_CAP) {
				stack.set(ForceComponents.TOOL_LUCK, currentLuck + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.FORTUNE));
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceSwordItem) {
			int currentLuck = stack.getOrDefault(ForceComponents.TOOL_LUCK, 0);
			if (currentLuck < MAX_CAP) {
				stack.set(ForceComponents.TOOL_LUCK, currentLuck + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.LOOTING));
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceBowItem) {
			int currentLuck = stack.getOrDefault(ForceComponents.TOOL_LUCK, 0);
			if (currentLuck < MAX_CAP) {
				stack.set(ForceComponents.TOOL_LUCK, currentLuck + 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			int currentLuck = stack.getOrDefault(ForceComponents.TOOL_LUCK, 0);
			if (currentLuck < MAX_CAP) {
				stack.set(ForceComponents.TOOL_LUCK, currentLuck + 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addDamageModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.damageCap.get();
		if (item instanceof ForceSwordItem) {
			int currentSharpness = stack.getOrDefault(ForceComponents.TOOL_SHARPNESS, 0);
			if (currentSharpness < MAX_CAP) {
				stack.set(ForceComponents.TOOL_SHARPNESS, currentSharpness + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.SHARPNESS));
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceBowItem) {
			int currentSharpness = stack.getOrDefault(ForceComponents.TOOL_SHARPNESS, 0);
			if (currentSharpness < MAX_CAP) {
				stack.set(ForceComponents.TOOL_SHARPNESS, currentSharpness + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.POWER));
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			int currentSharpness = stack.getOrDefault(ForceComponents.TOOL_SHARPNESS, 0);
			if (currentSharpness < 1) {
				stack.set(ForceComponents.TOOL_SHARPNESS, currentSharpness + 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addSilkTouchModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		if (item instanceof ForceAxeItem || item instanceof ForceShovelItem || item instanceof ForcePickaxeItem) {
			if (!stack.has(ForceComponents.TOOL_SILK)) {
				stack.set(ForceComponents.TOOL_SILK, true);
				stack.enchant(provider.holderOrThrow(Enchantments.SILK_TOUCH), 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addForceModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.forceCap.get();
		if (item instanceof ForceSwordItem || item instanceof ForceAxeItem) {
			int currentForce = stack.getOrDefault(ForceComponents.TOOL_FORCE, 0);
			if (currentForce < MAX_CAP) {
				stack.set(ForceComponents.TOOL_FORCE, currentForce + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.KNOCKBACK));
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addHeatModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem) {
			if (!stack.has(ForceComponents.TOOL_HEAT)) {
				stack.set(ForceComponents.TOOL_HEAT, true);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceSwordItem || item instanceof ForceAxeItem) {
			if (!stack.has(ForceComponents.TOOL_HEAT)) {
				stack.set(ForceComponents.TOOL_HEAT, true);
				stack.enchant(provider.holderOrThrow(Enchantments.FIRE_ASPECT), 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			if (!stack.has(ForceComponents.TOOL_HEAT)) {
				stack.set(ForceComponents.TOOL_HEAT, true);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static boolean addSpeedModifier(ItemStack stack, HolderLookup.Provider provider) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.speedCap.get();
		if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceAxeItem) {
			int currentSpeed = stack.getOrDefault(ForceComponents.TOOL_SPEED, 0);
			if (currentSpeed < MAX_CAP) {
				stack.set(ForceComponents.TOOL_SPEED, currentSpeed + 1);
				EnchantUtils.incrementLevel(stack, provider.holderOrThrow(Enchantments.EFFICIENCY));
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceBowItem) {
			int currentSpeed = stack.getOrDefault(ForceComponents.TOOL_SPEED, 0);
			if (currentSpeed < 1) {
				stack.set(ForceComponents.TOOL_SPEED, currentSpeed + 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			int currentSpeed = stack.getOrDefault(ForceComponents.TOOL_SPEED, 0);
			if (currentSpeed < 1) {
				stack.set(ForceComponents.TOOL_SPEED, currentSpeed + 1);
				addInfusedTag(stack);

				return true;
			}
		} else if (item instanceof ForceRodItem) {
			int currentSpeed = stack.getOrDefault(ForceComponents.TOOL_SPEED, 0);
			if (currentSpeed < ConfigHandler.COMMON.rodSpeedCap.get()) {
				stack.set(ForceComponents.TOOL_SPEED, currentSpeed + 1);
				addInfusedTag(stack);

				return true;
			}
		}
		return false;
	}

	static void addInfusedTag(ItemStack stack) {
		stack.set(ForceComponents.FORCE_INFUSED, true);
	}

	public int fill(FluidStack resource, FluidAction action) {
		FluidStack resourceCopy = resource.copy();

		if (action.execute()) {
			if (tank.getFluid().isEmpty() || FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
				tank.fill(resourceCopy, action);
			}
		}
		return resource.getAmount();
	}

	public Fluid getFluid() {
		if (!tank.isEmpty()) {
			return tank.getFluid().getFluid();
		}
		return null;
	}

	public FluidStack getFluidStack() {
		if (!tank.isEmpty()) {
			return tank.getFluid();
		}
		return null;
	}

	public boolean isWorkAllowed() {
		return hasTool() && hasValidBook();
	}

	public boolean hasValidRecipe() {
		return isWorkAllowed() && ((areAllModifiersEmpty() && canCharge()) || allSlotsMatchRecipe());
	}

	public boolean updateValidRecipe() {
		if (canWork) {
			canWork = false;
			processTime = 0;

			refreshClient();
		}
		return hasValidRecipe();
	}

	public boolean allSlotsMatchRecipe() {
		int requiredForce = 0;
		int requiredPower = 0;

		List<RecipeHolder<InfuseRecipe>> holders = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
		boolean foundMatch = false;

		for (RecipeHolder<InfuseRecipe> holder : holders) {
			InfuseRecipe recipe = holder.value();
			ItemStack centerStack = getFromToolSlot();
			int amountFound = 0;
			for (int i = 0; i < SLOT_TOOL; i++) {
				ItemStack modifier = getModifier(i);
				if (modifier.isEmpty()) continue;

				if (recipe.matchesModifier(new RecipeWrapper(this.handler), modifier, false)) {
					foundMatch = true;
					amountFound++;
					requiredForce += FLUID_COST_PER;
					requiredPower += recipe.getTime() * ENERGY_COST_PER;
				}
			}
			if (amountFound > 0 && amountFound > recipe.getModifier().getLevelCap(centerStack)) {
				return false;
			}
		}

		if (!foundMatch) {
			return false;
		}

		return getFluidAmount() >= requiredForce && getEnergy() >= requiredPower;
	}

	public int getBookTier() {
		if (!getBookInSlot().isEmpty()) {
			return this.getBookInSlot().getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT).tier().ordinal();
		}
		return 0;
	}

	public int getEnergyCostPer() {
		return ENERGY_COST_PER;
	}

	public int getEnergy() {
		return energyStorage.getEnergyStored();
	}

	public int getFluidAmount() {
		return tank.getFluidAmount();
	}

	public void setFluidAmount(int amount) {
		if (amount > 0) {
			if (!tank.getFluid().isEmpty()) {
				tank.getFluid().setAmount(amount);
			}
		} else {
			tank.setFluid(FluidStack.EMPTY);
		}
	}

	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	public void setEnergyStored(int energy) {
		energyStorage.setEnergy(energy);
	}

	public float getFluidPercentage() {
		return (float) tank.getFluidAmount() / (float) tank.getCapacity();
	}

	protected boolean isFluidEqual(FluidStack fluid) {
		return isFluidEqual(fluid.getFluid());
	}

	protected boolean isFluidEqual(Fluid fluid) {
		return tank.getFluid().getFluid().equals(fluid);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(Reference.MOD_ID + ".container.infuser");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
		return new InfuserMenu(id, playerInv, this);
	}

	/******** Fakeout stuff for IRecipe *********************/
	@Override
	public void clearContent() {
	}

	@Override
	public ItemStack removeItem(int arg0, int arg1) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getContainerSize() {
		return 0;
	}

	@Override
	public ItemStack getItem(int arg0) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
					(double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public ItemStack removeItemNoUpdate(int arg0) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int arg0, ItemStack arg1) {
	}

	public static boolean addRecipe(RecipeHolder<InfuseRecipe> holder) {
		Identifier id = holder.id();
		if (HASHES.contains(id.toString())) {
			return false;
		}
		InfuseRecipe recipe = holder.value();
		int thisTier = recipe.getTier().ordinal();
		//by level is for the GUI
		if (!LEVEL_RECIPE_LIST.containsKey(thisTier)) {
			LEVEL_RECIPE_LIST.put(thisTier, new ArrayList<>());
		}
		LEVEL_RECIPE_LIST.get(thisTier).add(id);
		HASHES.add(id.toString());
		ForceCraft.LOGGER.info("Recipe loaded {} -> {} , {}", id.toString(), recipe.getModifier(), recipe.getIngredient());
		return true;
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		super.preRemoveSideEffects(pos, state);
		try (Transaction tx = Transaction.openRoot()) {
			for (int i = 0; i < handler.size(); ++i) {
				if (!handler.getResource(i).isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getResource(i).toStack());
			}
			tx.commit();
		}
	}
}