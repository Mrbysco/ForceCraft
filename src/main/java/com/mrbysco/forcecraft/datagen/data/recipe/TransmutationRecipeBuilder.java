package com.mrbysco.forcecraft.datagen.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TransmutationRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	@Nullable
	private String group;
	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

	public TransmutationRecipeBuilder(ItemStack result) {
		this.result = result;
		this.requires(ForceRegistry.FORCE_ROD.get());
		this.unlockedBy("has_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ForceRegistry.FORCE_ROD.get()).build()));
	}

	public static TransmutationRecipeBuilder transmutation(ItemLike resultItem) {
		return new TransmutationRecipeBuilder(new ItemStack(resultItem, 1));
	}

	/**
	 * Creates a new builder for a shapeless recipe.
	 */
	public static TransmutationRecipeBuilder transmutation(ItemLike resultItem, int count) {
		return new TransmutationRecipeBuilder(new ItemStack(resultItem, count));
	}

	public static TransmutationRecipeBuilder transmutation(ItemStack result) {
		return new TransmutationRecipeBuilder(result);
	}

	/**
	 * Adds an ingredient that can be any item in the given tag.
	 */
	public TransmutationRecipeBuilder requires(TagKey<Item> pTag) {
		return this.requires(Ingredient.of(pTag));
	}

	/**
	 * Adds an ingredient of the given item.
	 */
	public TransmutationRecipeBuilder requires(ItemLike pItem) {
		return this.requires(pItem, 1);
	}

	/**
	 * Adds the given ingredient multiple times.
	 */
	public TransmutationRecipeBuilder requires(ItemLike pItem, int pQuantity) {
		for (int i = 0; i < pQuantity; ++i) {
			this.requires(Ingredient.of(pItem));
		}

		return this;
	}

	/**
	 * Adds an ingredient.
	 */
	public TransmutationRecipeBuilder requires(Ingredient pIngredient) {
		return this.requires(pIngredient, 1);
	}

	/**
	 * Adds an ingredient multiple times.
	 */
	public TransmutationRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
		for (int i = 0; i < pQuantity; ++i) {
			this.ingredients.add(pIngredient);
		}

		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String id, CriterionTriggerInstance triggerInstance) {
		this.advancement.addCriterion(id, triggerInstance);
		return this;
	}

	public TransmutationRecipeBuilder group(@Nullable String pGroupName) {
		this.group = pGroupName;
		return this;
	}

	@Override
	public Item getResult() {
		return result.getItem();
	}

	@Override
	public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation id) {
		this.ensureValid(id);
		this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
		recipeConsumer.accept(new TransmutationRecipeBuilder.Result(id, group == null ? "" : group, result, ingredients, this.advancement, id.withPrefix("recipes/transmutation/")));
	}

	private void ensureValid(ResourceLocation id) {
		if (this.advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}

	public static class Result extends CraftingRecipeBuilder.CraftingResult {
		private final ResourceLocation id;
		private final String group;
		private final ItemStack result;
		private final NonNullList<Ingredient> ingredients;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
			super(CraftingBookCategory.MISC);
			this.id = id;
			this.group = group;
			this.result = result;
			this.ingredients = ingredients;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}
			JsonArray ingredientsArray = new JsonArray();
			for (Ingredient ingredient : ingredients) {
				ingredientsArray.add(ingredient.toJson());
			}
			json.add("ingredients", ingredientsArray);

			json.add("result", serializeItemStack(result));
		}

		public RecipeSerializer<?> getType() {
			return ForceRecipeSerializers.TRANSMUTATION_SERIALIZER.get();
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
