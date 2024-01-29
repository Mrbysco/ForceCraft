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

	public static class SerializerGrindingRecipe implements RecipeSerializer<GrindingRecipe> {
		public static final Codec<GrindingRecipe> CODEC = RecordCodecBuilder.create(
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
														return DataResult.error(() -> "No results for grinding recipe");
													} else {
														return stacks.length > 2
																? DataResult.error(() -> "Too many itemstacks for grinding recipe. The maximum is: %s".formatted(9))
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

		@Override
		public Codec<GrindingRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public GrindingRecipe fromNetwork(FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for (int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItem());
			}

			float chance = buffer.readFloat();
			float f = buffer.readFloat();
			int i = buffer.readVarInt();
			return new GrindingRecipe(s, ingredient, resultList, chance, f, i);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, GrindingRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);

			buffer.writeVarInt(recipe.results.size());
			for (ItemStack stack : recipe.results) {
				buffer.writeItem(stack);
			}

			buffer.writeFloat(recipe.secondaryChance);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookingTime);
		}
	}
}
