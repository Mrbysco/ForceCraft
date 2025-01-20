package com.mrbysco.forcecraft.datagen.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.recipe.ForceRecipes;
import com.mrbysco.forcecraft.recipe.MultipleOutputFurnaceRecipe;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MultipleOutputRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
	private final NonNullList<ItemStack> results = NonNullList.create();
	private final Ingredient ingredient;
	private final float experience;
	private final int time;
	private final float chance;
	@Nullable
	private String group;
	private final RecipeType<? extends MultipleOutputFurnaceRecipe> type;
	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

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
	public RecipeBuilder unlockedBy(String id, CriterionTriggerInstance triggerInstance) {
		this.advancement.addCriterion(id, triggerInstance);
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
	public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation id) {
		this.ensureValid(id);
		this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);

		if (results.isEmpty() || results.size() > 2) {
			throw new IllegalStateException("Must have between 1 and 2 results.");
		}

		if (type == ForceRecipes.FREEZING.get()) {
			recipeConsumer.accept(new FreezingResult(id, group == null ? "" : group, ingredient,
					results, experience, time, this.advancement, id.withPrefix("recipes/freezing/")));

		} else if (type == ForceRecipes.GRINDING.get()) {
			recipeConsumer.accept(new GrindingResult(id, group == null ? "" : group, ingredient,
					results, chance, experience, time, this.advancement, id.withPrefix("recipes/grinding/")));
		}
	}

	private void ensureValid(ResourceLocation id) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}

	public static class FreezingResult extends CraftingRecipeBuilder.CraftingResult {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final NonNullList<ItemStack> results;
		private final float experience;
		private final int time;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;


		public FreezingResult(ResourceLocation id, String group, Ingredient ingredient, NonNullList<ItemStack> results,
		                      float experience, int time, Advancement.Builder advancement, ResourceLocation advancementId) {
			super(CraftingBookCategory.MISC);
			this.id = id;
			this.group = group;
			this.ingredient = ingredient;
			this.results = results;
			this.experience = experience;
			this.time = time;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}
			json.add("ingredient", ingredient.toJson());
			JsonArray resultsArray = new JsonArray();
			for (ItemStack result : results) {
				resultsArray.add(serializeItemStack(result));
			}
			json.add("results", resultsArray);
			json.addProperty("experience", experience);
			json.addProperty("processtime", time);
		}

		public RecipeSerializer<?> getType() {
			return ForceRecipeSerializers.FREEZING_SERIALIZER.get();
		}

		public ResourceLocation getId() {
			return this.id;
		}

		@Nullable
		public JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Nullable
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}

		static JsonElement serializeItemStack(@NotNull ItemStack stack) {
			JsonObject json = new JsonObject();
			json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
			if (stack.getCount() > 1) {
				json.addProperty("count", stack.getCount());
			}
			if (stack.hasTag()) {
				json.addProperty("nbt", stack.getTag().toString());
			}
			return json;
		}
	}

	public static class GrindingResult extends CraftingRecipeBuilder.CraftingResult {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final NonNullList<ItemStack> results;
		private final float chance;
		private final float experience;
		private final int time;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;


		public GrindingResult(ResourceLocation id, String group, Ingredient ingredient, NonNullList<ItemStack> results,
		                      float chance, float experience, int time, Advancement.Builder advancement,
		                      ResourceLocation advancementId) {
			super(CraftingBookCategory.MISC);
			this.id = id;
			this.group = group;
			this.ingredient = ingredient;
			this.results = results;
			this.chance = chance;
			this.experience = experience;
			this.time = time;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}
			json.add("ingredient", ingredient.toJson());
			JsonArray resultsArray = new JsonArray();
			for (ItemStack result : results) {
				resultsArray.add(serializeItemStack(result));
			}
			json.add("results", resultsArray);
			json.addProperty("secondaryChance", chance);
			json.addProperty("experience", experience);
			json.addProperty("processtime", time);
		}

		public RecipeSerializer<?> getType() {
			return ForceRecipeSerializers.GRINDING_SERIALIZER.get();
		}

		public ResourceLocation getId() {
			return this.id;
		}

		@Nullable
		public JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Nullable
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}

		static JsonElement serializeItemStack(@NotNull ItemStack stack) {
			JsonObject json = new JsonObject();
			json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
			if (stack.getCount() > 1) {
				json.addProperty("count", stack.getCount());
			}
			if (stack.hasTag()) {
				json.addProperty("nbt", stack.getTag().toString());
			}
			return json;
		}
	}
}
