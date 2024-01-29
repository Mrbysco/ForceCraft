package com.mrbysco.forcecraft.datagen.data;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blockentities.InfuserModifierType;
import com.mrbysco.forcecraft.datagen.data.recipe.InfuseRecipeBuilder;
import com.mrbysco.forcecraft.datagen.data.recipe.MultipleOutputRecipeBuilder;
import com.mrbysco.forcecraft.datagen.data.recipe.NoRemainderShapedBuilder;
import com.mrbysco.forcecraft.datagen.data.recipe.TransmutationRecipeBuilder;
import com.mrbysco.forcecraft.items.infuser.UpgradeBookTier;
import com.mrbysco.forcecraft.recipe.condition.TorchEnabledCondition;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.NBTIngredient;

import java.util.List;


public class ForceRecipeProvider extends RecipeProvider {
	public ForceRecipeProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		//Stairs
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_PLANK_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_planks", has(ForceRegistry.FORCE_PLANKS.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_RED_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_RED.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_RED.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_YELLOW_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_YELLOW.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_YELLOW.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GREEN_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_GREEN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GREEN.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLUE_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_BLUE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLUE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_WHITE_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_WHITE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_WHITE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLACK_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_BLACK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLACK.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BROWN_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_BROWN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BROWN.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_ORANGE_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_ORANGE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_ORANGE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_MAGENTA.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_MAGENTA.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PINK_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_PINK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PINK.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIME_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_LIME.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIME.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_CYAN_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_CYAN.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_CYAN.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PURPLE_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_PURPLE.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PURPLE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GRAY_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK_GRAY.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GRAY.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_STAIRS.get(), 4).define('#', ForceRegistry.FORCE_BRICK.get()).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK.get())).save(output);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_RED.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_RED_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_RED.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_red_stairs_from_force_brick_red"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_YELLOW.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_YELLOW_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_YELLOW.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_yellow_stairs_from_force_brick_yellow"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_GREEN.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GREEN_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GREEN.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_green_stairs_from_force_brick_green"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_BLUE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLUE_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLUE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_blue_stairs_from_force_brick_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_WHITE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_WHITE_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_WHITE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_white_stairs_from_force_brick_white"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_BLACK.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLACK_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLACK.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_black_stairs_from_force_brick_black"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_BROWN.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BROWN_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BROWN.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_brown_stairs_from_force_brick_brown"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_ORANGE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_ORANGE_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_ORANGE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_orange_stairs_from_force_brick_orange"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_light_blue_stairs_from_force_brick_light_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_MAGENTA.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_MAGENTA.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_magenta_stairs_from_force_brick_magenta"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_PINK.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PINK_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PINK.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_pink_stairs_from_force_brick_pink"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_light_gray_stairs_from_force_brick_light_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_LIME.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIME_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIME.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_lime_stairs_from_force_brick_lime"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_CYAN.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_CYAN_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_CYAN.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_cyan_stairs_from_force_brick_cyan"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_PURPLE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PURPLE_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PURPLE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_purple_stairs_from_force_brick_purple"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_GRAY.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GRAY_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GRAY.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_gray_stairs_from_force_brick_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_STAIRS.get()).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_stairs_from_force_brick"));
		//Slabs
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_PLANK_SLAB.get(), 6).define('#', ForceRegistry.FORCE_PLANKS.get()).pattern("###").unlockedBy("has_planks", has(ForceRegistry.FORCE_PLANKS.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_RED_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_RED.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_RED.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_YELLOW_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_YELLOW.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_YELLOW.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GREEN_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_GREEN.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GREEN.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLUE_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_BLUE.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLUE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_WHITE_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_WHITE.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_WHITE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLACK_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_BLACK.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLACK.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BROWN_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_BROWN.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BROWN.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_ORANGE_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_ORANGE.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_ORANGE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_MAGENTA_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_MAGENTA.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_MAGENTA.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PINK_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_PINK.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PINK.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIME_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_LIME.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIME.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_CYAN_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_CYAN.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_CYAN.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PURPLE_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_PURPLE.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PURPLE.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GRAY_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK_GRAY.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GRAY.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_SLAB.get(), 6).define('#', ForceRegistry.FORCE_BRICK.get()).pattern("###").unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK.get())).save(output);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_RED.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_RED_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_RED.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_red_slab_from_force_brick_red"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_YELLOW.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_YELLOW_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_YELLOW.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_yellow_slab_from_force_brick_yellow"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_GREEN.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GREEN_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GREEN.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_green_slab_from_force_brick_green"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_BLUE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLUE_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLUE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_blue_slab_from_force_brick_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_WHITE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_WHITE_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_WHITE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_white_slab_from_force_brick_white"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_BLACK.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLACK_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BLACK.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_black_slab_from_force_brick_black"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_BROWN.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BROWN_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_BROWN.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_brown_slab_from_force_brick_brown"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_ORANGE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_ORANGE_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_ORANGE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_orange_slab_from_force_brick_orange"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_light_blue_slab_from_force_brick_light_blue"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_MAGENTA.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_MAGENTA_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_MAGENTA.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_magenta_slab_from_force_brick_magenta"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_PINK.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PINK_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PINK.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_pink_slab_from_force_brick_pink"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_light_gray_slab_from_force_brick_light_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_LIME.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIME_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_LIME.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_lime_slab_from_force_brick_lime"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_CYAN.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_CYAN_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_CYAN.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_cyan_slab_from_force_brick_cyan"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_PURPLE.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PURPLE_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_PURPLE.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_purple_slab_from_force_brick_purple"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK_GRAY.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GRAY_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK_GRAY.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_gray_slab_from_force_brick_gray"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ForceRegistry.FORCE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_SLAB.get(), 2).unlockedBy("has_bricks", has(ForceRegistry.FORCE_BRICK.get())).save(output, new ResourceLocation(Reference.MOD_ID, "force_brick_slab_from_force_brick"));

		//Smelting
		oreSmelting(output, List.of(ForceRegistry.POWER_ORE.get(), ForceRegistry.DEEPSLATE_POWER_ORE.get()),
				RecipeCategory.MISC, ForceRegistry.FORCE_GEM.get(), 0.1F, 200, "");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ForceRegistry.RAW_BACON.get()), RecipeCategory.FOOD,
						ForceRegistry.COOKED_BACON.get(), 0.1F, 200)
				.unlockedBy("has_raw_bacon", has(ForceRegistry.RAW_BACON.get()))
				.save(output, new ResourceLocation(Reference.MOD_ID, "cooked_bacon_from_smelting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ForceTags.FORCE_LOGS), RecipeCategory.MISC,
						ForceRegistry.GOLDEN_POWER_SOURCE.get(), 0.15F, 200)
				.unlockedBy("has_force_logs", has(ForceTags.FORCE_LOGS))
				.save(output);
		//Smoking
		simpleCookingRecipe(output, "smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100,
				ForceRegistry.RAW_BACON.get(), ForceRegistry.COOKED_BACON.get(), 0.1F);
		//Campfire cooking
		simpleCookingRecipe(output, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600,
				ForceRegistry.RAW_BACON.get(), ForceRegistry.COOKED_BACON.get(), 0.1F);
		//Blasting
		oreBlasting(output, List.of(ForceRegistry.POWER_ORE.get(), ForceRegistry.DEEPSLATE_POWER_ORE.get()),
				RecipeCategory.MISC, ForceRegistry.FORCE_GEM.get(), 0.1F, 100, "");

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, getGuideBook())
				.requires(Items.BOOK)
				.requires(ForceRegistry.FORCE_GEM.get())
				.unlockedBy("has_book", has(Items.BOOK))
				.unlockedBy("has_force_gem", has(ForceRegistry.FORCE_GEM.get()))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_and_you"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.BACONATOR.get())
				.pattern(" M ")
				.pattern("IPI")
				.pattern("IPI")
				.define('M', ForceRegistry.PIG_FLASK.get())
				.define('P', ItemTags.PLANKS)
				.define('I', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_pig_flask", has(ForceRegistry.PIG_FLASK.get()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GUNPOWDER)
				.pattern("##")
				.pattern("##")
				.define('#', ForceRegistry.PILE_OF_GUNPOWDER.get())
				.unlockedBy("has_pile_of_gunpowder", has(ForceRegistry.PILE_OF_GUNPOWDER.get()))
				.save(output, new ResourceLocation(Reference.MOD_ID, "gunpowder_from_pile_of_gunpowder"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.WHITE_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_WHITE)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_white_dye", has(Tags.Items.DYES_WHITE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "white_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.ORANGE_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_ORANGE)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_orange_dye", has(Tags.Items.DYES_ORANGE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "orange_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.MAGENTA_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_MAGENTA)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_magenta_dye", has(Tags.Items.DYES_MAGENTA))
				.save(output, new ResourceLocation(Reference.MOD_ID, "magenta_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_LIGHT_BLUE)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_light_blue_dye", has(Tags.Items.DYES_LIGHT_BLUE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "light_blue_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.FORCE_FURNACE.get())
				.requires(ForceTags.FORCE_FURNACES)
				.requires(Tags.Items.DYES_YELLOW)
				.unlockedBy("has_force_furnace", has(ForceTags.FORCE_FURNACES))
				.unlockedBy("has_yellow_dye", has(Tags.Items.DYES_YELLOW))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.LIME_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_LIME)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_lime_dye", has(Tags.Items.DYES_LIME))
				.save(output, new ResourceLocation(Reference.MOD_ID, "lime_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.PINK_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_PINK)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_pink_dye", has(Tags.Items.DYES_PINK))
				.save(output, new ResourceLocation(Reference.MOD_ID, "pink_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.GRAY_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_GRAY)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_gray_dye", has(Tags.Items.DYES_GRAY))
				.save(output, new ResourceLocation(Reference.MOD_ID, "gray_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_LIGHT_GRAY)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_light_gray_dye", has(Tags.Items.DYES_LIGHT_GRAY))
				.save(output, new ResourceLocation(Reference.MOD_ID, "light_gray_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.CYAN_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_CYAN)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_cyan_dye", has(Tags.Items.DYES_CYAN))
				.save(output, new ResourceLocation(Reference.MOD_ID, "cyan_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.PURPLE_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_PURPLE)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_purple_dye", has(Tags.Items.DYES_PURPLE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "purple_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.BLUE_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_BLUE)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_blue_dye", has(Tags.Items.DYES_BLUE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "blue_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.BROWN_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_BROWN)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_brown_dye", has(Tags.Items.DYES_BROWN))
				.save(output, new ResourceLocation(Reference.MOD_ID, "brown_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.GREEN_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_GREEN)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_green_dye", has(Tags.Items.DYES_GREEN))
				.save(output, new ResourceLocation(Reference.MOD_ID, "green_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.RED_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_RED)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_red_dye", has(Tags.Items.DYES_RED))
				.save(output, new ResourceLocation(Reference.MOD_ID, "red_force_furnace_from_dye"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.BLACK_FORCE_FURNACE.get())
				.requires(ForceRegistry.FORCE_FURNACE.get())
				.requires(Tags.Items.DYES_BLACK)
				.unlockedBy("has_force_furnace", has(ForceRegistry.FORCE_FURNACE.get()))
				.unlockedBy("has_black_dye", has(Tags.Items.DYES_BLACK))
				.save(output, new ResourceLocation(Reference.MOD_ID, "black_force_furnace_from_dye"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK.get(), 8)
				.pattern("BBB")
				.pattern("BFB")
				.pattern("BBB")
				.define('B', ItemTags.STONE_BRICKS)
				.define('F', ForceRegistry.FORCE_GEM)
				.unlockedBy("has_force_gem", has(ForceRegistry.FORCE_GEM.get()))
				.unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
				.save(output);
		//Dyeing Force Bricks
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_WHITE.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_WHITE)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_white_dye", has(Tags.Items.DYES_WHITE))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_ORANGE.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_ORANGE)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_orange_dye", has(Tags.Items.DYES_ORANGE))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_MAGENTA.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_MAGENTA)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_magenta_dye", has(Tags.Items.DYES_MAGENTA))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_LIGHT_BLUE)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_light_blue_dye", has(Tags.Items.DYES_LIGHT_BLUE))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_YELLOW.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_YELLOW)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_yellow_dye", has(Tags.Items.DYES_YELLOW))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIME.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_LIME)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_lime_dye", has(Tags.Items.DYES_LIME))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PINK.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_PINK)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_pink_dye", has(Tags.Items.DYES_PINK))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GRAY.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_GRAY)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_gray_dye", has(Tags.Items.DYES_GRAY))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_LIGHT_GRAY)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_light_gray_dye", has(Tags.Items.DYES_LIGHT_GRAY))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_CYAN.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_CYAN)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_cyan_dye", has(Tags.Items.DYES_CYAN))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_PURPLE.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_PURPLE)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_purple_dye", has(Tags.Items.DYES_PURPLE))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLUE.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_BLUE)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_blue_dye", has(Tags.Items.DYES_BLUE))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BROWN.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_BROWN)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_brown_dye", has(Tags.Items.DYES_BROWN))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_GREEN.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_GREEN)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_green_dye", has(Tags.Items.DYES_GREEN))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_RED.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_RED)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_red_dye", has(Tags.Items.DYES_RED))
				.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BRICK_BLACK.get())
				.requires(ForceRegistry.FORCE_BRICK.get())
				.requires(Tags.Items.DYES_BLACK)
				.unlockedBy("has_force_brick", has(ForceRegistry.FORCE_BRICK.get()))
				.unlockedBy("has_black_dye", has(Tags.Items.DYES_BLACK))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_TORCH.get(), 4)
				.pattern("G")
				.pattern("S")
				.define('G', ForceRegistry.GOLDEN_POWER_SOURCE)
				.define('S', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_golden_power_source", has(ForceRegistry.GOLDEN_POWER_SOURCE))
				.unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
				.save(output);
		//Dyeing Force Torch
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_WHITE_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_WHITE)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_WHITE))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_ORANGE_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_ORANGE)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_ORANGE))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_MAGENTA_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_MAGENTA)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_MAGENTA))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_LIGHT_BLUE_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_LIGHT_BLUE)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_LIGHT_BLUE))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_LIME_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_LIME)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_LIME))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_PINK_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_PINK)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_PINK))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_GRAY_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_GRAY)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_GRAY))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_LIGHT_GRAY_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_LIGHT_GRAY)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_LIGHT_GRAY))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_CYAN_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_CYAN)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_CYAN))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_PURPLE_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_PURPLE)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_PURPLE))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BLUE_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_BLUE)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_BLUE))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BROWN_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_BROWN)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_BROWN))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_GREEN_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_GREEN)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_GREEN))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_RED_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_RED)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_RED))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_BLACK_TORCH.get(), 8)
				.pattern("TTT")
				.pattern("TDT")
				.pattern("TTT")
				.define('T', ForceRegistry.FORCE_TORCH)
				.define('D', Tags.Items.DYES_BLACK)
				.unlockedBy("has_force_torch", has(ForceRegistry.FORCE_TORCH))
				.unlockedBy("has_dye", has(Tags.Items.DYES_BLACK))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_ARROW.get(), 6)
				.pattern("X")
				.pattern("#")
				.pattern("Y")
				.define('X', ForceTags.FORCE_NUGGET)
				.define('#', ForceTags.FORCE_ROD)
				.define('Y', Tags.Items.FEATHERS)
				.unlockedBy("has_force_nugget", has(ForceTags.FORCE_NUGGET))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.unlockedBy("has_feather", has(Tags.Items.FEATHERS))
				.save(output);

		//Tools
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_AXE.get())
				.pattern("FF")
				.pattern("FS")
				.pattern(" S")
				.define('F', ForceTags.FORCE_INGOT)
				.define('S', ForceTags.FORCE_ROD)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_PICKAXE.get())
				.pattern("FFF")
				.pattern(" S ")
				.pattern(" S ")
				.define('F', ForceTags.FORCE_INGOT)
				.define('S', ForceTags.FORCE_ROD)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_SHOVEL.get())
				.pattern("F")
				.pattern("S")
				.pattern("S")
				.define('F', ForceTags.FORCE_INGOT)
				.define('S', ForceTags.FORCE_ROD)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ForceRegistry.FORCE_SWORD.get())
				.pattern("F")
				.pattern("F")
				.pattern("S")
				.define('F', ForceTags.FORCE_INGOT)
				.define('S', ForceTags.FORCE_ROD)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
