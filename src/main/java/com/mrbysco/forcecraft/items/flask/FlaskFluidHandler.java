package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.registry.ForceFluids;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FlaskFluidHandler extends FluidHandlerItemStackSimple {

	public FlaskFluidHandler(Supplier<DataComponentType<SimpleFluidContent>> componentType, ItemStack container) {
		super(componentType, container, 1000);
	}

	@Override
	public int getTankCapacity(int tank) {
		return 1000;
	}

	@NotNull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid();
	}

	@NotNull
	@Override
	public FluidStack getFluid() {
		Item item = container.getItem();
		if (item instanceof ForceFilledForceFlask) {
			return new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), 1000);
		} else if (item instanceof MilkFlaskItem && NeoForgeMod.MILK.isBound()) {
			return new FluidStack(NeoForgeMod.MILK.get(), 1000);
		} else {
			return FluidStack.EMPTY;
		}
	}

	@Override
	public boolean canFillFluidType(FluidStack stack) {
		return stack.getFluid().is(ForceTags.Fluids.FORCE) || (NeoForgeMod.MILK.isBound() && stack.getFluid().is(Tags.Fluids.MILK));
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
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
			if (!stack.isEmpty() && stack.getFluid().is(ForceTags.Fluids.FORCE)) {
				container = new ItemStack(ForceRegistry.FORCE_FILLED_FORCE_FLASK.get());
				return;
			}
			if (NeoForgeMod.MILK.isBound() && stack.getFluid().is(Tags.Fluids.MILK)) {
				container = new ItemStack(ForceRegistry.MILK_FORCE_FLASK.get());
				return;
			}
		}
	}
}
