package mrbysco.forcecraft.tiles;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.blocks.engine.ForceEngineBlock;
import mrbysco.forcecraft.container.engine.ForceEngineContainer;
import mrbysco.forcecraft.registry.ForceFluids;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class ForceEngineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	protected FluidTank tank = new FluidTank(10000) {
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (!isFluidEqual(tank, resource)) {
				return FluidStack.EMPTY;
			}
			if (action.simulate()) {
				int amount = tank.getFluidAmount() - resource.getAmount() < 0 ? tank.getFluidAmount() : resource.getAmount();
				return new FluidStack(tank.getFluid(), amount);
			}
			return super.drain(resource.getAmount(), action);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return super.drain(maxDrain, action);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return super.fill(resource, action);
		}

		@Override
		protected void onContentsChanged() {
			refreshClient();
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.isIn(ForceTags.FORCE) || fluid.isIn(FluidTags.LAVA) ||
					fluid.isIn(ForceTags.FUEL) || fluid.isIn(ForceTags.BIOFUEL);
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.isIn(ForceTags.FORCE) || fluid.isIn(FluidTags.LAVA) ||
					fluid.isIn(ForceTags.FUEL) || fluid.isIn(ForceTags.BIOFUEL);
		}
	};
	private LazyOptional<IFluidHandler> tankHolder = LazyOptional.of(() -> tank);

	protected FluidTank throttleTank = new FluidTank(10000) {
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (!isFluidEqual(throttleTank, resource)) {
				return FluidStack.EMPTY;
			}
			if (action.simulate()) {
				int amount = throttleTank.getFluidAmount() - resource.getAmount() < 0 ? throttleTank.getFluidAmount() : resource.getAmount();
				return new FluidStack(throttleTank.getFluid(), amount);
			}
			return super.drain(resource.getAmount(), action);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return super.drain(maxDrain, action);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return super.fill(resource, action);
		}

		@Override
		protected void onContentsChanged() {
			refreshClient();
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.isEquivalentTo(Fluids.WATER) || fluid.isIn(ForceTags.MILK);
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
			Fluid fluid = stack.getFluid();
			return fluid.isEquivalentTo(Fluids.WATER) || fluid.isIn(ForceTags.MILK);
		}
	};
	private LazyOptional<IFluidHandler> throttleTankHolder = LazyOptional.of(() -> throttleTank);

	public final ItemStackHandler handler = new ItemStackHandler(2) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
				if(stack.getMaxStackSize() > 1) {
					return 1;
				}
			}
			return 64;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if(slot == 0) {
				IFluidHandler fluidCap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
				if(fluidCap != null) {
					FluidStack fluidStack = fluidCap.getFluidInTank(0);
					if(!fluidStack.isEmpty()) {
						Fluid fluid = fluidStack.getFluid();
						return fluid.isIn(ForceTags.FORCE) || fluid.isIn(FluidTags.LAVA) ||
								fluid.isIn(ForceTags.FUEL) || fluid.isIn(ForceTags.BIOFUEL);
					}
				}
				return stack.getItem().isIn(ForceTags.FORGE_GEM) || stack.getItem().isIn(Tags.Items.NETHER_STARS)||
						(fluidCap != null && fluidCap.getFluidInTank(0).getFluid().isIn(ForceTags.FORCE));
			} else {
				return false;
			}
		}
	};
	private LazyOptional<IItemHandler> handlerHolder = LazyOptional.of(() -> handler);
	public final ItemStackHandler throttleHandler = new ItemStackHandler(1) {
		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
				if(stack.getMaxStackSize() > 1) {
					return 1;
				}
			}
			return 64;
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			IFluidHandler fluidCap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
			if(fluidCap != null) {
				FluidStack fluidStack = fluidCap.getFluidInTank(0);
				if(!fluidStack.isEmpty()) {
					Fluid fluid = fluidStack.getFluid();
					return fluid.isEquivalentTo(Fluids.WATER) || fluid.isIn(ForceTags.MILK);
				}
			}
			return false;
		}
	};
	private LazyOptional<IItemHandler> throttleHolder = LazyOptional.of(() -> throttleHandler);
	private static final int FLUID_PER_GEM = 500;

	public int processTime = 0;
	public int maxProcessTime = 20;
	public int throttleTime = 0;
	public int maxThrottleTime = 10;

	private Fluid cachedFuel;
	private Fluid cachedThrottle;

	public float generating = 0;

	public ForceEngineTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public ForceEngineTile() {
		this(ForceRegistry.FORCE_ENGINE_TILE.get());
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		this.processTime = nbt.getInt("processTime");
		this.maxProcessTime = nbt.getInt("maxProcessTime");
		this.throttleTime = nbt.getInt("throttleTime");
		this.maxThrottleTime = nbt.getInt("maxThrottleTime");

		this.generating = nbt.getFloat("generating");

		//Caps
		handler.deserializeNBT(nbt.getCompound("fuelStackHandler"));
		throttleHandler.deserializeNBT(nbt.getCompound("throttleStackHandler"));
		if(nbt.contains("fuelTank"))
			tank.readFromNBT(nbt.getCompound("fuelTank"));
		if(nbt.contains("coolantTank"))
			throttleTank.readFromNBT(nbt.getCompound("coolantTank"));

		super.read(state, nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

		compound.putInt("processTime", this.processTime);
		compound.putInt("maxProcessTime", this.maxProcessTime);
		compound.putInt("throttleTime", this.throttleTime);
		compound.putInt("maxThrottleTime", this.maxThrottleTime);
		compound.putFloat("generating", this.generating);
		//Caps
		compound.put("fuelStackHandler", handler.serializeNBT());
		compound.put("throttleStackHandler", throttleHandler.serializeNBT());
		compound.put("fuelTank", tank.writeToNBT(new CompoundNBT()));
		compound.put("coolantTank", throttleTank.writeToNBT(new CompoundNBT()));

		return compound;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(Reference.MOD_ID + ".container.force_engine");
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
		return new ForceEngineContainer(id, playerInv, this);
	}

	@Override
	public void tick() {
		if(world.isRemote) return;

		if (!handler.getStackInSlot(0).isEmpty()) {
			processFuelSlot();
			refreshClient();
		}
		if (!throttleHandler.getStackInSlot(0).isEmpty()) {
			processThrottleSlot();
			refreshClient();
		}

		if(isActive() && canWork()){
			checkFluids();
			if(getFuelAmount() > 0) {
				processTime++;
				insertPower();

				if(processTime >= this.maxProcessTime) {
					tank.drain(1, FluidAction.EXECUTE);
					processTime = 0;
				}
			}
			if(getThrottleAmount() > 0) {
				throttleTime++;

				if(throttleTime >= this.maxThrottleTime) {
					throttleTank.drain(1, FluidAction.EXECUTE);
					throttleTime = 0;
				}
			}

			refreshClient();
		} else {
			if(processTime != 0) processTime = 0;
		}
	}

	public void checkFluids() {
		if(cachedFuel == null || !getFuelFluid().isEquivalentTo(cachedFuel)) {
			this.cachedFuel = getFuelFluid();
			reevaluateValues();
		}
		if(cachedThrottle == null || !getThrottleFluid().isEquivalentTo(cachedThrottle)) {
			this.cachedThrottle = getThrottleFluid();
			reevaluateValues();
		}
	}

	public void reevaluateValues() {
		if(cachedFuel != null) {
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
		if(!throttleStack.isEmpty()) {
			Fluid fluid = throttleStack.getFluid();
			if(fluid.isIn(ForceTags.MILK)) {
				return 2.5F;
			} else if(fluid.isEquivalentTo(Fluids.WATER)) {
				return 2.0F;
			}
		}
		return 1.0F;
	}

	public int getPowerForFluid(FluidStack fluidStack) {
		if(!fluidStack.isEmpty()) {
			float throttleValue = getThrottleValue();
			Fluid fluid = fluidStack.getFluid();
			if(fluid.isIn(ForceTags.FORCE)) {
				return (int)(20F * throttleValue);
			} else if(fluid.isIn(FluidTags.LAVA)) {
				return (int)(5F * throttleValue);
			} else if(fluid.isIn(ForceTags.FUEL)) {
				return (int)(10F * throttleValue);
			} else if(fluid.isIn(ForceTags.BIOFUEL)) {
				return (int)(15F * throttleValue);
			}
		}
		return 0;
	}

	public int getTimePerFuelMB(FluidStack fluidStack) {
		if(!fluidStack.isEmpty()) {
			Fluid fluid = fluidStack.getFluid();
			if(fluid.isIn(ForceTags.FORCE)) {
				return 20;
			} else if(fluid.isIn(FluidTags.LAVA)) {
				return 20;
			} else if(fluid.isIn(ForceTags.FUEL)) {
				return 20;
			} else if(fluid.isIn(ForceTags.BIOFUEL)) {
				return 20;
			}
		}
		return 0;
	}

	public int getTimePerThrottleMB(FluidStack fluidStack) {
		if(!fluidStack.isEmpty()) {
			Fluid fluid = fluidStack.getFluid();
			if(fluid.isIn(ForceTags.MILK)) {
				return 5;
			} else if(fluid.isEquivalentTo(Fluids.WATER)) {
				return 5;
			}
		}
		return 0;
	}

	public boolean isActive() {
		return getBlockState().getBlock() instanceof ForceEngineBlock && getBlockState().get(ForceEngineBlock.ACTIVE);
	}

	public Direction getFacing() {
		if(getBlockState().getBlock() instanceof ForceEngineBlock) {
			return getBlockState().get(ForceEngineBlock.FACING);
		}
		return Direction.NORTH;
	}

	public boolean canWork() {
		BlockPos offsetPos = pos.offset(getFacing());
		TileEntity tile = world.getTileEntity(offsetPos);
		if(tile != null) {
			IEnergyStorage cap = tile.getCapability(CapabilityEnergy.ENERGY, getFacing().getOpposite()).orElse(null);
			if(cap != null) {
				return cap.canReceive() && cap.getEnergyStored() < cap.getMaxEnergyStored() && !tank.getFluid().isEmpty();
			}
		}
		return false;
	}

	public void insertPower() {
		BlockPos offsetPos = pos.offset(getFacing());
		TileEntity tile = world.getTileEntity(offsetPos);
		if(tile != null) {
			IEnergyStorage cap = tile.getCapability(CapabilityEnergy.ENERGY, getFacing().getOpposite()).orElse(null);
			if (cap != null) {
				if(cap.canReceive() && cap.getEnergyStored() < cap.getMaxEnergyStored()) {
					cap.receiveEnergy((int)generating, false);
				}
			}
		}
	}

	private void processFuelSlot() {
		ItemStack slotStack = handler.getStackInSlot(0);

		if(slotStack.getItem().isIn(ForceTags.FORGE_GEM)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);

			if(getFuelAmount() + force.getAmount() <= tank.getCapacity()) {
				fillFuel(force, FluidAction.EXECUTE);
				slotStack.shrink(1);
			}
		} else if(slotStack.getItem().isIn(Tags.Items.NETHER_STARS)) {
			FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM * 10);

			ItemStack extraSlot = handler.getStackInSlot(1);
			if(getFuelAmount() + force.getAmount() <= tank.getCapacity() && extraSlot.getCount() < handler.getSlotLimit(1)) {
				fillFuel(force, FluidAction.EXECUTE);
				slotStack.shrink(1);
				if(handler.getStackInSlot(1).isEmpty()) {
					handler.setStackInSlot(1, new ItemStack(ForceRegistry.INERT_CORE.get()));
				} else {
					extraSlot.setCount(extraSlot.getCount() + 1);
				}
			}
		} else {
			FluidActionResult result = FluidUtil.tryEmptyContainer(slotStack, tank, Integer.MAX_VALUE, null, true);
			if(result.isSuccess()) {
				handler.setStackInSlot(0, result.getResult());
			}
		}
	}

	public int fillFuel(FluidStack resource, FluidAction action) {
		FluidStack resourceCopy = resource.copy();

		if(action.execute()) {
			if(tank.getFluid().isEmpty() || tank.getFluid().isFluidEqual(resource)) {
				tank.fill(resourceCopy, action);
			}
		}
		return resource.getAmount();
	}

	private void processThrottleSlot() {
		ItemStack slotStack = throttleHandler.getStackInSlot(0);

		FluidActionResult result = FluidUtil.tryEmptyContainer(slotStack, throttleTank, Integer.MAX_VALUE, null, true);
		if(result.isSuccess()) {
			throttleHandler.setStackInSlot(0, result.getResult());
		}
	}

	public int fillThrottle(FluidStack resource, FluidAction action) {
		FluidStack resourceCopy = resource.copy();

		if(action.execute()) {
			if(throttleTank.getFluid().isEmpty() || throttleTank.getFluid().isFluidEqual(resource)) {
				throttleTank.fill(resourceCopy, action);
			}
		}
		return resource.getAmount();
	}

	public Fluid getFuelFluid() {
		return tank.getFluid().getFluid();
	}

	public FluidStack getFuelFluidStack() {
		return tank.getFluid();
	}

	public int getFuelAmount() {
		return tank.getFluidAmount();
	}

	public void setFuelAmount(int amount) {
		if(amount > 0) {
			if(!tank.getFluid().isEmpty()) {
				tank.getFluid().setAmount(amount);
			}
		} else {
			tank.setFluid(FluidStack.EMPTY);
		}
	}

	public Fluid getThrottleFluid() {
		return throttleTank.getFluid().getFluid();
	}

	public FluidStack getThrottleFluidStack() {
		return throttleTank.getFluid();
	}

	public int getThrottleAmount() {
		return throttleTank.getFluidAmount();
	}

	public void setThrottleAmount(int amount) {
		if(amount > 0) {
			if(!throttleTank.getFluid().isEmpty()) {
				throttleTank.getFluid().setAmount(amount);
			}
		} else {
			throttleTank.setFluid(FluidStack.EMPTY);
		}
	}

	protected boolean isFluidEqual(FluidTank fluidTank, FluidStack fluid) {
		return isFluidEqual(fluidTank, fluid.getFluid());
	}

	protected boolean isFluidEqual(FluidTank fluidTank, Fluid fluid) {
		return fluidTank.getFluid().getFluid().equals(fluid);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(getBlockState(), packet.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.read(state, tag);
	}

	@Override
	public CompoundNBT getTileData() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return nbt;
	}

	private void refreshClient() {
		markDirty();
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 2);
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(facing == getFacing() || facing == getFacing().getOpposite()) {
				return handlerHolder.cast();
			} else {
				return throttleHolder.cast();
			}
		}
		if (capability == FLUID_HANDLER_CAPABILITY) {
			if(facing == getFacing() || facing == getFacing().getOpposite()) {
				return tankHolder.cast();
			} else {
				return throttleTankHolder.cast();
			}
		}

		return super.getCapability(capability, facing);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		handlerHolder.invalidate();
		throttleHolder.invalidate();
		tankHolder.invalidate();
		throttleTankHolder.invalidate();
	}
}
