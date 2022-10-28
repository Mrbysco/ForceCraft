package com.mrbysco.forcecraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class FreezingRecipe extends MultipleOutputFurnaceRecipe {

	public FreezingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float experienceIn, int cookTimeIn) {
		super(ForceRecipes.FREEZING, idIn, groupIn, ingredientIn, results, 1.0F, experienceIn, cookTimeIn);
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ForceRegistry.FREEZING_CORE.get());
	}

	public IRecipeSerializer<?> getSerializer() {
		return ForceRecipes.FREEZING_SERIALIZER.get();
	}

	public static class SerializerFreezingRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FreezingRecipe> {
		@Override
		public FreezingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getAsString(json, "group", "");
			JsonElement jsonelement = (JsonElement) (JSONUtils.isArrayNode(json, "ingredient") ? JSONUtils.getAsJsonArray(json, "ingredient") : JSONUtils.getAsJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!json.has("results"))
				throw new com.google.gson.JsonSyntaxException("Missing results, expected to find a string or object");
			NonNullList<ItemStack> nonnulllist = readItemStacks(JSONUtils.getAsJsonArray(json, "results"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No results for freezing recipe");
			} else if (nonnulllist.size() > MAX_OUTPUT) {
				throw new JsonParseException("Too many results for freezing recipe the max is " + MAX_OUTPUT);
			}

			float f = JSONUtils.getAsFloat(json, "experience", 0.0F);
			int i = JSONUtils.getAsInt(json, "processtime", 200);
			return new FreezingRecipe(recipeId, s, ingredient, nonnulllist, f, i);
		}

		private static NonNullList<ItemStack> readItemStacks(JsonArray resultArray) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();

			for (int i = 0; i < resultArray.size(); ++i) {
				if (resultArray.get(i).isJsonObject()) {
					ItemStack stack = ShapedRecipe.itemFromJson(resultArray.get(i).getAsJsonObject());
					nonnulllist.add(stack);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public FreezingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for (int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItem());
			}

			float f = buffer.readFloat();
			int i = buffer.readVarInt();
			return new FreezingRecipe(recipeId, s, ingredient, resultList, f, i);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, FreezingRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);

			buffer.writeVarInt(recipe.resultItems.size());
			for (ItemStack stack : recipe.resultItems) {
				buffer.writeItem(stack);
			}

			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookingTime);
		}
	}
}
