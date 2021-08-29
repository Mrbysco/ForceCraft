package com.mrbysco.forcecraft.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class MultipleOutputFurnaceRecipe extends AbstractCookingRecipe {
	protected final float secondaryChance;
	static int MAX_OUTPUT = 2;

	protected final NonNullList<ItemStack> resultItems;

	public MultipleOutputFurnaceRecipe(IRecipeType<?> typeIn, ResourceLocation idIn, String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float secondaryChance, float experienceIn, int cookTimeIn) {
		super(typeIn, idIn, groupIn, ingredientIn, results.get(0), experienceIn, cookTimeIn);
		this.resultItems = results;
		this.secondaryChance = secondaryChance;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return this.resultItems.get(0).copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.resultItems.get(0);
	}

	public NonNullList<ItemStack> getCraftingResults(IInventory inv) {
		NonNullList<ItemStack> results = NonNullList.create();
		for(ItemStack stack : this.resultItems) {
			results.add(stack.copy());
		}
		return results;
	}

	public NonNullList<ItemStack> getRecipeOutputs() {
		return this.resultItems;
	}

	public float getSecondaryChance() {
		return this.secondaryChance;
	}
}
