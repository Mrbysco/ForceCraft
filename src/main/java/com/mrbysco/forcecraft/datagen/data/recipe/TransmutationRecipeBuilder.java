package com.mrbysco.forcecraft.datagen.data.recipe;

import com.mrbysco.forcecraft.recipe.TransmutationRecipe;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransmutationRecipeBuilder implements RecipeBuilder {
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	@Nullable
	private String group;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	public TransmutationRecipeBuilder(ItemStack result) {
		this.result = result;
		this.requires(ForceRegistry.FORCE_ROD.get());
		this.unlockedBy("has_rod", CriteriaTriggers.INVENTORY_CHANGED
				.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(),
						InventoryChangeTrigger.TriggerInstance.Slots.ANY,
						List.of(ItemPredicate.Builder.item().of(ForceRegistry.FORCE_ROD.get()).build()))
				)
		);
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

	public TransmutationRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
		this.criteria.put(pName, pCriterion);
		return this;
	}

	public TransmutationRecipeBuilder group(@javax.annotation.Nullable String pGroupName) {
		this.group = pGroupName;
		return this;
	}

	@Override
	public Item getResult() {
		return result.getItem();
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		Advancement.Builder advancement$builder = output.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);

		TransmutationRecipe recipe = new TransmutationRecipe(group == null ? "" : group, result, ingredients);
		output.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/transmutation/")));
	}
}
