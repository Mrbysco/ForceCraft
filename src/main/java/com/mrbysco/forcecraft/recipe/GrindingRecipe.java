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
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

public class GrindingRecipe extends MultiOutputFurnaceRecipe {
	public static final MapCodec<GrindingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(
							CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
							CookingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
							Ingredient.CODEC.fieldOf("ingredient").forGetter(GrindingRecipe::input),
							ItemStackTemplate.CODEC.fieldOf("result").forGetter(GrindingRecipe::result),
							ItemStackTemplate.CODEC.optionalFieldOf("result2").forGetter(GrindingRecipe::result2),
							Codec.FLOAT.fieldOf("secondaryChance").orElse(0.0F).forGetter(GrindingRecipe::secondaryChance),
							Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(GrindingRecipe::experience),
							Codec.INT.fieldOf("cookingtime").orElse(200).forGetter(GrindingRecipe::cookingTime)
					)
					.apply(i, GrindingRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, GrindingRecipe> STREAM_CODEC = StreamCodec.composite(
			CommonInfo.STREAM_CODEC,
			o -> o.commonInfo,
			CookingBookInfo.STREAM_CODEC,
			o -> o.bookInfo,
			Ingredient.CONTENTS_STREAM_CODEC,
			GrindingRecipe::input,
			ItemStackTemplate.STREAM_CODEC,
			GrindingRecipe::result,
			ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC),
			GrindingRecipe::result2,
			ByteBufCodecs.FLOAT,
			GrindingRecipe::secondaryChance,
			ByteBufCodecs.FLOAT,
			GrindingRecipe::experience,
			ByteBufCodecs.INT,
			GrindingRecipe::cookingTime,
			GrindingRecipe::new
	);
	public static final RecipeSerializer<GrindingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public GrindingRecipe(CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient,
	                      ItemStackTemplate result, Optional<ItemStackTemplate> result2, float secondaryChance, float experience, int cookingTime) {
		super(commonInfo, bookInfo, ingredient, result, result2, experience, cookingTime, secondaryChance);
	}

	@Override
	public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
		return ForceRecipes.GRINDING_SERIALIZER.get();
	}

	@Override
	public RecipeType<? extends AbstractCookingRecipe> getType() {
		return ForceRecipes.GRINDING.get();
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.FURNACE_MISC;
	}

	@Override
	protected Item furnaceIcon() {
		return ForceRegistry.GRINDING_CORE.get();
	}
}
