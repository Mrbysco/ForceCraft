package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blocks.engine.ForceEngineBlock;
import com.mrbysco.forcecraft.menu.engine.ForceEngineMenu;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
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

	private static final int MAX_FLUID = 10000;

	protected FluidStacksResourceHandler tankFuel = new FluidStacksResourceHandler(1, MAX_FLUID) {
		@Override
		protected void onContentsChanged(int index, FluidStack previousContents) {
			super.onContentsChanged(index, previousContents);
			refreshClient();
		}

		@Override
		public boolean isValid(int index, FluidResource resource) {
			return resource.is(ForceTags.FORCE) || resource.is(FluidTags.LAVA) ||
					resource.is(ForceTags.FUEL) || resource.is(ForceTags.BIOFUEL);
		}
	};

	protected FluidStacksResourceHandler tankThrottle = new FluidStacksResourceHandler(1, MAX_FLUID) {

		@Override
		protected void onContentsChanged(int index, FluidStack previousContents) {
			super.onContentsChanged(index, previousContents);
			refreshClient();
		}

		@Override
		public boolean isValid(int index, FluidResource resource) {
			return resource.is(Fluids.WATER) || resource.is(ForceTags.MILK);
		}
	};

//	private final FluidHandlerWrapper tankWrapper = new FluidHandlerWrapper(tankThrottle, tankFuel);

	public final ItemStacksResourceHandler inputHandler = new ItemStacksResourceHandler(2) {
		@Override
		protected int getCapacity(int index, ItemResource resource) {
//			if (resource.getCapability(Capabilities.Fluid.ITEM) != null) {
//				if (stack.getMaxStackSize() > 1) {
//					return 1;
//				}
//			}
			return super.getCapacity(index, resource);
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			ItemStack stack = resource.toStack(1);
			ResourceHandler<FluidResource> resourceHandler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));
			if (index == 0) {
				if (resourceHandler != null) {
					FluidResource fluidResource = resourceHandler.getResource(0);
					if (!fluidResource.isEmpty()) {
						return fluidResource.is(ForceTags.FORCE) || fluidResource.is(FluidTags.LAVA) ||
								fluidResource.is(ForceTags.FUEL) || fluidResource.is(ForceTags.BIOFUEL);
					}
				}
				return stack.is(ForceTags.FORCE_GEM) || stack.is(Tags.Items.NETHER_STARS) ||
						(resourceHandler != null && resourceHandler.getResource(0).is(ForceTags.FORCE));
			} else if (index == 1) {
				if (resourceHandler != null) {
					FluidResource fluidResource = resourceHandler.getResource(0);
					if (!fluidResource.isEmpty()) {
						return fluidResource.is(Fluids.WATER) || fluidResource.is(ForceTags.MILK);
					}
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
//			if (resource.getCapability(Capabilities.Fluid.ITEM) != null) {
//				if (stack.getMaxStackSize() > 1) {
//					return 1;
//				}
//			}
			return super.getCapacity(index, resource);
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			return false;
		}
	};

