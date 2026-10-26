package com.mrbysco.forcecraft.recipe;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public abstract class MultiOutputFurnaceRecipe extends AbstractCookingRecipe {
	private final Optional<ItemStackTemplate> result2;
	private final float secondaryChance;

	public MultiOutputFurnaceRecipe(CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, Optional<ItemStackTemplate> result2, float experience, int cookingTime, float secondaryChance) {
		super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
		this.result2 = result2;
		this.secondaryChance = secondaryChance;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public Optional<ItemStackTemplate> result2() {
		return result2;
	}

	public float secondaryChance() {
		return secondaryChance;
	}
}
