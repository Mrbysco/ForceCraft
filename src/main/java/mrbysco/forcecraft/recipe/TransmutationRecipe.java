package mrbysco.forcecraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import mrbysco.forcecraft.items.ExperienceTomeItem;
import mrbysco.forcecraft.items.tools.ForceRodItem;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.inventory.CraftingInventory;
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
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

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

	public ItemStack getRecipeOutput() {
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

		for(int j = 0; j < inv.getSizeInventory(); ++j) {
			ItemStack itemstack = inv.getStackInSlot(j);
			if (!itemstack.isEmpty()) { //TEMP fix!
				++i;
				if((itemstack.getItem() instanceof ForceRodItem)) {
					if (isSimple)
						recipeitemhelper.func_221264_a(itemstack, 1);
					else inputs.add(itemstack);
				} else {
					if(itemstack.getItem() instanceof ExperienceTomeItem) {
						if(itemstack.getOrCreateTag().getInt("Experience") < 100) {
							return false;
						} else {
							ItemStack experienceTome = new ItemStack(ForceRegistry.EXPERIENCE_TOME.get());
							CompoundNBT nbt = experienceTome.getOrCreateTag();
							nbt.putInt("Experience", 100);
							experienceTome.setTag(nbt);
							if (isSimple)
								recipeitemhelper.func_221264_a(experienceTome, 1);
							else inputs.add(experienceTome);
						}
					} else {
						if(!itemstack.isDamaged()) {
							if (isSimple)
								recipeitemhelper.func_221264_a(itemstack, 1);
							else inputs.add(itemstack);
						}
					}
				}
			}
		}

		return i == this.recipeItems.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeItems) != null);
	}

	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack resultStack = this.recipeOutput.copy();
		for(int j = 0; j < inv.getSizeInventory(); ++j) {
			ItemStack itemstack = inv.getStackInSlot(j);
			if((itemstack.getItem() instanceof ExperienceTomeItem)) {
				CompoundNBT tag = itemstack.getOrCreateTag();
				int count = Math.min((int)((float)tag.getInt("Experience") / 100f), 64);
				resultStack.setCount(count);
			}
		}
		return resultStack;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.recipeItems.size();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			if (itemstack.hasContainerItem()) {
				nonnulllist.set(i, itemstack.getContainerItem());
			} else if (itemstack.getItem() instanceof ForceRodItem) {
				ItemStack itemstack1 = itemstack.copy();
				//TODO: make it eat 25 force when force is stored. Else eat durability
				if(itemstack1.getItem().getDamage(itemstack1) >= itemstack1.getMaxDamage()) {
					itemstack1.shrink(1);
				} else {
					itemstack1.getItem().setDamage(itemstack1, itemstack1.getDamage() + 1);
					nonnulllist.set(i, itemstack1);
				}
				break;
			}
		}

		return nonnulllist;
	}

	public static class SerializerTransmutationRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TransmutationRecipe> {
		@Override
		public TransmutationRecipe read(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getString(json, "group", "");
			NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (nonnulllist.size() > 3 * 3) {
				throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
			} else {
				ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
				return new TransmutationRecipe(recipeId, s, itemstack, nonnulllist);
			}
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for(int i = 0; i < ingredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
				if (!ingredient.hasNoMatchingItems()) {
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public TransmutationRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readString(32767);
			int i = buffer.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for(int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.read(buffer));
			}

			ItemStack itemstack = buffer.readItemStack();
			return new TransmutationRecipe(recipeId, s, itemstack, nonnulllist);
		}

		@Override
		public void write(PacketBuffer buffer, TransmutationRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeVarInt(recipe.recipeItems.size());

			for(Ingredient ingredient : recipe.recipeItems) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.recipeOutput);
		}
	}
}