//	private final ItemStackHandlerWrapper stackWrapper = new ItemStackHandlerWrapper(inputHandler, outputHandler);

	private static final int FLUID_PER_GEM = 500;

	public int processTime = 0;
	public int maxProcessTime = 20;
	public int throttleTime = 0;
	public int maxThrottleTime = 10;

	private Fluid cachedFuel;
	private Fluid cachedThrottle;

	public float generating = 0;

	public ForceEngineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public ForceEngineBlockEntity(BlockPos pos, BlockState state) {
		this(ForceRegistry.FORCE_ENGINE_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.processTime = input.getIntOr("processTime", 0);
		this.maxProcessTime = input.getIntOr("maxProcessTime", 0);
		this.throttleTime = input.getIntOr("throttleTime", 0);
		this.maxThrottleTime = input.getIntOr("maxThrottleTime", 0);

		this.generating = input.getFloatOr("generating", 0);

		//Caps
		this.inputHandler.deserialize(input.childOrEmpty("inputHandler"));
		this.outputHandler.deserialize(input.childOrEmpty("outputHandler"));
		this.tankThrottle.deserialize(input.childOrEmpty("throttleTank"));
		this.tankFuel.deserialize(input.childOrEmpty("fuelTank"));
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);

		output.putInt("processTime", this.processTime);
		output.putInt("maxProcessTime", this.maxProcessTime);
		output.putInt("throttleTime", this.throttleTime);
		output.putInt("maxThrottleTime", this.maxThrottleTime);
		output.putFloat("generating", this.generating);

		//Caps
		this.inputHandler.serialize(output.child("inputHandler"));
		this.outputHandler.serialize(output.child("outputHandler"));
		this.tankThrottle.serialize(output.child("throttleTank"));
		this.tankFuel.serialize(output.child("fuelTank"));
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

	public static void serverTick(Level level, BlockPos pos, BlockState state, ForceEngineBlockEntity forceEngine) {
		if (!forceEngine.inputHandler.getResource(0).isEmpty()) {
			forceEngine.processFuelSlot();
			forceEngine.refreshClient();
		}
		if (!forceEngine.inputHandler.getResource(1).isEmpty()) {
			forceEngine.processThrottleSlot();
			forceEngine.refreshClient();
		}

		if (forceEngine.isActive() && forceEngine.canWork()) {
			forceEngine.checkFluids();
			if (forceEngine.getFuelAmount() > 0) {
				forceEngine.processTime++;
				forceEngine.insertPower();

				if (forceEngine.processTime >= forceEngine.maxProcessTime) {
					try (var tx = Transaction.openRoot()) {
						FluidResource fluidResource = forceEngine.tankFuel.getResource(0);
						if (forceEngine.tankFuel.extract(fluidResource, 1, tx) == 1) {
							tx.commit();
						}
					}
					forceEngine.processTime = 0;
				}
			}
			if (forceEngine.getThrottleAmount() > 0) {
				forceEngine.throttleTime++;

				if (forceEngine.throttleTime >= forceEngine.maxThrottleTime) {
					try (var tx = Transaction.openRoot()) {
						FluidResource fluidResource = forceEngine.tankThrottle.getResource(0);
						if (forceEngine.tankThrottle.extract(fluidResource, 1, tx) == 1) {
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
	}

	public void checkFluids() {
		if (cachedFuel == null || !getFuelFluid().isSame(cachedFuel)) {
			this.cachedFuel = getFuelFluid();
			reevaluateValues();
		}
		if (cachedThrottle == null || !getThrottleFluid().isSame(cachedThrottle)) {
			this.cachedThrottle = getThrottleFluid();
			reevaluateValues();
		}
	}

	public void reevaluateValues() {
		if (cachedFuel != null) {
			FluidStack fuelStack = getFuelFluidStack();

			processTime = 0;
			maxProcessTime = getTimePerFuelMB(fuelStack);

			generating = getPowerForFluid(fuelStack);

			throttleTime = 0;
			maxThrottleTime = getTimePerThrottleMB(getThrottleFluidStack());
		}
		refreshClient();
	}

	private float getThrottleValue() {
		FluidStack throttleStack = getThrottleFluidStack();
		if (!throttleStack.isEmpty()) {
			Fluid fluid = throttleStack.getFluid();
			if (fluid.is(ForceTags.MILK)) {
				return 2.5F;
			} else if (fluid.isSame(Fluids.WATER)) {
				return 2.0F;
			}
		}
		return 1.0F;
	}

	public int getPowerForFluid(FluidStack fluidStack) {
		if (!fluidStack.isEmpty()) {
			float throttleValue = getThrottleValue();
			Fluid fluid = fluidStack.getFluid();
			if (fluid.is(ForceTags.FORCE)) {
				return (int) (20F * throttleValue);
			} else if (fluid.is(FluidTags.LAVA)) {
				return (int) (5F * throttleValue);
			} else if (fluid.is(ForceTags.FUEL)) {
				return (int) (10F * throttleValue);
			} else if (fluid.is(ForceTags.BIOFUEL)) {
				return (int) (15F * throttleValue);
			}
		}
		return 0;
	}

	public int getTimePerFuelMB(FluidStack fluidStack) {
		if (!fluidStack.isEmpty()) {
			Fluid fluid = fluidStack.getFluid();
			if (fluid.is(ForceTags.FORCE)) {
				return 20;
			} else if (fluid.is(FluidTags.LAVA)) {
				return 20;
			} else if (fluid.is(ForceTags.FUEL)) {
				return 20;
			} else if (fluid.is(ForceTags.BIOFUEL)) {
				return 20;
			}
		}
		return 0;
	}

	public int getTimePerThrottleMB(FluidStack fluidStack) {
		if (!fluidStack.isEmpty()) {
			Fluid fluid = fluidStack.getFluid();
			if (fluid.is(ForceTags.MILK)) {
				return 5;
			} else if (fluid.isSame(Fluids.WATER)) {
				return 5;
			}
		}
		return 0;
	}

	public boolean isActive() {
		return getBlockState().getBlock() instanceof ForceEngineBlock && getBlockState().getValue(ForceEngineBlock.ACTIVE);
	}

	public Direction getFacing() {
		if (getBlockState().getBlock() instanceof ForceEngineBlock) {
			return getBlockState().getValue(ForceEngineBlock.FACING);
		}
		return Direction.NORTH;
	}

	public boolean canWork() {
		if (level != null) {
			BlockPos offsetPos = worldPosition.relative(getFacing());
			BlockEntity tile = level.getBlockEntity(offsetPos);
			if (tile != null) {
				EnergyHandler cap = level.getCapability(Capabilities.Energy.BLOCK, offsetPos, getFacing().getOpposite());
				if (cap != null) {
					return cap.getAmountAsInt() < cap.getCapacityAsInt() && !tankFuel.getResource(0).isEmpty();
				}
			}
		}
		return false;
	}

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

	private void processFuelSlot() {
		ItemResource inputResource = inputHandler.getResource(0);
		ItemResource outputResource = outputHandler.getResource(0);

		if (inputResource.is(ForceTags.FORCE_GEM)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);
			try (var tx = Transaction.openRoot()) {
				if (tankFuel.insert(FluidResource.of(force), force.amount(), tx) == force.amount()) {
					if (inputHandler.extract(inputResource, 1, tx) == 1) {
						tx.commit();
					}
				}
			}
		} else if (inputResource.is(Tags.Items.NETHER_STARS)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM * 10);

			try (var tx = Transaction.openRoot()) {
				if (tankFuel.insert(FluidResource.of(force), force.amount(), tx) == force.amount()) {
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
			if (outputResource.isEmpty()) {
				try (var tx = Transaction.openRoot()) {
					var itemAccess = ItemAccess.forHandlerIndex(inputHandler, 0).oneByOne();
					var resourceHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);

					if (ResourceHandlerUtil.move(resourceHandler, tankFuel, (resource) -> true, Integer.MAX_VALUE, tx) != 0) {
						if (inputHandler.extract(inputResource, 1, tx) != 1) {
							return;
						}
						tx.commit();
					}
				}
			}
		}
	}

	private void processThrottleSlot() {
		ItemResource slotStack = inputHandler.getResource(1);

		if (outputHandler.getResource(1).isEmpty()) {
			try (var tx = Transaction.openRoot()) {
				var itemAccess = ItemAccess.forHandlerIndex(inputHandler, 1).oneByOne();
				var resourceHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);

				if (ResourceHandlerUtil.move(resourceHandler, tankFuel, (resource) -> true, Integer.MAX_VALUE, tx) != 0) {
					if (inputHandler.extract(slotStack, 1, tx) != 1) {
						return;
					}
					tx.commit();
				}
			}
		}
	}

	public Fluid getFuelFluid() {
		return getFuelFluidStack().getFluid();
	}

	public FluidStack getFuelFluidStack() {
		return tankFuel.getResource(0).toStack(tankFuel.getAmountAsInt(0));
	}

	public int getFuelAmount() {
		return tankFuel.getAmountAsInt(0);
	}

	public void setFuelAmount(int amount) {
		try (var tx = Transaction.openRoot()) {
			if (amount > 0) {
				if (!tankFuel.getResource(0).isEmpty()) {
					tankFuel.set(0, tankFuel.getResource(0), amount);
				}
			} else {
				tankFuel.set(0, FluidResource.EMPTY, 0);
			}
			tx.commit();
		}
	}

	public Fluid getThrottleFluid() {
		return getThrottleFluidStack().getFluid();
	}

	public FluidStack getThrottleFluidStack() {
		return tankThrottle.getResource(0).toStack(tankThrottle.getAmountAsInt(0));
	}

	public int getThrottleAmount() {
		return tankThrottle.getAmountAsInt(0);
	}

	public void setThrottleAmount(int amount) {
		try (var tx = Transaction.openRoot()) {
			if (amount > 0) {
				if (!tankThrottle.getResource(0).isEmpty()) {
					tankThrottle.set(0, tankThrottle.getResource(0), amount);
				}
			} else {
				tankThrottle.set(0, FluidResource.EMPTY, 0);
			}
			tx.commit();
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ValueInput valueInput) {
		super.onDataPacket(net, valueInput);
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

	private void refreshClient() {
		setChanged();
		if (level != null) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}

	public boolean isUsableByPlayer(Player player) {
		if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		super.preRemoveSideEffects(pos, state);
		try (Transaction tx = Transaction.openRoot()) {
			for (int i = 0; i < inputHandler.size(); ++i) {
				if (!inputHandler.getResource(i).isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inputHandler.getResource(i).toStack());
			}
			for (int i = 0; i < outputHandler.size(); ++i) {
				if (!outputHandler.getResource(i).isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inputHandler.getResource(i).toStack());
			}
			tx.commit();
		}
	}
}
