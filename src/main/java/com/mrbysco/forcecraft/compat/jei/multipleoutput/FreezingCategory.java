package com.mrbysco.forcecraft.compat.jei.multipleoutput;

import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class FreezingCategory extends AbstractMultiOutputCategory<FreezingRecipe> {

	public FreezingCategory(IGuiHelper guiHelper) {
		super(guiHelper, ForceRegistry.FORCE_FURNACE.get(), "forcecraft.gui.jei.category.freezing", 37, false);
	}

	@Override
	public RecipeType<FreezingRecipe> getRecipeType() {
		return JeiCompat.FREEZING_TYPE;
	}
}
