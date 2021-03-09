package mrbysco.forcecraft.items;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ForceFluidBucketItem extends BucketItem {
	public ForceFluidBucketItem(Item.Properties properties, Supplier<? extends Fluid> fluidSupplier) {
		super(fluidSupplier, properties.containerItem(Items.BUCKET).maxStackSize(1));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new FluidBucketWrapper(stack);
	}
}
