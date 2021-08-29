package com.mrbysco.forcecraft.compat.multipleoutput;

import mezz.jei.api.helpers.IGuiHelper;
import com.mrbysco.forcecraft.compat.JeiCompat;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.util.ResourceLocation;

public class GrindingCategory extends AbstractMultiOutputCategory<GrindingRecipe> {

	public GrindingCategory(IGuiHelper guiHelper) {
		super(guiHelper, ForceRegistry.FORCE_FURNACE.get(), "forcecraft.gui.jei.category.grinding", 0, true);
	}

	@Override
	public ResourceLocation getUid() {
		return JeiCompat.GRINDING;
	}

	@Override
	public Class<? extends GrindingRecipe> getRecipeClass() {
		return GrindingRecipe.class;
	}
}
