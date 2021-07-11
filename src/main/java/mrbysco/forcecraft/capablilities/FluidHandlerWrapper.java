package mrbysco.forcecraft.capablilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Wraps two {@link ItemStackHandler}s: Input and Output. Input's slots come first then the Output's slots come after. Items can only be inserted into Input. Items can only be extracted from Output.
 * Note that the above only applies to operations on the wrapper, the backing handlers are not restricted. For persistence, either the backing {@link ItemStackHandler}s can be saved, or the wrapper
 * itself.
 */
public class FluidHandlerWrapper implements IFluidHandler, INBTSerializable<CompoundNBT> {

	public static final String NBT_INPUT = "Input";
	public static final String NBT_OUTPUT = "Output";
	protected final FluidTank tankInput; // input is for example coolant
	protected final FluidTank tankOutput; // output is fuel

	public FluidHandlerWrapper(FluidTank input, FluidTank output) {
		this.tankInput = input;
		this.tankOutput  = output;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put(NBT_INPUT, tankInput.writeToNBT(new CompoundNBT()));
		nbt.put(NBT_OUTPUT, tankOutput.writeToNBT(new CompoundNBT()));
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains(NBT_OUTPUT))
			tankOutput.readFromNBT(nbt.getCompound(NBT_OUTPUT));
		if (nbt.contains(NBT_INPUT))
			tankInput.readFromNBT(nbt.getCompound(NBT_INPUT));

	}

	@Override
	public int getTanks() {
		return 2;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		// TODO: we could make an array of tanks, or switch statement to clean up these
		// maybe
		return (tank == 0) ? this.tankOutput.getFluid() : tankInput.getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return (tank == 0) ? this.tankOutput.getCapacity() : tankInput.getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return (tank == 0) ? this.tankOutput.isFluidValid(stack) : tankInput.isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return this.tankInput.fill(resource, action);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return this.tankOutput.drain(resource, action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return this.tankOutput.drain(maxDrain, action);
	}
}
