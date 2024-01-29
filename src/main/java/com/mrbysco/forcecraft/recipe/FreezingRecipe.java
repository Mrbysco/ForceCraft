package com.mrbysco.forcecraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import com.mrbysco.forcecraft.registry.ForceRecipes;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class FreezingRecipe extends MultipleOutputFurnaceRecipe {

	public FreezingRecipe(String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float experience, int freezingTime) {
		super(ForceRecipes.FREEZING.get(), groupIn, ingredientIn, results, 1.0F, experience, freezingTime);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ForceRegistry.FREEZING_CORE.get());
	}

	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.FREEZING_SERIALIZER.get();
	}

	public static class SerializerFreezingRecipe implements RecipeSerializer<FreezingRecipe> {
		public static final Codec<FreezingRecipe> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
								Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
								ItemStack.RESULT_CODEC.codec()
										.listOf()
										.fieldOf("results")
										.flatXmap(
												array -> {
													ItemStack[] stacks = array.toArray(ItemStack[]::new);
													if (stacks.length == 0) {
														return DataResult.error(() -> "No results for freezing recipe");
													} else {
														return stacks.length > 2
																? DataResult.error(() -> "Too many itemstacks for freezing recipe. The maximum is: %s".formatted(9))
																: DataResult.success(NonNullList.of(ItemStack.EMPTY, stacks));
													}
												},
												DataResult::success
										)
										.forGetter(recipe -> recipe.results),
								Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(recipe -> recipe.experience),
								Codec.INT.fieldOf("freezingTime").orElse(200).forGetter(recipe -> recipe.cookingTime)
						)
						.apply(instance, FreezingRecipe::new)
		);

		@Override
		public Codec<FreezingRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public FreezingRecipe fromNetwork(FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for (int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItem());
			}

			float f = buffer.readFloat();
			int i = buffer.readVarInt();
			return new FreezingRecipe(s, ingredient, resultList, f, i);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, FreezingRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);

			buffer.writeVarInt(recipe.results.size());
			for (ItemStack stack : recipe.results) {
				buffer.writeItem(stack);
			}

			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookingTime);
		}
	}
}
