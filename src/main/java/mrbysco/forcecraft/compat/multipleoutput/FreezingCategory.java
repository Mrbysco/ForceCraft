package mrbysco.forcecraft.compat.multipleoutput;

import mezz.jei.api.helpers.IGuiHelper;
import mrbysco.forcecraft.compat.JeiCompat;
import mrbysco.forcecraft.recipe.FreezingRecipe;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.util.ResourceLocation;

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