//		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_HOE.get()) TODO: Add Hoe?
//				.pattern("FF")
//				.pattern(" S")
//				.pattern(" S")
//				.define('F', ForceTags.FORCE_INGOT)
//				.define('S', ForceTags.FORCE_ROD)
//				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
//				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
//				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_SHEARS.get())
				.pattern("F ")
				.pattern(" F")
				.define('F', ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ForceRegistry.FORCE_BOW.get())
				.pattern(" #S")
				.pattern("#NS")
				.pattern(" #S")
				.define('N', ForceTags.FORCE_NUGGET)
				.define('S', ForceTags.FORCE_ROD)
				.define('#', Tags.Items.STRING)
				.unlockedBy("has_force_nugget", has(ForceTags.FORCE_NUGGET))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
//		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ForceRegistry.FORCE_CROSSBOW.get()) TODO: Add crossbow?
//				.pattern("SIS")
//				.pattern("#T#")
//				.pattern(" S ")
//				.define('I', ForceTags.FORCE_INGOT)
//				.define('S', ForceTags.FORCE_ROD)
//				.define('#', Tags.Items.STRING)
//				.define('T', Tags.Items.STRING)
//				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
//				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
//				.unlockedBy("has_string", has(Tags.Items.STRING))
//				.unlockedBy("has_tripwire_hook", has(Items.TRIPWIRE_HOOK))
//				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_WRENCH.get())
				.pattern("F F")
				.pattern(" G ")
				.pattern(" F ")
				.define('F', ForceTags.FORCE_INGOT)
				.define('G', ForceRegistry.FORCE_GEAR)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_MITT.get())
				.pattern("CI ")
				.pattern("CFI")
				.pattern("CLL")
				.define('F', ForceTags.FORCE_INGOT)
				.define('L', Tags.Items.LEATHER)
				.define('C', Tags.Items.COBBLESTONE_NORMAL)
				.define('I', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.unlockedBy("has_cobblestone", has(Tags.Items.COBBLESTONE_NORMAL))
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.save(output);
		//Rod
		ItemStack rodStack = new ItemStack(ForceRegistry.FORCE_ROD.get());
		CompoundTag rodTag = rodStack.getOrCreateTag();
		rodTag.putBoolean("ForceInfused", true);
		rodStack.setTag(rodTag);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, rodStack)
				.pattern("  F")
				.pattern(" S ")
				.pattern("N  ")
				.define('F', ForceTags.FORCE_INGOT)
				.define('S', ForceTags.FORCE_ROD)
				.define('N', ForceTags.FORCE_NUGGET)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_nugget", has(ForceTags.FORCE_NUGGET))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output);
		ItemStack damagedRod = rodStack.copy();
		damagedRod.setDamageValue(73);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, damagedRod)
				.pattern("  F")
				.pattern(" S ")
				.pattern("N  ")
				.define('F', ForceTags.FORCE_INGOT)
				.define('S', Items.STICK)
				.define('N', ForceTags.FORCE_NUGGET)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_force_nugget", has(ForceTags.FORCE_NUGGET))
				.unlockedBy("has_force_rod", has(ForceTags.FORCE_ROD))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_rod_from_stick"));
		//Armor
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_BOOTS.get())
				.pattern("F F")
				.pattern("F F")
				.define('F', ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_LEGS.get())
				.pattern("FFF")
				.pattern("F F")
				.pattern("F F")
				.define('F', ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_CHEST.get())
				.pattern("F F")
				.pattern("FFF")
				.pattern("FFF")
				.define('F', ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_HELMET.get())
				.pattern("FFF")
				.pattern("F F")
				.define('F', ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		//Stick
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_STICK.get(), 4)
				.pattern(" W")
				.pattern("W ")
				.define('W', ForceRegistry.FORCE_PLANKS.get())
				.unlockedBy("has_force_planks", has(ForceRegistry.FORCE_PLANKS.get()))
				.save(output);
		//Planks
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ForceRegistry.FORCE_PLANKS.get(), 4)
				.requires(ForceTags.FORCE_LOGS)
				.unlockedBy("has_force_log", has(ForceTags.FORCE_LOGS))
				.save(output);
		//Force Belt
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_BELT.get())
				.pattern("LWL")
				.pattern("FIF")
				.pattern("LWL")
				.define('F', ForceTags.FORCE_INGOT)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('L', Tags.Items.LEATHER)
				.define('W', ItemTags.WOOL)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.unlockedBy("has_wool", has(ItemTags.WOOL))
				.save(output);
		//Force Engine
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ForceRegistry.FORCE_ENGINE.get())
				.pattern("FFF")
				.pattern(" # ")
				.pattern("GPG")
				.define('F', ForceTags.FORCE_INGOT)
				.define('#', Tags.Items.GLASS)
				.define('G', ForceTags.FORCE_GEAR)
				.define('P', Items.PISTON)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_glass", has(Tags.Items.GLASS))
				.unlockedBy("has_force_gear", has(ForceTags.FORCE_GEAR))
				.unlockedBy("has_piston", has(Items.PISTON))
				.save(output);
		//Force Furnace
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ForceRegistry.FORCE_FURNACE.get())
				.pattern(" F ")
				.pattern("F F")
				.pattern("IUI")
				.define('F', ForceTags.FORCE_INGOT)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('U', Items.FURNACE)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.unlockedBy("has_furnace", has(Items.FURNACE))
				.save(output);
		//Force Gear
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_GEAR.get())
				.pattern(" F ")
				.pattern("F F")
				.pattern(" F ")
				.define('F', ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		//Force Nugget
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.FORCE_NUGGET.get(), 9)
				.requires(ForceTags.FORCE_INGOT)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.save(output);
		//Force Pack
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ForceRegistry.FORCE_PACK.get())
				.pattern("FLF")
				.pattern("LCL")
				.pattern("FLF")
				.define('F', ForceTags.FORCE_INGOT)
				.define('L', Tags.Items.LEATHER)
				.define('C', ItemTags.PLANKS)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.unlockedBy("has_planks", has(ItemTags.PLANKS))
				.save(output);
		//Force Pack Upgrade
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_PACK_UPGRADE.get())
				.pattern("WLW")
				.pattern("LIL")
				.pattern("WLW")
				.define('I', ForceTags.FORCE_INGOT)
				.define('L', Tags.Items.LEATHER)
				.define('W', ItemTags.WOOL)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.unlockedBy("has_wool", has(ItemTags.WOOL))
				.save(output);
		//Force Flask
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_FLASK.get(), 3)
				.pattern(" N ")
				.pattern("G G")
				.pattern(" G ")
				.define('N', ForceTags.FORCE_NUGGET)
				.define('G', Tags.Items.GLASS)
				.unlockedBy("has_force_nugget", has(ForceTags.FORCE_NUGGET))
				.unlockedBy("has_glass", has(Tags.Items.GLASS))
				.save(output);
		NoRemainderShapedBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_FLASK.get())
				.pattern("E")
				.define('E', ForceTags.ENTITY_FLASKS)
				.unlockedBy("has_entity_flask", has(ForceTags.ENTITY_FLASKS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_flask_from_entity_flask"));
		//Fortune Cookie
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ForceRegistry.FORTUNE_COOKIE.get())
				.requires(Items.COOKIE)
				.requires(Items.PAPER)
				.unlockedBy("has_cookie", has(Items.COOKIE))
				.unlockedBy("has_paper", has(Items.PAPER))
				.save(output);
		//Force Ingot recipes
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.FORCE_INGOT.get(), 3)
				.requires(Tags.Items.INGOTS_GOLD)
				.requires(Tags.Items.INGOTS_GOLD)
				.requires(ForceTags.FORCE_GEM)
				.unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
				.unlockedBy("has_force_gem", has(ForceTags.FORCE_GEM))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_ingot_from_gold"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ForceRegistry.FORCE_INGOT.get(), 2)
				.requires(Tags.Items.INGOTS_IRON)
				.requires(Tags.Items.INGOTS_IRON)
				.requires(ForceTags.FORCE_GEM)
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
				.unlockedBy("has_force_gem", has(ForceTags.FORCE_GEM))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_ingot_from_iron"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.FORCE_INGOT.get())
				.pattern("NNN")
				.pattern("NNN")
				.pattern("NNN")
				.define('N', ForceTags.FORCE_NUGGET)
				.unlockedBy("has_force_nugget", has(ForceTags.FORCE_NUGGET))
				.save(output, new ResourceLocation(Reference.MOD_ID, "force_ingot_from_nuggets"));
		//Item Card
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.ITEM_CARD.get())
				.pattern("PRP")
				.pattern("PIP")
				.pattern("PBP")
				.define('P', Items.PAPER)
				.define('R', Tags.Items.DYES_RED)
				.define('I', ForceTags.FORCE_INGOT)
				.define('B', Tags.Items.DYES_BLUE)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_red_dye", has(Tags.Items.DYES_RED))
				.unlockedBy("has_blue_dye", has(Tags.Items.DYES_BLUE))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.ITEM_CARD.get())
				.pattern("PPP")
				.pattern("RIB")
				.pattern("PPP")
				.define('P', Items.PAPER)
				.define('R', Tags.Items.DYES_RED)
				.define('I', ForceTags.FORCE_INGOT)
				.define('B', Tags.Items.DYES_BLUE)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_red_dye", has(Tags.Items.DYES_RED))
				.unlockedBy("has_blue_dye", has(Tags.Items.DYES_BLUE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "item_card_flipped"));
		NoRemainderShapedBuilder.shaped(RecipeCategory.MISC, ForceRegistry.ITEM_CARD.get())
				.pattern("E")
				.define('E', ForceRegistry.ITEM_CARD.get())
				.unlockedBy("has_item_card", has(ForceRegistry.ITEM_CARD.get()))
				.save(output, new ResourceLocation(Reference.MOD_ID, "item_card_empty"));
		//Magnet Glove
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.MAGNET_GLOVE.get())
				.pattern(" N ")
				.pattern("FCF")
				.pattern(" F ")
				.define('F', ForceTags.FORCE_INGOT)
				.define('C', Items.COMPASS)
				.define('N', Tags.Items.NETHER_STARS)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_compass", has(Items.COMPASS))
				.unlockedBy("has_nether_star", has(Tags.Items.NETHER_STARS))
				.save(output);
		//Red Potion
		NoRemainderShapedBuilder.shaped(RecipeCategory.MISC, ForceRegistry.RED_POTION.get())
				.pattern("RR")
				.pattern("RR")
				.pattern("RF")
				.define('R', ForceRegistry.RED_CHU_JELLY.get())
				.define('F', ForceRegistry.FORCE_FILLED_FORCE_FLASK.get())
				.unlockedBy("has_red_chu_jelly", has(ForceRegistry.RED_CHU_JELLY.get()))
				.unlockedBy("has_force_filled_force_flask", has(ForceRegistry.FORCE_FILLED_FORCE_FLASK.get()))
				.save(output);
		//Slimeball
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL)
				.requires(ForceTags.CHU_JELLY)
				.unlockedBy("has_chu_jelly", has(ForceTags.CHU_JELLY))
				.save(output, new ResourceLocation(Reference.MOD_ID, "slime_ball_from_chu_jelly"));
		//Snow Cookie
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ForceRegistry.SNOW_COOKIE.get())
				.requires(Items.SNOWBALL)
				.unlockedBy("has_snowball", has(Items.SNOWBALL))
				.save(output);
		//Soul Waver
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ForceRegistry.SOUL_WAFER.get())
				.pattern(" G ")
				.pattern("BSF")
				.pattern(" L ")
				.define('G', Tags.Items.GUNPOWDER)
				.define('S', ForceRegistry.SNOW_COOKIE.get())
				.define('B', Tags.Items.BONES)
				.define('F', Items.ROTTEN_FLESH)
				.define('L', Tags.Items.DYES_LIGHT_BLUE)
				.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
				.unlockedBy("has_snow_cookie", has(ForceRegistry.SNOW_COOKIE.get()))
				.unlockedBy("has_bone", has(Tags.Items.BONES))
				.unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
				.save(output);
		//Treasure Core
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.TREASURE_CORE.get())
				.pattern("FGF")
				.pattern("DND")
				.pattern("FGF")
				.define('F', ForceTags.FORCE_INGOT)
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('N', Tags.Items.NETHER_STARS)
				.unlockedBy("has_force_ingot", has(ForceTags.FORCE_INGOT))
				.unlockedBy("has_gold_block", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.unlockedBy("has_nether_star", has(Tags.Items.NETHER_STARS))
				.save(output);
		//Spoils bag
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.SPOILS_BAG.get())
				.pattern("##")
				.pattern("##")
				.define('#', ForceRegistry.LIFE_CARD.get())
				.unlockedBy("has_card", has(ForceRegistry.LIFE_CARD.get()))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.SPOILS_BAG_T2.get())
				.pattern("##")
				.pattern("##")
				.define('#', ForceRegistry.DARKNESS_CARD.get())
				.unlockedBy("has_card", has(ForceRegistry.DARKNESS_CARD.get()))
				.save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForceRegistry.SPOILS_BAG_T3.get())
				.pattern("##")
				.pattern("##")
				.define('#', ForceRegistry.UNDEATH_CARD.get())
				.unlockedBy("has_card", has(ForceRegistry.UNDEATH_CARD.get()))
				.save(output);

		//Add infuser recipes
		addInfuserRecipes(output);

		//Add multiple output recipes
		addMultiOutputRecipes(output);

		//Add Transmutation recipes
		addTransmutationRecipes(output);
	}

	public static ItemStack getGuideBook() {
		Item guideBook = BuiltInRegistries.ITEM.get(new ResourceLocation("patchouli", "guide_book"));
		if (guideBook != null) {
			ItemStack patchouliBook = new ItemStack(guideBook);
			CompoundTag tag = patchouliBook.getOrCreateTag();
			tag.putString("patchouli:book", "forcecraft:force_and_you");
			return patchouliBook;
		}
		return ItemStack.EMPTY;
	}

	private void addInfuserRecipes(RecipeOutput output) {
		//0
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.CLAW.get()),
						Ingredient.of(ForceTags.VALID_DAMAGE_TOOLS),
						UpgradeBookTier.ZERO,
						20
				).modifierType(InfuserModifierType.DAMAGE)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_damage"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceTags.FORCE_NUGGET),
						Ingredient.of(ForceTags.VALID_KNOCKBACK_TOOLS),
						UpgradeBookTier.ZERO,
						20
				).modifierType(InfuserModifierType.FORCE)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_knockback"));

		//1
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.GOLDEN_POWER_SOURCE.get()),
						Ingredient.of(ForceTags.VALID_HEAT_TOOLS),
						UpgradeBookTier.ONE,
						60
				).modifierType(InfuserModifierType.HEAT)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_heat"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.FORCE_LOG.get()),
						Ingredient.of(ForceTags.VALID_LUMBER_TOOLS),
						UpgradeBookTier.ONE,
						60
				).modifierType(InfuserModifierType.LUMBERJACK)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_lumberjack"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.SUGAR),
						Ingredient.of(ForceTags.VALID_SPEED_TOOLS),
						UpgradeBookTier.ONE,
						20
				).modifierType(InfuserModifierType.SPEED)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_speed"));

		//2
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.SNOW_COOKIE.get()),
						Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						UpgradeBookTier.TWO,
						20
				).output(ForceRegistry.FREEZING_CORE.get())
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/convert_freezing"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.FLINT),
						Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						UpgradeBookTier.TWO,
						20
				).output(ForceRegistry.GRINDING_CORE.get())
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/convert_grinding"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.SUGAR),
						Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						UpgradeBookTier.TWO,
						20
				).output(ForceRegistry.SPEED_CORE.get())
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/convert_speed"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.EXPERIENCE_BOTTLE),
						Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						UpgradeBookTier.TWO,
						20
				).output(ForceRegistry.EXPERIENCE_CORE.get())
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/convert_xp"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.EXPERIENCE_BOTTLE),
						Ingredient.of(Items.BOOK),
						UpgradeBookTier.TWO,
						20
				).output(ForceRegistry.HEAT_CORE.get())
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/convert_xp_book"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceTags.FORTUNE),
						Ingredient.of(ForceTags.VALID_LUCKY_TOOLS),
						UpgradeBookTier.TWO,
						20
				).modifierType(InfuserModifierType.DAMAGE)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_fortune"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.SNOW_COOKIE.get()),
						Ingredient.of(ForceTags.VALID_FREEZING_TOOLS),
						UpgradeBookTier.TWO,
						20
				).modifierType(InfuserModifierType.FREEZING)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_freezing"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.BLUE_DYE),
						Ingredient.of(ForceRegistry.FORCE_SHEARS.get()),
						UpgradeBookTier.TWO,
						20
				).modifierType(InfuserModifierType.RAINBOW)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_rainbow"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE.get()),
						Ingredient.of(ForceRegistry.FORCE_PACK.get()),
						UpgradeBookTier.TWO,
						20
				).modifierType(InfuserModifierType.PACK1)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_pack_upgrade1"));

		//3
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.ARROW),
						Ingredient.of(ForceTags.VALID_BLEEDING_TOOLS),
						UpgradeBookTier.THREE,
						20
				).modifierType(InfuserModifierType.BLEEDING)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_bleeding"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.COBWEB),
						Ingredient.of(ForceTags.VALID_SILKY_TOOLS),
						UpgradeBookTier.THREE,
						20
				).modifierType(InfuserModifierType.SILK)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_silk"));
		final ItemStack invisibility = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY);
		InfuseRecipeBuilder.infuse(
						NBTIngredient.of(false, invisibility),
						Ingredient.of(ForceTags.VALID_CAMO_TOOLS),
						UpgradeBookTier.THREE,
						20
				).modifierType(InfuserModifierType.CAMO)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_camo"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE.get()),
						Ingredient.of(ForceRegistry.FORCE_PACK.get()),
						UpgradeBookTier.THREE,
						20
				).modifierType(InfuserModifierType.PACK2)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_pack_upgrade2"));

		//4
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.GOLDEN_POWER_SOURCE.get()),
						Ingredient.of(ForceRegistry.UPGRADE_CORE.get()),
						UpgradeBookTier.FOUR,
						60
				).output(ForceRegistry.HEAT_CORE.get())
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/convert_heat"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.SPIDER_EYE),
						Ingredient.of(ForceTags.VALID_BANE_TOOLS),
						UpgradeBookTier.FOUR,
						20
				).modifierType(InfuserModifierType.BANE)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_bane"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE.get()),
						Ingredient.of(ForceRegistry.FORCE_PACK.get()),
						UpgradeBookTier.FOUR,
						20
				).modifierType(InfuserModifierType.PACK3)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_pack_upgrade3"));

		//5
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.FORCE_PACK_UPGRADE.get()),
						Ingredient.of(ForceRegistry.FORCE_PACK.get()),
						UpgradeBookTier.FIVE,
						20
				).modifierType(InfuserModifierType.PACK4)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_pack_upgrade4"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.GHAST_TEAR),
						Ingredient.of(ForceTags.VALID_HEALING_TOOLS),
						UpgradeBookTier.FIVE,
						20
				).modifierType(InfuserModifierType.HEALING)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_healing"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.FEATHER),
						Ingredient.of(ForceTags.VALID_WING_TOOLS),
						UpgradeBookTier.FIVE,
						20
				).modifierType(InfuserModifierType.WING)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_wing"));

		//6
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.CLOCK),
						Ingredient.of(ForceRegistry.FORCE_TORCH.get()),
						UpgradeBookTier.SIX,
						60
				).output(ForceRegistry.TIME_TORCH.get())
				.save(output.withConditions(new TorchEnabledCondition()), new ResourceLocation(Reference.MOD_ID, "infuser/convert_time_torch"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.OBSIDIAN),
						Ingredient.of(ForceTags.VALID_STURDY_TOOLS),
						UpgradeBookTier.SIX,
						20
				).modifierType(InfuserModifierType.STURDY)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_sturdy"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceTags.ENDER),
						Ingredient.of(ForceTags.VALID_ENDER_TOOLS),
						UpgradeBookTier.SIX,
						20
				).modifierType(InfuserModifierType.ENDER)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_ender"));
		//7
		InfuseRecipeBuilder.infuse(
						Ingredient.of(Items.GLOWSTONE),
						Ingredient.of(ForceTags.VALID_LIGHT_TOOLS),
						UpgradeBookTier.SEVEN,
						20
				).modifierType(InfuserModifierType.LIGHT)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_light"));
		InfuseRecipeBuilder.infuse(
						Ingredient.of(ForceRegistry.TREASURE_CORE.get()),
						Ingredient.of(ForceTags.VALID_LIGHT_TOOLS),
						UpgradeBookTier.SEVEN,
						20
				).modifierType(InfuserModifierType.TREASURE)
				.save(output, new ResourceLocation(Reference.MOD_ID, "infuser/infuse_treasure"));
	}

	private void addMultiOutputRecipes(RecipeOutput output) {
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.RODS_BLAZE), 0.1F, 200)
				.setResult(Items.BONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/bone_from_blaze_rod"));
		MultipleOutputRecipeBuilder.freezing(Items.BLAZE_POWDER, 0.1F, 200)
				.setResult(Items.BONE_MEAL, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/bone_meal_from_blaze_powder"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.INGOTS_NETHER_BRICK), 0.1F, 200)
				.setResult(Items.BRICK, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/brick_from_nether_brick"));
		MultipleOutputRecipeBuilder.freezing(Items.ROTTEN_FLESH, 0.1F, 200)
				.setResult(Items.LEATHER, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/leather_from_rotten_flesh"));
		final ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
		MultipleOutputRecipeBuilder.freezing(NBTIngredient.of(false, waterBottle), 0.1F, 200)
				.setResult(Items.ICE, 1)
				.setResult(Items.GLASS_BOTTLE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/ice_from_water_bottle"));
		MultipleOutputRecipeBuilder.freezing(Items.WATER_BUCKET, 0.1F, 200)
				.setResult(Items.PACKED_ICE, 1)
				.setResult(Items.BUCKET, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/ice_from_water_bucket"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.NETHERRACK), 0.1F, 200)
				.setResult(Items.COBBLESTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/cobblestone_from_netherrack"));
		MultipleOutputRecipeBuilder.freezing(Items.LAVA_BUCKET, 0.1F, 200)
				.setResult(Items.OBSIDIAN, 1)
				.setResult(Items.BUCKET, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/obsidian_from_lava_bucket"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.SAND_COLORLESS), 0.1F, 200)
				.setResult(Items.SANDSTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/sandstone_from_sand"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.SAND_RED), 0.1F, 200)
				.setResult(Items.RED_SANDSTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/sandstone_from_red_sand"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.SLIMEBALLS), 0.1F, 200)
				.setResult(Items.SNOWBALL, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/snowball_from_slimeball"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.COBBLESTONE_NORMAL), 0.1F, 200)
				.setResult(Items.STONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/stone_from_cobblestone"));
		MultipleOutputRecipeBuilder.freezing(Ingredient.of(Tags.Items.STONE), 0.1F, 200)
				.setResult(Items.STONE_BRICKS, 1)
				.unlockedBy("has_core", has(ForceRegistry.FREEZING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "freezing/stone_bricks_from_stone"));

		//Oak
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(Tags.Items.BOOKSHELVES), 1.0F, 0.1F, 400)
				.setResult(Items.OAK_PLANKS, 6)
				.setResult(Items.PAPER, 9)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/oak_planks_from_bookshelf"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(Tags.Items.CHESTS_WOODEN), 1.0F, 0.1F, 400)
				.setResult(Items.OAK_PLANKS, 8)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/oak_planks_from_chest"));
		MultipleOutputRecipeBuilder.grinding(Items.OAK_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/oak_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.OAK_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.OAK_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/oak_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.OAK_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/oak_planks_from_pressure_plate"));
		MultipleOutputRecipeBuilder.grinding(Items.CRAFTING_TABLE, 1.0F, 0.1F, 400)
				.setResult(Items.OAK_PLANKS, 4)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/oak_planks_from_crafting_table"));
		//Acacia
		MultipleOutputRecipeBuilder.grinding(Items.ACACIA_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.ACACIA_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/acacia_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.ACACIA_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.ACACIA_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/acacia_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.ACACIA_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.ACACIA_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/acacia_planks_from_pressure_plate"));
		//Birch
		MultipleOutputRecipeBuilder.grinding(Items.BIRCH_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.BIRCH_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/birch_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.BIRCH_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.BIRCH_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/birch_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.BIRCH_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.BIRCH_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/birch_planks_from_pressure_plate"));
		//Spruce
		MultipleOutputRecipeBuilder.grinding(Items.SPRUCE_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.SPRUCE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/spruce_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.SPRUCE_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.SPRUCE_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/spruce_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.SPRUCE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.SPRUCE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/spruce_planks_from_pressure_plate"));
		//Jungle
		MultipleOutputRecipeBuilder.grinding(Items.JUNGLE_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.JUNGLE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/jungle_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.JUNGLE_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.JUNGLE_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/jungle_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.JUNGLE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.JUNGLE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/jungle_planks_from_pressure_plate"));
		//Cherry
		MultipleOutputRecipeBuilder.grinding(Items.CHERRY_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.CHERRY_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/cherry_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.CHERRY_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.CHERRY_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/cherry_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.CHERRY_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.CHERRY_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/cherry_planks_from_pressure_plate"));
		//Mangrove
		MultipleOutputRecipeBuilder.grinding(Items.MANGROVE_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.MANGROVE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/mangrove_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.MANGROVE_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.MANGROVE_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/mangrove_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.MANGROVE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.MANGROVE_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/mangrove_planks_from_pressure_plate"));
		//Dark_Oak
		MultipleOutputRecipeBuilder.grinding(Items.DARK_OAK_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.DARK_OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/dark_oak_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.DARK_OAK_LOGS), 1.0F, 0.1F, 400)
				.setResult(Items.DARK_OAK_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/dark_oak_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.DARK_OAK_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.DARK_OAK_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/dark_oak_planks_from_pressure_plate"));
		//Crimson
		MultipleOutputRecipeBuilder.grinding(Items.CRIMSON_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.CRIMSON_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/crimson_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.CRIMSON_STEMS), 1.0F, 0.1F, 400)
				.setResult(Items.CRIMSON_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/crimson_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.CRIMSON_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.CRIMSON_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/crimson_planks_from_pressure_plate"));
		//Warped
		MultipleOutputRecipeBuilder.grinding(Items.WARPED_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.WARPED_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/warped_planks_from_door"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.WARPED_STEMS), 1.0F, 0.1F, 400)
				.setResult(Items.WARPED_PLANKS, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/warped_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Items.WARPED_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.WARPED_PLANKS, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/warped_planks_from_pressure_plate"));
		//Force
		MultipleOutputRecipeBuilder.grinding(ForceRegistry.FORCE_LOG.get(), 1.0F, 0.1F, 400)
				.setResult(ForceRegistry.FORCE_PLANKS.get(), 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/force_planks_from_log"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ForceTags.FORCE_FURNACES), 1.0F, 0.1F, 400)
				.setResult(ForceRegistry.FORCE_INGOT.get(), 3)
				.setResult(Items.IRON_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/ingots_from_force_furnace"));

		//Flint
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(Tags.Items.GRAVEL), 1.0F, 0.1F, 400)
				.setResult(Items.FLINT, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/flint_from_gravel"));
		//Iron
		MultipleOutputRecipeBuilder.grinding(Items.IRON_DOOR, 1.0F, 0.1F, 400)
				.setResult(Items.IRON_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/iron_ingot_from_door"));
		//Paper
		MultipleOutputRecipeBuilder.grinding(Items.BOOK, 1.0F, 0.1F, 400)
				.setResult(Items.PAPER, 3)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/paper_from_book"));

		//String
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.WOOL), 1.0F, 0.1F, 400)
				.setResult(ForceRegistry.FORCE_PLANKS.get(), 4)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/string_from_wool"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(ItemTags.WOOL_CARPETS), 1.0F, 0.1F, 400)
				.setResult(ForceRegistry.FORCE_PLANKS.get(), 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/string_from_wool_carpet"));

		//Pressure plates
		MultipleOutputRecipeBuilder.grinding(Items.STONE_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.STONE, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/stone_from_pressure_plate"));
		MultipleOutputRecipeBuilder.grinding(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.GOLD_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/gold_ingot_from_light_weighted_pressure_plate"));
		MultipleOutputRecipeBuilder.grinding(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, 1.0F, 0.1F, 400)
				.setResult(Items.IRON_INGOT, 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/iron_ingot_from_heavy_weighted_pressure_plate"));

		//Sand
		MultipleOutputRecipeBuilder.grinding(Items.SANDSTONE, 1.0F, 0.1F, 400)
				.setResult(Items.SAND, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/sand_from_sandstone"));
		MultipleOutputRecipeBuilder.grinding(Items.RED_SANDSTONE, 1.0F, 0.1F, 400)
				.setResult(Items.RED_SAND, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/sand_from_red_sandstone"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(Tags.Items.COBBLESTONE_NORMAL), 1.0F, 0.1F, 400)
				.setResult(Items.COBBLESTONE, 1)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/sand_from_cobblestone"));

		//Cobblestone
		MultipleOutputRecipeBuilder.grinding(Items.FURNACE, 1.0F, 0.1F, 400)
				.setResult(Items.COBBLESTONE, 8)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/cobblestone_from_furnace"));

		//Bacon
		MultipleOutputRecipeBuilder.grinding(Items.PORKCHOP, 1.0F, 0.1F, 400)
				.setResult(ForceRegistry.RAW_BACON.get(), 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/raw_bacon_from_porkchop"));
		MultipleOutputRecipeBuilder.grinding(Items.COOKED_PORKCHOP, 1.0F, 0.1F, 400)
				.setResult(ForceRegistry.COOKED_BACON.get(), 2)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/cooked_bacon_from_cooked_porkchop"));

		//Powder
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(Tags.Items.RODS_BLAZE), 1.0F, 0.1F, 400)
				.setResult(Items.BLAZE_POWDER, 6)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/blaze_powder_from_blaze_rod"));
		MultipleOutputRecipeBuilder.grinding(Ingredient.of(Tags.Items.BONES), 1.0F, 0.1F, 400)
				.setResult(Items.BONE_MEAL, 5)
				.unlockedBy("has_core", has(ForceRegistry.GRINDING_CORE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "grinding/bone_meal_from_bone"));
	}

	private void addTransmutationRecipes(RecipeOutput output) {
		//Mushrooms
		TransmutationRecipeBuilder.transmutation(Items.BROWN_MUSHROOM)
				.requires(Items.RED_MUSHROOM)
				.unlockedBy("has_red_mushroom", has(Items.RED_MUSHROOM))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/red_to_brown_mushroom"));
		TransmutationRecipeBuilder.transmutation(Items.RED_MUSHROOM)
				.requires(Items.BROWN_MUSHROOM)
				.unlockedBy("has_brown_mushroom", has(Items.BROWN_MUSHROOM))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/brown_to_red_mushroom"));

		//Raw food
		TransmutationRecipeBuilder.transmutation(Items.BEEF)
				.requires(Items.CHICKEN)
				.unlockedBy("has_chicken", has(Items.CHICKEN))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/chicken_to_beef"));
		TransmutationRecipeBuilder.transmutation(Items.CHICKEN)
				.requires(Items.MUTTON)
				.unlockedBy("has_mutton", has(Items.MUTTON))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/mutton_to_chicken"));
		TransmutationRecipeBuilder.transmutation(Items.MUTTON)
				.requires(Items.PORKCHOP)
				.unlockedBy("has_porkchop", has(Items.PORKCHOP))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/porkchop_to_mutton"));
		TransmutationRecipeBuilder.transmutation(Items.PORKCHOP)
				.requires(Items.BEEF)
				.unlockedBy("has_beef", has(Items.BEEF))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/beef_to_porkchop"));

		//Cooked food
		TransmutationRecipeBuilder.transmutation(Items.COOKED_BEEF)
				.requires(Items.COOKED_CHICKEN)
				.unlockedBy("has_chicken", has(Items.COOKED_CHICKEN))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/cooked_chicken_to_beef"));
		TransmutationRecipeBuilder.transmutation(Items.COOKED_CHICKEN)
				.requires(Items.COOKED_MUTTON)
				.unlockedBy("has_mutton", has(Items.COOKED_MUTTON))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/cooked_mutton_to_chicken"));
		TransmutationRecipeBuilder.transmutation(Items.COOKED_MUTTON)
				.requires(Items.COOKED_PORKCHOP)
				.unlockedBy("has_porkchop", has(Items.COOKED_PORKCHOP))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/cooked_porkchop_to_mutton"));
		TransmutationRecipeBuilder.transmutation(Items.COOKED_PORKCHOP)
				.requires(Items.COOKED_BEEF)
				.unlockedBy("has_beef", has(Items.COOKED_BEEF))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/cooked_beef_to_porkchop"));

		//Flowers
		TransmutationRecipeBuilder.transmutation(Items.DANDELION)
				.requires(Items.POPPY)
				.unlockedBy("has_poppy", has(Items.POPPY))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/poppy_to_dandelion"));
		TransmutationRecipeBuilder.transmutation(Items.POPPY)
				.requires(Items.DANDELION)
				.unlockedBy("has_dandelion", has(Items.DANDELION))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/dandelion_to_poppy"));

		//Diamond
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 2)
				.requires(Items.DIAMOND_AXE)
				.unlockedBy("has_tool", has(Items.DIAMOND_AXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_axe"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 2)
				.requires(Items.DIAMOND_HOE)
				.unlockedBy("has_tool", has(Items.DIAMOND_HOE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_hoe"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 1)
				.requires(Items.DIAMOND_SHOVEL)
				.unlockedBy("has_tool", has(Items.DIAMOND_SHOVEL))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_shovel"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 2)
				.requires(Items.DIAMOND_SWORD)
				.unlockedBy("has_tool", has(Items.DIAMOND_SWORD))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_sword"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 2)
				.requires(Items.DIAMOND_PICKAXE)
				.unlockedBy("has_tool", has(Items.DIAMOND_PICKAXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_pickaxe"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 4)
				.requires(Items.DIAMOND_BOOTS)
				.unlockedBy("has_armor", has(Items.DIAMOND_BOOTS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_boots"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 7)
				.requires(Items.DIAMOND_LEGGINGS)
				.unlockedBy("has_armor", has(Items.DIAMOND_LEGGINGS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_leggings"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 8)
				.requires(Items.DIAMOND_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.DIAMOND_CHESTPLATE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_chestplate"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 5)
				.requires(Items.DIAMOND_HELMET)
				.unlockedBy("has_armor", has(Items.DIAMOND_HELMET))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_helmet"));
		TransmutationRecipeBuilder.transmutation(Items.DIAMOND, 4)
				.requires(Items.DIAMOND_HORSE_ARMOR)
				.unlockedBy("has_horse_armor", has(Items.DIAMOND_HORSE_ARMOR))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/diamond_from_horse_armor"));

		//Gold
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 2)
				.requires(Items.GOLDEN_AXE)
				.unlockedBy("has_tool", has(Items.GOLDEN_AXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_axe"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 2)
				.requires(Items.GOLDEN_HOE)
				.unlockedBy("has_tool", has(Items.GOLDEN_HOE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_hoe"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 1)
				.requires(Items.GOLDEN_SHOVEL)
				.unlockedBy("has_tool", has(Items.GOLDEN_SHOVEL))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_shovel"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 2)
				.requires(Items.GOLDEN_SWORD)
				.unlockedBy("has_tool", has(Items.GOLDEN_SWORD))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_sword"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 2)
				.requires(Items.GOLDEN_PICKAXE)
				.unlockedBy("has_tool", has(Items.GOLDEN_PICKAXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_pickaxe"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 4)
				.requires(Items.GOLDEN_BOOTS)
				.unlockedBy("has_armor", has(Items.GOLDEN_BOOTS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_boots"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 7)
				.requires(Items.GOLDEN_LEGGINGS)
				.unlockedBy("has_armor", has(Items.GOLDEN_LEGGINGS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_leggings"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 8)
				.requires(Items.GOLDEN_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.GOLDEN_CHESTPLATE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_chestplate"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 5)
				.requires(Items.GOLDEN_HELMET)
				.unlockedBy("has_armor", has(Items.GOLDEN_HELMET))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_helmet"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 4)
				.requires(Items.GOLDEN_HORSE_ARMOR)
				.unlockedBy("has_horse_armor", has(Items.GOLDEN_HORSE_ARMOR))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_horse_armor"));
		TransmutationRecipeBuilder.transmutation(Items.GOLD_INGOT, 4)
				.requires(Items.CLOCK)
				.unlockedBy("has_clock", has(Items.CLOCK))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/gold_ingot_from_clock"));

		//Iron
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 2)
				.requires(Items.IRON_AXE)
				.unlockedBy("has_tool", has(Items.IRON_AXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_axe"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 2)
				.requires(Items.IRON_HOE)
				.unlockedBy("has_tool", has(Items.IRON_HOE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_hoe"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 1)
				.requires(Items.IRON_SHOVEL)
				.unlockedBy("has_tool", has(Items.IRON_SHOVEL))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_shovel"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 2)
				.requires(Items.IRON_SWORD)
				.unlockedBy("has_tool", has(Items.IRON_SWORD))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_sword"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 2)
				.requires(Items.IRON_PICKAXE)
				.unlockedBy("has_tool", has(Items.IRON_PICKAXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_pickaxe"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 4)
				.requires(Items.IRON_BOOTS)
				.unlockedBy("has_armor", has(Items.IRON_BOOTS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_boots"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 7)
				.requires(Items.IRON_LEGGINGS)
				.unlockedBy("has_armor", has(Items.IRON_LEGGINGS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_leggings"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 8)
				.requires(Items.IRON_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.IRON_CHESTPLATE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_chestplate"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 5)
				.requires(Items.IRON_HELMET)
				.unlockedBy("has_armor", has(Items.IRON_HELMET))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_helmet"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 2)
				.requires(Items.CHAINMAIL_BOOTS)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_BOOTS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_chainmail_boots"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 4)
				.requires(Items.CHAINMAIL_LEGGINGS)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_LEGGINGS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_chainmail_leggings"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 6)
				.requires(Items.CHAINMAIL_CHESTPLATE)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_CHESTPLATE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_chainmail_chestplate"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 3)
				.requires(Items.CHAINMAIL_HELMET)
				.unlockedBy("has_armor", has(Items.CHAINMAIL_HELMET))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_chainmail_helmet"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 4)
				.requires(Items.IRON_HORSE_ARMOR)
				.unlockedBy("has_horse_armor", has(Items.IRON_HORSE_ARMOR))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_horse_armor"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 4)
				.requires(Items.COMPASS)
				.unlockedBy("has_compass", has(Items.COMPASS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/irom_ingot_from_compass"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 7)
				.requires(Items.CAULDRON)
				.unlockedBy("has_cauldron", has(Items.CAULDRON))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/irom_ingot_from_cauldron"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 3)
				.requires(Items.BUCKET)
				.unlockedBy("has_bucket", has(Items.BUCKET))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_bucket"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 2)
				.requires(Items.IRON_DOOR)
				.unlockedBy("has_door", has(Items.IRON_DOOR))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_door"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 5)
				.requires(Items.MINECART)
				.unlockedBy("has_minecart", has(Items.MINECART))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_minecart"));
		TransmutationRecipeBuilder.transmutation(Items.IRON_INGOT, 31)
				.requires(Items.ANVIL)
				.unlockedBy("has_anvil", has(Items.ANVIL))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/iron_ingot_from_anvil"));

		//Force
		TransmutationRecipeBuilder.transmutation(ForceRegistry.FORCE_BRICK)
				.requires(Items.STONE_BRICKS)
				.unlockedBy("has_stone_bricks", has(Items.STONE_BRICKS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/force_brick_from_stone_bricks"));
		TransmutationRecipeBuilder.transmutation(ForceRegistry.FORCE_SAPLING)
				.requires(ItemTags.SAPLINGS)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/force_sapling_from_sapling"));

		//Misc
		TransmutationRecipeBuilder.transmutation(Items.EXPERIENCE_BOTTLE)
				.requires(Items.ENCHANTED_BOOK)
				.unlockedBy("has_enchanted_book", has(Items.ENCHANTED_BOOK))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/experience_bottle_from_enchanted_book"));
		TransmutationRecipeBuilder.transmutation(ForceRegistry.FORTUNE.get())
				.requires(ForceRegistry.FORTUNE_COOKIE.get())
				.unlockedBy("has_fortune_cookie", has(ForceRegistry.FORTUNE_COOKIE.get()))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/fortune_from_fortune_cookie"));
		TransmutationRecipeBuilder.transmutation(ForceRegistry.INFUSER.get())
				.requires(Items.ENCHANTING_TABLE)
				.unlockedBy("has_enchanting_table", has(Items.ENCHANTING_TABLE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/infuser_from_enchanting_table"));
		TransmutationRecipeBuilder.transmutation(Items.STRING)
				.requires(Items.BOW)
				.unlockedBy("has_bow", has(Items.BOW))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/string_from_bow"));
		TransmutationRecipeBuilder.transmutation(Items.TRIPWIRE_HOOK)
				.requires(Items.CROSSBOW)
				.unlockedBy("has_bow", has(Items.CROSSBOW))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/tripwire_hook_from_crossbow"));

		//Stick
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.STONE_AXE)
				.unlockedBy("has_tool", has(Items.STONE_AXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_stone_axe"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.STONE_HOE)
				.unlockedBy("has_tool", has(Items.STONE_HOE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_stone_hoe"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.STONE_PICKAXE)
				.unlockedBy("has_tool", has(Items.STONE_PICKAXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_stone_pickaxe"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.STONE_SHOVEL)
				.unlockedBy("has_tool", has(Items.STONE_SHOVEL))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_stone_shovel"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.STONE_SWORD)
				.unlockedBy("has_tool", has(Items.STONE_SWORD))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_stone_sword"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.WOODEN_AXE)
				.unlockedBy("has_tool", has(Items.WOODEN_AXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_wooden_axe"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.WOODEN_HOE)
				.unlockedBy("has_tool", has(Items.WOODEN_HOE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_wooden_hoe"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.WOODEN_PICKAXE)
				.unlockedBy("has_tool", has(Items.WOODEN_PICKAXE))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_wooden_pickaxe"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.WOODEN_SHOVEL)
				.unlockedBy("has_tool", has(Items.WOODEN_SHOVEL))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_wooden_shovel"));
		TransmutationRecipeBuilder.transmutation(Items.STICK)
				.requires(Items.WOODEN_SWORD)
				.unlockedBy("has_tool", has(Items.WOODEN_SWORD))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/stick_from_wooden_sword"));

		//Cores
		TransmutationRecipeBuilder.transmutation(ForceRegistry.UPGRADE_TOME.get())
				.requires(Items.BOOK)
				.unlockedBy("has_book", has(Items.BOOK))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/upgrade_tome"));

		ItemStack upgradeStack = new ItemStack(ForceRegistry.UPGRADE_TOME.get());
		CompoundTag tag = upgradeStack.getOrCreateTag();
		tag.putInt("Experience", 100);
		upgradeStack.setTag(tag);
		TransmutationRecipeBuilder.transmutation(ForceRegistry.UPGRADE_CORE.get())
				.requires(NBTIngredient.of(false, upgradeStack))
				.unlockedBy("has_upgrade_tome", has(ForceRegistry.UPGRADE_TOME.get()))
				.save(output, new ResourceLocation(Reference.MOD_ID, "transmutation/upgrade_core"));

	}
}