package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

public class FreezingRecipe extends MultiOutputFurnaceRecipe {
	public static final MapCodec<FreezingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(
							Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
							AbstractCookingRecipe.CookingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
							Ingredient.CODEC.fieldOf("ingredient").forGetter(FreezingRecipe::input),
							ItemStackTemplate.CODEC.fieldOf("result").forGetter(FreezingRecipe::result),
							ItemStackTemplate.CODEC.optionalFieldOf("result2").forGetter(FreezingRecipe::result2),
							Codec.FLOAT.fieldOf("secondaryChance").orElse(1.0F).forGetter(FreezingRecipe::secondaryChance),
							Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(FreezingRecipe::experience),
							Codec.INT.fieldOf("cookingtime").orElse(200).forGetter(FreezingRecipe::cookingTime)
					)
					.apply(i, FreezingRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, FreezingRecipe> STREAM_CODEC = StreamCodec.composite(
			Recipe.CommonInfo.STREAM_CODEC,
			o -> o.commonInfo,
			AbstractCookingRecipe.CookingBookInfo.STREAM_CODEC,
			o -> o.bookInfo,
			Ingredient.CONTENTS_STREAM_CODEC,
			FreezingRecipe::input,
			ItemStackTemplate.STREAM_CODEC,
			FreezingRecipe::result,
			ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC),
			FreezingRecipe::result2,
			ByteBufCodecs.FLOAT,
			FreezingRecipe::secondaryChance,
			ByteBufCodecs.FLOAT,
			FreezingRecipe::experience,
			ByteBufCodecs.INT,
			FreezingRecipe::cookingTime,
			FreezingRecipe::new
	);
	public static final RecipeSerializer<FreezingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public FreezingRecipe(CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient,
	                      ItemStackTemplate result, Optional<ItemStackTemplate> result2, float secondaryChance, float experience, int cookingTime) {
		super(commonInfo, bookInfo, ingredient, result, result2, experience, cookingTime, secondaryChance);
	}

	@Override
	public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
		return ForceRecipes.FREEZING_SERIALIZER.get();
	}

	@Override
	public RecipeType<? extends AbstractCookingRecipe> getType() {
		return ForceRecipes.FREEZING.get();
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.FURNACE_MISC;
	}

	@Override
	protected Item furnaceIcon() {
		return ForceRegistry.FROZEN_CORE.get();
	}
}
