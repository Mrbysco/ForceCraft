package com.mrbysco.forcecraft.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class MultipleOutputFurnaceRecipe extends AbstractCookingRecipe {
	protected final float secondaryChance;
	static int MAX_OUTPUT = 2;

	protected final NonNullList<ItemStack> results;

	public MultipleOutputFurnaceRecipe(RecipeType<?> typeIn, String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float secondaryChance, float experienceIn, int cookTimeIn) {
		super(typeIn, groupIn, CookingBookCategory.MISC, ingredientIn, results.get(0), experienceIn, cookTimeIn);
		this.results = results;
		this.secondaryChance = secondaryChance;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
		return getResultItem(registryAccess).copy();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.results.get(0);
	}

	public NonNullList<ItemStack> getRecipeOutputs() {
		return this.results;
	}

	public float getSecondaryChance() {
		return this.secondaryChance;
	}
}
