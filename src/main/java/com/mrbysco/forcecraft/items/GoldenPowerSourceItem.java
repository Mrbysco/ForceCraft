package com.mrbysco.forcecraft.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;

//TODO: Use datamap for this instead of using getBurnTime
public class GoldenPowerSourceItem extends BaseItem {
	public GoldenPowerSourceItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @org.jspecify.annotations.Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
		return 2000;
	}
}
