package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blocks.engine.ForceEngineBlock;
import com.mrbysco.forcecraft.capabilities.FluidHandlerWrapper;
import com.mrbysco.forcecraft.capabilities.ItemStackHandlerWrapper;
import com.mrbysco.forcecraft.menu.engine.ForceEngineMenu;
import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.FluidTags;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForceEngineBlockEntity extends BlockEntity implements MenuProvider {

	private static final int MAX_FLUID = 10000;

	protected FluidTank tankFuel = new FluidTank(MAX_FLUID) {
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (!isFluidEqual(this, resource)) {
				return FluidStack.EMPTY;
			}
			if (action.simulate()) {
				int amount = this.getFluidAmount() - resource.getAmount() < 0 ? this.getFluidAmount() : resource.getAmount();
				return new FluidStack(this.getFluid(), amount);
			}
			return super.drain(resource.getAmount(), action);
		}

		@Override
		protected void onContentsChanged() {
			refreshClient();
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.is(ForceTags.FORCE) || fluid.is(FluidTags.LAVA) ||
					fluid.is(ForceTags.FUEL) || fluid.is(ForceTags.BIOFUEL);
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.is(ForceTags.FORCE) || fluid.is(FluidTags.LAVA) ||
					fluid.is(ForceTags.FUEL) || fluid.is(ForceTags.BIOFUEL);
		}
	};

	protected FluidTank tankThrottle = new FluidTank(MAX_FLUID) {
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (!isFluidEqual(this, resource)) {
				return FluidStack.EMPTY;
			}
			if (action.simulate()) {
				int amount = this.getFluidAmount() - resource.getAmount() < 0 ? this.getFluidAmount() : resource.getAmount();
				return new FluidStack(this.getFluid(), amount);
			}
			return super.drain(resource.getAmount(), action);
		}

		@Override
		protected void onContentsChanged() {
			refreshClient();
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.isSame(Fluids.WATER) || fluid.is(ForceTags.MILK);
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.isSame(Fluids.WATER) || fluid.is(ForceTags.MILK);
		}
	};

	private final FluidHandlerWrapper tankWrapper = new FluidHandlerWrapper(tankThrottle, tankFuel);
	private final LazyOptional<IFluidHandler> tankWrapperCap = LazyOptional.of(() -> tankWrapper);

	public final ItemStackHandler inputHandler = new ItemStackHandler(2) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
				if (stack.getMaxStackSize() > 1) {
					return 1;
				}
			}
			return 64;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			IFluidHandler fluidCap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
			if (slot == 0) {
				if (fluidCap != null) {
					FluidStack fluidStack = fluidCap.getFluidInTank(0);
					if (!fluidStack.isEmpty()) {
						Fluid fluid = fluidStack.getFluid();
						return fluid.is(ForceTags.FORCE) || fluid.is(FluidTags.LAVA) ||
								fluid.is(ForceTags.FUEL) || fluid.is(ForceTags.BIOFUEL);
					}
				}
				return stack.is(ForceTags.FORGE_GEM) || stack.is(Tags.Items.NETHER_STARS) ||
						(fluidCap != null && fluidCap.getFluidInTank(0).getFluid().is(ForceTags.FORCE));
			} else if (slot == 1) {
				if (fluidCap != null) {
					FluidStack fluidStack = fluidCap.getFluidInTank(0);
					if (!fluidStack.isEmpty()) {
						Fluid fluid = fluidStack.getFluid();
						return fluid.isSame(Fluids.WATER) || fluid.is(ForceTags.MILK);
					}
				}
				return false;
			} else {
				return false;
			}
		}
	};
	public final ItemStackHandler outputHandler = new ItemStackHandler(2) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
				if (stack.getMaxStackSize() > 1) {
					return 1;
				}
			}
			return 64;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return false;
		}
	};

	private final ItemStackHandlerWrapper stackWrapper = new ItemStackHandlerWrapper(inputHandler, outputHandler);
	private final LazyOptional<IItemHandler> stackWrapperCap = LazyOptional.of(() -> stackWrapper);

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
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.processTime = nbt.getInt("processTime");
		this.maxProcessTime = nbt.getInt("maxProcessTime");
		this.throttleTime = nbt.getInt("throttleTime");
		this.maxThrottleTime = nbt.getInt("maxThrottleTime");

		this.generating = nbt.getFloat("generating");

		//Caps
		this.stackWrapper.deserializeNBT(nbt.getCompound("stackHandler"));
		this.tankWrapper.deserializeNBT(nbt.getCompound("fluid"));
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);

		compound.putInt("processTime", this.processTime);
		compound.putInt("maxProcessTime", this.maxProcessTime);
		compound.putInt("throttleTime", this.throttleTime);
		compound.putInt("maxThrottleTime", this.maxThrottleTime);
		compound.putFloat("generating", this.generating);
		//Caps
		compound.put("stackHandler", stackWrapper.serializeNBT());
		compound.put("fluid", tankWrapper.serializeNBT());
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
		if (!forceEngine.inputHandler.getStackInSlot(0).isEmpty()) {
			forceEngine.processFuelSlot();
			forceEngine.refreshClient();
		}
		if (!forceEngine.inputHandler.getStackInSlot(1).isEmpty()) {
			forceEngine.processThrottleSlot();
			forceEngine.refreshClient();
		}

		if (forceEngine.isActive() && forceEngine.canWork()) {
			forceEngine.checkFluids();
			if (forceEngine.getFuelAmount() > 0) {
				forceEngine.processTime++;
				forceEngine.insertPower();

				if (forceEngine.processTime >= forceEngine.maxProcessTime) {
					forceEngine.tankFuel.drain(1, FluidAction.EXECUTE);
					forceEngine.processTime = 0;
				}
			}
			if (forceEngine.getThrottleAmount() > 0) {
				forceEngine.throttleTime++;

				if (forceEngine.throttleTime >= forceEngine.maxThrottleTime) {
					forceEngine.tankThrottle.drain(1, FluidAction.EXECUTE);
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
		BlockPos offsetPos = worldPosition.relative(getFacing());
		BlockEntity tile = level.getBlockEntity(offsetPos);
		if (tile != null) {
			IEnergyStorage cap = tile.getCapability(ForgeCapabilities.ENERGY, getFacing().getOpposite()).orElse(null);
			if (cap != null) {
				return cap.canReceive() && cap.getEnergyStored() < cap.getMaxEnergyStored() && !tankFuel.getFluid().isEmpty();
			}
		}
		return false;
	}

	public void insertPower() {
		BlockPos offsetPos = worldPosition.relative(getFacing());
		BlockEntity tile = level.getBlockEntity(offsetPos);
		if (tile != null) {
			IEnergyStorage cap = tile.getCapability(ForgeCapabilities.ENERGY, getFacing().getOpposite()).orElse(null);
			if (cap != null) {
				if (cap.canReceive() && cap.getEnergyStored() < cap.getMaxEnergyStored()) {
					cap.receiveEnergy((int) generating, false);
				}
			}
		}
	}

	private void processFuelSlot() {
		ItemStack slotStack = stackWrapper.getStackInSlot(0);

		if (slotStack.is(ForceTags.FORGE_GEM)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);

			if (getFuelAmount() + force.getAmount() <= tankFuel.getCapacity()) {
				fillFuel(force, FluidAction.EXECUTE);
				slotStack.shrink(1);
			}
		} else if (slotStack.is(Tags.Items.NETHER_STARS)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM * 10);

			ItemStack extraSlot = outputHandler.getStackInSlot(0);
			if (getFuelAmount() + force.getAmount() <= tankFuel.getCapacity() && extraSlot.getCount() < inputHandler.getSlotLimit(1)) {
				fillFuel(force, FluidAction.EXECUTE);
				slotStack.shrink(1);
				if (outputHandler.getStackInSlot(0).isEmpty()) {
					outputHandler.setStackInSlot(0, new ItemStack(ForceRegistry.INERT_CORE.get()));
				} else {
					extraSlot.setCount(extraSlot.getCount() + 1);
				}
			}
		} else {
			if (outputHandler.getStackInSlot(0).isEmpty()) {
				FluidActionResult result = FluidUtil.tryEmptyContainer(slotStack, tankFuel, Integer.MAX_VALUE, null, true);
				if (result.isSuccess()) {
					slotStack.shrink(1);
					outputHandler.setStackInSlot(0, result.getResult());
				}
			}
		}
	}

	public int fillFuel(FluidStack resource, FluidAction action) {
		FluidStack resourceCopy = resource.copy();

		if (action.execute()) {
			if (tankFuel.getFluid().isEmpty() || tankFuel.getFluid().isFluidEqual(resource)) {
				tankFuel.fill(resourceCopy, action);
			}
		}
		return resource.getAmount();
	}

	private void processThrottleSlot() {
		ItemStack slotStack = inputHandler.getStackInSlot(1);

		if (outputHandler.getStackInSlot(1).isEmpty()) {
			FluidActionResult result = FluidUtil.tryEmptyContainer(slotStack, tankThrottle, Integer.MAX_VALUE, null, true);
			if (result.isSuccess()) {
				slotStack.shrink(1);
				outputHandler.setStackInSlot(1, result.getResult());
			}
		}
	}

	public int fillThrottle(FluidStack resource, FluidAction action) {
		FluidStack resourceCopy = resource.copy();

		if (action.execute()) {
			if (tankThrottle.getFluid().isEmpty() || tankThrottle.getFluid().isFluidEqual(resource)) {
				tankThrottle.fill(resourceCopy, action);
			}
		}
		return resource.getAmount();
	}

	public Fluid getFuelFluid() {
		return getFuelFluidStack().getFluid();
	}

	public FluidStack getFuelFluidStack() {
		return tankFuel.getFluid();
	}

	public int getFuelAmount() {
		return tankFuel.getFluidAmount();
	}

	public void setFuelAmount(int amount) {
		if (amount > 0) {
			if (!tankFuel.getFluid().isEmpty()) {
				tankFuel.getFluid().setAmount(amount);
			}
		} else {
			tankFuel.setFluid(FluidStack.EMPTY);
		}
	}

	public Fluid getThrottleFluid() {
		return getThrottleFluidStack().getFluid();
	}

	public FluidStack getThrottleFluidStack() {
		return tankThrottle.getFluid();
	}

	public int getThrottleAmount() {
		return tankThrottle.getFluidAmount();
	}

	public void setThrottleAmount(int amount) {
		if (amount > 0) {
			if (!tankThrottle.getFluid().isEmpty()) {
				tankThrottle.getFluid().setAmount(amount);
			}
		} else {
			tankThrottle.setFluid(FluidStack.EMPTY);
		}
	}

	protected boolean isFluidEqual(FluidTank fluidTank, FluidStack fluid) {
		return isFluidEqual(fluidTank, fluid.getFluid());
	}

	protected boolean isFluidEqual(FluidTank fluidTank, Fluid fluid) {
		return fluidTank.getFluid().getFluid().equals(fluid);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		this.load(packet.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	private void refreshClient() {
		setChanged();
		BlockState state = level.getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 2);
	}

	public boolean isUsableByPlayer(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			return this.stackWrapperCap.cast();
		}
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return this.tankWrapperCap.cast();
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		stackWrapperCap.invalidate();
		tankWrapperCap.invalidate();
	}
}
