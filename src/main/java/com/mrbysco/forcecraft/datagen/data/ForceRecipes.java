package com.mrbysco.forcecraft.datagen.data;

import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.Reference;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.nio.file.Path;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

public class ForceRecipes extends RecipeProvider {
	public ForceRecipes(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		//Stairs
		ShapedRecipeBuilder.shaped(FORCE_PLANK_STAIRS.get(), 4).define('#', FORCE_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_planks", has(FORCE_PLANKS.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_RED_STAIRS.get(), 4).define('#', FORCE_BRICK_RED.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_YELLOW_STAIRS.get(), 4).define('#', FORCE_BRICK_YELLOW.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_GREEN_STAIRS.get(), 4).define('#', FORCE_BRICK_GREEN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_BLUE_STAIRS.get(), 4).define('#', FORCE_BRICK_BLUE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_WHITE_STAIRS.get(), 4).define('#', FORCE_BRICK_WHITE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_BLACK_STAIRS.get(), 4).define('#', FORCE_BRICK_BLACK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_BROWN_STAIRS.get(), 4).define('#', FORCE_BRICK_BROWN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_ORANGE_STAIRS.get(), 4).define('#', FORCE_BRICK_ORANGE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), 4).define('#', FORCE_BRICK_LIGHT_BLUE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_MAGENTA_STAIRS.get(), 4).define('#', FORCE_BRICK_MAGENTA.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_PINK_STAIRS.get(), 4).define('#', FORCE_BRICK_PINK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), 4).define('#', FORCE_BRICK_LIGHT_GRAY.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_LIME_STAIRS.get(), 4).define('#', FORCE_BRICK_LIME.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_CYAN_STAIRS.get(), 4).define('#', FORCE_BRICK_CYAN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_PURPLE_STAIRS.get(), 4).define('#', FORCE_BRICK_PURPLE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_GRAY_STAIRS.get(), 4).define('#', FORCE_BRICK_GRAY.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_STAIRS.get(), 4).define('#', FORCE_BRICK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK.get())).save(consumer);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_RED.get()), FORCE_BRICK_RED_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_red_stairs_from_force_brick_red"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_YELLOW.get()), FORCE_BRICK_YELLOW_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_yellow_stairs_from_force_brick_yellow"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GREEN.get()), FORCE_BRICK_GREEN_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_green_stairs_from_force_brick_green"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLUE.get()), FORCE_BRICK_BLUE_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_blue_stairs_from_force_brick_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_WHITE.get()), FORCE_BRICK_WHITE_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_white_stairs_from_force_brick_white"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLACK.get()), FORCE_BRICK_BLACK_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_black_stairs_from_force_brick_black"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BROWN.get()), FORCE_BRICK_BROWN_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_brown_stairs_from_force_brick_brown"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_ORANGE.get()), FORCE_BRICK_ORANGE_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_orange_stairs_from_force_brick_orange"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_BLUE.get()), FORCE_BRICK_LIGHT_BLUE_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_light_blue_stairs_from_force_brick_light_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_MAGENTA.get()), FORCE_BRICK_MAGENTA_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_magenta_stairs_from_force_brick_magenta"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PINK.get()), FORCE_BRICK_PINK_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_pink_stairs_from_force_brick_pink"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_GRAY.get()), FORCE_BRICK_LIGHT_GRAY_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_light_gray_stairs_from_force_brick_light_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIME.get()), FORCE_BRICK_LIME_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_lime_stairs_from_force_brick_lime"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_CYAN.get()), FORCE_BRICK_CYAN_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_cyan_stairs_from_force_brick_cyan"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PURPLE.get()), FORCE_BRICK_PURPLE_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_purple_stairs_from_force_brick_purple"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GRAY.get()), FORCE_BRICK_GRAY_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_gray_stairs_from_force_brick_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK.get()), FORCE_BRICK_STAIRS.get()).unlockedBy("has_bricks", has(FORCE_BRICK.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_stairs_from_force_brick"));
		//Slabs
		ShapedRecipeBuilder.shaped(FORCE_PLANK_SLAB.get(), 6).define('#', FORCE_PLANKS.get()).pattern("###").unlockedBy("has_planks", has(FORCE_PLANKS.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_RED_SLAB.get(), 6).define('#', FORCE_BRICK_RED.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_YELLOW_SLAB.get(), 6).define('#', FORCE_BRICK_YELLOW.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_GREEN_SLAB.get(), 6).define('#', FORCE_BRICK_GREEN.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_BLUE_SLAB.get(), 6).define('#', FORCE_BRICK_BLUE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_WHITE_SLAB.get(), 6).define('#', FORCE_BRICK_WHITE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_BLACK_SLAB.get(), 6).define('#', FORCE_BRICK_BLACK.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_BROWN_SLAB.get(), 6).define('#', FORCE_BRICK_BROWN.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_ORANGE_SLAB.get(), 6).define('#', FORCE_BRICK_ORANGE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 6).define('#', FORCE_BRICK_LIGHT_BLUE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_MAGENTA_SLAB.get(), 6).define('#', FORCE_BRICK_MAGENTA.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_PINK_SLAB.get(), 6).define('#', FORCE_BRICK_PINK.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 6).define('#', FORCE_BRICK_LIGHT_GRAY.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_LIME_SLAB.get(), 6).define('#', FORCE_BRICK_LIME.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_CYAN_SLAB.get(), 6).define('#', FORCE_BRICK_CYAN.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_PURPLE_SLAB.get(), 6).define('#', FORCE_BRICK_PURPLE.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_GRAY_SLAB.get(), 6).define('#', FORCE_BRICK_GRAY.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer);
		ShapedRecipeBuilder.shaped(FORCE_BRICK_SLAB.get(), 6).define('#', FORCE_BRICK.get()).pattern("###").unlockedBy("has_bricks", has(FORCE_BRICK.get())).save(consumer);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_RED.get()), FORCE_BRICK_RED_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_RED.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_red_slab_from_force_brick_red"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_YELLOW.get()), FORCE_BRICK_YELLOW_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_YELLOW.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_yellow_slab_from_force_brick_yellow"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GREEN.get()), FORCE_BRICK_GREEN_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_GREEN.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_green_slab_from_force_brick_green"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLUE.get()), FORCE_BRICK_BLUE_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_BLUE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_blue_slab_from_force_brick_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_WHITE.get()), FORCE_BRICK_WHITE_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_WHITE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_white_slab_from_force_brick_white"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BLACK.get()), FORCE_BRICK_BLACK_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_BLACK.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_black_slab_from_force_brick_black"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_BROWN.get()), FORCE_BRICK_BROWN_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_BROWN.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_brown_slab_from_force_brick_brown"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_ORANGE.get()), FORCE_BRICK_ORANGE_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_ORANGE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_orange_slab_from_force_brick_orange"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_BLUE.get()), FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_BLUE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_light_blue_slab_from_force_brick_light_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_MAGENTA.get()), FORCE_BRICK_MAGENTA_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_MAGENTA.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_magenta_slab_from_force_brick_magenta"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PINK.get()), FORCE_BRICK_PINK_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_PINK.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_pink_slab_from_force_brick_pink"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIGHT_GRAY.get()), FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_LIGHT_GRAY.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_light_gray_slab_from_force_brick_light_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_LIME.get()), FORCE_BRICK_LIME_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_LIME.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_lime_slab_from_force_brick_lime"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_CYAN.get()), FORCE_BRICK_CYAN_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_CYAN.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_cyan_slab_from_force_brick_cyan"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_PURPLE.get()), FORCE_BRICK_PURPLE_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_PURPLE.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_purple_slab_from_force_brick_purple"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK_GRAY.get()), FORCE_BRICK_GRAY_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK_GRAY.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_gray_slab_from_force_brick_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(FORCE_BRICK.get()), FORCE_BRICK_SLAB.get(), 2).unlockedBy("has_bricks", has(FORCE_BRICK.get())).save(consumer, new ResourceLocation(Reference.MOD_ID, "force_brick_slab_from_force_brick"));
	}

	@Override
	protected void saveAdvancement(CachedOutput cache, JsonObject advancementJson, Path path) {
		// Nope
	}
}