package com.mrbysco.forcecraft.compat.rei.multipleoutput.display;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMultipleOutputDisplay implements Display {

	private final EntryIngredient inputEntry;
	private final List<EntryIngredient> outputEntries;

	public AbstractMultipleOutputDisplay(Ingredient input, List<ItemStack> outputs) {
		this.inputEntry = EntryIngredients.ofIngredient(input);
		this.outputEntries = new ArrayList<>();
		outputs.forEach(stack -> this.outputEntries.add(EntryIngredients.of(stack)));
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return List.of(inputEntry);
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputEntries;
	}
}
