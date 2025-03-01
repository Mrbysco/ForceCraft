package com.mrbysco.forcecraft.compat.rei.multipleoutput.display;

import com.mrbysco.forcecraft.compat.rei.REIPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class GrindingDisplay extends AbstractMultipleOutputDisplay {

	public GrindingDisplay(Ingredient input, List<ItemStack> outputs) {
		super(input, outputs);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIPlugin.GRINDING_CATEGORY;
	}
}
