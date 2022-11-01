package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.energy.ForceEnergyStorage;
import com.mrbysco.forcecraft.capabilities.forcerod.IForceRodModifier;
import com.mrbysco.forcecraft.capabilities.pack.PackItemStackHandler;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.config.ConfigHandler;
import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.ForcePackItem;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
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
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.storage.StorageManager;
import com.mrbysco.forcecraft.util.EnchantUtils;
import com.mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_FORCEROD;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class InfuserBlockEntity extends BlockEntity implements MenuProvider, Container {

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

	protected Map<Integer, InfuseRecipe> currentRecipes = new HashMap<>();

	protected FluidTank tank = new FluidTank(50000) {
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (!isFluidEqual(resource)) {
				return FluidStack.EMPTY;
			}
			if (action.simulate()) {
				int amount = tank.getFluidAmount() - resource.getAmount() < 0 ? tank.getFluidAmount() : resource.getAmount();
				return new FluidStack(tank.getFluid(), amount);
			}
			return super.drain(resource.getAmount(), action);
		}

		@Override
		protected void onContentsChanged() {
			refreshClient();
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return super.drain(maxDrain, action);
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			return stack.getFluid().is(ForceTags.FORCE);
		}
	};
	private LazyOptional<IFluidHandler> tankHolder = LazyOptional.of(() -> tank);

	public final ItemStackHandler handler = new ItemStackHandler(11) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			if (slot == SLOT_GEM) {
				return 64;
			}
			return 1;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (slot < SLOT_TOOL) {
				return matchesModifier(stack);
				// is valid modifier TODO : fixing this check will fix placing and shift-clicking
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
	private LazyOptional<IItemHandler> handlerHolder = LazyOptional.of(() -> handler);

	public ForceEnergyStorage energyStorage = new ForceEnergyStorage(64000, 1000);
	private LazyOptional<ForceEnergyStorage> energyHolder = LazyOptional.of(() -> energyStorage);

	private NonNullList<ItemStack> infuserContents = NonNullList.create();


	public InfuserBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public InfuserBlockEntity(BlockPos pos, BlockState state) {
		this(ForceRegistry.INFUSER_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		this.processTime = compound.getInt("processTime");
		this.maxProcessTime = compound.getInt("maxProcessTime");
		//Items
		canWork = compound.getBoolean("canWork");
		handler.deserializeNBT(compound.getCompound("ItemStackHandler"));
		ContainerHelper.loadAllItems(compound, this.infuserContents);
		energyStorage.setEnergy(compound.getInt("EnergyHandler"));
		tank.readFromNBT(compound);

		super.load(compound);
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		compound.putInt("processTime", this.processTime);
		compound.putInt("maxProcessTime", this.maxProcessTime);
		//Items
		compound.putBoolean("canWork", canWork);
		compound.put("ItemStackHandler", handler.serializeNBT());
		compound.putInt("EnergyHandler", energyStorage.getEnergyStored());
		ContainerHelper.saveAllItems(compound, this.infuserContents);
		tank.writeToNBT(compound);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, InfuserBlockEntity infuser) {
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
			//even if tool or book slot become empty, dont auto run next insert
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
			if (level.random.nextInt(10) == 0) {
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
		level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), event, SoundSource.BLOCKS, volume, pitch);
	}

	public void stopWorkSound() {
		if (!level.isClientSide) {
			BlockPos pos = getBlockPos();
			for (Player playerentity : level.players()) {
				if (playerentity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 200) {
					PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) playerentity), new com.mrbysco.forcecraft.networking.message.StopInfuserSoundMessage());
				}
			}
		}
	}

	private void setMaxTimeFromRecipes() {
		maxProcessTime = 0;
		if (!this.getBookInSlot().isEmpty()) { //Make sure it doesn't run if the book is missing
			List<InfuseRecipe> recipes = new ArrayList<>(getMatchingRecipes().values());
			if (!recipes.isEmpty()) {
				for (InfuseRecipe recipeCurrent : recipes) {
//					ForceCraft.LOGGER.info(recipeCurrent.getId() + " increase  "+ recipeCurrent.getTime());
					maxProcessTime += recipeCurrent.getTime();
				}
			}
		}
	}

	protected Map<Integer, InfuseRecipe> getMatchingRecipes() {
		if (getBookInSlot().isEmpty()) return new HashMap<>();
		if (!currentRecipes.isEmpty() && recipesStillMatch()) return currentRecipes;
		else {
			Map<Integer, InfuseRecipe> matchingRecipes = new HashMap<>();
			for (int i = 0; i < SLOT_TOOL; i++) {
				ItemStack modifier = getModifier(i);
				if (modifier.isEmpty()) continue;

				List<InfuseRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
				for (InfuseRecipe recipe : recipes) {
					if (recipe.matchesModifier(this, modifier, false)) {
						matchingRecipes.put(i, recipe);
						break;
					}
				}
			}
			return currentRecipes = matchingRecipes;
		}
	}

	protected boolean matchesModifier(ItemStack stack) {
		if (level != null) {
			List<InfuseRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
			for (InfuseRecipe recipe : recipes) {
				if (recipe.matchesModifier(this, stack)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean matchesTool(ItemStack toolStack) {
		if (level != null) {
			List<InfuseRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
			for (InfuseRecipe recipe : recipes) {
				if (recipe.matchesTool(toolStack, true)) {
					return true;
				}
			}
		}
		return toolStack.is(ForceTags.VALID_INFUSER_CHARGE);
	}

	protected boolean recipesStillMatch() {
		for (HashMap.Entry<Integer, InfuseRecipe> entry : currentRecipes.entrySet()) {
			ItemStack modifier = getModifier(entry.getKey());
			if (!entry.getValue().matchesModifier(this, modifier, false)) {
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
		force.charge(FLUID_CHARGE);
		tank.drain(FLUID_CHARGE, FluidAction.EXECUTE);
		force.write(tool);
	}

	private void processTool() {
		for (HashMap.Entry<Integer, InfuseRecipe> entry : currentRecipes.entrySet()) {
			ItemStack modifier = getModifier(entry.getKey());
			InfuseRecipe recipe = entry.getValue();
			if (recipe.matchesModifier(this, modifier, true)) {
				ItemStack tool = getFromToolSlot();
				boolean success = applyModifier(tool, modifier, recipe);
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

		// TODO: is this notfiy needed?
		level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		load(packet.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			return handlerHolder.cast();
		}
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return tankHolder.cast();
		}
		if (capability == ForgeCapabilities.ENERGY) {
			return energyHolder.cast();
		}

		return super.getCapability(capability, facing);
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
		CompoundTag tag = stack.getTag();
		return stack.is(ForceTags.VALID_INFUSER_CHARGE) && tag != null && tag.contains("ForceInfused") && stack.getCount() == 1;
	}

	/**
	 * Loop on all modifiers and apply first one that matches the input test.
	 *
	 * @param tool
	 * @param modifier
	 * @return
	 */
	private boolean applyModifier(ItemStack tool, ItemStack modifier, InfuseRecipe recipe) {
		UpgradeBookData bd = new UpgradeBookData(this.getBookInSlot());
		//if the recipe level does not exceed what the book has
		//test the ingredient of this recipe, if it matches me

		if (recipe.resultModifier.apply(tool, modifier, bd)) {
			bd.onRecipeApply(recipe, getBookInSlot());

			if (recipe.resultModifier == InfuserModifierType.ITEM && recipe.hasOutput()) {
				//overwrite / convert item
				handler.setStackInSlot(SLOT_TOOL, recipe.getResultItem().copy());
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
		List<MobEffectInstance> effects = PotionUtils.getMobEffects(mod);
		for (MobEffectInstance e : effects) {
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
			IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
			if (rodCap != null && !rodCap.hasLight()) {
				rodCap.setLight(true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasLight()) {
				modifierCap.setLight(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	private static boolean addCamoModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceRodItem) {
			IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
			if (rodCap != null && !rodCap.hasCamoModifier()) {
				rodCap.setCamoModifier(true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier toolCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (toolCap != null && !toolCap.hasCamo()) {
				toolCap.setCamo(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	private static boolean addSightModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceRodItem) {
			IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
			if (rodCap != null && !rodCap.hasSightModifier()) {
				rodCap.setSightModifier(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean addWingModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceSwordItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasWing()) {
				modifierCap.setWing(true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasWing()) {
				modifierCap.setWing(true);
				addInfusedTag(stack);
				return true;
			}
		}

		return false;
	}

	static boolean addBaneModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceSwordItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasBane()) {
				modifierCap.setBane(1);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasBane()) {
				modifierCap.setBane(1);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap != null && !modifierCap.hasBane()) {
					modifierCap.setBane(1);
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addBleedingModifier(ItemStack stack) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.bleedCap.get();
		if (item instanceof ForceSwordItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && modifierCap.getBleedLevel() < MAX_CAP) {
				modifierCap.incrementBleed();
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && modifierCap.getBleedLevel() < MAX_CAP) {
				modifierCap.incrementBleed();
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getBleedLevel() < MAX_CAP) {
					modifierCap.incrementBleed();
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addEnderModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceRodItem) {
			IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
			if (rodCap != null && !rodCap.isRodofEnder()) {
				rodCap.setEnderModifier(true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceSwordItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasEnder()) {
				modifierCap.setEnder(true);
				addInfusedTag(stack);
				return true;
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasEnder()) {
				modifierCap.setEnder(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean addFreezingModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasFreezing()) {
				modifierCap.setFreezing(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean addHealingModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceRodItem) {
			IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
			if (rodCap != null) {
				int MAX_CAP = ConfigHandler.COMMON.healingCap.get();
				if (rodCap.getHealingLevel() < MAX_CAP) {
					rodCap.incrementHealing();
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addLumberjackModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceAxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasLumberjack()) {
				modifierCap.setLumberjack(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean addRainbowModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceShearsItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				modifierCap.setRainbow(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean addTreasureModifier(ItemStack stack) {
		if (stack.getItem() instanceof ForceSwordItem || stack.getItem() instanceof ForceAxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null && !modifierCap.hasTreasure()) {
				modifierCap.setTreasure(true);
				addInfusedTag(stack);
				return true;
			}
		}
		return false;
	}

	static boolean upgradeBag(ItemStack stack, UpgradeBookData bd) {
		if (stack.getItem() instanceof ForcePackItem) {
			PackItemStackHandler handler = StorageManager.getOrCreatePack(stack).getInventory();

			if (handler.canUpgrade(bd)) {
				handler.applyUpgrade();

				CompoundTag tag = stack.getOrCreateTag();
				tag.putInt(ForcePackItem.SLOTS_USED, ItemHandlerUtils.getUsedSlots(handler));
				tag.putInt(ForcePackItem.SLOTS_TOTAL, handler.getSlotsInUse());
				tag.putInt("BookTier", handler.getUpgrades());

				stack.setTag(tag);
				return true;
			}
			return false;
		}
		return false;
	}

	static boolean addSturdyModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceSwordItem || item instanceof ForceAxeItem || item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceRodItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSturdyLevel() < ConfigHandler.COMMON.sturdyToolCap.get()) {
					modifierCap.incrementSturdy();
					EnchantUtils.incrementLevel(stack, Enchantments.UNBREAKING);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSturdyLevel() == 0) {
					modifierCap.incrementSturdy();
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addLuckModifier(ItemStack stack) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.luckCap.get();
		if (item instanceof ForcePickaxeItem || item instanceof ForceShovelItem || item instanceof ForceAxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getLuckLevel() < MAX_CAP) {
					modifierCap.incrementLuck();
					EnchantUtils.incrementLevel(stack, Enchantments.BLOCK_FORTUNE);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceSwordItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getLuckLevel() < MAX_CAP) {
					modifierCap.incrementLuck();
					EnchantUtils.incrementLevel(stack, Enchantments.MOB_LOOTING);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getLuckLevel() < MAX_CAP) {
					modifierCap.incrementLuck();
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getLuckLevel() < MAX_CAP) {
					modifierCap.incrementLuck();
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addDamageModifier(ItemStack stack) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.damageCap.get();
		if (item instanceof ForceSwordItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSharpLevel() < MAX_CAP) {
					modifierCap.incrementSharp();
					EnchantUtils.incrementLevel(stack, Enchantments.SHARPNESS);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSharpLevel() < MAX_CAP) {
					modifierCap.incrementSharp();
					EnchantUtils.incrementLevel(stack, Enchantments.POWER_ARROWS);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSharpLevel() < 1) {
					modifierCap.incrementSharp();
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addSilkTouchModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceAxeItem || item instanceof ForceShovelItem || item instanceof ForcePickaxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (!modifierCap.hasSilk()) {
					modifierCap.setSilk(true);
					stack.enchant(Enchantments.SILK_TOUCH, 1);
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addForceModifier(ItemStack stack) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.forceCap.get();
		if (item instanceof ForceSwordItem || item instanceof ForceAxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getForceLevel() < MAX_CAP) {
					modifierCap.incrementForce();
					EnchantUtils.incrementLevel(stack, Enchantments.KNOCKBACK);
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addHeatModifier(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (!modifierCap.hasHeat()) {
					modifierCap.setHeat(true);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceSwordItem || item instanceof ForceAxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (!modifierCap.hasHeat()) {
					stack.enchant(Enchantments.FIRE_ASPECT, 1);
					modifierCap.setHeat(true);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (!modifierCap.hasHeat()) {
					modifierCap.setHeat(true);
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static boolean addSpeedModifier(ItemStack stack) {
		Item item = stack.getItem();
		int MAX_CAP = ConfigHandler.COMMON.speedCap.get();
		if (item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceAxeItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSpeedLevel() < MAX_CAP) {
					modifierCap.incrementSpeed();
					EnchantUtils.incrementLevel(stack, Enchantments.BLOCK_EFFICIENCY);
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceBowItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSpeedLevel() < 1) {
					modifierCap.incrementSpeed();
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceArmorItem) {
			IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSpeedLevel() < 1) {
					modifierCap.incrementSpeed();
					addInfusedTag(stack);
					return true;
				}
			}
		} else if (item instanceof ForceRodItem) {
			IForceRodModifier modifierCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
			if (modifierCap != null) {
				if (modifierCap.getSpeedLevel() < ConfigHandler.COMMON.rodSpeedCap.get()) {
					modifierCap.incrementSpeed();
					addInfusedTag(stack);
					return true;
				}
			}
		}
		return false;
	}

	static void addInfusedTag(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("ForceInfused")) {
			tag.putBoolean("ForceInfused", true);
			stack.setTag(tag);
		}
	}

	public int fill(FluidStack resource, FluidAction action) {
		FluidStack resourceCopy = resource.copy();

		if (action.execute()) {
			if (tank.getFluid().isEmpty() || tank.getFluid().isFluidEqual(resource)) {
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

		List<InfuseRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ForceRecipes.INFUSER_TYPE.get());
		boolean foundMatch = false;

		for (InfuseRecipe recipe : recipes) {
			ItemStack centerStack = getFromToolSlot();
			int amountFound = 0;
			for (int i = 0; i < SLOT_TOOL; i++) {
				ItemStack modifier = getModifier(i);
				if (modifier.isEmpty()) continue;

				if (recipe.matchesModifier(this, modifier, false)) {
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
			return new UpgradeBookData(this.getBookInSlot()).getTier().ordinal();
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

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.tankHolder.invalidate();
		this.handlerHolder.invalidate();
		this.energyHolder.invalidate();
	}
}