package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

import javax.annotation.Nonnull;

public class FlaskFluidHandler extends FluidHandlerItemStackSimple {

	public FlaskFluidHandler(@Nonnull ItemStack container) {
		super(container, 1000);
	}

	@Override
	public int getTankCapacity(int tank) {
		return 1000;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid();
	}

	@Nonnull
	@Override
	public FluidStack getFluid() {
		Item item = container.getItem();
		if (item instanceof ForceFilledForceFlask) {
			return new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), 1000);
		} else if (item instanceof MilkFlaskItem && ForgeMod.MILK.isPresent()) {
			return new FluidStack(ForgeMod.MILK.get(), 1000);
		} else {
			return FluidStack.EMPTY;
		}
	}

	@Override
	public boolean canFillFluidType(FluidStack stack) {
		return stack.getFluid().is(ForceTags.FORCE) || (ForgeMod.MILK.isPresent() && stack.getFluid().is(ForceTags.MILK));
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		return canFillFluidType(stack);
	}

	@Override
	protected void setContainerToEmpty() {
		container = new ItemStack(ForceRegistry.FORCE_FLASK.get());
	}

	@Override
	protected void setFluid(FluidStack stack) {
		if (stack.isEmpty()) {
			setContainerToEmpty();
		} else {
			if (stack.getFluid().is(ForceTags.FORCE)) {
				container = new ItemStack(ForceRegistry.FORCE_FILLED_FORCE_FLASK.get());
				return;
			}
			if (ForgeMod.MILK.isPresent() && stack.getFluid().is(ForceTags.MILK)) {
				container = new ItemStack(ForceRegistry.MILK_FORCE_FLASK.get());
				return;
			}
		}
	}
}
