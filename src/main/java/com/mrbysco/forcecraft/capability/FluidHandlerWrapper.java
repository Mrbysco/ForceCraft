//package com.mrbysco.forcecraft.capability;
//
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.neoforged.neoforge.common.util.INBTSerializable;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.fluids.FluidType;
//import net.neoforged.neoforge.fluids.capability.IFluidHandler;
//import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
//import net.neoforged.neoforge.items.ItemStackHandler;
//
///**
// * Wraps two {@link ItemStackHandler}s: Input and Output. Input's slots come first then the Output's slots come after. Items can only be inserted into Input. Items can only be extracted from Output.
// * Note that the above only applies to operations on the wrapper, the backing handlers are not restricted. For persistence, either the backing {@link ItemStackHandler}s can be saved, or the wrapper
// * itself.
// */
//public class FluidHandlerWrapper implements IFluidHandler, INBTSerializable<CompoundTag> {
//
//	public static final String NBT_INPUT = "Input";
//	public static final String NBT_OUTPUT = "Output";
//	protected final FluidTank throttleTank;
//	protected final FluidTank fuelTank;
//
//	public FluidHandlerWrapper(FluidTank throttle, FluidTank fuel) {
//		this.throttleTank = throttle;
//		this.fuelTank = fuel;
//	}
//
//	@Override
//	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
//		CompoundTag tag = new CompoundTag();
//		tag.put(NBT_INPUT, throttleTank.writeToNBT(provider, new CompoundTag()));
//		tag.put(NBT_OUTPUT, fuelTank.writeToNBT(provider, new CompoundTag()));
//		return tag;
//	}
//
//	@Override
//	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
//		if (tag.contains(NBT_OUTPUT))
//			fuelTank.readFromNBT(provider, tag.getCompound(NBT_OUTPUT));
//		if (tag.contains(NBT_INPUT))
//			throttleTank.readFromNBT(provider, tag.getCompound(NBT_INPUT));
//	}
//
//	@Override
//	public int getTanks() {
//		return 2;
//	}
//
//	@Override
//	public FluidStack getFluidInTank(int tank) {
//		switch (tank) {
//			default:
//				return fuelTank.getFluid();
//			case 1:
//				return throttleTank.getFluid();
//		}
//	}
//
//	@Override
//	public int getTankCapacity(int tank) {
//		switch (tank) {
//			default:
//				return fuelTank.getCapacity();
//			case 1:
//				return throttleTank.getCapacity();
//		}
//	}
//
//	@Override
//	public boolean isFluidValid(int tank, FluidStack stack) {
//		switch (tank) {
//			default:
//				return fuelTank.isFluidValid(stack);
//			case 1:
//				return throttleTank.isFluidValid(stack);
//		}
//	}
//
//	@Override
//	public int fill(FluidStack resource, FluidAction action) {
//		if (resource.isEmpty()) {
//			return 0;
//		}
//
//		if (this.fuelTank.isFluidValid(resource)) {
//			int count = this.fuelTank.fill(resource, action);
//			return count < FluidType.BUCKET_VOLUME ? 0 : count;
//		} else {
//			int count = this.throttleTank.fill(resource, action);
//			return count < FluidType.BUCKET_VOLUME ? 0 : count;
//		}
//	}
//
//	@Override
//	public FluidStack drain(FluidStack resource, FluidAction action) {
//		if (resource.isEmpty()) {
//			return FluidStack.EMPTY;
//		}
//
//		if (this.fuelTank.isFluidValid(resource)) {
//			return this.fuelTank.drain(resource, action);
//		}
//		return this.throttleTank.drain(resource, action);
//	}
//
//	@Override
//	public FluidStack drain(int maxDrain, FluidAction action) {
//		if (this.fuelTank.getFluidAmount() > 0) {
//			return this.fuelTank.drain(maxDrain, action);
//		}
//		return this.throttleTank.drain(maxDrain, action);
//	}
//}
