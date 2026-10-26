package com.mrbysco.forcecraft.datagen.server.builder;

import com.mrbysco.forcecraft.recipe.FreezingRecipe;
import com.mrbysco.forcecraft.recipe.GrindingRecipe;
import com.mrbysco.forcecraft.recipe.MultiOutputFurnaceRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class MultiOutputRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory craftingCategory = RecipeCategory.MISC;
	private final CookingBookCategory cookingCategory = CookingBookCategory.MISC;
	private final Ingredient ingredient;
	private final float experience;
	private final int cookingTime;
	private final float secondaryChance;
	private final Factory<?> factory;
	private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
	private @Nullable String group;
	private @Nullable ItemStackTemplate result;
	private @Nullable ItemStackTemplate result2;

	private MultiOutputRecipeBuilder(Ingredient ingredient, float secondaryChance, float experience, int cookingTime, Factory<?> factory) {
		this.ingredient = ingredient;
		this.secondaryChance = secondaryChance;
		this.experience = experience;
		this.cookingTime = cookingTime;
		this.factory = factory;
	}

	public static <T extends MultiOutputFurnaceRecipe> MultiOutputRecipeBuilder generic(
			Ingredient ingredient, float secondaryChance, float experience, int cookingTime, Factory<T> factory) {
		return new MultiOutputRecipeBuilder(ingredient, secondaryChance, experience, cookingTime, factory);
	}

	public static MultiOutputRecipeBuilder freezing(Ingredient ingredient, float experience, int cookingTime) {
		return new MultiOutputRecipeBuilder(ingredient, 0.0F, experience, cookingTime, FreezingRecipe::new);
	}

	public static MultiOutputRecipeBuilder freezing(ItemLike input, float experience, int cookingTime) {
		return freezing(Ingredient.of(input), experience, cookingTime);
	}

	public static MultiOutputRecipeBuilder grinding(Ingredient ingredient, float chance, float experience, int cookingTime) {
		return new MultiOutputRecipeBuilder(ingredient, chance, experience, cookingTime, GrindingRecipe::new);
	}

	public static MultiOutputRecipeBuilder grinding(ItemLike input, float chance, float experience, int cookingTime) {
		return grinding(Ingredient.of(input), chance, experience, cookingTime);
	}

	public MultiOutputRecipeBuilder result(ItemLike item, int count) {
		return result(new ItemStackTemplate(item.asItem(), count));
	}

	public MultiOutputRecipeBuilder result(ItemStackTemplate template) {
		this.result = template;
		return this;
	}

	public MultiOutputRecipeBuilder secondaryResult(ItemLike item, int count) {
		return secondaryResult(new ItemStackTemplate(item.asItem(), count));
	}

	public MultiOutputRecipeBuilder secondaryResult(ItemStackTemplate template) {
		this.result2 = template;
		return this;
	}

	@Override
	public MultiOutputRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.advancementBuilder.unlockedBy(name, criterion);
		return this;
	}

	@Override
	public MultiOutputRecipeBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		if (this.result == null) {
			throw new IllegalStateException("Recipe needs a result before an id can be derived");
		}
		return RecipeBuilder.getDefaultRecipeId(this.result);
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
		MultiOutputFurnaceRecipe recipe = this.factory.create(
				RecipeBuilder.createCraftingCommonInfo(true),
				new AbstractCookingRecipe.CookingBookInfo(this.cookingCategory, Objects.requireNonNullElse(this.group, "")),
				this.ingredient,
				this.result,
				Optional.ofNullable(this.result2),
				this.secondaryChance,
				this.experience,
				this.cookingTime
		);
		output.accept(id, recipe, this.advancementBuilder.build(output, id, this.craftingCategory));
	}

	@FunctionalInterface
	public interface Factory<T extends MultiOutputFurnaceRecipe> {
		T create(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient,
		         ItemStackTemplate result, Optional<ItemStackTemplate> result2, float secondaryChance, float experience, int cookingTime);
	}
}