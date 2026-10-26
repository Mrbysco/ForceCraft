package com.mrbysco.forcecraft.compat.jei.category;

import com.mrbysco.forcecraft.recipe.TransmutationRecipe;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public class TransmutationCategory implements ICraftingCategoryExtension<TransmutationRecipe> {

	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<TransmutationRecipe> recipeHolder) {
		return recipeHolder.value().getIngredients().stream()
				.map(Ingredient::display).toList();
	}
}
