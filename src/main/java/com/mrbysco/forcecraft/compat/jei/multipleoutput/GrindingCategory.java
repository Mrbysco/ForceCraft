package com.mrbysco.forcecraft.compat.jei.multipleoutput;

import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;

public class GrindingCategory extends AbstractMultiOutputCategory<GrindingRecipe> {

	public GrindingCategory(IGuiHelper guiHelper) {
		super(guiHelper, ForceRegistry.FORCE_FURNACE.get(), "forcecraft.gui.jei.category.grinding", 0, true);
	}

	@Override
	public RecipeType<GrindingRecipe> getRecipeType() {
		return JeiCompat.GRINDING_TYPE;
	}
}
