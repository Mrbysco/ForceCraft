package com.mrbysco.forcecraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mrbysco.forcecraft.registry.ForceRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ShapelessNoRemainderRecipe extends ShapelessRecipe {
	private final ResourceLocation id;
	final String group;
	final CraftingBookCategory category;
	final ItemStack result;
	final NonNullList<Ingredient> ingredients;
	private final boolean isSimple;

	public ShapelessNoRemainderRecipe(ResourceLocation id, String group, CraftingBookCategory category,
	                                  ItemStack result, NonNullList<Ingredient> ingredients) {
		super(id, group, category, result, ingredients);
		this.id = id;
		this.group = group;
		this.category = category;
		this.result = result;
		this.ingredients = ingredients;
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ForceRecipeSerializers.SHAPELESS_NO_REMAINDER_SERIALIZER.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		return nonnulllist;
	}

	public static class SerializerShapelessNoRemainderRecipe implements RecipeSerializer<ShapelessNoRemainderRecipe> {
		public ShapelessNoRemainderRecipe fromJson(ResourceLocation p_44290_, JsonObject p_44291_) {
			String s = GsonHelper.getAsString(p_44291_, "group", "");
			CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(p_44291_, "category", (String) null), CraftingBookCategory.MISC);
			NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(p_44291_, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (nonnulllist.size() > 3 * 3) {
				throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + (3 * 3));
			} else {
				ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44291_, "result"));
				return new ShapelessNoRemainderRecipe(p_44290_, s, craftingbookcategory, itemstack, nonnulllist);
			}
		}

		private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for (int i = 0; i < p_44276_.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i), false);
				if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}

		public ShapelessNoRemainderRecipe fromNetwork(ResourceLocation p_44293_, FriendlyByteBuf p_44294_) {
			String s = p_44294_.readUtf();
			CraftingBookCategory craftingbookcategory = p_44294_.readEnum(CraftingBookCategory.class);
			int i = p_44294_.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(p_44294_));
			}

			ItemStack itemstack = p_44294_.readItem();
			return new ShapelessNoRemainderRecipe(p_44293_, s, craftingbookcategory, itemstack, nonnulllist);
		}

		public void toNetwork(FriendlyByteBuf p_44281_, ShapelessNoRemainderRecipe p_44282_) {
			p_44281_.writeUtf(p_44282_.group);
			p_44281_.writeEnum(p_44282_.category);
			p_44281_.writeVarInt(p_44282_.ingredients.size());

			for (Ingredient ingredient : p_44282_.ingredients) {
				ingredient.toNetwork(p_44281_);
			}

			p_44281_.writeItem(p_44282_.result);
		}
	}
}
