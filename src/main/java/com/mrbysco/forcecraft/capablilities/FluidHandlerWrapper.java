package com.mrbysco.forcecraft.capablilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * Wraps two {@link ItemStackHandler}s: Input and Output. Input's slots come first then the Output's slots come after. Items can only be inserted into Input. Items can only be extracted from Output.
 * Note that the above only applies to operations on the wrapper, the backing handlers are not restricted. For persistence, either the backing {@link ItemStackHandler}s can be saved, or the wrapper
 * itself.
 */
public class FluidHandlerWrapper implements IFluidHandler, INBTSerializable<CompoundNBT> {

	public static final String NBT_INPUT = "Input";
	public static final String NBT_OUTPUT = "Output";
	protected final FluidTank throttleTank;
	protected final FluidTank fuelTank;

	public FluidHandlerWrapper(FluidTank throttle, FluidTank fuel) {
		this.throttleTank = throttle;
		this.fuelTank = fuel;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put(NBT_INPUT, throttleTank.writeToNBT(new CompoundNBT()));
		nbt.put(NBT_OUTPUT, fuelTank.writeToNBT(new CompoundNBT()));
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains(NBT_OUTPUT))
			fuelTank.readFromNBT(nbt.getCompound(NBT_OUTPUT));
		if (nbt.contains(NBT_INPUT))
			throttleTank.readFromNBT(nbt.getCompound(NBT_INPUT));

	}

	@Override
	public int getTanks() {
		return 2;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		// TODO: we could make an array of tanks, or switch statement to clean up these
		// maybe
		return (tank == 0) ? this.fuelTank.getFluid() : throttleTank.getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return (tank == 0) ? this.fuelTank.getCapacity() : throttleTank.getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return (tank == 0) ? this.fuelTank.isFluidValid(stack) : throttleTank.isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (this.fuelTank.isFluidValid(resource)) {
			return this.fuelTank.fill(resource, action);
		}
		return this.throttleTank.fill(resource, action);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (this.fuelTank.getFluid().isFluidEqual(resource)) {
			return this.fuelTank.drain(resource, action);
		}
		return this.throttleTank.drain(resource, action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (this.fuelTank.getFluidAmount() > 0) {
			return this.fuelTank.drain(maxDrain, action);
		}
		return this.throttleTank.drain(maxDrain, action);
	}
}
