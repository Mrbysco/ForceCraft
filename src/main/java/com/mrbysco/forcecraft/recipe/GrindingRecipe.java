package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GrindingRecipe extends MultipleOutputFurnaceRecipe {

	public GrindingRecipe(String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results,
	                      float secondaryChance, float experience, int processTime) {
		super(ForceRecipes.GRINDING.get(), groupIn, ingredientIn, results, secondaryChance, experience, processTime);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ForceRegistry.GRINDING_CORE.get());
	}

	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.GRINDING_SERIALIZER.get();
	}

	public static class Serializer implements RecipeSerializer<GrindingRecipe> {
		private static final MapCodec<GrindingRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
								ItemStack.CODEC
										.listOf()
										.fieldOf("results")
										.flatXmap(
												array -> {
													ItemStack[] stacks = array.toArray(ItemStack[]::new);
													if (stacks.length == 0) {
														return DataResult.error(() -> "No results for freezing recipe");
													} else {
														return stacks.length > MAX_OUTPUT
																? DataResult.error(() -> "Too many itemstacks for freezing recipe. The maximum is: %s".formatted(9))
																: DataResult.success(NonNullList.of(ItemStack.EMPTY, stacks));
													}
												},
												DataResult::success
										)
										.forGetter(recipe -> recipe.results),
								Codec.FLOAT.fieldOf("secondaryChance").orElse(0.0F).forGetter(recipe -> recipe.secondaryChance),
								Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(recipe -> recipe.experience),
								Codec.INT.fieldOf("processTime").orElse(200).forGetter(recipe -> recipe.cookingTime)
						)
						.apply(instance, GrindingRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, GrindingRecipe> STREAM_CODEC = StreamCodec.of(
				GrindingRecipe.Serializer::toNetwork, GrindingRecipe.Serializer::fromNetwork
		);

		@Override
		public MapCodec<GrindingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, GrindingRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		public static GrindingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for (int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, ItemStack.STREAM_CODEC.decode(buffer));
			}

			float chance = buffer.readFloat();
			float f = buffer.readFloat();
			int i = buffer.readVarInt();
			return new GrindingRecipe(s, ingredient, resultList, chance, f, i);
		}

		public static void toNetwork(RegistryFriendlyByteBuf buffer, GrindingRecipe recipe) {
			buffer.writeUtf(recipe.group);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);

			buffer.writeVarInt(recipe.results.size());
			for (ItemStack stack : recipe.results) {
				ItemStack.STREAM_CODEC.encode(buffer, stack);
			}

			buffer.writeFloat(recipe.secondaryChance);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookingTime);
		}
	}
}
