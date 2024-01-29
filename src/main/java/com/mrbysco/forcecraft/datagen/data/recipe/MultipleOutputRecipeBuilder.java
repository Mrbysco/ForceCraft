package com.mrbysco.forcecraft.datagen.data.recipe;

import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class MultipleOutputRecipeBuilder implements RecipeBuilder {
	private final NonNullList<ItemStack> results = NonNullList.create();
	private final Ingredient ingredient;
	private final float experience;
	private final int time;
	private final float chance;
	@Nullable
	private String group;
	private final RecipeType<? extends MultipleOutputFurnaceRecipe> type;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	public MultipleOutputRecipeBuilder(RecipeType<? extends MultipleOutputFurnaceRecipe> type, Ingredient ingredient, float pExperience, int grindingTime) {
		this.type = type;
		this.ingredient = ingredient;
		this.experience = pExperience;
		this.time = grindingTime;
		this.chance = 0.0F;
	}

	public MultipleOutputRecipeBuilder(RecipeType<? extends MultipleOutputFurnaceRecipe> type, Ingredient ingredient, float chance, float pExperience, int grindingTime) {
		this.type = type;
		this.ingredient = ingredient;
		this.experience = pExperience;
		this.time = grindingTime;
		this.chance = chance;
	}

	public static MultipleOutputRecipeBuilder freezing(Ingredient ingredient, float experience, int freezingTime) {
		return new MultipleOutputRecipeBuilder(
				ForceRecipes.FREEZING.get(), ingredient, experience, freezingTime
		);
	}

	public static MultipleOutputRecipeBuilder freezing(ItemLike input, float experience, int freezingTime) {
		return new MultipleOutputRecipeBuilder(
				ForceRecipes.FREEZING.get(), Ingredient.of(input), experience, freezingTime
		);
	}

	public static MultipleOutputRecipeBuilder grinding(Ingredient ingredient, float chance, float experience, int processTime) {
		return new MultipleOutputRecipeBuilder(
				ForceRecipes.GRINDING.get(), ingredient, chance, experience, processTime
		);
	}

	public static MultipleOutputRecipeBuilder grinding(ItemLike input, float chance, float experience, int processTime) {
		return new MultipleOutputRecipeBuilder(
				ForceRecipes.GRINDING.get(), Ingredient.of(input), chance, experience, processTime
		);
	}

	public MultipleOutputRecipeBuilder setResult(ItemLike item, int count) {
		this.results.add(new ItemStack(item, count));
		return this;
	}

	public MultipleOutputRecipeBuilder setResult(ItemStack stack) {
		this.results.add(stack);
		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	@Override
	public RecipeBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

	@Override
	public Item getResult() {
		return results.get(0).getItem();
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		Advancement.Builder advancement$builder = output.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);

		if (results.isEmpty() || results.size() > 2) {
			throw new IllegalStateException("Must have between 1 and 2 results.");
		}

		if (type == ForceRecipes.FREEZING.get()) {
			FreezingRecipe recipe = new FreezingRecipe(group == null ? "" : group, ingredient, results, experience, time);
			output.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/freezing/")));
		} else if (type == ForceRecipes.GRINDING.get()) {
			GrindingRecipe recipe = new GrindingRecipe(group == null ? "" : group, ingredient, results, chance, experience, time);
			output.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/grinding/")));
		}
	}
}
