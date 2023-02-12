package com.mrbysco.forcecraft.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class MultipleOutputFurnaceRecipe extends AbstractCookingRecipe {
	protected final float secondaryChance;
	static int MAX_OUTPUT = 2;

	protected final NonNullList<ItemStack> resultItems;

	public MultipleOutputFurnaceRecipe(RecipeType<?> typeIn, ResourceLocation idIn, String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float secondaryChance, float experienceIn, int cookTimeIn) {
		super(typeIn, idIn, groupIn, CookingBookCategory.MISC, ingredientIn, results.get(0), experienceIn, cookTimeIn);
		this.resultItems = results;
		this.secondaryChance = secondaryChance;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack assemble(Container inv) {
		return this.resultItems.get(0).copy();
	}

	@Override
	public ItemStack getResultItem() {
		return this.resultItems.get(0);
	}

	public NonNullList<ItemStack> getCraftingResults(Container inv) {
		NonNullList<ItemStack> results = NonNullList.create();
		for (ItemStack stack : this.resultItems) {
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
