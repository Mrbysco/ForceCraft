package com.mrbysco.forcecraft.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ForceFluidBucketItem extends BucketItem {
	public ForceFluidBucketItem(Item.Properties properties, Supplier<? extends Fluid> fluidSupplier) {
		super(fluidSupplier, properties.craftRemainder(Items.BUCKET).stacksTo(1));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidBucketWrapper(stack);
	}
}
