package mrbysco.forcecraft.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ShapedNoRemainderRecipe extends ShapedRecipe {
	static int MAX_WIDTH = 3;
	static int MAX_HEIGHT = 3;

	private final int recipeWidth;
	private final int recipeHeight;
	private final NonNullList<Ingredient> recipeItems;
	private final ItemStack recipeOutput;
	private final ResourceLocation id;
	private final String group;

	public ShapedNoRemainderRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
		this.id = idIn;
		this.group = groupIn;
		this.recipeWidth = recipeWidthIn;
		this.recipeHeight = recipeHeightIn;
		this.recipeItems = recipeItemsIn;
		this.recipeOutput = recipeOutputIn;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ForceRecipes.SHAPED_NO_REMAINDER_SERIALIZER.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		return nonnulllist;
	}

	private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
		NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(keys.keySet());
		set.remove(" ");

		for(int i = 0; i < pattern.length; ++i) {
			for(int j = 0; j < pattern[i].length(); ++j) {
				String s = pattern[i].substring(j, j + 1);
				Ingredient ingredient = keys.get(s);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
				}

				set.remove(s);
				nonnulllist.set(j + patternWidth * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return nonnulllist;
		}
	}

	@VisibleForTesting
	static String[] shrink(String... toShrink) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for(int i1 = 0; i1 < toShrink.length; ++i1) {
			String s = toShrink[i1];
			i = Math.min(i, firstNonSpace(s));
			int j1 = lastNonSpace(s);
			j = Math.max(j, j1);
			if (j1 < 0) {
				if (k == i1) {
					++k;
				}

				++l;
			} else {
				l = 0;
			}
		}

		if (toShrink.length == l) {
			return new String[0];
		} else {
			String[] astring = new String[toShrink.length - l - k];

			for(int k1 = 0; k1 < astring.length; ++k1) {
				astring[k1] = toShrink[k1 + k].substring(i, j + 1);
			}

			return astring;
		}
	}

	private static int firstNonSpace(String str) {
		int i;
		for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
		}

		return i;
	}

	private static int lastNonSpace(String str) {
		int i;
		for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
		}

		return i;
	}

	private static String[] patternFromJson(JsonArray jsonArr) {
		String[] astring = new String[jsonArr.size()];
		if (astring.length > MAX_HEIGHT) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
		} else if (astring.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for(int i = 0; i < astring.length; ++i) {
				String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");
				if (s.length() > MAX_WIDTH) {
					throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
				}

				if (i > 0 && astring[0].length() != s.length()) {
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}

				astring[i] = s;
			}

			return astring;
		}
	}

	/**
	 * Returns a key json object as a Java HashMap.
	 */
	private static Map<String, Ingredient> deserializeKey(JsonObject json) {
		Map<String, Ingredient> map = Maps.newHashMap();

		for(Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
		}

		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	public static ItemStack deserializeItem(JsonObject object) {
		String s = JSONUtils.getString(object, "item");
		// the non-deprecated version. same one used by CraftingHelper
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
        if (item == null) {
			throw new JsonSyntaxException("Unknown item '" + s + "'");
		}
		if (object.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
		}
	}

	public static class SerializerShapedNoRemainderRecipe extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<ShapedNoRemainderRecipe> {
		public ShapedNoRemainderRecipe read(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getString(json, "group", "");
			Map<String, Ingredient> map = ShapedNoRemainderRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
			String[] astring = ShapedNoRemainderRecipe.shrink(ShapedNoRemainderRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = ShapedNoRemainderRecipe.deserializeIngredients(astring, map, i, j);
			ItemStack itemstack = ShapedNoRemainderRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			return new ShapedNoRemainderRecipe(recipeId, s, i, j, nonnulllist, itemstack);
		}

		public ShapedNoRemainderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int i = buffer.readVarInt();
			int j = buffer.readVarInt();
			String s = buffer.readString(32767);
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

			for(int k = 0; k < nonnulllist.size(); ++k) {
				nonnulllist.set(k, Ingredient.read(buffer));
			}

			ItemStack itemstack = buffer.readItemStack();
			return new ShapedNoRemainderRecipe(recipeId, s, i, j, nonnulllist, itemstack);
		}

		public void write(PacketBuffer buffer, ShapedNoRemainderRecipe recipe) {
			buffer.writeVarInt(recipe.recipeWidth);
			buffer.writeVarInt(recipe.recipeHeight);
			buffer.writeString(recipe.group);

			for(Ingredient ingredient : recipe.recipeItems) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.recipeOutput);
		}
	}
}
