package com.mrbysco.forcecraft.datagen.server.builder;

import com.mrbysco.forcecraft.recipe.TransmutationRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class TransmutationRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final ItemStackTemplate result;
	private final Ingredient input;
	private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
	private @Nullable String group;

	private TransmutationRecipeBuilder(RecipeCategory category, ItemStackTemplate result, Ingredient input) {
		this.category = category;
		this.result = result;
		this.input = input;
		this.advancementBuilder.unlockedBy("has_force_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ForceRegistry.FORCE_ROD));
	}

	public static TransmutationRecipeBuilder transmutation(RecipeCategory category, ItemLike result, int count, Ingredient input) {
		return new TransmutationRecipeBuilder(category, new ItemStackTemplate(result.asItem(), count), input);
	}

	public static TransmutationRecipeBuilder transmutation(RecipeCategory category, ItemLike result, int count, ItemLike input) {
		return transmutation(category, result, count, Ingredient.of(input));
	}

	public static TransmutationRecipeBuilder transmutation(RecipeCategory category, ItemLike result, Ingredient input) {
		return new TransmutationRecipeBuilder(category, new ItemStackTemplate(result.asItem(), 1), input);
	}

	public static TransmutationRecipeBuilder transmutation(RecipeCategory category, ItemLike result, ItemLike input) {
		return transmutation(category, result, 1, Ingredient.of(input));
	}

	@Override
	public TransmutationRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		advancementBuilder.unlockedBy(name, criterion);
		return this;
	}

	@Override
	public TransmutationRecipeBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return RecipeBuilder.getDefaultRecipeId(result);
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
		TransmutationRecipe recipe = new TransmutationRecipe(
				RecipeBuilder.createCraftingCommonInfo(true),
				RecipeBuilder.createCraftingBookInfo(category, group),
				result,
				List.of(Ingredient.of(ForceRegistry.FORCE_ROD), input)
		);
		output.accept(id, recipe, advancementBuilder.build(output, id, category));
	}
}
