package com.mrbysco.forcecraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import com.mrbysco.forcecraft.items.ExperienceTomeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public class TransmutationRecipe implements ICraftingRecipe {
	private final ResourceLocation id;
	private final String group;
	private final ItemStack recipeOutput;
	private final NonNullList<Ingredient> recipeItems;
	private final boolean isSimple;

	public TransmutationRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
		this.id = idIn;
		this.group = groupIn;
		this.recipeOutput = recipeOutputIn;
		this.recipeItems = recipeItemsIn;
		this.isSimple = recipeItemsIn.stream().allMatch(Ingredient::isSimple);
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public IRecipeSerializer<?> getSerializer() {
		return ForceRecipes.TRANSMUTATION_SERIALIZER.get();
	}

	public String getGroup() {
		return this.group;
	}

	public ItemStack getResultItem() {
		return this.recipeOutput;
	}

	public NonNullList<Ingredient> getIngredients() {
		return this.recipeItems;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < inv.getContainerSize(); ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) { //TEMP fix!
				++i;
				if ((itemstack.getItem() instanceof ForceRodItem)) {
					if (isSimple)
						recipeitemhelper.accountStack(itemstack, 1);
					else inputs.add(itemstack);
				} else {
					if (itemstack.getItem() instanceof ExperienceTomeItem) {
						if (itemstack.hasTag() && itemstack.getTag().getInt("Experience") < 100) {
							return false;
						} else {
							ItemStack experienceTome = new ItemStack(ForceRegistry.EXPERIENCE_TOME.get());
							CompoundNBT nbt = experienceTome.getOrCreateTag();
							nbt.putInt("Experience", 100);
							experienceTome.setTag(nbt);
							if (isSimple)
								recipeitemhelper.accountStack(experienceTome, 1);
							else inputs.add(experienceTome);
						}
					} else {
						if (!itemstack.isDamaged()) {
							if (isSimple)
								recipeitemhelper.accountStack(itemstack, 1);
							else inputs.add(itemstack);
						}
					}
				}
			}
		}

		return i == this.recipeItems.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList) null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
	}

	public ItemStack assemble(CraftingInventory inv) {
		ItemStack resultStack = this.recipeOutput.copy();
		for (int j = 0; j < inv.getContainerSize(); ++j) {
			ItemStack itemstack = inv.getItem(j);
			if ((itemstack.getItem() instanceof ExperienceTomeItem)) {
				CompoundNBT tag = itemstack.getOrCreateTag();
				int experience = tag.getInt("Experience");
				int count = Math.min((int) ((float) experience / 100f), 64);
				resultStack.setCount(count);
			}
			if (itemstack.getItem() instanceof EnchantedBookItem && resultStack.getItem() instanceof ExperienceBottleItem) {
				Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(itemstack);
				if (!enchantmentMap.isEmpty()) {
					int amount = 0;
					for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
						amount += entry.getValue();
					}
					resultStack.setCount(MathHelper.clamp(amount, 1, 64));
				}
			}
		}
		return resultStack;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.recipeItems.size();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = inv.getItem(i);
			if (itemstack.hasContainerItem()) {
				nonnulllist.set(i, itemstack.getContainerItem());
			} else if (itemstack.getItem() instanceof ForceRodItem) {
				ItemStack itemstack1 = itemstack.copy();
				ForceRodItem rod = (ForceRodItem) itemstack1.getItem();
				int damage = rod.damageItem(itemstack1, 1);

				if (itemstack1.getItem().getDamage(itemstack1) >= itemstack1.getMaxDamage()) {
					itemstack1.shrink(1);
				} else {
					itemstack1.setDamageValue(itemstack1.getDamageValue() + damage);
					nonnulllist.set(i, itemstack1);
				}
				continue;
			} else if ((itemstack.getItem() instanceof ExperienceTomeItem)) {
				ItemStack itemstack1 = itemstack.copy();
				CompoundNBT tag = itemstack.getOrCreateTag();
				int experience = tag.getInt("Experience");
				int count = (int) ((float) experience / 100f);
				int newExperience;
				if (count > 64) {
					newExperience = experience - (64 * 100);
				} else {
					newExperience = experience - (count * 100);
				}
				tag.putInt("Experience", newExperience);
				itemstack1.setTag(tag);
				nonnulllist.set(i, itemstack1);
			}
		}

		return nonnulllist;
	}

	public static class SerializerTransmutationRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TransmutationRecipe> {
		@Override
		public TransmutationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getAsString(json, "group", "");
			NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (nonnulllist.size() > 3 * 3) {
				throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
			} else {
				ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
				return new TransmutationRecipe(recipeId, s, itemstack, nonnulllist);
			}
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for (int i = 0; i < ingredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
				if (!ingredient.isEmpty()) {
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public TransmutationRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readUtf(32767);
			int i = buffer.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(buffer));
			}

			ItemStack itemstack = buffer.readItem();
			return new TransmutationRecipe(recipeId, s, itemstack, nonnulllist);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, TransmutationRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeVarInt(recipe.recipeItems.size());

			for (Ingredient ingredient : recipe.recipeItems) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.recipeOutput);
		}
	}
}
