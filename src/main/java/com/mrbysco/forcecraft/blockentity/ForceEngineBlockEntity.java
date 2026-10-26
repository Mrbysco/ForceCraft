package com.mrbysco.forcecraft.blockentity;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.block.ForceEngineBlock;
import com.mrbysco.forcecraft.menu.ForceEngineMenu;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public class ForceEngineBlockEntity extends BlockEntity implements MenuProvider {

	private final FluidStacksResourceHandler tank = new FluidStacksResourceHandler(2, 10_000) {
		@Override
		protected void onContentsChanged(int index, FluidStack previousContents) {
			super.onContentsChanged(index, previousContents);
			refreshClient();
		}

		@Override
		public boolean isValid(int index, FluidResource resource) {
			if (index == 0) { // Fuel
				return resource.is(ForceTags.Fluids.FORCE) || resource.is(FluidTags.LAVA) ||
						resource.is(ForceTags.Fluids.FUEL) || resource.is(ForceTags.Fluids.BIOFUEL);
			} else { // Throttle
				return resource.is(Tags.Fluids.WATER) || resource.is(Tags.Fluids.MILK);
			}
		}
	};

	public final ItemStacksResourceHandler inputHandler = new ItemStacksResourceHandler(2) {
		@Override
		protected int getCapacity(int index, ItemResource resource) {
			if (ItemAccess.forStack(resource.toStack(1)).getCapability(Capabilities.Fluid.ITEM) != null) {
				if (resource.getMaxStackSize() > 1) {
					return 1;
				}
			}
			return super.getCapacity(index, resource);
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			ItemStack stack = resource.toStack(1);
			ResourceHandler<FluidResource> resourceHandler = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
			if (index == 0) {
				if (resourceHandler != null) {
					FluidResource fluidResource = resourceHandler.getResource(0);
					if (fluidResource.isEmpty()) return true;
					return fluidResource.is(ForceTags.Fluids.FORCE) || fluidResource.is(FluidTags.LAVA) ||
							fluidResource.is(ForceTags.Fluids.FUEL) || fluidResource.is(ForceTags.Fluids.BIOFUEL);
				}
				return stack.is(ForceTags.Items.GEMS_FORCE) || stack.is(Tags.Items.NETHER_STARS);
			} else if (index == 1) {
				if (resourceHandler != null) {
					FluidResource fluidResource = resourceHandler.getResource(0);
					if (fluidResource.isEmpty()) return true;
					return fluidResource.is(Tags.Fluids.WATER) || fluidResource.is(Tags.Fluids.MILK);
				}
				return false;
			} else {
				return false;
			}
		}
	};

	public final ItemStacksResourceHandler outputHandler = new ItemStacksResourceHandler(2) {
		@Override
		protected int getCapacity(int index, ItemResource resource) {
			if (ItemAccess.forStack(resource.toStack(1)).getCapability(Capabilities.Fluid.ITEM) != null) {
				if (resource.getMaxStackSize() > 1) {
					return 1;
				}
			}
			return super.getCapacity(index, resource);
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			return false;
		}
	};
	private static final int FLUID_PER_GEM = 250;

	public int processTime = 0;
	public int maxProcessTime = 20;
	public int throttleTime = 0;
	public int maxThrottleTime = 10;

	private Fluid cachedFuel;
	private Fluid cachedThrottle;

	public float generating = 0;

	public ForceEngineBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.processTime = input.getIntOr("processTime", 0);
		this.maxProcessTime = input.getIntOr("maxProcessTime", 0);
		this.throttleTime = input.getIntOr("throttleTime", 0);
		this.maxThrottleTime = input.getIntOr("maxThrottleTime", 0);

		this.generating = input.getFloatOr("generating", 0);

		this.tank.deserialize(input.childOrEmpty("tanks"));
		this.inputHandler.deserialize(input.childOrEmpty("inputHandler"));
		this.outputHandler.deserialize(input.childOrEmpty("outputHandler"));
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);

		output.putInt("processTime", this.processTime);
		output.putInt("maxProcessTime", this.maxProcessTime);
		output.putInt("throttleTime", this.throttleTime);
		output.putInt("maxThrottleTime", this.maxThrottleTime);
		output.putFloat("generating", this.generating);

		this.tank.serialize(output.child("tanks"));
		this.inputHandler.serialize(output.child("inputHandler"));
		this.outputHandler.serialize(output.child("outputHandler"));
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(Reference.MOD_ID + ".container.force_engine");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
		return new ForceEngineMenu(id, playerInv, this);
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState state, ForceEngineBlockEntity forceEngine) {
		boolean working = false;
		if (!forceEngine.inputHandler.getResource(0).isEmpty()) {
			forceEngine.processFuelSlot();
			forceEngine.refreshClient();
		}
		if (!forceEngine.inputHandler.getResource(1).isEmpty()) {
			forceEngine.processThrottleSlot();
			forceEngine.refreshClient();
		}

		if (forceEngine.isPowered() && forceEngine.canWork()) {
			forceEngine.checkFluids();
			if (forceEngine.tank.getAmountAsInt(0) > 0) {
				working = true;
				forceEngine.processTime++;
				forceEngine.insertPower();

				if (forceEngine.processTime >= forceEngine.maxProcessTime) {
					try (var tx = Transaction.openRoot()) {
						FluidResource fluidResource = forceEngine.tank.getResource(0);
						if (forceEngine.tank.extract(0, fluidResource, 1, tx) == 1) {
							tx.commit();
						}
					}
					forceEngine.processTime = 0;
				}
			}
			if (forceEngine.tank.getAmountAsInt(1) > 0) {
				forceEngine.throttleTime++;

				if (forceEngine.throttleTime >= forceEngine.maxThrottleTime) {
					try (var tx = Transaction.openRoot()) {
						FluidResource fluidResource = forceEngine.tank.getResource(1);
						if (forceEngine.tank.extract(1, fluidResource, 1, tx) == 1) {
							tx.commit();
						}
					}
					forceEngine.throttleTime = 0;
				}
			}

			forceEngine.refreshClient();
		} else {
			if (forceEngine.processTime != 0) forceEngine.processTime = 0;
		}

		forceEngine.updateGenerating(working);
	}

	/**
	 * Process slots
	 */
	private void processFuelSlot() {
		ItemResource inputResource = inputHandler.getResource(0);
		ItemResource outputResource = outputHandler.getResource(0);

		if (inputResource.is(ForceTags.Items.GEMS_FORCE)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);
			try (var tx = Transaction.openRoot()) {
				if (tank.insert(0, FluidResource.of(force), force.amount(), tx) == force.amount()) {
					if (inputHandler.extract(inputResource, 1, tx) == 1) {
						tx.commit();
					}
				}
			}
		} else if (inputResource.is(Tags.Items.NETHER_STARS)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM * 10);

			try (var tx = Transaction.openRoot()) {
				if (tank.insert(1, FluidResource.of(force), force.amount(), tx) == force.amount()) {
					if (inputHandler.extract(inputResource, 1, tx) == 1) {
						if (outputHandler.getResource(0).isEmpty()) {
							outputHandler.set(0, ItemResource.of(ForceRegistry.INERT_CORE.get()), 1);
						} else {
							if (outputHandler.insert(outputResource, 1, tx) != 1) return;
						}
						tx.commit();
					}
				}
			}
		} else {
			if (!outputHandler.getResource(0).isEmpty()) return;

			try (var tx = Transaction.openRoot()) {
				var itemAccess = ItemAccess.forHandlerIndex(inputHandler, 0).oneByOne();
				var fh = itemAccess.getCapability(Capabilities.Fluid.ITEM);
				if (fh == null) return;

				int amount = fh.getAmountAsInt(0);
				if (ResourceHandlerUtil.move(fh, tank, (resource) -> true, amount, tx) != amount) return;
				tx.commit();
			}

			ItemResource emptied = inputHandler.getResource(0);
			if (!emptied.isEmpty()) {
				outputHandler.set(0, emptied, 1);
				try (var tx = Transaction.openRoot()) {
					if (inputHandler.extract(0, emptied, 1, tx) == 1) tx.commit();
				}
			}
		}
	}

	private void processThrottleSlot() {
		if (!outputHandler.getResource(1).isEmpty()) return;

		try (var tx = Transaction.openRoot()) {
			var itemAccess = ItemAccess.forHandlerIndex(inputHandler, 1).oneByOne();
			var fh = itemAccess.getCapability(Capabilities.Fluid.ITEM);
			if (fh == null) return;

			int amount = fh.getAmountAsInt(0);
			if (amount <= 0) return;
			if (ResourceHandlerUtil.move(fh, tank, (resource) -> true, amount, tx) != amount) return;
			tx.commit();
		}

		ItemResource emptied = inputHandler.getResource(1);
		if (!emptied.isEmpty()) {
			outputHandler.set(1, emptied, 1);
			try (var tx = Transaction.openRoot()) {
				if (inputHandler.extract(1, emptied, 1, tx) == 1) tx.commit();
			}
		}
	}

	/**
	 * Insert power
	 */
	public void insertPower() {
		BlockPos offsetPos = worldPosition.relative(getFacing());
		if (level != null) {
			BlockEntity tile = level.getBlockEntity(offsetPos);
			if (tile != null) {
				EnergyHandler cap = level.getCapability(Capabilities.Energy.BLOCK, offsetPos, getFacing().getOpposite());
				if (cap != null) {
					if (cap.getAmountAsInt() < cap.getCapacityAsInt()) {
						try (var tx = Transaction.openRoot()) {
							if (cap.insert((int) generating, tx) == (int) generating) {
								tx.commit();
							}
						}
					}
				}
			}
		}
	}

	private void updateGenerating(boolean generating) {
		if (level == null) return;
		BlockState currentState = getBlockState();
		if (currentState.getBlock() instanceof ForceEngineBlock
				&& currentState.getValue(ForceEngineBlock.GENERATING) != generating) {
			level.setBlock(worldPosition, currentState.setValue(ForceEngineBlock.GENERATING, generating), 3);
		}
	}

	public void checkFluids() {
		if (cachedFuel == null || !tank.getResource(0).is(cachedFuel)) {
			this.cachedFuel = tank.getResource(0).getFluid();
			reevaluateValues();
		}
		if (cachedThrottle == null || !tank.getResource(1).is(cachedThrottle)) {
			this.cachedThrottle = tank.getResource(1).getFluid();
			reevaluateValues();
		}
	}

	public void reevaluateValues() {
		if (cachedFuel != null) {
			FluidResource fuelResource = tank.getResource(0);
			FluidResource throttleResource = tank.getResource(1);

			processTime = 0;
			maxProcessTime = getTimePerFuelMB(fuelResource);

			generating = getPowerForFluid(fuelResource);

			throttleTime = 0;
			maxThrottleTime = getTimePerThrottleMB(throttleResource);
		}
		refreshClient();
	}

	private float getThrottleValue() {
		FluidResource throttleResource = tank.getResource(1);
		if (!throttleResource.isEmpty()) {
			if (throttleResource.is(Tags.Fluids.MILK)) {
				return 2.5F;
			} else if (throttleResource.is(Tags.Fluids.WATER)) {
				return 2.0F;
			}
		}
		return 1.0F;
	}

	public int getPowerForFluid(FluidResource fluidResource) {
		if (!fluidResource.isEmpty()) {
			float throttleValue = getThrottleValue();
			if (fluidResource.is(ForceTags.Fluids.FORCE)) {
				return (int) (20F * throttleValue);
			} else if (fluidResource.is(FluidTags.LAVA)) {
				return (int) (5F * throttleValue);
			} else if (fluidResource.is(ForceTags.Fluids.FUEL)) {
				return (int) (10F * throttleValue);
			} else if (fluidResource.is(ForceTags.Fluids.BIOFUEL)) {
				return (int) (15F * throttleValue);
			}
		}
		return 0;
	}

	public int getTimePerFuelMB(FluidResource fluidResource) {
		if (!fluidResource.isEmpty()) {
			if (fluidResource.is(ForceTags.Fluids.FORCE)) {
				return 20;
			} else if (fluidResource.is(FluidTags.LAVA)) {
				return 20;
			} else if (fluidResource.is(ForceTags.Fluids.FUEL)) {
				return 20;
			} else if (fluidResource.is(ForceTags.Fluids.BIOFUEL)) {
				return 20;
			}
		}
		return 0;
	}

	public int getTimePerThrottleMB(FluidResource fluidResource) {
		if (!fluidResource.isEmpty()) {
			if (fluidResource.is(Tags.Fluids.MILK)) {
				return 5;
			} else if (fluidResource.is(Tags.Fluids.WATER)) {
				return 5;
			}
		}
		return 0;
	}

	public Direction getFacing() {
		if (getBlockState().getBlock() instanceof ForceEngineBlock) {
			return getBlockState().getValue(ForceEngineBlock.FACING);
		}
		return Direction.NORTH;
	}

	public boolean isPowered() {
		return getBlockState().getBlock() instanceof ForceEngineBlock && getBlockState().getValue(ForceEngineBlock.POWERED);
	}

	public ItemStacksResourceHandler getItemHandler(@Nullable Direction facing) {
		return inputHandler;
	}

	public FluidStacksResourceHandler getFluidTank(@Nullable Direction facing) {
		return tank;
	}

	public FluidStacksResourceHandler getTank() {
		return getFluidTank(null);
	}

	public boolean canWork() {
		if (level != null) {
			BlockPos offsetPos = worldPosition.relative(getFacing());
			BlockEntity blockEntity = level.getBlockEntity(offsetPos);
			if (blockEntity != null) {
				EnergyHandler cap = level.getCapability(Capabilities.Energy.BLOCK, offsetPos, getFacing().getOpposite());
				if (cap != null) {
					return cap.getAmountAsInt() < cap.getCapacityAsInt() && !tank.getResource(0).isEmpty();
				}
			}
		}
		return false;
	}

	private void refreshClient() {
		setChanged();
		if (level != null) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
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
