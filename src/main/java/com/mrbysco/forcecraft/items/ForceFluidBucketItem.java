package com.mrbysco.forcecraft.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class ForceFluidBucketItem extends BucketItem {
	public ForceFluidBucketItem(Item.Properties properties, Supplier<? extends Fluid> fluidSupplier) {
		super(fluidSupplier, properties.craftRemainder(Items.BUCKET).stacksTo(1));
	}
}
