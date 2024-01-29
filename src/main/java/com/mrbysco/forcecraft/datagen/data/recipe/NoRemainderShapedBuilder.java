package com.mrbysco.forcecraft.datagen.data.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mrbysco.forcecraft.recipe.ShapedNoRemainderRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NoRemainderShapedBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final Item result;
	private final int count;
	private final ItemStack resultStack; // Neo: add stack result support
	private final List<String> rows = Lists.newArrayList();
	private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	private String group;
	private boolean showNotification = true;

	public NoRemainderShapedBuilder(RecipeCategory category, ItemLike result, int pCount) {
		this(category, new ItemStack(result, pCount));
	}

	public NoRemainderShapedBuilder(RecipeCategory category, ItemStack result) {
		this.category = category;
		this.result = result.getItem();
		this.count = result.getCount();
		this.resultStack = result;
	}

	/**
	 * Creates a new builder for a shaped recipe.
	 */
	public static NoRemainderShapedBuilder shaped(RecipeCategory category, ItemLike result) {
		return shaped(category, result, 1);
	}

	/**
	 * Creates a new builder for a shaped recipe.
	 */
	public static NoRemainderShapedBuilder shaped(RecipeCategory category, ItemLike result, int count) {
		return new NoRemainderShapedBuilder(category, result, count);
	}

	public static NoRemainderShapedBuilder shaped(RecipeCategory category, ItemStack result) {
		return new NoRemainderShapedBuilder(category, result);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public NoRemainderShapedBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(tag));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public NoRemainderShapedBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public NoRemainderShapedBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol)) {
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
		} else if (symbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.key.put(symbol, ingredient);
			return this;
		}
	}

	/**
	 * Adds a new entry to the patterns for this recipe.
	 */
	public NoRemainderShapedBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(pattern);
			return this;
		}
	}

	public NoRemainderShapedBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	public NoRemainderShapedBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

	public NoRemainderShapedBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	@Override
	public Item getResult() {
		return this.result;
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		ShapedRecipePattern shapedrecipepattern = this.ensureValid(id);
		Advancement.Builder advancement$builder = output.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);
		ShapedNoRemainderRecipe shapedNoRemainderRecipe = new ShapedNoRemainderRecipe(
				Objects.requireNonNullElse(this.group, ""),
				RecipeBuilder.determineBookCategory(this.category),
				shapedrecipepattern,
				this.resultStack,
				this.showNotification
		);
		output.accept(id, shapedNoRemainderRecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private ShapedRecipePattern ensureValid(ResourceLocation id) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		} else {
			return ShapedRecipePattern.of(this.key, this.rows);
		}
	}
}
