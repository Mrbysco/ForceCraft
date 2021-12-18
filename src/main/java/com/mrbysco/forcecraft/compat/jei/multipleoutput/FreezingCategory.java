package com.mrbysco.forcecraft.compat.jei.multipleoutput;

import com.mrbysco.forcecraft.compat.jei.JeiCompat;
import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class FreezingCategory extends AbstractMultiOutputCategory<FreezingRecipe> {

	public FreezingCategory(IGuiHelper guiHelper) {
		super(guiHelper, ForceRegistry.FORCE_FURNACE.get(), "forcecraft.gui.jei.category.freezing", 37, false);
	}

	@Override
	public ResourceLocation getUid() {
		return JeiCompat.FREEZING;
	}

	@Override
	public Class<? extends FreezingRecipe> getRecipeClass() {
		return FreezingRecipe.class;
	}
}
