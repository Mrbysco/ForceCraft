package com.mrbysco.forcecraft.blockentity.infuser;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ForceConfig;
import com.mrbysco.forcecraft.menu.InfuserMenu;
import com.mrbysco.forcecraft.networking.message.StopInfuserSoundPayload;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.ForceTags;
import com.mrbysco.forcecraft.upgrade.ConvertUpgrade;
import com.mrbysco.forcecraft.upgrade.UpgradeBookData;
import com.mrbysco.forcecraft.util.ForceUtil;
import com.mrbysco.forcecraft.util.SpecialUsers;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class InfuserBlockEntity extends BlockEntity implements MenuProvider {
	public static final int SLOT_TOOL = 8;
	public static final int SLOT_GEM = 9;
	public static final int SLOT_BOOK = 10;

	private static final int FLUID_TO_FORCE_RATE = 1; // 1 unit of Liquid Force = 1 Force
	private static final int ENERGY_COST_PER = 20;
	private static final int FLUID_COST_PER = 1000;
	private static final int FLUID_PER_GEM = 250;// If on list!

	private FluidStacksResourceHandler tank = new FluidStacksResourceHandler(1, 50_000) {
		@Override
		protected void onContentsChanged(int index, FluidStack previousContents) {
			super.onContentsChanged(index, previousContents);
			refreshClient();
		}

		@Override
		public boolean isValid(int index, FluidResource resource) {
			return resource.is(ForceTags.Fluids.FORCE);
		}
	};

	private final ItemStacksResourceHandler handler = new ItemStacksResourceHandler(11) {
		@Override
		protected int getCapacity(int index, ItemResource resource) {
			return 1;
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			if (index < SLOT_TOOL) {
				return matchesModifier(resource.toStack());
			} else if (index == SLOT_TOOL) {
				return matchesTool(resource.toStack());
			} else if (index == SLOT_BOOK) {
				return resource.is(ForceRegistry.UPGRADE_TOME.get());
			} else if (index == SLOT_GEM) {
				return resource.is(ForceRegistry.FORCE_GEM.get());
			}
			return true;
		}
	};

	private SimpleEnergyHandler energy = new SimpleEnergyHandler(50_000, 1000);

	public boolean makesSpecialSound = false;
	public int processTime = 0;
	public int maxProcessTime = 20;
	public boolean canWork;
	private final Int2ObjectOpenHashMap<RecipeHolder<InfuseRecipe>> currentRecipes = new Int2ObjectOpenHashMap<>();
	private @Nullable UUID lastUser;

	public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
		super(ForceRegistry.INFUSER_BLOCK_ENTITY.get(), pos, blockState);
	}

	public ItemStacksResourceHandler getItemHandler(@Nullable Direction facing) {
		return handler;
	}

	public FluidStacksResourceHandler getFluidTank(@Nullable Direction facing) {
		return tank;
	}

	public EnergyHandler getEnergyStorage(@Nullable Direction direction) {
		return energy;
	}

	public FluidStacksResourceHandler getTank() {
		return getFluidTank(null);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(Reference.MOD_ID + ".container.infuser");
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		this.lastUser = player.getUUID();
		return new InfuserMenu(containerId, inventory, this);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		processTime = input.getIntOr("processTime", 0);
		maxProcessTime = input.getIntOr("maxProcessTime", 0);
		canWork = input.getBooleanOr("canWork", false);
		handler.deserialize(input.childOrEmpty("ItemHandler"));
		energy.deserialize(input.childOrEmpty("EnergyHandler"));
		tank.deserialize(input.childOrEmpty("Tank"));
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTime", processTime);
		output.putInt("maxProcessTime", maxProcessTime);
		output.putBoolean("canWork", canWork);
		handler.serialize(output.child("ItemHandler"));
		energy.serialize(output.child("EnergyHandler"));
		tank.serialize(output.child("Tank"));
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, InfuserBlockEntity infuser) {
		infuser.processForceGems();

		if (!infuser.canWork) {
			infuser.processTime = 0;
			return;
		}

		if (!infuser.consumeEnergy(ENERGY_COST_PER)) return;

		infuser.processTime++;
		if (level.getGameTime() % 60 == 0) infuser.makeWorkSound();
		if (infuser.processTime < infuser.maxProcessTime) return;

		infuser.finishWork((ServerLevel) level);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, InfuserBlockEntity infuser) {

	}

	private void finishWork(ServerLevel level) {
		if (isWorkAllowed() && lastUser != null && SpecialUsers.isExplodyUser(lastUser)
				&& level.getRandom().nextFloat() < SpecialUsers.EXPLODE_CHANCE) {
			BlockPos pos = getBlockPos();
			level.removeBlock(pos, false);
			level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, Level.ExplosionInteraction.BLOCK);
			return;
		}

		if (isWorkAllowed()) {
			if (areAllModifiersEmpty() && canCharge()) {
				processForceCharging();
			} else if (recipesStillMatch()) {
				processTool(level);
			}
			stopWorkSound();
			playSound(makesSpecialSound ? ForceSounds.INFUSER_SPECIAL_DONE.get() : ForceSounds.INFUSER_DONE.get(), 1.0F, 1.0F);
			makesSpecialSound = false;
		}
		canWork = false;
		processTime = 0;
		refreshClient();
	}

	private void processTool(ServerLevel level) {
		for (int slot = 0; slot < SLOT_TOOL; slot++) {
			RecipeHolder<InfuseRecipe> holder = currentRecipes.get(slot);
			if (holder != null) applyRecipe(level, slot, holder);
		}
	}

	private void applyRecipe(ServerLevel level, int slot, RecipeHolder<InfuseRecipe> holder) {
		InfuseRecipe recipe = holder.value();
		ItemStack tool = getTool();
		ItemStack modifier = handler.getResource(slot).toStack(1);
		UpgradeBookData book = getBook();

		if (modifier.isEmpty() || !recipe.matchesModifier(tool, modifier, book, true)) return;
		if (tank.getAmountAsInt(0) < FLUID_COST_PER) return;
		if (!recipe.getUpgrade().apply(tool, book)) return;

		try (Transaction tx = Transaction.openRoot()) {
			if (tank.extract(0, tank.getResource(0), FLUID_COST_PER, tx) != FLUID_COST_PER) return;
			if (handler.extract(slot, handler.getResource(slot), 1, tx) != 1) return;
			tx.commit();
		}

		ItemStack result = recipe.getUpgrade() instanceof ConvertUpgrade && recipe.hasOutput() ? recipe.getOutput() : tool;
		handler.set(SLOT_TOOL, ItemResource.of(result), result.getCount());

		ItemStack bookStack = handler.getResource(SLOT_BOOK).toStack(1);
		Identifier usedId = recipe.group().isEmpty()
				? holder.id().identifier()
				: Reference.modLoc(recipe.group());
		bookStack.set(ForceComponents.UPGRADE_BOOK,
				book.withInfusion(usedId, recipe.getTier(), recipe.getPoints()));
		handler.set(SLOT_BOOK, ItemResource.of(bookStack), 1);

		refreshClient();
	}

	/**
	 * Process charging a Force Tool up with Force
	 */
	private void processForceCharging() {
		ItemStack tool = getTool();
		int charge = FLUID_COST_PER;

		if (tool.isDamaged()) {
			int repaired = Math.min(tool.getDamageValue(), charge / ForceUtil.FORCE_DMG_RATIO);
			tool.setDamageValue(tool.getDamageValue() - repaired);
			charge -= repaired * ForceUtil.FORCE_DMG_RATIO;
		}
		if (charge > 0) ForceUtil.chargeForce(tool, charge);

		try (Transaction tx = Transaction.openRoot()) {
			if (tank.extract(0, tank.getResource(0), FLUID_COST_PER, tx) != FLUID_COST_PER) return;
			tx.commit();
		}
		handler.set(SLOT_TOOL, ItemResource.of(tool), 1);
	}

	public void startWork(boolean instant) {
		if (!(level instanceof ServerLevel serverLevel)) return;

		refreshMatches(serverLevel);
		processTime = 0;

		if (areAllModifiersEmpty() && canCharge()) {
			maxProcessTime = ForceConfig.COMMON.forceInfusingTime.get();
		} else {
			maxProcessTime = 0;
			for (RecipeHolder<InfuseRecipe> holder : currentRecipes.values()) {
				maxProcessTime += holder.value().getTime();
			}
		}

		canWork = maxProcessTime > 0 && isWorkAllowed();
		if (!canWork) maxProcessTime = 0;

		if (canWork && instant) {
			finishWork(serverLevel);
			return;
		}

		if (canWork) {
			stopWorkSound();
			makesSpecialSound = level.getRandom().nextInt(10) == 0;
			if (makesSpecialSound) playSound(ForceSounds.INFUSER_SPECIAL_BEEP.get(), 1.0F, 1.0F);
			makeWorkSound();
		}
		refreshClient();
	}

	public boolean hasValidRecipe() {
		if (!(level instanceof ServerLevel serverLevel) || !isWorkAllowed()) return false;
		return (areAllModifiersEmpty() && canCharge()) || allSlotsMatchRecipe(serverLevel);
	}

	private void processForceGems() {
		ItemResource gem = handler.getResource(SLOT_GEM);
		if (!gem.is(ForceRegistry.FORCE_GEM.get())) return;

		FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);
		try (Transaction tx = Transaction.openRoot()) {
			if (tank.insert(0, FluidResource.of(force), force.amount(), tx) == force.amount()
					&& handler.extract(SLOT_GEM, gem, 1, tx) == 1) {
				tx.commit();
			}
		}
	}

	private boolean consumeEnergy(int amount) {
		try (Transaction tx = Transaction.openRoot()) {
			if (energy.extract(amount, tx) == amount) {
				tx.commit();
				return true;
			}
		}
		return false;
	}

	public boolean areAllModifiersEmpty() {
		for (int i = 0; i < SLOT_TOOL; i++) {
			if (!handler.getResource(i).isEmpty()) return false;
		}
		return true;
	}

	public boolean hasTool() {
		return !handler.getResource(SLOT_TOOL).isEmpty();
	}

	public boolean hasValidBook() {
		return handler.getResource(SLOT_BOOK).is(ForceRegistry.UPGRADE_TOME.get());
	}

	public boolean isWorkAllowed() {
		return hasTool() && hasValidBook();
	}

	public boolean canCharge() {
		ItemResource tool = handler.getResource(SLOT_TOOL);
		ItemStack stack = tool.toStack(1);
		return stack.is(ForceTags.Items.VALID_INFUSER_CHARGE) && stack.has(ForceComponents.FORCE_INFUSED)
				&& handler.getAmountAsInt(SLOT_TOOL) == 1 && tank.getAmountAsInt(0) >= FLUID_COST_PER;
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		super.preRemoveSideEffects(pos, state);
		if (level == null) return;
		for (int i = 0; i < handler.size(); i++) {
			ItemResource resource = handler.getResource(i);
			if (!resource.isEmpty()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), resource.toStack(handler.getAmountAsInt(i)));
			}
		}
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
			level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), event, SoundSource.BLOCKS, volume, pitch);
	}

	public void stopWorkSound() {
		if (level != null && !level.isClientSide()) {
			BlockPos pos = getBlockPos();
			for (Player player : level.players()) {
				if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 200) {
					PacketDistributor.sendToPlayer(((ServerPlayer) player), new StopInfuserSoundPayload());
				}
			}
		}
	}

	// Recipe Checking
	private Collection<RecipeHolder<InfuseRecipe>> recipes(ServerLevel serverLevel) {
		return serverLevel.recipeAccess().recipeMap().byType(ForceRecipes.INFUSE.get());
	}

	private ItemStack getTool() {
		return handler.getResource(SLOT_TOOL).toStack(1);
	}

	private UpgradeBookData getBook() {
		return handler.getResource(SLOT_BOOK).toStack(1).getOrDefault(ForceComponents.UPGRADE_BOOK, UpgradeBookData.DEFAULT);
	}

	private void refreshMatches(ServerLevel serverLevel) {
		currentRecipes.clear();
		ItemStack tool = getTool();
		if (tool.isEmpty() || !hasValidBook()) return;

		UpgradeBookData book = getBook();
		for (int slot = 0; slot < SLOT_TOOL; slot++) {
			if (handler.getResource(slot).isEmpty()) continue;
			ItemStack modifier = handler.getResource(slot).toStack(1);

			for (RecipeHolder<InfuseRecipe> holder : recipes(serverLevel)) {
				if (holder.value().matchesModifier(tool, modifier, book, false)) {
					currentRecipes.put(slot, holder);
					break;
				}
			}
		}
	}

	private boolean recipesStillMatch() {
		if (currentRecipes.isEmpty()) return false;
		ItemStack tool = getTool();
		UpgradeBookData book = getBook();
		for (int slot = 0; slot < SLOT_TOOL; slot++) {
			RecipeHolder<InfuseRecipe> holder = currentRecipes.get(slot);
			if (holder == null) continue;
			ItemStack modifier = handler.getResource(slot).toStack(1);
			if (modifier.isEmpty() || !holder.value().matchesModifier(tool, modifier, book, false)) return false;
		}
		return true;
	}

	public boolean allSlotsMatchRecipe(ServerLevel serverLevel) {
		refreshMatches(serverLevel);
		if (currentRecipes.isEmpty()) return false;

		ItemStack tool = getTool();
		int requiredForce = 0;
		int requiredPower = 0;
		for (int slot = 0; slot < SLOT_TOOL; slot++) {
			RecipeHolder<InfuseRecipe> holder = currentRecipes.get(slot);
			if (holder == null) continue;

			int count = 0;
			for (int other = 0; other < SLOT_TOOL; other++) {
				if (currentRecipes.get(other) == holder) count++;
			}
			if (count > holder.value().getUpgrade().levelCap(tool)) return false;

			requiredForce += FLUID_COST_PER;
			requiredPower += holder.value().getTime() * ENERGY_COST_PER;
		}
		return tank.getAmountAsInt(0) >= requiredForce && energy.getAmountAsInt() >= requiredPower;
	}

	private boolean matchesModifier(ItemStack stack) {
		if (!(level instanceof ServerLevel serverLevel)) {
			// client has no recipes, so reject what can never be a modifier
			return !stack.is(ForceTags.Items.VALID_INFUSER_TOOLS)
					&& !stack.is(ForceTags.Items.VALID_INFUSER_CHARGE);
		}
		ItemStack tool = getTool();
		for (RecipeHolder<InfuseRecipe> holder : recipes(serverLevel)) {
			InfuseRecipe recipe = holder.value();
			if (recipe.getIngredient().test(stack) && (tool.isEmpty() || recipe.getCenter().test(tool))) return true;
		}
		return false;
	}

	private boolean matchesTool(ItemStack stack) {
		if (!(level instanceof ServerLevel serverLevel)) return true;
		for (RecipeHolder<InfuseRecipe> holder : recipes(serverLevel)) {
			if (holder.value().matchesTool(stack, true)) return true;
		}
		return stack.is(ForceTags.Items.VALID_INFUSER_CHARGE);
	}

	// Helper methods

	public ItemStack getBookInSlot() {
		return handler.getResource(SLOT_BOOK).toStack(1);
	}

	public int getBookTier() {
		return hasValidBook() ? getBook().tier().ordinal() + 1 : 0;
	}

	public int getEnergyCostPer() {
		return ENERGY_COST_PER;
	}

	public int getFluidAmount() {
		return tank.getAmountAsInt(0);
	}

	public int getEnergyStored() {
		return energy.getAmountAsInt();
	}

	public int getEnergyCapacity() {
		return energy.getCapacityAsInt();
	}

	public FluidResource getFluidResource() {
		return tank.getResource(0);
	}

	private void refreshClient() {
		setChanged();
		BlockState state = level.getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		CompoundTag tag = new CompoundTag();
		try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(ForceCraft.LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(problemreporter$scopedcollector, lookupProvider);
			this.saveAdditional(output);
			tag.merge(output.buildResult());
		}
		return tag;
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(ForceCraft.LOGGER)) {
			HolderLookup.Provider lookupProvider = this.level != null ? this.level.registryAccess() : VanillaRegistries.createLookup();
			TagValueOutput output = TagValueOutput.createWithContext(problemreporter$scopedcollector, lookupProvider);
			this.saveAdditional(output);
			tag.merge(output.buildResult());
		}
		return tag;
	}

}
