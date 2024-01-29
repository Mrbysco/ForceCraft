package com.mrbysco.forcecraft.datagen.data.recipe;

import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class InfuseRecipeBuilder implements RecipeBuilder {
	private final Ingredient center;
	private final Ingredient ingredient;
	private final UpgradeBookTier tier;
	private final int time;
	private InfuserModifierType resultModifier = InfuserModifierType.ITEM;
	private ItemStack output = ItemStack.EMPTY;

	public InfuseRecipeBuilder(Ingredient center, Ingredient ingredient, UpgradeBookTier tier, int time) {
		this.ingredient = ingredient;
		this.center = center;
		this.tier = tier;
		this.time = time;
	}

	public static InfuseRecipeBuilder infuse(Ingredient ingredient, Ingredient center, UpgradeBookTier tier, int time) {
		return new InfuseRecipeBuilder(center, ingredient, tier, time);
	}

	public InfuseRecipeBuilder modifierType(InfuserModifierType type) {
		this.resultModifier = type;
		return this;
	}

	public InfuseRecipeBuilder output(ItemLike outputItem) {
		this.output = new ItemStack(outputItem.asItem());
		this.resultModifier = InfuserModifierType.ITEM;
		return this;
	}

	public InfuseRecipeBuilder output(ItemLike outputItem, int count) {
		this.output = new ItemStack(outputItem, count);
		this.resultModifier = InfuserModifierType.ITEM;
		return this;
	}

	public InfuseRecipeBuilder output(ItemStack outputStack) {
		this.output = outputStack;
		this.resultModifier = InfuserModifierType.ITEM;
		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
		return null;
	}

	@Override
	public RecipeBuilder group(@Nullable String pGroupName) {
		return this;
	}

	@Override
	public Item getResult() {
		return this.output.getItem();
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		InfuseRecipe recipe = new InfuseRecipe(this.center, this.ingredient, this.resultModifier, this.tier, this.output, this.time);
		output.accept(id, recipe, null);
	}
}
