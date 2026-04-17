//package com.mrbysco.forcecraft.compat.rei.infuser.display;
//
//import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
//import com.mrbysco.forcecraft.compat.rei.REIPlugin;
//import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
//import me.shedaniel.rei.api.common.category.CategoryIdentifier;
//import me.shedaniel.rei.api.common.display.Display;
//import me.shedaniel.rei.api.common.entry.EntryIngredient;
//import me.shedaniel.rei.api.common.util.EntryIngredients;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class InfuserDisplay implements Display {
//	protected List<EntryIngredient> inputs;
//	protected EntryIngredient ingredient;
//	protected EntryIngredient center;
//	protected InfuserModifierType resultModifier;
//	protected List<EntryIngredient> output;
//	protected UpgradeBookTier tier;
//	protected int time;
//
//	public InfuserDisplay(Ingredient center, Ingredient ingredient, InfuserModifierType resultType, UpgradeBookTier tier, ItemStack outputStack, int time) {
//		this.inputs = new ArrayList<>();
//		this.center = EntryIngredients.ofIngredient(center);
//		this.inputs.add(this.center);
//		this.ingredient = EntryIngredients.ofIngredient(ingredient);
//		this.inputs.add(this.ingredient);
//
//		this.output = new ArrayList<>();
//		if (!outputStack.isEmpty())
//			this.output.add(EntryIngredients.of(outputStack));
//
//		this.resultModifier = resultType;
//		this.tier = tier;
//		this.time = time;
//	}
//
//	public UpgradeBookTier getTier() {
//		return tier;
//	}
//
//	@Override
//	public List<EntryIngredient> getInputEntries() {
//		return this.inputs;
//	}
//
//	@Override
//	public List<EntryIngredient> getOutputEntries() {
//		return this.output;
//	}
//
//	public InfuserModifierType getResultModifier() {
//		return resultModifier;
//	}
//
//	@Override
//	public CategoryIdentifier<?> getCategoryIdentifier() {
//		return REIPlugin.INFUSER_CATEGORY;
//	}
//}
