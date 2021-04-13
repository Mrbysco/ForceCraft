package mrbysco.forcecraft.items.flask;

import mrbysco.forcecraft.registry.ForceFluids;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

import javax.annotation.Nonnull;

public class FlaskFluidHandler extends FluidHandlerItemStackSimple {

	public FlaskFluidHandler(@Nonnull ItemStack container) {
		super(container, FluidAttributes.BUCKET_VOLUME);
	}

	@Override
	public int getTankCapacity(int tank) {
		return FluidAttributes.BUCKET_VOLUME;
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
			return new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FluidAttributes.BUCKET_VOLUME);
		} else if (item instanceof MilkFlaskItem && ForgeMod.MILK.isPresent()) {
			return new FluidStack(ForgeMod.MILK.get(), FluidAttributes.BUCKET_VOLUME);
		} else {
			return FluidStack.EMPTY;
		}
	}

	@Override
	public boolean canFillFluidType(FluidStack stack) {
		for (Fluid fluid : FluidTags.getCollection().get(new ResourceLocation("forge","force")).getAllElements()){
			if (fluid == stack.getFluid()){
				return true;
			}
		}
		if(ForgeMod.MILK.isPresent()) {
			for (Fluid fluid : FluidTags.getCollection().get(new ResourceLocation("forge","milk")).getAllElements()){
				if (fluid == stack.getFluid()){
					return true;
				}
			}
		}
		return false;
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
			for (Fluid fluid : FluidTags.getCollection().get(new ResourceLocation("forge", "force")).getAllElements()) {
				if (fluid == stack.getFluid()) {
					container = new ItemStack(ForceRegistry.FORCE_FILLED_FORCE_FLASK.get());
					return;
				}
			}
			if (ForgeMod.MILK.isPresent()) {
				for (Fluid fluid : FluidTags.getCollection().get(new ResourceLocation("forge", "milk")).getAllElements()) {
					if (fluid == stack.getFluid()) {
						container = new ItemStack(ForceRegistry.MILK_FORCE_FLASK.get());
						return;
					}
				}
			}
		}
	}
}
