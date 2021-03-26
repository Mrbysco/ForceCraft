package mrbysco.forcecraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mrbysco.forcecraft.registry.ForceRegistry;
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

public class GrindingRecipe extends MultipleOutputFurnaceRecipe{

	public GrindingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, NonNullList<ItemStack> results, float chance, float experienceIn, int cookTimeIn) {
		super(ForceRecipes.GRINDING, idIn, groupIn, ingredientIn, results, chance, experienceIn, cookTimeIn);
	}

	public ItemStack getIcon() {
		return new ItemStack(ForceRegistry.GRINDING_CORE.get());
	}

	public IRecipeSerializer<?> getSerializer() {
		return ForceRecipes.GRINDING_SERIALIZER.get();
	}

	public static class SerializerGrindingRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GrindingRecipe> {
		@Override
		public GrindingRecipe read(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getString(json, "group", "");
			JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.deserialize(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!json.has("results")) throw new com.google.gson.JsonSyntaxException("Missing results, expected to find a string or object");
			NonNullList<ItemStack> nonnulllist = readItemStacks(JSONUtils.getJsonArray(json, "results"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No results for grinding recipe");
			} else if (nonnulllist.size() > MAX_OUTPUT) {
				throw new JsonParseException("Too many results for grinding recipe the max is " + MAX_OUTPUT);
			}

			float chance = JSONUtils.getFloat(json, "secondaryChance", 0.0F);
			float f = JSONUtils.getFloat(json, "experience", 0.0F);
			int i = JSONUtils.getInt(json, "processtime", 200);
			return new GrindingRecipe(recipeId, s, ingredient, nonnulllist,chance, f, i);
		}

		private static NonNullList<ItemStack> readItemStacks(JsonArray resultArray) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();

			for(int i = 0; i < resultArray.size(); ++i) {
				if(resultArray.get(i).isJsonObject()) {
					ItemStack stack = ShapedRecipe.deserializeItem(resultArray.get(i).getAsJsonObject());
					nonnulllist.add(stack);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public GrindingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readString(32767);
			Ingredient ingredient = Ingredient.read(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for(int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItemStack());
			}

			float chance = buffer.readFloat();
			float f = buffer.readFloat();
			int i = buffer.readVarInt();
			return new GrindingRecipe(recipeId, s, ingredient, resultList, chance, f, i);
		}

		@Override
		public void write(PacketBuffer buffer, GrindingRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);

			buffer.writeVarInt(recipe.resultItems.size());
			for(ItemStack stack : recipe.resultItems) {
				buffer.writeItemStack(stack);
			}

			buffer.writeFloat(recipe.secondaryChance);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
