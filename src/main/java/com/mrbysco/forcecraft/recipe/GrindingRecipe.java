package com.mrbysco.forcecraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class GrindingRecipe extends MultipleOutputFurnaceRecipe{

	public GrindingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float chance, float experienceIn, int cookTimeIn) {
		super(ForceRecipes.GRINDING, idIn, groupIn, ingredientIn, results, chance, experienceIn, cookTimeIn);
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ForceRegistry.GRINDING_CORE.get());
	}

	public RecipeSerializer<?> getSerializer() {
		return ForceRecipes.GRINDING_SERIALIZER.get();
	}

	public static class SerializerGrindingRecipe extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GrindingRecipe> {
		@Override
		public GrindingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String s = GsonHelper.getAsString(json, "group", "");
			JsonElement jsonelement = (JsonElement)(GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!json.has("results")) throw new com.google.gson.JsonSyntaxException("Missing results, expected to find a string or object");
			NonNullList<ItemStack> nonnulllist = readItemStacks(GsonHelper.getAsJsonArray(json, "results"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No results for grinding recipe");
			} else if (nonnulllist.size() > MAX_OUTPUT) {
				throw new JsonParseException("Too many results for grinding recipe the max is " + MAX_OUTPUT);
			}

			float chance = GsonHelper.getAsFloat(json, "secondaryChance", 0.0F);
			float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
			int i = GsonHelper.getAsInt(json, "processtime", 200);
			return new GrindingRecipe(recipeId, s, ingredient, nonnulllist,chance, f, i);
		}

		private static NonNullList<ItemStack> readItemStacks(JsonArray resultArray) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();

			for(int i = 0; i < resultArray.size(); ++i) {
				if(resultArray.get(i).isJsonObject()) {
					ItemStack stack = ShapedRecipe.itemStackFromJson(resultArray.get(i).getAsJsonObject());
					nonnulllist.add(stack);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public GrindingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for(int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItem());
			}

			float chance = buffer.readFloat();
			float f = buffer.readFloat();
			int i = buffer.readVarInt();
			return new GrindingRecipe(recipeId, s, ingredient, resultList, chance, f, i);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, GrindingRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);

			buffer.writeVarInt(recipe.resultItems.size());
			for(ItemStack stack : recipe.resultItems) {
				buffer.writeItem(stack);
			}

			buffer.writeFloat(recipe.secondaryChance);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookingTime);
		}
	}
}
