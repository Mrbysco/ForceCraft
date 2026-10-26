package com.mrbysco.forcecraft.datagen.server.builder;

import com.mrbysco.forcecraft.blockentity.infuser.InfuserUpgrade;
import com.mrbysco.forcecraft.recipe.InfuseRecipe;
import com.mrbysco.forcecraft.upgrade.ConvertUpgrade;
import com.mrbysco.forcecraft.upgrade.UpgradeBookTier;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class InfuseRecipeBuilder {
	private final Ingredient ingredient;
	private final Ingredient center;
	private final InfuserUpgrade upgrade;
	private final UpgradeBookTier tier;
	private final int points;
	private final int time;
	private @Nullable ItemStackTemplate output;
	private String group = "";

	private InfuseRecipeBuilder(Ingredient ingredient, Ingredient center, InfuserUpgrade upgrade, UpgradeBookTier tier, int points, int time) {
		this.ingredient = ingredient;
		this.center = center;
		this.upgrade = upgrade;
		this.tier = tier;
		this.points = points;
		this.time = time;
	}

	public static InfuseRecipeBuilder infuse(Ingredient ingredient, Ingredient center, InfuserUpgrade upgrade, UpgradeBookTier tier, int points, int time) {
		return new InfuseRecipeBuilder(ingredient, center, upgrade, tier, points, time);
	}

	public static InfuseRecipeBuilder convert(Ingredient center, Ingredient ingredient, ItemStackTemplate result, UpgradeBookTier tier, int points, int time) {
		InfuseRecipeBuilder builder = new InfuseRecipeBuilder(ingredient, center, new ConvertUpgrade(), tier, points, time);
		builder.output = result;
		return builder;
	}

	public InfuseRecipeBuilder group(String group) {
		this.group = group;
		return this;
	}

	public void save(RecipeOutput recipeOutput, Identifier recipeId) {
		ResourceKey<Recipe<?>> id = ResourceKey.create(Registries.RECIPE, recipeId);
		InfuseRecipe recipe = new InfuseRecipe(center, ingredient, upgrade, tier, Optional.ofNullable(output), points, time, group);
		recipeOutput.accept(id, recipe, null);
	}
}